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
class TrainerServiceImplTest {

  @Mock
  private TrainerDao trainerDao;

  @Mock
  private TraineeDao traineeDao;

  @Mock
  private PasswordGenerator passwordGenerator;

  private TrainerServiceImpl service;

  @BeforeEach
  void setUp() {
    service = new TrainerServiceImpl();
    service.setTrainerDao(trainerDao);
    service.setTraineeDao(traineeDao);
    service.setPasswordGenerator(passwordGenerator);
  }

  @Test
  void shouldSaveTrainerWithGeneratedCredentials() {
    Trainer trainer = trainer("John", "Doe");
    when(traineeDao.findByUsername("John.Doe")).thenReturn(Optional.empty());
    when(trainerDao.findByUsername("John.Doe")).thenReturn(Optional.empty());
    when(passwordGenerator.generateRandomPassword()).thenReturn("password123");
    when(trainerDao.save(trainer)).thenReturn(trainer);

    Trainer result = service.save(trainer);

    assertSame(trainer, result);
    assertEquals("John.Doe", trainer.getUsername());
    assertEquals("password123", trainer.getPassword());
    verify(trainerDao).save(trainer);
  }

  @Test
  void shouldChooseAvailableUsernameWhenOriginalUsernameIsTaken() {
    Trainer trainer = trainer("John", "Doe");
    when(traineeDao.findByUsername("John.Doe")).thenReturn(Optional.of(new Trainee()));
    when(traineeDao.findByUsername("John.Doe1")).thenReturn(Optional.empty());
    when(trainerDao.findByUsername("John.Doe1")).thenReturn(Optional.empty());
    when(passwordGenerator.generateRandomPassword()).thenReturn("password123");
    when(trainerDao.save(trainer)).thenReturn(trainer);

    service.save(trainer);

    assertEquals("John.Doe1", trainer.getUsername());
    verify(trainerDao).save(trainer);
  }

  @Test
  void shouldPropagateExceptionWhenSavingTrainerFails() {
    Trainer trainer = trainer("John", "Doe");
    when(traineeDao.findByUsername("John.Doe")).thenReturn(Optional.empty());
    when(trainerDao.findByUsername("John.Doe")).thenReturn(Optional.empty());
    when(passwordGenerator.generateRandomPassword()).thenReturn("password123");
    when(trainerDao.save(trainer)).thenThrow(new RuntimeException("Database unavailable"));

    RuntimeException exception = assertThrows(RuntimeException.class, () -> service.save(trainer));

    assertEquals("Database unavailable", exception.getMessage());
    verify(trainerDao).save(trainer);
  }

  @Test
  void shouldUpdateTrainerAndKeepExistingCredentials() {
    Trainer existing = trainer("Old", "Name");
    existing.assignId(1L);
    existing.setUsername("old.name");
    existing.setPassword("old-password");
    Trainer changed = trainer("New", "Name");
    changed.assignId(1L);
    when(trainerDao.findById(1L)).thenReturn(Optional.of(existing));
    when(trainerDao.update(changed)).thenReturn(changed);

    Trainer result = service.update(changed);

    assertSame(changed, result);
    assertEquals("old.name", changed.getUsername());
    assertEquals("old-password", changed.getPassword());
    verify(trainerDao).update(changed);
  }

  @Test
  void shouldNotUpdateTrainerWhenItDoesNotExist() {
    Trainer trainer = trainer("John", "Doe");
    trainer.assignId(1L);
    when(trainerDao.findById(1L)).thenReturn(Optional.empty());

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.update(trainer));

    assertEquals("Trainer not found", exception.getMessage());
    verify(trainerDao, never()).update(any());
  }

  private Trainer trainer(String firstName, String lastName) {
    Trainer trainer = new Trainer();
    trainer.setFirstName(firstName);
    trainer.setLastName(lastName);
    return trainer;
  }
}
