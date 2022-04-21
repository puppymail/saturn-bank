package com.epam.saturn.operator.controller;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.Card;
import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.repository.AccountRepository;
import com.epam.saturn.operator.repository.CardRepository;
import com.epam.saturn.operator.repository.UserRepository;
import com.epam.saturn.operator.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


//This is test controller for card, will be deleted
@Controller
@RequestMapping("/cards")
public class CardController {

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

    @GetMapping("/issue-card")
    public String issueCard() {
        Account account = accountRepository.findAll()
                .stream()
                .findAny()
                .orElseThrow();
        User user = userRepository.findAll()
                .stream()
                .findAny()
                .orElseThrow();
        Card card = cardService.issueCard(account, user);
        return "redirect:/cards";
    }

    @GetMapping("change-pincode-by-operator")
    public String changePinCodeByOperator(){
        String cardNumber = cardRepository.findAll()
                .stream()
                .findAny()
                .orElseThrow()
                .getNumber();
        cardService.changePinCodeByOperator(cardNumber);
        return "redirect:/cards";
    }

    @GetMapping("change-pincode-by-client&old-pincode={oldPinCode}&new-pincode={newPinCode}")
    public String changePinCodeByClient(@PathVariable String oldPinCode, @PathVariable String newPinCode){
        Card card = cardRepository.findAll()
                .stream()
                .findAny()
                .orElseThrow();
        cardService.changePinCodeByClient(card.getNumber(), oldPinCode, newPinCode);
        return "redirect:/cards";
    }

    //test method for soft-deleting row from DB
    @GetMapping("delete")
    public String remove() {
        Card card = cardRepository.findAll().stream().findFirst().orElseThrow();
        cardRepository.delete(card);
        return "redirect:/cards";
    }
}
