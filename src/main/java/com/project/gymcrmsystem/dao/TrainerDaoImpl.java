package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Trainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Slf4j
public class TrainerDaoImpl implements TrainerDao {
  private Map<Long, Trainer> storage;
  private AtomicLong sequence;

  @Autowired
  public void setStorage(@Qualifier("trainerStorage") Map<Long, Trainer> storage) {
    this.storage = storage;
    long maxId = storage.keySet().stream().mapToLong(Long::longValue).max().orElse(0L);
    this.sequence = new AtomicLong(maxId);
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
    log.debug("Saving trainer with firstName={} and lastName={}",
        trainer.getFirstName(), trainer.getLastName());
    long id = sequence.incrementAndGet();
    trainer.assignId(id);
    storage.put(id, trainer);
    log.info("Trainer saved with id={} and username={}",
        trainer.getId(), trainer.getUsername());
    return trainer;
  }

  @Override
  public Trainer update(Trainer trainer) {
    log.debug("Updating trainer with id={} and username={}",
        trainer.getId(), trainer.getUsername());
    if (!storage.containsKey(trainer.getId())) {
      log.warn("Trainer with id={} not found", trainer.getId());
      throw new IllegalArgumentException("Trainer not found.");
    }
    storage.replace(trainer.getId(), trainer);
    log.info("Trainer updated with id={} and username={}",
        trainer.getId(), trainer.getUsername());
    return trainer;
  }
}
