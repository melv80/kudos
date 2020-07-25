package com.kudos.server.model.dto;

import java.time.ZonedDateTime;

public class DisplayCard {

  public String writer;

  public String title;

  public String formattedDate;

  public ZonedDateTime edited;

  public long imageId;

  public String message;

  public ZonedDateTime getEdited() {
    return edited;
  }
}
