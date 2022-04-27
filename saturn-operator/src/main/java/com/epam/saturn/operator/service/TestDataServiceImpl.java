package com.epam.saturn.operator.service;

import com.epam.saturn.operator.dao.Account;
import com.epam.saturn.operator.dao.AccountCoin;
import com.epam.saturn.operator.dao.AccountType;
import com.epam.saturn.operator.dao.Card;
import com.epam.saturn.operator.dao.Transaction;
import com.epam.saturn.operator.dao.TransactionState;
import com.epam.saturn.operator.dao.TransactionType;
import com.epam.saturn.operator.dao.User;
import com.epam.saturn.operator.dao.UserRole;
import com.epam.saturn.operator.dao.UserType;
import com.epam.saturn.operator.repository.AccountRepository;
import com.epam.saturn.operator.repository.CardRepository;
import com.epam.saturn.operator.repository.TransactionRepository;
import com.epam.saturn.operator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Profile("test")
public class TestDataServiceImpl implements TestDataService{

    static final String TEST_USERS_FILE = "saturn-operator/src/main/resources/testUserData.xml";
    static final String TEST_ACCOUNTS_FILE = "saturn-operator/src/main/resources/testAccountData.xml";
    static final String TEST_CARDS_FILE = "saturn-operator/src/main/resources/testCardData.xml";
    static final String TEST_TRANSACTIONS_FILE = "saturn-operator/src/main/resources/testTransactionData.xml";

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;

    @Autowired
    TestDataServiceImpl(UserRepository userRepository,
                        AccountRepository accountRepository,
                        TransactionRepository transactionRepository,
                        CardRepository cardRepository){

        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this. cardRepository = cardRepository;
    }

    @PostConstruct
    public void populateDB() throws ParserConfigurationException, IOException, SAXException {
        List<User> users = userRepository.findAll();
        List<Account> accounts = accountRepository.findAll();
        List<Card> cards = cardRepository.findAll();
        List<Transaction> transactions = transactionRepository.findAll();
        if (users.isEmpty() && accounts.isEmpty() && cards.isEmpty() && transactions.isEmpty()) {
            users = parseTestUsers();
            for (User user : users) {
                userRepository.save(user);
            }
            accounts = parseTestAccounts();
            for (Account account: accounts) {
                accountRepository.save(account);
            }
            cards = parseTestCards();
            for (Card card: cards){
                cardRepository.save(card);
            }
            transactions = parseTestTransactions();
            for (Transaction transaction: transactions){
                transactionRepository.save(transaction);
            }
        }
    }

    private List<User> parseTestUsers() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new File(TEST_USERS_FILE));
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("user");
        Node node;
        List<User> users = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); ++i){
            User user = new User();
            node = nodeList.item(i);
            user.setFirstName(Objects.requireNonNull(getChildContent((Element) node, "firstName")).getTextContent());
            user.setLastName(Objects.requireNonNull(getChildContent((Element) node, "lastName")).getTextContent());
            user.setMiddleName(Objects.requireNonNull(getChildContent((Element) node, "middleName")).getTextContent());
            user.setPhoneNumber(Objects.requireNonNull(getChildContent((Element) node, "phoneNumber")).getTextContent());
            user.setBirthDate(LocalDate.parse(Objects.requireNonNull(getChildContent((Element) node, "dateOfBirth")).getTextContent()));
            user.setEmail(Objects.requireNonNull(getChildContent((Element) node, "email")).getTextContent());
            user.setType(UserType.valueOf(Objects.requireNonNull(getChildContent((Element) node, "type")).getTextContent()));
            user.setRole(UserRole.valueOf(Objects.requireNonNull(getChildContent((Element) node, "role")).getTextContent()));
            user.setRegistrationDate(LocalDateTime.now());
            user.setLastModified(user.getRegistrationDate());
            user.setLastLogin(user.getRegistrationDate());
            users.add(user);
        }
        return users;
    }

    private List<Account> parseTestAccounts() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new File(TEST_ACCOUNTS_FILE));
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("account");
        Node node;
        List<Account> accounts = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); ++i){
            Account account = new Account();
            node = nodeList.item(i);
            account.setNumber(Objects.requireNonNull(getChildContent((Element) node, "number")).getTextContent());
            account.setDefault(Boolean.parseBoolean(Objects.requireNonNull(getChildContent((Element) node, "isDefault")).getTextContent()));
            User userId = new User();
            userId.setId(Long.parseLong(Objects.requireNonNull(getChildContent((Element) node, "user")).getTextContent()));
            Example<User> ex = Example.of(userId);
            userId = Objects.requireNonNull(userRepository.findAll(ex)).get(0);
            if (userId != null){
                account.setUser(userId);
            } else {
                throw new IOException("invalid user id in the source file");
            }
            account.setType(AccountType.valueOf(Objects.requireNonNull(getChildContent((Element) node, "type")).getTextContent()));
            account.setPercent(BigDecimal.valueOf(Double.parseDouble(Objects.requireNonNull(getChildContent((Element) node, "percent")).getTextContent())));
            account.setAmount(BigDecimal.valueOf(Double.parseDouble(Objects.requireNonNull(getChildContent((Element) node, "amount")).getTextContent())));
            account.setCoin(AccountCoin.valueOf(Objects.requireNonNull(getChildContent((Element) node, "coin")).getTextContent()));
            account.setDeleted(Boolean.parseBoolean(Objects.requireNonNull(getChildContent((Element) node, "isDeleted")).getTextContent()));
            accounts.add(account);
        }
        return accounts;
    }

    private List<Card> parseTestCards() throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new File(TEST_CARDS_FILE));
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("card");
        Node node;
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); ++i){
            Card card = new Card();
            node = nodeList.item(i);
            card.setNumber(Objects.requireNonNull(getChildContent((Element) node, "number")).getTextContent());
            User userId = new User();
            userId.setId(Long.parseLong(Objects.requireNonNull(getChildContent((Element) node, "user")).getTextContent()));
            Example<User> exUser = Example.of(userId);
            userId = Objects.requireNonNull(userRepository.findAll(exUser)).get(0);
            if (userId != null){
                card.setUser(userId);
            } else {
                throw new IOException("invalid user id in the source file");
            }
            Account accountId = new Account();
            accountId.setId(Long.parseLong(Objects.requireNonNull(getChildContent((Element) node, "account")).getTextContent()));
            Example<Account> exAccount = Example.of(accountId, ExampleMatcher.matching().withIgnorePaths("isDefault", "isDeleted"));
            accountId = Objects.requireNonNull(accountRepository.findAll(exAccount)).get(0);
            if (accountId != null){
                card.setAccount(accountId);
            } else {
                throw new IOException("invalid account id in the source file");
            }
            card.setIssueDate(LocalDate.parse(Objects.requireNonNull(getChildContent((Element) node, "issueDate")).getTextContent()));
            card.setValidTill(LocalDate.parse(Objects.requireNonNull(getChildContent((Element) node, "validTill")).getTextContent()));
            card.setPinCode(Objects.requireNonNull(getChildContent((Element) node, "pinCode")).getTextContent());
            card.setCvv2(Objects.requireNonNull(getChildContent((Element) node, "cvv")).getTextContent());
            cards.add(card);
        }
        return cards;
    }

    private List<Transaction> parseTestTransactions() throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new File(TEST_TRANSACTIONS_FILE));
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("transaction");
        Node node;
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); ++i){
            Transaction transaction = new Transaction();
            node = nodeList.item(i);
            transaction.setSrc(Objects.requireNonNull(getChildContent((Element) node, "accountSrc")).getTextContent());
            transaction.setDst(Objects.requireNonNull(getChildContent((Element) node, "accountDst")).getTextContent());
            transaction.setAmount(BigDecimal.valueOf(Double.parseDouble(Objects.requireNonNull(getChildContent((Element) node, "amount")).getTextContent())));
            transaction.setPurpose(Objects.requireNonNull(getChildContent((Element) node, "purpose")).getTextContent());
            transaction.setState(TransactionState.valueOf(Objects.requireNonNull(getChildContent((Element) node, "state")).getTextContent()));
            transaction.setDateTime(LocalDateTime.parse(Objects.requireNonNull(getChildContent((Element) node, "dateTime")).getTextContent()));
            transaction.setType(TransactionType.valueOf(Objects.requireNonNull(getChildContent((Element) node, "type")).getTextContent()));
            transactions.add(transaction);
        }
        return transactions;
    }

    public static Element getChildContent(Element parent, String name) {
        for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof Element && name.equals(child.getNodeName())) {
                return (Element) child;
            }
        }
        return null;
    }
}
