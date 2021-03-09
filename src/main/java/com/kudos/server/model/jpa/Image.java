package com.kudos.server.model.jpa;

import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Entity
public class Image extends KudosItem {


  @Column(name = "pathondisk",unique=true)
  public String pathOnDisk;

  public String name;

  public int width;

  public int height;

  public int usedInCards;

  public long size;

  public KudosType type;

  @Nullable
  @OneToOne(fetch = FetchType.EAGER)
  public Image thumbnail;

  public String getUrl() {
    return "/images/"+getId();
  }

  @Override
  public String toString() {
    return "Image{" +
        "name='" + name + '\'' +
        ", type=" + type +
        '}';
  }
}
