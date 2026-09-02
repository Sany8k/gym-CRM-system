package com.project.gymcrmsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "trainers")
public class Trainer extends User {


  @ManyToOne
  @JoinColumn(name = "specialization_id", nullable = false)
  private TrainingType specialization;

  @ManyToMany(mappedBy = "trainers", fetch = FetchType.LAZY)
  private Set<Trainee> trainees = new HashSet<>();

  @OneToMany(mappedBy = "trainer")
  private List<Training> trainings;
}
