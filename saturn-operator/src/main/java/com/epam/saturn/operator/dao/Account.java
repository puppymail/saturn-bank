package com.epam.saturn.operator.dao;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.Enumerated;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
@SQLDelete(sql = "UPDATE saturn_bank.account SET is_deleted = true, is_default = false WHERE id=?")
@Where(clause = "is_deleted = false")
@Entity
public class Account implements SoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "is_default")
    private boolean isDefault = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ToString.Exclude
    @OneToMany(mappedBy = "account", orphanRemoval = true)
    private List<Card> cards;

    @Enumerated
    @Column(columnDefinition = "smallint", name = "type")
    private AccountType type;

    @Column(name = "percent", precision = 20, scale = 2)
    private BigDecimal percent;

    @Column(name = "amount", precision = 100, scale = 2)
    private BigDecimal amount;

    @Enumerated
    @Column(columnDefinition = "smallint", name = "account_coin")
    private AccountCoin coin;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = Boolean.FALSE;

}
