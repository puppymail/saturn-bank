package com.saturn_bank.operator.service;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

import com.saturn_bank.operator.dao.Account;
import com.saturn_bank.operator.dao.AccountCoin;
import com.saturn_bank.operator.dao.AccountType;
import com.saturn_bank.operator.dao.Card;
import com.saturn_bank.operator.dao.CardFactory;
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

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    CardRepository cardRepository;

    @Mock
    AccountRepository accountRepository;

    @Mock
    CardFactory cardFactory;

    CardService cardService;

    @BeforeEach
    void setUp() {
        cardService = new CardServiceImpl(
                cardRepository,
                accountRepository,
                userRepository,
                cardFactory);
    }

    @Test
    public void issueCard_issueNewCard_saveInvoked() throws NoSuchEntityException, DeletedEntityException {
        Account account = Account.builder()
                .coin(AccountCoin.RUB)
                .type(AccountType.REGULAR)
                .build();
        User user = new User();
        Card card = new Card();
        card.setId(1L);
        when(accountRepository.findOne(any())).thenReturn(Optional.of(account));
        when(userRepository.findOne(any())).thenReturn(Optional.of(user));
        when(cardRepository.save(any())).thenReturn(card);
        when(cardFactory.createCard(account, user)).thenReturn(card);
        cardService.issueCard(account, user);

        verify(accountRepository, times(1)).findOne(any());
        verify(userRepository, times(1)).findOne(any());
        verify(cardRepository, times(2)).save(any());

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
