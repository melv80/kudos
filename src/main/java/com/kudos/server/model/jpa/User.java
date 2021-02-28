package com.kudos.server.model.jpa;

import javax.persistence.Entity;
import javax.persistence.UniqueConstraint;

@Entity
public class User extends KudosItem {
  private String name;
  private String email;

  public User() {
  }

  public User(String name, String email) {
    this.name = name;
    this.email = email;
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

}
