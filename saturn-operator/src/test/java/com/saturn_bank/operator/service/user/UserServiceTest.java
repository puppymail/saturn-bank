package com.saturn_bank.operator.service.user;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.saturn_bank.operator.dao.User;
import com.saturn_bank.operator.repository.UserRepository;
import com.saturn_bank.operator.exception.EntityAlreadyPresentException;
import com.saturn_bank.operator.exception.NoSuchEntityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder encoder;

    UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, encoder);
    }

    @Test
    void findAll_findAllUsers_findAllInvoked() {
        List<User> expected = List.of(
                User.builder().id(1L).build(),
                User.builder().id(2L).build(),
                User.builder().id(3L).build());
        when(userRepository.findAll()).thenReturn(expected);
        List<User> actual = userService.findAll();
        assertEquals(expected, actual);
        verify(userRepository, times(1)).findAll();

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findAll_findAllNotDeletedUsers_findByIsDeletedInvoked() {
        List<User> expected = List.of(
                User.builder().id(1L).build(),
                User.builder().id(2L).build());
        when(userRepository.findByIsDeleted(FALSE)).thenReturn(expected);
        List<User> actual = userService.findAll(FALSE);
        assertEquals(expected, actual);
        verify(userRepository, times(1)).findByIsDeleted(FALSE);

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void createUser_createNewUser_saveAndExistsInvoked() throws EntityAlreadyPresentException {
        User user = User.builder().id(1L).password("password").build();
        User spyUser = spy(user);

        when(encoder.encode(anyString())).thenReturn("password");
        userService.createUser(spyUser);

        InOrder inOrder = inOrder(userRepository, encoder, spyUser);
        inOrder.verify(userRepository, times(1)).existsById(anyLong());
        inOrder.verify(spyUser, times(1)).getPassword();
        inOrder.verify(encoder, times(1)).encode(anyString());
        inOrder.verify(spyUser, times(1)).setPassword(anyString());
        inOrder.verify(userRepository, times(1)).save(any());

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void createUser_createExistingUser_exceptionThrown() {
        User user = User.builder().id(1L).password("password").build();

        when(userRepository.existsById(anyLong())).thenReturn(TRUE);
        assertThrows(EntityAlreadyPresentException.class, () -> userService.createUser(user));

        verify(userRepository, times(1)).existsById(anyLong());

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void editUser_updateUserById_saveAndFindInvoked() throws NoSuchEntityException {
        User existingUser = User.builder().id(1L).firstName("Name 1").build();
        User updatedUser = User.builder().id(1L).firstName("Name 2").build();

        when(userRepository.findOne(any())).thenReturn(Optional.of(existingUser));
        userService.editUser(updatedUser, existingUser.getId());

        InOrder inOrder = inOrder(userRepository);
        inOrder.verify(userRepository, times(1)).findOne(any());
        inOrder.verify(userRepository, times(1)).save(any());

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void editUser_updateUserByExample_saveAndFindInvoked() throws NoSuchEntityException {
        User existingUser = User.builder().id(1L).firstName("Name 1").build();
        User updatedUser = User.builder().id(1L).firstName("Name 2").build();

        when(userRepository.findOne(Example.of(existingUser))).thenReturn(Optional.of(existingUser));
        userService.editUser(updatedUser, existingUser);

        InOrder inOrder = inOrder(userRepository);
        inOrder.verify(userRepository, times(1)).findOne(any());
        inOrder.verify(userRepository, times(1)).save(any());

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void editUser_updateNonExistentUser_exceptionThrown() throws NoSuchEntityException {
        User existingUser = User.builder().id(1L).firstName("Name 1").build();
        User updatedUser = User.builder().id(1L).firstName("Name 2").build();

        when(userRepository.findOne(Example.of(existingUser))).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> userService.editUser(updatedUser, existingUser));

        verify(userRepository, times(1)).findOne(any());

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void changePassword_updateUserById_saveAndFindInvoked() throws NoSuchEntityException {
        User user = User.builder().id(1L).password("old password").build();
        User spyUser = spy(user);

        when(encoder.encode(anyString())).thenReturn("new password");
        when(userRepository.findOne(any())).thenReturn(Optional.of(spyUser));

        assertEquals("old password", spyUser.getPassword());
        userService.changePassword(1L, "new password");
        assertEquals("new password", spyUser.getPassword());

        InOrder inOrder = inOrder(userRepository, encoder, spyUser);
        inOrder.verify(spyUser, times(1)).getPassword();
        inOrder.verify(userRepository, times(1)).findOne(any());
        inOrder.verify(encoder, times(1)).encode(anyString());
        inOrder.verify(spyUser, times(1)).setPassword(anyString());
        inOrder.verify(userRepository, times(1)).save(any());
        inOrder.verify(spyUser, times(1)).getPassword();

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void changePassword_updateUserByExample_saveAndFindInvoked() throws NoSuchEntityException {
        User user = User.builder().id(1L).password("old password").build();
        User spyUser = spy(user);

        when(encoder.encode(anyString())).thenReturn("new password");
        when(userRepository.findOne(any())).thenReturn(Optional.of(spyUser));

        assertEquals("old password", spyUser.getPassword());
        userService.changePassword(spyUser, "new password");
        assertEquals("new password", spyUser.getPassword());

        InOrder inOrder = inOrder(userRepository, encoder, spyUser);
        inOrder.verify(spyUser, times(1)).getPassword();
        inOrder.verify(userRepository, times(1)).findOne(any());
        inOrder.verify(encoder, times(1)).encode(anyString());
        inOrder.verify(spyUser, times(1)).setPassword(anyString());
        inOrder.verify(userRepository, times(1)).save(any());
        inOrder.verify(spyUser, times(1)).getPassword();

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void changePassword_updateNonExistentUser_exceptionThrown() throws NoSuchEntityException {
        User user = User.builder().id(1L).password("old password").build();
        User spyUser = spy(user);

        when(encoder.encode(anyString())).thenReturn("new password");
        when(userRepository.findOne(any())).thenReturn(Optional.of(spyUser));

        assertEquals("old password", spyUser.getPassword());
        userService.changePassword(spyUser, "new password");
        assertEquals("new password", spyUser.getPassword());

        InOrder inOrder = inOrder(userRepository, encoder, spyUser);
        inOrder.verify(spyUser, times(1)).getPassword();
        inOrder.verify(userRepository, times(1)).findOne(any());
        inOrder.verify(encoder, times(1)).encode(anyString());
        inOrder.verify(spyUser, times(1)).setPassword(anyString());
        inOrder.verify(userRepository, times(1)).save(any());
        inOrder.verify(spyUser, times(1)).getPassword();

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void deleteUser_deleteUserById_deleteInvoked() throws NoSuchEntityException {
        User user = User.builder().id(1L).build();

        when(userRepository.findOne(any())).thenReturn(Optional.of(user));
        userService.deleteUser(1L);

        InOrder inOrder = inOrder(userRepository);
        inOrder.verify(userRepository, times(1)).findOne(any());
        inOrder.verify(userRepository, times(1)).delete(any());

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void deleteUser_deleteUserByExample_deleteInvoked() throws NoSuchEntityException {
        User user = User.builder().id(1L).build();

        when(userRepository.findOne(any())).thenReturn(Optional.of(user));
        userService.deleteUser(user);

        InOrder inOrder = inOrder(userRepository);
        inOrder.verify(userRepository, times(1)).findOne(any());
        inOrder.verify(userRepository, times(1)).delete(any());

        inOrder.verifyNoMoreInteractions();
    }

}
