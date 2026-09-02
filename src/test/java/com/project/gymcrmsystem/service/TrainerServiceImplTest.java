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
    when(passwordGenerator.generateRandomPassword()).thenReturn("pass123456");
    when(trainerDao.save(trainer)).thenReturn(trainer);

    Trainer result = service.save(trainer);

    assertSame(trainer, result);
    assertEquals("John.Doe", trainer.getUsername());
    assertEquals("pass123456", trainer.getPassword());
    verify(trainerDao).save(trainer);
  }

  @Test
  void shouldChooseAvailableUsernameWhenOriginalUsernameIsTaken() {
    Trainer trainer = trainer("John", "Doe");
    when(traineeDao.findByUsername("John.Doe")).thenReturn(Optional.of(new Trainee()));
    when(traineeDao.findByUsername("John.Doe1")).thenReturn(Optional.empty());
    when(trainerDao.findByUsername("John.Doe1")).thenReturn(Optional.empty());
    when(passwordGenerator.generateRandomPassword()).thenReturn("pass123456");
    when(trainerDao.save(trainer)).thenReturn(trainer);

    service.save(trainer);

    assertEquals("John.Doe1", trainer.getUsername());
    verify(trainerDao).save(trainer);
  }

  @Test
  void shouldUpdateTrainerAndKeepExistingCredentials() {
    Trainer existing = trainer("Old", "Name");
    existing.setId(1L);
    existing.setUsername("old.name");
    existing.setPassword("old-password");
    Trainer changed = trainer("New", "Name");
    changed.setId(1L);
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
    trainer.setId(1L);
    when(trainerDao.findById(1L)).thenReturn(Optional.empty());

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.update(trainer));

    assertEquals("Trainer not found", exception.getMessage());
    verify(trainerDao, never()).update(any());
  }

  @Test
  void shouldReturnAllTrainers() {
    List<Trainer> trainers = List.of(trainer("John", "Doe"), trainer("Jane", "Doe"));
    when(trainerDao.findAll()).thenReturn(trainers);

    List<Trainer> result = service.findAll();

    assertSame(trainers, result);
    verify(trainerDao).findAll();
  }

  @Test
  void shouldFindTrainerById() {
    Trainer trainer = trainer("John", "Doe");
    when(trainerDao.findById(1L)).thenReturn(Optional.of(trainer));

    Optional<Trainer> result = service.findById(1L);

    assertTrue(result.isPresent());
    assertSame(trainer, result.orElseThrow());
    verify(trainerDao).findById(1L);
  }

  @Test
  void shouldChangeTrainerPasswordWhenCredentialsAndPasswordAreValid() {
    Trainer trainer = trainer("John", "Doe");
    trainer.setUsername("john.doe");
    trainer.setPassword("old-password");
    when(trainerDao.findByUsername("john.doe")).thenReturn(Optional.of(trainer));

    service.changePassword("john.doe", "old-password", "new-password");

    assertEquals("new-password", trainer.getPassword());
  }

  @Test
  void shouldChangeTrainerPasswordWhenItMatchesCurrentPassword() {
    Trainer trainer = trainer("John", "Doe");
    trainer.setUsername("john.doe");
    trainer.setPassword("old-password");
    when(trainerDao.findByUsername("john.doe")).thenReturn(Optional.of(trainer));

    service.changePassword("john.doe", "old-password", "old-password");

    assertEquals("old-password", trainer.getPassword());
  }

  @Test
  void shouldAuthenticateTrainerWithValidCredentials() {
    Trainer trainer = trainer("John", "Doe");
    trainer.setPassword("valid-password");
    when(trainerDao.findByUsername("john.doe")).thenReturn(Optional.of(trainer));

    boolean authenticated = service.authenticate("john.doe", "valid-password");

    assertTrue(authenticated);
  }

  @Test
  void shouldNotAuthenticateTrainerWithInvalidCredentials() {
    Trainer trainer = trainer("John", "Doe");
    trainer.setPassword("valid-password");
    when(trainerDao.findByUsername("john.doe")).thenReturn(Optional.of(trainer));

    boolean authenticated = service.authenticate("john.doe", "wrong-password");

    assertFalse(authenticated);
  }

  @Test
  void shouldRejectTrainerPasswordShorterThanTenCharacters() {
    Trainer trainer = trainer("John", "Doe");
    trainer.setUsername("john.doe");
    trainer.setPassword("old-password");
    when(trainerDao.findByUsername("john.doe")).thenReturn(Optional.of(trainer));

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> service.changePassword("john.doe", "old-password", "short"));

    assertEquals("New password does not meet the requirements", exception.getMessage());
    assertEquals("old-password", trainer.getPassword());
  }

  @Test
  void shouldToggleTrainerActiveStatusWhenCredentialsAreValid() {
    Trainer trainer = trainer("John", "Doe");
    trainer.setUsername("john.doe");
    trainer.setPassword("valid-password");
    trainer.setActive(true);
    when(trainerDao.findByUsername("john.doe")).thenReturn(Optional.of(trainer));

    service.toggleActive("john.doe", "valid-password");

    assertFalse(trainer.isActive());
  }

  @Test
  void shouldNotToggleTrainerStatusWhenCredentialsAreInvalid() {
    Trainer trainer = trainer("John", "Doe");
    trainer.setUsername("john.doe");
    trainer.setPassword("valid-password");
    trainer.setActive(true);
    when(trainerDao.findByUsername("john.doe")).thenReturn(Optional.of(trainer));

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> service.toggleActive("john.doe", "wrong-password"));

    assertEquals("Invalid username or password", exception.getMessage());
    assertTrue(trainer.isActive());
  }

  private Trainer trainer(String firstName, String lastName) {
    Trainer trainer = new Trainer();
    trainer.setFirstName(firstName);
    trainer.setLastName(lastName);
    return trainer;
  }
}
