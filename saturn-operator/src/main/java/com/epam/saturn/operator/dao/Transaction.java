package com.epam.saturn.operator.dao;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name = "account_src", referencedColumnName = "id")
    private Account src;

    @ManyToOne
    @JoinColumn(name = "account_dst", referencedColumnName = "id")
    private Account dst;

    @Column
    private BigDecimal amount;

    @DateTimeFormat
    private LocalDateTime dateTime;

    @Enumerated
    private TransactionState state;

}
