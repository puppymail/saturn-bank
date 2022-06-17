package com.saturn_bank.operator.dao;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@ToString
@NoArgsConstructor
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "account_src")
    private String src;

    @Column(name = "account_dst")
    private String dst;

    @Column
    private BigDecimal amount;

    @Column
    private String purpose;

    @DateTimeFormat
    private LocalDateTime dateTime;

    @Enumerated
    private TransactionState state;

    @Enumerated
    private TransactionType type;

    @Column(name = "is_with_currency_converting")
    private boolean isWithCurrencyConverting;

}
