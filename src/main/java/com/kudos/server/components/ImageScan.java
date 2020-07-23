package com.kudos.server.components;

import com.kudos.server.config.AppConfig;
import com.kudos.server.model.Image;
import com.kudos.server.model.KudosType;
import com.kudos.server.repositories.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ImageScan {

  private Logger logger = LoggerFactory.getLogger(ImageScan.class);


  private final AppConfig appConfig;
  private final ImageRepository imageRepository;


  public ImageScan(@Autowired AppConfig config, @Autowired ImageRepository imageRepository) {
    this.appConfig = config;
    this.imageRepository = imageRepository;
  }

  @PostConstruct
  public void insertIntoDatabase() {
    List<Image> currentImages = imageRepository.findAll();
    Map<String, Image> toInsert = scanDirectories();
    for (Image currentImage : currentImages) {
      toInsert.remove(currentImage.pathOnDisk);
    }

    logger.info("inserting to database: "+toInsert.size());
    imageRepository.saveAll(toInsert.values());
  }


  public Map<String, Image> scanDirectories() {
    long start = System.currentTimeMillis();
    logger.info("scanning base directory " + appConfig.getBasedir() + " : ...");

    Map<String, Image> images = new HashMap<>();
    try {
      Files.walkFileTree(appConfig.getBasedir(), new SimpleFileVisitor<Path>() {

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          final Image imageFromPath = createImageFromPath(file.normalize());
          if (imageFromPath != null) {
            logger.info("found " + imageFromPath);
            images.put(imageFromPath.pathOnDisk, imageFromPath);
          }
          else
            logger.warn("could not access file: "+file);

          return super.visitFile(file, attrs);
        }
      });
      logger.info(String.format("base directory scanned, %s new images, took: %s ms", images.size(), System.currentTimeMillis()-start));
    } catch (IOException e) {
      logger.warn("exception during directory scan: ", e);
    }
    return images;
  }

  public Image createImageFromPath(Path file) {
    final Path relativePath = appConfig.getBasedir().normalize().relativize(file);
    Image image = new Image();
    image.pathOnDisk = relativePath.toString();
    try {
      image.size = Files.size(file);
      image.name = file.getFileName().toString();
      BufferedImage bufferedImage = ImageIO.read(file.toFile());
      image.width  = bufferedImage.getWidth();
      image.height = bufferedImage.getHeight();
      image.type   = KudosType.getTypeFrom(relativePath.getName(0).toString());
    } catch (IOException e) {
      return null;
    }
    return image;

  }

}
