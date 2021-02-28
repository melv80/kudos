package com.kudos.server.model.jpa;

import javax.persistence.Entity;

@Entity
public class PictureChannel extends KudosItem {
  public String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
