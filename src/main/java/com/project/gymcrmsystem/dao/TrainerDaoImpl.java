package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TrainerDaoImpl implements TrainerDao {
  private final Map<Long, Trainer> storage;
  private final AtomicLong sequence = new AtomicLong(0);

  @Autowired
  public TrainerDaoImpl(@Qualifier("trainerStorage") Map<Long, Trainer> storage) {
    this.storage = storage;
  }

  @Override
  public Optional<Trainer> findById(Long id) {
    return Optional.ofNullable(storage.get(id));
  }

  @Override
  public Trainer save(Trainer trainer) {
    long id = sequence.incrementAndGet();
    trainer.assignId(id);
    storage.put(id, trainer);
    return trainer;
  }

  @Override
  public Trainer update(Trainer trainer) {
    storage.replace(trainer.getId(), trainer);
    return trainer;
  }
}
