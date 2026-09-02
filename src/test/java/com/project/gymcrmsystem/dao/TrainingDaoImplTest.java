package com.project.gymcrmsystem.dao;

import com.project.gymcrmsystem.model.Trainee;
import com.project.gymcrmsystem.model.Trainer;
import com.project.gymcrmsystem.model.Training;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingDaoImplTest {

  @Mock
  private EntityManager entityManager;

  private TrainingDaoImpl dao;

  @BeforeEach
  void setUp() {
    dao = new TrainingDaoImpl();
    ReflectionTestUtils.setField(dao, "em", entityManager);
  }

  @Test
  void shouldSaveTrainingAssignIdAndStoreIt() {
    Training training = training(1L, 2L);
    doAnswer(invocation -> {
      training.setId(1L);
      return null;
    }).when(entityManager).persist(training);

    Training saved = dao.save(training);

    assertSame(training, saved);
    assertEquals(1L, training.getId());
    verify(entityManager).persist(training);
  }

  @Test
  void shouldContinueIdSequenceFromExistingStorage() {
    Training newTraining = training(3L, 4L);
    doAnswer(invocation -> {
      newTraining.setId(6L);
      return null;
    }).when(entityManager).persist(newTraining);

    dao.save(newTraining);

    assertEquals(6L, newTraining.getId());
    verify(entityManager).persist(newTraining);
  }

  @Test
  void shouldReturnEmptyWhenTrainingDoesNotExist() {
    when(entityManager.find(Training.class, 99L)).thenReturn(null);

    assertTrue(dao.findById(99L).isEmpty());
  }

  private Training training(Long traineeId, Long trainerId) {
    Trainee trainee = new Trainee();
    trainee.setId(traineeId);
    Trainer trainer = new Trainer();
    trainer.setId(trainerId);
    Training training = new Training();
    training.setTrainee(trainee);
    training.setTrainer(trainer);
    return training;
  }
}
