package com.kudos.server.model.dto.ui;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for displaying a card in gallery
 */
public class DisplayCard {

  public String writer;

  public String title;

  public String formattedDate;

  public ZonedDateTime created;

  public long imageId;

  public String message;

  public List<DisplayComment> comments = new ArrayList<>();

  public ZonedDateTime getCreated() {
    return created;
  }
}
