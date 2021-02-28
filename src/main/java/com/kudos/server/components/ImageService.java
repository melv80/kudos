package com.kudos.server.components;

import com.kudos.server.model.jpa.Image;
import com.kudos.server.model.jpa.KudosType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
public interface ImageService {

  Image pickRandomImage(KudosType type);
  Image importImage(Path file) throws IOException;

}
