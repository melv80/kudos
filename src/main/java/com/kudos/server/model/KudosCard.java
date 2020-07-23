package com.kudos.server.model;

import javax.persistence.*;
import java.time.Instant;

@Entity
public class KudosCard extends KudosItem {

  public String writer;
  public Instant created;
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

  public Instant getCreated() {
    return created;
  }

  public void setCreated(Instant created) {
    this.created = created;
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
}
