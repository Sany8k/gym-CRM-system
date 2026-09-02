package com.project.gymcrmsystem.service;

import com.project.gymcrmsystem.dao.TraineeDao;
import com.project.gymcrmsystem.dao.TrainerDao;
import com.project.gymcrmsystem.model.Trainee;
import com.project.gymcrmsystem.model.Trainer;
import com.project.gymcrmsystem.util.PasswordGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

  @Mock
  private TraineeDao traineeDao;

  @Mock
  private TrainerDao trainerDao;

  @Mock
  private PasswordGenerator passwordGenerator;

  private TraineeServiceImpl service;

  @BeforeEach
  void setUp() {
    service = new TraineeServiceImpl();
    service.setTraineeDao(traineeDao);
    service.setTrainerDao(trainerDao);
    service.setPasswordGenerator(passwordGenerator);
  }

  @Test
  void shouldSaveTraineeWithGeneratedCredentials() {
    Trainee trainee = trainee("Jane", "Doe");
    when(traineeDao.findByUsername("Jane.Doe")).thenReturn(Optional.empty());
    when(trainerDao.findByUsername("Jane.Doe")).thenReturn(Optional.empty());
    when(passwordGenerator.generateRandomPassword()).thenReturn("pass123456");
    when(traineeDao.save(trainee)).thenReturn(trainee);

    Trainee result = service.save(trainee);

    assertSame(trainee, result);
    assertEquals("Jane.Doe", trainee.getUsername());
    assertEquals("pass123456", trainee.getPassword());
    verify(traineeDao).save(trainee);
  }

  @Test
  void shouldChooseAvailableUsernameWhenOriginalUsernameIsTakenByTrainer() {
    Trainee trainee = trainee("Jane", "Doe");
    when(traineeDao.findByUsername("Jane.Doe")).thenReturn(Optional.empty());
    when(trainerDao.findByUsername("Jane.Doe")).thenReturn(Optional.of(new Trainer()));
    when(traineeDao.findByUsername("Jane.Doe1")).thenReturn(Optional.empty());
    when(trainerDao.findByUsername("Jane.Doe1")).thenReturn(Optional.empty());
    when(passwordGenerator.generateRandomPassword()).thenReturn("pass123456");
    when(traineeDao.save(trainee)).thenReturn(trainee);

    service.save(trainee);

    assertEquals("Jane.Doe1", trainee.getUsername());
    verify(traineeDao).save(trainee);
  }

  @Test
  void shouldUpdateTraineeAndKeepExistingCredentials() {
    Trainee existing = trainee("Old", "Name");
    existing.setId(1L);
    existing.setUsername("old.name");
    existing.setPassword("old-password");
    Trainee changed = trainee("New", "Name");
    changed.setId(1L);
    when(traineeDao.findById(1L)).thenReturn(Optional.of(existing));
    when(traineeDao.update(changed)).thenReturn(changed);

    Trainee result = service.update(changed);

    assertSame(changed, result);
    assertEquals("old.name", changed.getUsername());
    assertEquals("old-password", changed.getPassword());
    verify(traineeDao).update(changed);
  }

  @Test
  void shouldNotUpdateTraineeWhenItDoesNotExist() {
    Trainee trainee = trainee("Jane", "Doe");
    trainee.setId(1L);
    when(traineeDao.findById(1L)).thenReturn(Optional.empty());

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.update(trainee));

    assertEquals("Trainee not found", exception.getMessage());
    verify(traineeDao, never()).update(any());
  }

  @Test
  void shouldDeleteTrainee() {
    service.deleteById(1L);

    verify(traineeDao).deleteById(1L);
  }

  private Trainee trainee(String firstName, String lastName) {
    Trainee trainee = new Trainee();
    trainee.setFirstName(firstName);
    trainee.setLastName(lastName);
    return trainee;
  }
}
