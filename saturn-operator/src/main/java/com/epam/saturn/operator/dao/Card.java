package com.epam.saturn.operator.dao;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import java.time.LocalDate;

@Getter
@Setter
@Table(name = "card")
@SQLDelete(sql = "UPDATE saturn_bank.card SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Entity
public class Card implements SoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "valid_till")
    private LocalDate validTill;

    @Column(name = "pincode")
    private String pinCode;

    @Column(name = "cvv2")
    private String cvv2;

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;

}

