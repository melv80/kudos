package com.kudos.server.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

  @CreationTimestamp
  private Instant created = Instant.now();

  @UpdateTimestamp
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


  public Instant getCreated() {
    return created;
  }

  public void setCreated(Instant created) {
    this.created = created;
  }


  public void setEdited(Instant edited) {
    this.edited = edited;
  }
}
