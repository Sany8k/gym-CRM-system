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
  private Boolean isActive;
}
