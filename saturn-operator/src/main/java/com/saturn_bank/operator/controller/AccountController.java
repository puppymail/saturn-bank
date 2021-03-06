package com.saturn_bank.operator.controller;

import com.saturn_bank.operator.dao.Account;
import com.saturn_bank.operator.dao.TransactionType;
import com.saturn_bank.operator.dao.User;
import com.saturn_bank.operator.dto.AccountDto;
import com.saturn_bank.operator.dto.TimeRange;
import com.saturn_bank.operator.dto.TransactionHistoryDto;
import com.saturn_bank.operator.dto.TransactionDto;
import com.saturn_bank.operator.repository.AccountRepository;
import com.saturn_bank.operator.service.TransactionService;
import com.saturn_bank.operator.service.user.UserService;
import com.saturn_bank.operator.service.account.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    private final String ATTRIBUTE_BANK_USER= "bankUser"; 
    private final String ATTRIBUTE_ACCOUNTS = "accounts";
    private final String ATTRIBUTE_ACCOUNT = "account";
    private final String ATTRIBUTE_NO_ACCOUNT = "noAccount";
    private final String ATTRIBUTE_ACCOUNT_HISTORY = "accountHistory";
    private final String ATTRIBUTE_HISTORY_PERIOD = "fullHistoryPeriod";
    private final String ATTRIBUTE_HISTORY_RANGE = "historyRange";

    private final String REDIRECT_USERS = "redirect:/users/";
    private final String REDIRECT_ACTIVE_USER_ACCOUNTS = "redirect:/accounts/active-accounts/";
    private final String REDIRECT_ADD_ACCOUNT = "redirect:/add-account/";

    private final String TEMPLATE_LIST_OF_ACCOUNTS_ALL_USERS = "accounts/listOfAccounts";
    private final String TEMPLATE_USER_ACCOUNTS = "accounts/userAccounts";
    private final String TEMPLATE_USER_ACCOUNT = "accounts/userAccount";
    private final String TEMPLATE_LIST_OF_USER_ACCOUNTS = "accounts/userAccountsAll";
    private final String TEMPLATE_OPEN_NEW_ACCOUNT = "accounts/openNewAccount";
    private final String TEMPLATE_ACCOUNT_TRANSACTIONS_HISTORY = "accounts/accountHistory";


    @Autowired
    public AccountController(AccountService accountService,
                             UserService userService,
                             AccountRepository accountRepository,
                             TransactionService transactionService) {
        this.accountService = accountService;
        this.userService = userService;
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public String listOfAccounts(Model model) {
        List<Account> accounts = accountService.findAll();
        model.addAttribute(ATTRIBUTE_ACCOUNTS, accounts);
        List<User> users = userService.findAll();
        model.addAttribute(ATTRIBUTE_BANK_USER, users);
        return TEMPLATE_LIST_OF_ACCOUNTS_ALL_USERS;
    }

    @GetMapping("/accounts-info/{id}")
    public String showUserAccountsForm(@PathVariable("id") Long id, Model model) {
        return tryAddUserToModel(id, model).orElse(TEMPLATE_USER_ACCOUNTS);
    }

    @GetMapping("/all-accounts/{id}")
    public String showUserAccountsAll(@PathVariable("id") Long id, Model model) {
        final Optional<String> errResponse = tryAddUserToModel(id, model);
        if (errResponse.isPresent()) {
            return errResponse.get();
        }

        User user = (User) model.getAttribute(ATTRIBUTE_BANK_USER);
        List<Account> accounts = accountService.getAllUserAccounts(user);
        model.addAttribute(ATTRIBUTE_ACCOUNTS, accounts);
        return TEMPLATE_LIST_OF_USER_ACCOUNTS;
    }

    @GetMapping("/all-accounts/{id}/{idAcc}")
    public String showUserAccount(@PathVariable("id") Long id, @PathVariable("idAcc") Long idAcc, Model model) {
        final Optional<String> errResponse = tryAddUserToModel(id, model);
        if (errResponse.isPresent()) {
            return errResponse.get();
        }

        addAccountToModel(idAcc, model);
        return TEMPLATE_USER_ACCOUNT;
    }

    @PostMapping("/add")
    public String addPostTransactionByService(@RequestBody TransactionDto transaction) {
        transactionService.transfer(transaction.getAccountSrc(), transaction.getAccountDst(), transaction.getAmount(), transaction.getPurpose(), transaction.getType());
        return "transactions/hello";
    }

    @PutMapping("/all-accounts/{id}/{idAcc}")
    public String setAccountDefault(@PathVariable("id") Long id, @PathVariable("idAcc") Long idAcc, Model model) {
        final Optional<String> errResponse = tryAddUserToModel(id, model);
        if (errResponse.isPresent()) {
            return errResponse.get();
        }

        addAccountToModel(idAcc, model);
        if (model.containsAttribute(ATTRIBUTE_ACCOUNT)) {
            accountService.setAccountDefault(idAcc);
        }
        return TEMPLATE_USER_ACCOUNT;
    }

    @DeleteMapping("/all-accounts/{id}/{idAcc}")
    public String deleteUserAccount(@PathVariable("id") Long id, @PathVariable("idAcc") Long idAcc, Model model) {
        Optional<Account> accountOptional = accountService.findAccountByAccountId(idAcc);
        if (accountOptional.isEmpty()) {
            model.addAttribute(ATTRIBUTE_NO_ACCOUNT, Boolean.TRUE);
            return TEMPLATE_USER_ACCOUNT;
        }
        accountService.closeAccount(accountOptional.get());
        return REDIRECT_ACTIVE_USER_ACCOUNTS + id;
    }

    @GetMapping("/active-accounts/{id}")
    public String showUserAccountsActive(@PathVariable("id") Long id, Model model) {
        final Optional<String> errResponse = tryAddUserToModel(id, model);
        if (errResponse.isPresent()) {
            return errResponse.get();
        }

        User user = (User) model.getAttribute(ATTRIBUTE_BANK_USER);
        List<Account> accounts = accountService.getActiveUserAccounts(user);
        model.addAttribute(ATTRIBUTE_ACCOUNTS, accounts);
        return TEMPLATE_LIST_OF_USER_ACCOUNTS;
    }

    @GetMapping("/add-account/{id}")
    public String showOpenAccountForm(@PathVariable("id") Long id, Model model) {
        return tryAddUserToModel(id, model).orElse(TEMPLATE_OPEN_NEW_ACCOUNT);
    }

    @GetMapping("/all-accounts/{id}/{idAcc}/account-history/{full}")
    public String showAccountHistory(@PathVariable("id") Long id,
                                     @PathVariable("idAcc") Long idAcc,
                                     @PathVariable Boolean full,
                                     @ModelAttribute("historyRange") TimeRange range,
                                     Model model) {
        final Optional<String> errResponse = tryAddUserToModel(id, model);
        if (errResponse.isPresent()) {
            return errResponse.get();
        }
        addAccountToModel(idAcc, model);
        model.addAttribute(ATTRIBUTE_HISTORY_PERIOD, full);
        Account account = (Account) model.getAttribute(ATTRIBUTE_ACCOUNT);
        List<TransactionHistoryDto> transactionDtoList = accountService.getAccountTransactionsHistory(account, full);
        model.addAttribute(ATTRIBUTE_ACCOUNT_HISTORY, transactionDtoList);
        return TEMPLATE_ACCOUNT_TRANSACTIONS_HISTORY;
    }

    @PutMapping("/all-accounts/{id}/{idAcc}/account-history/range")
    public String showAccountHistoryForTimeRange(@PathVariable("id") Long id,
                                                 @PathVariable("idAcc") Long idAcc,
                                                 @ModelAttribute("historyRange") TimeRange range,
                                                 Model model) {
        final Optional<String> errResponse = tryAddUserToModel(id, model);
        if (errResponse.isPresent()) {
            return errResponse.get();
        }
        addAccountToModel(idAcc, model);
        model.addAttribute(ATTRIBUTE_HISTORY_PERIOD, null);
        Account account = (Account) model.getAttribute(ATTRIBUTE_ACCOUNT);
        List<TransactionHistoryDto> transactionDtoList = accountService.getAccountTransactionsHistoryForTimeRange(account, range);
        model.addAttribute(ATTRIBUTE_ACCOUNT_HISTORY, transactionDtoList);
        return TEMPLATE_ACCOUNT_TRANSACTIONS_HISTORY;
    }

    @PostMapping("/add-account/{id}")
    public String openAccount(@PathVariable("id") Long id,
                              @ModelAttribute("accountDto") AccountDto accountDto,
                              BindingResult bindingResult,
                              Model model) {
        final Optional<String> errResponse = tryAddUserToModel(id, model);
        if (errResponse.isPresent()) {
            return errResponse.get();
        }
        if (bindingResult.hasErrors()) {
            return REDIRECT_ADD_ACCOUNT + id;
        }
        accountService.openAccount(accountDto);
        return REDIRECT_ACTIVE_USER_ACCOUNTS + id;
    }

    @GetMapping("/transfer-by-card&src-number={srcNumber}&card={card}&amount={amount}")
    public String transferByCard (@PathVariable String srcNumber, @PathVariable String card,
                                  @PathVariable BigDecimal amount) {
        Account accountSrc = accountRepository.findByNumber(srcNumber)
                .orElseThrow(() -> new IllegalArgumentException("No source account with this number"));
        accountService.transfer(accountSrc, card, amount, "For learning Spring", TransactionType.TRANSFER,  "BY_CARD");
        return "redirect:/accounts/";
    }

    @GetMapping("/transfer-by-phone&src-number={srcNumber}&phone={phone}&amount={amount}")
    public String transferByPhoneNumber (@PathVariable String srcNumber, @PathVariable String phone,
                                         @PathVariable BigDecimal amount) {
        Account accountSrc = accountRepository.findByNumber(srcNumber)
                .orElseThrow(() -> new IllegalArgumentException("No source account with this number"));
        accountService.transfer(accountSrc, phone, amount, "For learning Spring", TransactionType.TRANSFER, "BY_PHONE");
        return "redirect:/accounts/";
    }

    @GetMapping("/transfer-by-account-number&src-number={srcNumber}&dst-number={dstNumber}&amount={amount}")
    public String transferByAccountNumber (@PathVariable String srcNumber, @PathVariable String dstNumber,
                                           @PathVariable BigDecimal amount) {
        Account accountSrc = accountRepository.findByNumber(srcNumber)
                .orElseThrow(() -> new IllegalArgumentException("No source account with this number"));
        accountService.transfer(accountSrc, dstNumber, amount, "For learning Spring", TransactionType.TRANSFER, "BY_ACCOUNT");
        return "redirect:/accounts/";
    }

    @ModelAttribute("accountDto")
    public AccountDto getDefaultAccountDto() {
        return new AccountDto();
    }

    @ModelAttribute("historyRange")
    public TimeRange getDefaultTimeRange() {
        return new TimeRange();
    }

    private void addAccountToModel(Long idAcc, Model model) {
        Optional<Account> accountOptional = accountService.findAccountByAccountId(idAcc);
        if (accountOptional.isPresent()) {
            model.addAttribute(ATTRIBUTE_ACCOUNT, accountOptional.get());
        } else {
            model.addAttribute(ATTRIBUTE_NO_ACCOUNT, Boolean.TRUE);
        }
    }

    /**
     * @return empty response if user found, and redirect response if not found
     */
    private Optional<String> tryAddUserToModel(Long id, Model model) {
        final Optional<User> userOptional = userService.findById(id);
        if (userOptional.isEmpty()) {
            return Optional.of(REDIRECT_USERS);
        }

        User user = userOptional.get();
        model.addAttribute(ATTRIBUTE_BANK_USER, user);

        return Optional.empty();
    }
}
