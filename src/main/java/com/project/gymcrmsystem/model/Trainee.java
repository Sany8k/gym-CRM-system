package com.project.gymcrmsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "trainees")
public class Trainee extends User {

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "trainee_trainers",
      joinColumns = @JoinColumn(name = "trainee_id"),
      inverseJoinColumns = @JoinColumn(name = "trainer_id")
  )
  private Set<Trainer> trainers = new HashSet<>();

  @OneToMany(mappedBy = "trainee", cascade = CascadeType.REMOVE)
  private List<Training> trainings = new ArrayList<>();

  @Column
  private LocalDate dateOfBirth;

  @Column
  private String address;
}
