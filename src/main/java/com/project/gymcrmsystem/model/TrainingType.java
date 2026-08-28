package com.project.gymcrmsystem.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainingType {

  @Setter(value = AccessLevel.NONE)
  private Long id;

  private String name;
}
