package com.kudos.server.components;

import com.kudos.server.model.dto.ImageInfo;
import com.kudos.server.model.jpa.Image;
import com.kudos.server.model.jpa.KudosType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
public interface ImageService {

  String SCALED_IMAGES = "webimages";

  void scanForNewImages();
  Image pickRandomImage(KudosType type);
  ImageInfo importImage(Path file) throws IOException;

}
