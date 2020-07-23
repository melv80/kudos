package com.kudos.server.model;

import java.nio.file.Path;

public enum KudosType {
  AWESOME("awesome"),
  THANK_YOU("thankyou"),
  APPRECIATION("appreciation");

  private final String folder;

  KudosType(String folder) {
    this.folder = folder;
  }

  public static KudosType getTypeFrom(String name) {
    String check = name.toLowerCase();
    for (KudosType value : values()) {
      if (value.folder.equals(check)) return value;
    }
    return THANK_YOU;
  }
}
