package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Trainer;
import com.project.gymcrmsystem.model.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainerDao {

  List<Trainer> findAll();

  Optional<Trainer> findById(Long id);

  Optional<Trainer> findByUsername(String username);

  List<Training> findByTrainerCriteria(
      String username,
      LocalDate fromDate,
      LocalDate toDate,
      String traineeName,
      String trainingTypeName
  );

  Trainer save(Trainer trainer);

  Trainer update(Trainer trainer);


}
