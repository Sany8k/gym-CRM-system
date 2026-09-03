package com.project.gymcrmsystem.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Trainee extends User {
  private LocalDate dateOfBirth;
  private String address;
}
