package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Trainee;
import com.project.gymcrmsystem.model.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TraineeDao {

  List<Trainee> findAll();

  Optional<Trainee> findById(Long id);

  Optional<Trainee> findByUsername(String username);

  List<Training> findByTraineeCriteria(
      String username,
      LocalDate fromDate,
      LocalDate toDate,
      String trainerName,
      String trainingTypeName
  );

  Trainee save(Trainee trainee);

  Trainee update(Trainee trainee);

  void deleteById(Long id);
}
