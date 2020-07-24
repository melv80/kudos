package com.kudos.server.model;

import javax.persistence.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Entity
public class KudosCard extends KudosItem {

  public String writer;
  public String message;

  @Enumerated(EnumType.STRING)
  public KudosType type;

  @ManyToOne
  public Image backgroundImage;

  public String getWriter() {
    return writer;
  }

  public void setWriter(String writer) {
    this.writer = writer;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public KudosType getType() {
    return type;
  }

  public void setType(KudosType type) {
    this.type = type;
  }

  public Image getBackgroundImage() {
    return backgroundImage;
  }

  public void setBackgroundImage(Image backgroundImage) {
    this.backgroundImage = backgroundImage;
  }

  public long daysBetween(KudosCard other) {
    return ChronoUnit.DAYS.between(getEdited(), other.getEdited());
  }
}
