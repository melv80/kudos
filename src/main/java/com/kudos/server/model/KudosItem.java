package com.kudos.server.model;

import javax.persistence.*;
import java.time.Instant;

@javax.persistence.Entity
@Table
@Inheritance(
    strategy = InheritanceType.TABLE_PER_CLASS
)
public abstract class KudosItem {
  @Id
  @GeneratedValue()
  @Column(name = "id", unique = true)
  public long id;

  public Instant edited = Instant.now();
}
