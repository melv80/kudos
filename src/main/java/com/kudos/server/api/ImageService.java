package com.kudos.server.api;

import com.kudos.server.model.KudosType;

import java.util.List;

public interface ImageService {

  List<String> getAllImagesFor(KudosType type);

  String getRandomImage(KudosType type);

}
