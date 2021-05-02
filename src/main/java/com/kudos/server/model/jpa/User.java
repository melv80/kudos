package com.kudos.server.model.jpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User extends KudosItem {
  private String name;
  @Column(unique=true)
  private String email;
  private String password;

  private String roles;


  @OneToOne
  private PictureChannel lastChannel;

  public User() {
  }

  public User(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
    setActive(true);
  }

  public String getPasswordHash() {
    return password;
  }

  public void setPasswordHash(String password) {
    this.password = password;
  }

  public String getRoles() {
    return roles;
  }

  public void setRoles(String roles) {
    this.roles = roles;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public PictureChannel getLastChannel() {
    return lastChannel;
  }

  public void setLastChannel(PictureChannel lastChannel) {
    this.lastChannel = lastChannel;
  }
}
