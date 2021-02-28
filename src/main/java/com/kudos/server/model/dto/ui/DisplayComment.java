package com.kudos.server.model.dto.ui;

public class DisplayComment {
  public String writer;
  public String message;
  public String created;

  public DisplayComment(String writer, String message, String created) {
    this.writer = writer;
    this.message = message;
    this.created = created;
  }
}
