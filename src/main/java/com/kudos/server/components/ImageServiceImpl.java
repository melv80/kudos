package com.kudos.server.components;

import com.kudos.server.config.AppConfig;
import com.kudos.server.model.dto.ImageInfo;
import com.kudos.server.model.jpa.Image;
import com.kudos.server.model.jpa.KudosType;
import com.kudos.server.repositories.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ImageServiceImpl implements ImageService {

  private Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);


  private final AppConfig appConfig;
  private final ImageRepository imageRepository;


  public ImageServiceImpl(@Autowired AppConfig config, @Autowired ImageRepository imageRepository) {
    this.appConfig = config;
    this.imageRepository = imageRepository;
  }

  public void scanForNewImages() {
    List<Image> currentImages = imageRepository.findAll();
    try {
      Files.createDirectories(Paths.get(SCALED_IMAGES).toAbsolutePath());
    } catch (IOException e) {
      logger.error("could not create scaled images path", e);
    }
    Map<String, Image> toInsert = scanDirectories();
    for (Image currentImage : currentImages) {
      toInsert.remove(currentImage.pathOnDisk);
    }

    if (toInsert.isEmpty()) {
      logger.info("no *new* images found");
    } else {
      logger.info("inserting new images into database: " + toInsert.size());
    }
    imageRepository.saveAll(toInsert.values());
    imageRepository.flush();
  }


  // TODO: 25.07.2020 to slow
  @Nullable
  public Image pickRandomImage(KudosType type) {
    final List<Image> all = imageRepository.findAll().stream().filter(image -> image.type == type && image.thumbnail != null).collect(Collectors.toList());
    if (all.isEmpty()) {
      if (type == KudosType.THANK_YOU)
        return null;
      else
        return pickRandomImage(KudosType.THANK_YOU);
    }
    return all.get(new Random().nextInt(all.size()));
  }

  public Map<String, Image> scanDirectories() {
    long start = System.currentTimeMillis();
    logger.info("searching for new images in " + appConfig.getBaseDir() + " ...");
    if (!Files.isReadable(appConfig.getBaseDir())) {
      logger.error("can not read :" + appConfig.getBaseDir() + " ...");

      return Collections.emptyMap();
    }

    Map<String, Image> images = new HashMap<>();
    try {
      Files.walkFileTree(appConfig.getBaseDir(), new SimpleFileVisitor<Path>() {

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          final ImageInfo imageFromPath = importImage(file.normalize());
          if (imageFromPath != null) {
            logger.trace("discovered image: " + imageFromPath);
            images.put(imageFromPath.image.pathOnDisk, imageFromPath.image);
          } else
            logger.warn("could not access file: " + file);

          return super.visitFile(file, attrs);
        }
      });
      logger.info(String.format("base directory scanned, %s new images, took: %s ms", images.size(), System.currentTimeMillis() - start));
    } catch (IOException e) {
      logger.warn("exception during directory scan: ", e);
    }
    return images;
  }

  public ImageInfo createImageFromPath(Path file) throws IOException {
    final Path relativePath = file.isAbsolute() ? appConfig.getBaseDir().toAbsolutePath().relativize(file) : appConfig.getBaseDir().relativize(file);
    Image image = new Image();

    image.pathOnDisk = relativePath.toString();
    image.size = Files.size(file);
    image.name = file.getFileName().toString();
    BufferedImage bufferedImage = ImageIO.read(file.toFile());

    image.width = bufferedImage.getWidth();
    image.height = bufferedImage.getHeight();
    image.type = KudosType.getTypeFrom(file.getParent().getFileName().toString());
    return new ImageInfo(image, bufferedImage, file);

  }

  public ImageInfo importImage(Path file) throws IOException {
    ImageInfo imageInfo = createImageFromPath(file);
    Path scaledPath = createScaledImage(imageInfo);
    ImageInfo scaledImage = createImageFromPath(scaledPath);
    imageInfo.image.thumbnail = scaledImage.image;
    List<Image> images = new ArrayList<>();
    images.add(imageInfo.image);
    images.add(scaledImage.image);
    try {
      imageRepository.saveAll(images);
      imageRepository.flush();
    } catch (Exception e) {
      logger.error("could not insert image "+file, e);
    }
    return imageInfo;
  }

  private Path createScaledImage(ImageInfo imageInfo) throws IOException {
    final Dimension originalSize = new Dimension(imageInfo.image.width, imageInfo.image.height);

    final Path originalFileName = imageInfo.path.getFileName();
    String[] splitted = originalFileName.toString().split("\\.");
    Path scaledPath;
    if (splitted.length > 0) {
      scaledPath = Paths.get(splitted[0]+".jpg");
    } else {
      scaledPath = Paths.get(originalFileName.getFileName().toString()+".jpg");
    }

    scaledPath = Paths.get(SCALED_IMAGES, imageInfo.image.type.getFolder(), scaledPath.getFileName().toString());

    Dimension scaledDimension = getScaledDimension(originalSize, new Dimension(1080, 768));
    logger.info("scaling from : " + originalSize + " to " + scaledDimension + " name: " + scaledPath);
    BufferedImage scaledRendered = new BufferedImage(scaledDimension.width, scaledDimension.height, BufferedImage.TYPE_INT_RGB);
    scaledRendered.getGraphics().drawImage(imageInfo.bufferedImage.getScaledInstance(scaledDimension.width, scaledDimension.height, BufferedImage.SCALE_SMOOTH), 0, 0, null);
    Files.createDirectories(scaledPath.getParent());
    ImageIO.write(scaledRendered, "jpg", scaledPath.toFile());

    return scaledPath;
  }

  public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

    int original_width = imgSize.width;
    int original_height = imgSize.height;
    int bound_width = boundary.width;
    int bound_height = boundary.height;
    int new_width = original_width;
    int new_height = original_height;

    // first check if we need to scale width
    if (original_width > bound_width) {
      //scale width to fit
      new_width = bound_width;
      //scale height to maintain aspect ratio
      new_height = (new_width * original_height) / original_width;
    }

    // then check if we need to scale even with the new height
    if (new_height > bound_height) {
      //scale height to fit instead
      new_height = bound_height;
      //scale width to maintain aspect ratio
      new_width = (new_height * original_width) / original_height;
    }

    return new Dimension(new_width, new_height);
  }


}
