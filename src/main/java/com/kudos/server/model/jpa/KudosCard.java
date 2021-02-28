package com.kudos.server.model.jpa;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class KudosCard extends KudosItem {


  // imported from external system with ID
  @Basic(optional = true)
  private Long importerID;

  @ManyToOne
  private User writer;

  @Size(max = 512)
  private String message;

  @Enumerated(EnumType.STRING)
  private KudosType type;

  @OneToMany
  private List<Comment> comments = new ArrayList<>();

  @ManyToOne
  @Nullable
  private Image backgroundImage;

  @ManyToOne
  @NotNull
  private PictureChannel pictureChannel;

  public Long getImporterID() {
    return importerID;
  }

  public void setImporterID(Long importerID) {
    this.importerID = importerID;
  }

  public User getWriter() {
    return writer;
  }

  public void setWriter(User writer) {
    this.writer = writer;
  }

  public KudosType getType() {
    return type;
  }

  public void setType(@NotNull KudosType type) {
    this.type = type;
  }

  public void addComment(@NotNull Comment comment) {
    this.comments.add(comment);
  }

  public void removeComment(@NotNull Comment comment) {
    this.comments.remove(comment);
  }

  public List<Comment> getComments() {
    return comments;
  }

  @Nullable
  public Image getBackgroundImage() {
    return backgroundImage;
  }

  public void setBackgroundImage(@NotNull Image backgroundImage) {
    this.backgroundImage = backgroundImage;
  }

  public PictureChannel getPictureChannel() {
    return pictureChannel;
  }

  public void setPictureChannel(@NotNull PictureChannel pictureChannel) {
    this.pictureChannel = pictureChannel;
  }
}
