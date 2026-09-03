package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerDao {

  List<Trainer> findAll();

  Optional<Trainer> findById(Long id);

  Optional<Trainer> findByUsername(String username);

  Trainer save(Trainer trainer);

  Trainer update(Trainer trainer);
}
