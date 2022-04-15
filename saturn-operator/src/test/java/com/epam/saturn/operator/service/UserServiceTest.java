package com.epam.saturn.operator.service;

import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
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
        Mockito.when(userRepository.findAll()).thenReturn(expected);
        List<User> actual = userService.findAll();
        Assertions.assertEquals(expected, actual);
        Mockito.verify(userRepository).findAll();

        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findAll_findAllNotDeletedUsers_findByIsDeletedInvoked() {
        List<User> expected = List.of(userProvider.get(rnd.nextInt(userProvider.getUsersSize())));
        Mockito.when(userRepository.findByIsDeleted(Boolean.FALSE)).thenReturn(expected);
        List<User> actual = userService.findAll(Boolean.FALSE);
        Assertions.assertEquals(expected, actual);
        Mockito.verify(userRepository).findByIsDeleted(Boolean.FALSE);

        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    void createUser_createRandomUser_saveAndExistsInvoked() {
        int id = rnd.nextInt(userProvider.getUsersSize());
        User user = userProvider.get(id);
        userService.createUser(user);

        InOrder inOrder = Mockito.inOrder(userRepository);
        inOrder.verify(userRepository).existsById(Mockito.anyLong());
        inOrder.verify(userRepository).save(Mockito.any());

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void editUser_updateUserById_saveAndFindInvoked() {
        int id = rnd.nextInt(userProvider.getUsersSize());
        int existingId = rnd.nextInt(userProvider.getUsersSize());
        User updatedUser = userProvider.get(id);
        User existingUser = userProvider.get(existingId);
        existingUser.setId(id + 1L);

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(existingUser));
        userService.editUser(updatedUser, id + 1L);

        InOrder inOrder = Mockito.inOrder(userRepository);
        inOrder.verify(userRepository).findById(Mockito.anyLong());
        inOrder.verify(userRepository).save(Mockito.any());

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void editUser_updateUserByExample_saveAndFindInvoked() {
        int id = rnd.nextInt(userProvider.getUsersSize());
        int existingId = rnd.nextInt(userProvider.getUsersSize());
        User updatedUser = userProvider.get(id);
        User existingUser = userProvider.get(existingId);
        existingUser.setId(id + 1L);

        Mockito.when(userRepository.findOne(Example.of(existingUser))).thenReturn(Optional.of(existingUser));
        userService.editUser(updatedUser, existingUser);

        InOrder inOrder = Mockito.inOrder(userRepository);
        inOrder.verify(userRepository).findOne(Mockito.any());
        inOrder.verify(userRepository).save(Mockito.any());

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void deleteUser_deleteUserById_deleteInvoked() {
        int id = rnd.nextInt(userProvider.getUsersSize());
        User user = userProvider.get(id);

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        userService.deleteUser(id + 1L);

        InOrder inOrder = Mockito.inOrder(userRepository);
        inOrder.verify(userRepository).findById(Mockito.anyLong());
        inOrder.verify(userRepository).delete(Mockito.any());

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void deleteUser_deleteUserByExample_deleteInvoked() {
        int id = rnd.nextInt(userProvider.getUsersSize());
        User user = userProvider.get(id);

        Mockito.when(userRepository.findOne(Example.of(user))).thenReturn(Optional.of(user));
        userService.deleteUser(user);

        InOrder inOrder = Mockito.inOrder(userRepository);
        inOrder.verify(userRepository).findOne(Mockito.any());
        inOrder.verify(userRepository).delete(Mockito.any());

        inOrder.verifyNoMoreInteractions();
    }

}
