package com.project.gymcrmsystem.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Training {

  @Setter(value = AccessLevel.NONE)
  private Long id;

  private Long traineeId;
  private Long trainerId;
  private String name;
  private TrainingType trainingType;
  private LocalDate date;
  private int duration;

  public void assignId(Long id) {
    if (this.id != null) {
      throw new IllegalStateException("ID has already been assigned and cannot be changed.");
    }

    this.id = id;
  }
}
