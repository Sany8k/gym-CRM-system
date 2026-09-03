package com.project.gymcrmsystem.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trainer extends User {

  private TrainingType specialization;
}
