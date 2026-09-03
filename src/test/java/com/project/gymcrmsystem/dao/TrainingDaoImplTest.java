package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrainingDaoImplTest {

  private final Map<Long, Training> storage = new LinkedHashMap<>();
  private TrainingDaoImpl dao;

  @BeforeEach
  void setUp() {
    storage.clear();
    dao = new TrainingDaoImpl();
    dao.setStorage(storage);
  }

  @Test
  void shouldSaveTrainingAssignIdAndStoreIt() {
    Training training = training(1L, 2L);

    Training saved = dao.save(training);

    assertSame(training, saved);
    assertEquals(1L, training.getId());
    assertSame(training, storage.get(1L));
  }

  @Test
  void shouldContinueIdSequenceFromExistingStorage() {
    Training existing = training(1L, 2L);
    existing.assignId(5L);
    storage.put(5L, existing);
    dao.setStorage(storage);
    Training newTraining = training(3L, 4L);

    dao.save(newTraining);

    assertEquals(6L, newTraining.getId());
    assertSame(newTraining, storage.get(6L));
  }

  @Test
  void shouldReturnEmptyWhenTrainingDoesNotExist() {
    assertTrue(dao.findById(99L).isEmpty());
  }

  private Training training(Long traineeId, Long trainerId) {
    Training training = new Training();
    training.setTraineeId(traineeId);
    training.setTrainerId(trainerId);
    return training;
  }
}
