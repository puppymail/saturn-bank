package com.saturn_bank.operator.controller;

import com.saturn_bank.operator.dao.Account;
import com.saturn_bank.operator.dao.Card;
import com.saturn_bank.operator.dao.User;
import com.saturn_bank.operator.repository.AccountRepository;
import com.saturn_bank.operator.repository.CardRepository;
import com.saturn_bank.operator.repository.UserRepository;
import com.saturn_bank.operator.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


//This is test controller for card, will be deleted
@Controller
@RequestMapping("/cards")
public class CardController {

    private static final String NO_ACCOUNT_WITH_THIS_NUMBER = "No account with this number";
    private static final String NO_USER_WITH_THIS_ID = "No user with this id";

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CardService cardService;

    @Autowired
    public CardController(CardRepository cardRepository, UserRepository userRepository, AccountRepository accountRepository, CardService cardService) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.cardService = cardService;
    }

    @GetMapping("/issue-card-for&account-number={accountNumber}&user-id={userId}")
    @ResponseBody
    public String issueCard(@PathVariable String accountNumber, @PathVariable long userId) {
        Account account = accountRepository.findByNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException(NO_ACCOUNT_WITH_THIS_NUMBER));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(NO_USER_WITH_THIS_ID));
        Card card = cardService.issueCard(account, user);
        return "Card was issued: " + card;
    }

    @GetMapping("change-pincode-by-operator&card-number={cardNumber}")
    @ResponseBody
    public String changePinCodeByOperator(@PathVariable String cardNumber){
        cardService.changePinCodeByOperator(cardNumber);
        Card card = cardRepository.findByNumber(cardNumber).orElseThrow();
        return "Pincode was changed at card: " + card;
    }

    @GetMapping("change-pincode-by-client&card-number={cardNumber}&old-pincode={oldPinCode}&new-pincode={newPinCode}")
    @ResponseBody
    public String changePinCodeByClient(@PathVariable String cardNumber, @PathVariable String oldPinCode, @PathVariable String newPinCode){
        cardService.changePinCodeByClient(cardNumber, oldPinCode, newPinCode);
        Card card = cardRepository.findByNumber(cardNumber).orElseThrow();
        return "Pincode was changed at card: " + card;
    }

    //test method for soft-deleting row from DB
    @GetMapping("close&card-number={cardNumber}")
    @ResponseBody
    public String close(@PathVariable String cardNumber) {
        Card card = cardRepository.findByNumber(cardNumber).orElseThrow();
        cardService.closeCard(card);
        return card + " was closed";
    }
}
