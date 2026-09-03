package com.project.gymcrmsystem.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class User {

  @Setter(value = AccessLevel.NONE)
  private Long id;

  private String firstName;
  private String lastName;
  private String username;
  private String password;
  private boolean active;

  public void assignId(Long id) {
    if (this.id != null) {
      throw new IllegalStateException("ID has already been assigned and cannot be changed.");
    }

    this.id = id;
  }
}
