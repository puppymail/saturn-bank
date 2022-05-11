package com.saturn_bank.operator.service;

import static java.util.Objects.isNull;

import com.saturn_bank.operator.dao.Account;
import com.saturn_bank.operator.dao.Card;
import com.saturn_bank.operator.dao.CardFactory;
import com.saturn_bank.operator.dao.User;
import com.saturn_bank.operator.exception.DeletedEntityException;
import com.saturn_bank.operator.exception.NoSuchEntityException;
import com.saturn_bank.operator.repository.AccountRepository;
import com.saturn_bank.operator.repository.CardRepository;
import com.saturn_bank.operator.repository.UserRepository;
import com.saturn_bank.operator.service.validation.PinCodeValidator;
import com.saturn_bank.operator.service.validation.ValidationStrings;
import com.saturn_bank.operator.service.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
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

    @Transactional
    @Override
    public Card issueCard(Account account, User user) throws NoSuchEntityException, DeletedEntityException {
        if (isNull(account)) {
            log.error("!Account provided is null!");
            throw new NullPointerException("Account provided is null");
        }
        if (isNull(user)) {
            log.error("!User provided is null!");
            throw new NullPointerException("User provided is null");
        }

        Optional<Account> accountOpt = accountRepository.findOne(Example.of(account));
        Account existingAccount = null;

        existingAccount = Utils.softDeleteEntityValidityCheck(accountOpt, Account.class);

        Optional<User> userOpt = userRepository.findOne(Example.of(user));
        User existingUser = null;

        existingUser = Utils.softDeleteEntityValidityCheck(userOpt, User.class);

        Card card = CardFactory.createCard(existingAccount, existingUser);

        // FIXME: you can get saved card from 'save' method on repo, no need to access database again.
        cardRepository.save(card);

        Card savedCard = cardRepository.findById(card.getId()).orElseThrow(() -> new IllegalArgumentException("Saved card not found"));
        String binNumber = savedCard.getNumber().substring(0, 6);
        String coinType = savedCard.getNumber().substring(6, 8);
        String accountType = savedCard.getNumber().substring(8, 11);
        String identityNumber = String.format("%05d", savedCard.getId());

        savedCard.setNumber(binNumber + coinType + accountType + identityNumber);

        return savedCard;
    }

    public Card issueCard(Account account) throws NoSuchEntityException, DeletedEntityException {
        return this.issueCard(account, account.getUser());
    }

    @Override
    public void changePinCodeByOperator(String cardNumber) {
        if (Validator.validate(ValidationStrings.cardNumberRegexp, cardNumber)) {
            Card existingCard = cardRepository.findByNumber(cardNumber)
                    .orElseThrow(() -> new IllegalArgumentException("No such card number at DB"));
            existingCard.setPinCode(generatePinCode());
            cardRepository.save(existingCard);
            log.info("Card pincode was changed at card with number: " + existingCard.getNumber());
        } else {
            throw new IllegalArgumentException("card number didn't pass validation");
        }
    }

    @Override
    public void changePinCodeByClient(String cardNumber, String oldPinCode, String newPinCode) {
        if (Validator.validate(ValidationStrings.cardNumberRegexp, cardNumber)) {
            Card existingCard = cardRepository.findByNumber(cardNumber)
                    .orElseThrow(() -> new IllegalArgumentException("No such card number at DB"));
            PinCodeValidator.validatePinCode(existingCard.getPinCode(), oldPinCode, newPinCode);
            existingCard.setPinCode(newPinCode);
            cardRepository.save(existingCard);
            log.info("Card pincode was changed at card with number: " + existingCard.getNumber());
        } else {
            throw new IllegalArgumentException("card number didn't pass validation");
        }
    }

    private String generatePinCode() {
        int range = 9999;
        return String.format("%04d", new Random().nextInt(range));
    }

    @Transactional
    @Override
    public void closeCard(Card card) {
        if (isNull(card)) {
            log.error("!Card provided is null!");
            throw new NullPointerException("Card provided is null");
        }
        Optional<Card> existingCard;
        if ( ( existingCard = cardRepository.findOne(Example.of(card)) ).isEmpty() ) {
            log.error("!No such card found!");
            return;
        }
        card = existingCard.get();
        cardRepository.delete(card);
        log.info("Card with id=" + card.getId() + " set as deleted.");
    }

    @Override
    public void closeCard(Long id) {
        if (isNull(id) || id <= 0) {
            log.error("!Invalid id provided!");
            throw new IllegalArgumentException("Invalid id provided");
        }
        Card card = new Card();
        card.setId(id);
        closeCard(card);
    }

}
