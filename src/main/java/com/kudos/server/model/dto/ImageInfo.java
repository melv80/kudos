package com.kudos.server.model.dto;

import com.kudos.server.model.jpa.Image;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

public class ImageInfo {
  public final Image image;
  public final BufferedImage bufferedImage;
  public final Path path;

  public ImageInfo(Image image, BufferedImage bufferedImage, Path path) {
    this.image = image;
    this.bufferedImage = bufferedImage;
    this.path = path;
  }
}
