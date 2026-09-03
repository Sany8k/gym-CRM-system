package com.project.gymcrmsystem.service;

import com.project.gymcrmsystem.dao.TraineeDao;
import com.project.gymcrmsystem.dao.TrainerDao;
import com.project.gymcrmsystem.dao.TrainingDao;
import com.project.gymcrmsystem.model.Trainee;
import com.project.gymcrmsystem.model.Trainer;
import com.project.gymcrmsystem.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

  @Mock
  private TrainingDao trainingDao;

  @Mock
  private TrainerDao trainerDao;

  @Mock
  private TraineeDao traineeDao;

  private TrainingServiceImpl service;

  @BeforeEach
  void setUp() {
    service = new TrainingServiceImpl();
    service.setTrainingDao(trainingDao);
    service.setTrainerDao(trainerDao);
    service.setTraineeDao(traineeDao);
  }

  @Test
  void shouldSaveTrainingWhenTraineeAndTrainerExist() {
    Training training = new Training();
    training.setTraineeId(1L);
    training.setTrainerId(2L);

    when(traineeDao.findById(1L))
        .thenReturn(Optional.of(new Trainee()));

    when(trainerDao.findById(2L))
        .thenReturn(Optional.of(new Trainer()));

    when(trainingDao.save(training))
        .thenReturn(training);

    Training result = service.save(training);

    assertSame(training, result);
    verify(trainingDao).save(training);
  }

  @Test
  void shouldThrowExceptionWhenTraineeDoesNotExist() {
    Training training = new Training();
    training.setTraineeId(1L);
    training.setTrainerId(2L);

    when(traineeDao.findById(1L))
        .thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class,
        () -> service.save(training));

    verify(trainingDao, never()).save(any());
  }

  @Test
  void shouldThrowExceptionWhenTrainerDoesNotExist() {
    Training training = new Training();
    training.setTraineeId(1L);
    training.setTrainerId(2L);

    when(traineeDao.findById(1L))
        .thenReturn(Optional.of(new Trainee()));

    when(trainerDao.findById(2L))
        .thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class,
        () -> service.save(training));

    verify(trainingDao, never()).save(any());
  }
}
