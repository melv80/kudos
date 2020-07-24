package com.kudos.server.model;

import javax.persistence.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@javax.persistence.Entity
@Table
@Inheritance(
    strategy = InheritanceType.TABLE_PER_CLASS
)
public abstract class KudosItem {
  @Id
  @GeneratedValue()
  @Column(name = "id", unique = true)
  private long id;

  private Instant edited = Instant.now();

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Instant getEdited() {
    return edited;
  }

  public ZonedDateTime localEdited() {
    return edited.atZone(ZoneId.systemDefault());
  }

  public String formatDate() {
    return localEdited().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
  }

  public void setEdited(Instant edited) {
    this.edited = edited;
  }
}
