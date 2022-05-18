package com.saturn_bank.operator.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.OneToMany;
import javax.persistence.Enumerated;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@SQLDelete(sql = "UPDATE saturn_bank.bank_user SET is_deleted=TRUE, last_modified=DEFAULT WHERE id=?")
@Table(name = "bank_user")
public class User implements SoftDeleteEntity, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @ToString.Exclude
    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "email", nullable = false)
    private String email;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Account> accountList = new ArrayList<>();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @Column(name = "last_modified", nullable = false)
    private LocalDateTime lastModified;

    @Enumerated
    @Column(name = "type", nullable = false)
    private UserType type;

    @Enumerated
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "password", nullable = false)
    private String password;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = Boolean.FALSE;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role.getAuthority());
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !isDeleted;
    }

}
