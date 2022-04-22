package com.epam.saturn.operator.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.repository.UserRepository;
import com.epam.saturn.operator.service.exceptions.EntityAlreadyPresentException;
import com.epam.saturn.operator.service.exceptions.NoSuchEntityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    UserService userService;

    @Autowired
    TestUserProvider userProvider;

    Random rnd;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
        rnd = new Random();
    }

    @Test
    void findAll_findAllUsers_findAllInvoked() {
        List<User> expected = List.of(userProvider.get(rnd.nextInt(userProvider.getUsersSize())));
        when(userRepository.findAll()).thenReturn(expected);
        List<User> actual = userService.findAll();
        assertEquals(expected, actual);
        verify(userRepository, times(1)).findAll();

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findAll_findAllNotDeletedUsers_findByIsDeletedInvoked() {
        List<User> expected = List.of(userProvider.get(rnd.nextInt(userProvider.getUsersSize())));
        when(userRepository.findByIsDeleted(Boolean.FALSE)).thenReturn(expected);
        List<User> actual = userService.findAll(Boolean.FALSE);
        assertEquals(expected, actual);
        verify(userRepository, times(1)).findByIsDeleted(Boolean.FALSE);

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void createUser_createRandomUser_saveAndExistsInvoked() {
        int id = rnd.nextInt(userProvider.getUsersSize());
        User user = userProvider.get(id);
        userService.createUser(user);

        InOrder inOrder = inOrder(userRepository);
        inOrder.verify(userRepository, times(1)).existsById(anyLong());
        inOrder.verify(userRepository, times(1)).save(any());

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void createUser_createExistingUser_exceptionThrown() {
        int id = rnd.nextInt(userProvider.getUsersSize());
        User user = userProvider.get(id);
        when(userRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        assertThrows(EntityAlreadyPresentException.class, () -> userService.createUser(user));

        verify(userRepository, times(1)).existsById(anyLong());

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void editUser_updateUserById_saveAndFindInvoked() {
        int id = rnd.nextInt(userProvider.getUsersSize());
        int existingId = rnd.nextInt(userProvider.getUsersSize());
        User updatedUser = userProvider.get(id);
        User existingUser = userProvider.get(existingId);
        existingUser.setId(id + 1L);

        when(userRepository.findOne(any())).thenReturn(Optional.of(existingUser));
        userService.editUser(updatedUser, existingUser.getId());

        InOrder inOrder = inOrder(userRepository);
        inOrder.verify(userRepository, times(1)).findOne(any());
        inOrder.verify(userRepository, times(1)).save(any());

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void editUser_updateUserByExample_saveAndFindInvoked() {
        int id = rnd.nextInt(userProvider.getUsersSize());
        int existingId = rnd.nextInt(userProvider.getUsersSize());
        User updatedUser = userProvider.get(id);
        User existingUser = userProvider.get(existingId);
        existingUser.setId(id + 1L);

        when(userRepository.findOne(Example.of(existingUser))).thenReturn(Optional.of(existingUser));
        userService.editUser(updatedUser, existingUser);

        InOrder inOrder = inOrder(userRepository);
        inOrder.verify(userRepository, times(1)).findOne(any());
        inOrder.verify(userRepository, times(1)).save(any());

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void editUser_updateNonExistentUser_exceptionThrown() {
        int id = rnd.nextInt(userProvider.getUsersSize());
        int existingId = rnd.nextInt(userProvider.getUsersSize());
        User updatedUser = userProvider.get(id);
        User existingUser = userProvider.get(existingId);
        existingUser.setId(id + 1L);

        when(userRepository.findOne(Example.of(existingUser))).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> userService.editUser(updatedUser, existingUser));

        verify(userRepository, times(1)).findOne(any());

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deleteUser_deleteUserById_deleteInvoked() {
        int id = rnd.nextInt(userProvider.getUsersSize());
        User user = userProvider.get(id);

        when(userRepository.findOne(Mockito.any())).thenReturn(Optional.of(user));
        userService.deleteUser(id + 1L);

        InOrder inOrder = inOrder(userRepository);
        inOrder.verify(userRepository, times(1)).findOne(any());
        inOrder.verify(userRepository, times(1)).delete(any());

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void deleteUser_deleteUserByExample_deleteInvoked() {
        int id = rnd.nextInt(userProvider.getUsersSize());
        User user = userProvider.get(id);

        when(userRepository.findOne(Example.of(user))).thenReturn(Optional.of(user));
        userService.deleteUser(user);

        InOrder inOrder = inOrder(userRepository);
        inOrder.verify(userRepository, times(1)).findOne(any());
        inOrder.verify(userRepository, times(1)).delete(any());

        inOrder.verifyNoMoreInteractions();
    }

}
