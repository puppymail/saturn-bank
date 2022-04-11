package com.epam.saturn.operator.service;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.Card;
import com.epam.saturn.operator.dao.CardFactory;
import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.repository.AccountRepository;
import com.epam.saturn.operator.repository.CardRepository;
import com.epam.saturn.operator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository,
                           AccountRepository accountRepository,
                           UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Card issueCard(Account account, User user) {
        Account existingAccount = accountRepository.findOne(Example.of(account))
                .orElseThrow();
        User cardUser = userRepository.findOne(Example.of(user))
                .orElseThrow();
        Card card = CardFactory.createCard(existingAccount, cardUser);
        
        cardRepository.save(card);

        return card;
    }

    public Card issueCard(Account account) {
        return this.issueCard(account, account.getUser());
    }

}
