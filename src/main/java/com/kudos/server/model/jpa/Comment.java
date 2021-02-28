package com.kudos.server.model.jpa;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Comment extends KudosItem {

  private String message;

  @ManyToOne
  private User writer;

  public Comment() {
  }

  public Comment(String message, User writer) {
    this.writer = writer;
    this.message = message;
  }

  public User getWriter() {
    return writer;
  }

  public void setWriter(User writer) {
    this.writer = writer;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
