package com.project.gymcrmsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "trainings")
public class Training {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
    @JoinColumn(name = "trainee_id", nullable = false)
  private Trainee trainee;

  @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
  private Trainer trainer;

  @Column(nullable = false)
  private String name;

  @ManyToOne
  @JoinColumn(name = "training_type_id", nullable = false)
  private TrainingType trainingType;

  @Column(nullable = false)
  private LocalDate date;

  @Column(nullable = false)
  private int duration;
}
