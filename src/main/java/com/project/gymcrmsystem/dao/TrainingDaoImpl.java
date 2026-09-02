package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
public class TrainingDaoImpl implements TrainingDao {

  @PersistenceContext
  private EntityManager em;

  @Override
  public Optional<Training> findById(Long id) {
    return Optional.ofNullable(em.find(Training.class, id));
  }

  @Override
  public Training save(Training training) {
    log.debug("Saving training with traineeId={} and trainerId={}",
        training.getTrainee().getId(), training.getTrainer().getId());
     Training savedTraining = em.merge(training);
    log.debug("Training saved with id={} and traineeId={} and trainerId={}",
        savedTraining.getId(), savedTraining.getTrainee().getId(), savedTraining.getTrainer().getId());
    return savedTraining;
  }
}
