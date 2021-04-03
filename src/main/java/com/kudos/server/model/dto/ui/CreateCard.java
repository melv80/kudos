package com.kudos.server.model.dto.ui;

import com.kudos.server.model.jpa.KudosType;

/**
 * DTO to create a card
 */
public class CreateCard {

  private String comment;
  private String writer;
  private Long writerID;

  private Long channelID;

  private KudosType kudostype;

  public String getComment() {
    return comment;
  }

  public Long getWriterID() {
    return writerID;
  }

  public Long getChannelID() {
    return channelID;
  }

  public void setChannelID(Long channelID) {
    this.channelID = channelID;
  }

  public void setWriterID(Long writerID) {
    this.writerID = writerID;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getWriter() {
    return writer;
  }

  public void setWriter(String writer) {
    this.writer = writer;
  }

  public KudosType getKudostype() {
    return kudostype;
  }

  public void setKudostype(KudosType kudostype) {
    this.kudostype = kudostype;
  }
}
