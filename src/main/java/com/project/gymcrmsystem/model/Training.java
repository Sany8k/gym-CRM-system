package com.project.gymcrmsystem.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Training {

  @Setter(value = AccessLevel.NONE)
  private Long id;

  private Trainee traineeId;
  private Trainer trainerId;
  private String name;
  private String type;
  private Date date;
  private int duration;
}
