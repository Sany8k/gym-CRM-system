package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TrainerDaoImpl implements TrainerDao {
  private Map<Long, Trainer> storage;
  private AtomicLong sequence;

  @Autowired
  public void setStorage(@Qualifier("trainerStorage") Map<Long, Trainer> storage) {
    this.storage = storage;
    this.sequence = new AtomicLong(storage.size() + 1);
  }

  @Override
  public List<Trainer> findAll() {
    return List.copyOf(storage.values());
  }

  @Override
  public Optional<Trainer> findById(Long id) {
    return Optional.ofNullable(storage.get(id));
  }

  @Override
  public Optional<Trainer> findByUsername(String username) {
    for (Trainer trainer : storage.values()) {
      if (trainer.getUsername().equals(username)) {
        return Optional.of(trainer);
      }
    }
    return Optional.empty();
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
    if (!storage.containsKey(trainer.getId())) {
      throw new IllegalArgumentException("Trainer not found.");
    }
    storage.replace(trainer.getId(), trainer);
    return trainer;
  }
}
