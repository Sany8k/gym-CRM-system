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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

  @Test
  void shouldReturnAllTrainees() {
    List<Trainee> trainees = List.of(trainee("Jane", "Doe"), trainee("John", "Doe"));
    when(traineeDao.findAll()).thenReturn(trainees);

    List<Trainee> result = service.findAll();

    assertSame(trainees, result);
    verify(traineeDao).findAll();
  }

  @Test
  void shouldFindTraineeById() {
    Trainee trainee = trainee("Jane", "Doe");
    when(traineeDao.findById(1L)).thenReturn(Optional.of(trainee));

    Optional<Trainee> result = service.findById(1L);

    assertTrue(result.isPresent());
    assertSame(trainee, result.orElseThrow());
    verify(traineeDao).findById(1L);
  }

  @Test
  void shouldChangeTraineePasswordWhenCredentialsAndPasswordAreValid() {
    Trainee trainee = trainee("Jane", "Doe");
    trainee.setUsername("jane.doe");
    trainee.setPassword("old-password");
    when(traineeDao.findByUsername("jane.doe")).thenReturn(Optional.of(trainee));

    service.changePassword("jane.doe", "old-password", "new-password");

    assertEquals("new-password", trainee.getPassword());
  }

  @Test
  void shouldRejectTraineePasswordShorterThanTenCharacters() {
    Trainee trainee = trainee("Jane", "Doe");
    trainee.setUsername("jane.doe");
    trainee.setPassword("old-password");
    when(traineeDao.findByUsername("jane.doe")).thenReturn(Optional.of(trainee));

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> service.changePassword("jane.doe", "old-password", "short"));

    assertEquals("New Password must be at least 10 characters long", exception.getMessage());
    assertEquals("old-password", trainee.getPassword());
  }

  @Test
  void shouldToggleTraineeActiveStatusWhenCredentialsAreValid() {
    Trainee trainee = trainee("Jane", "Doe");
    trainee.setUsername("jane.doe");
    trainee.setPassword("valid-password");
    trainee.setActive(true);
    when(traineeDao.findByUsername("jane.doe")).thenReturn(Optional.of(trainee));

    service.toggleActive("jane.doe", "valid-password");

    assertFalse(trainee.isActive());
  }

  @Test
  void shouldNotToggleTraineeStatusWhenCredentialsAreInvalid() {
    Trainee trainee = trainee("Jane", "Doe");
    trainee.setUsername("jane.doe");
    trainee.setPassword("valid-password");
    trainee.setActive(true);
    when(traineeDao.findByUsername("jane.doe")).thenReturn(Optional.of(trainee));

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> service.toggleActive("jane.doe", "wrong-password"));

    assertEquals("Invalid username or password", exception.getMessage());
    assertTrue(trainee.isActive());
  }

  private Trainee trainee(String firstName, String lastName) {
    Trainee trainee = new Trainee();
    trainee.setFirstName(firstName);
    trainee.setLastName(lastName);
    return trainee;
  }
}
