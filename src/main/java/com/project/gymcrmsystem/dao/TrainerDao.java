package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Trainer;

import java.util.Optional;

public interface TrainerDao {

  Optional<Trainer> findById(Long id);

  Trainer save(Trainer trainer);

  Trainer update(Trainer trainer);
}
