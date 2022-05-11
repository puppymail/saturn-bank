package com.saturn_bank.operator.service;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

import com.saturn_bank.operator.dao.Account;
import com.saturn_bank.operator.dao.Card;
import com.saturn_bank.operator.dao.User;
import com.saturn_bank.operator.exception.DeletedEntityException;
import com.saturn_bank.operator.exception.NoSuchEntityException;
import com.saturn_bank.operator.repository.AccountRepository;
import com.saturn_bank.operator.repository.CardRepository;
import com.saturn_bank.operator.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CardServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    CardRepository cardRepository;

    @Mock
    AccountRepository accountRepository;

    CardService cardService;

    @BeforeEach
    void setUp() {
        cardService = new CardServiceImpl(
                cardRepository,
                accountRepository,
                userRepository);
    }

    @Test
    public void issueCard_issueNewCard_saveInvoked() throws NoSuchEntityException, DeletedEntityException {
        Account account = new Account();
        User user = new User();
        when(accountRepository.findOne(any())).thenReturn(Optional.of(account));
        when(userRepository.findOne(any())).thenReturn(Optional.of(user));
        cardService.issueCard(account, user);

        verify(accountRepository, times(1)).findOne(any());
        verify(userRepository, times(1)).findOne(any());
        verify(cardRepository, times(1)).save(any());

        verifyNoMoreInteractions(userRepository, accountRepository, cardRepository);
    }

    @Test
    public void closeCardByExample_closeExistingCard_deleteInvoked() {
        Card card = new Card();
        when(cardRepository.findOne(any())).thenReturn(Optional.of(card));
        cardService.closeCard(card);

        InOrder inOrder = inOrder(cardRepository);
        inOrder.verify(cardRepository, times(1)).findOne(any());
        inOrder.verify(cardRepository, times(1)).delete(any());

        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void closeCardById_closeExistingCard_deleteInvoked() {
        Card card = new Card();
        when(cardRepository.findOne(any())).thenReturn(Optional.of(card));
        cardService.closeCard(1L);

        InOrder inOrder = inOrder(cardRepository);
        inOrder.verify(cardRepository, times(1)).findOne(any());
        inOrder.verify(cardRepository, times(1)).delete(any());

        inOrder.verifyNoMoreInteractions();
    }
}