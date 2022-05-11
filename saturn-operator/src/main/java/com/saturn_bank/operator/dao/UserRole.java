package com.saturn_bank.operator.dao;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
public enum UserRole {

    CLIENT("USER", UserType.CLIENT),
    OPERATOR("STAFF", UserType.EMPLOYEE),
    ADMIN("ADMIN", UserType.EMPLOYEE),
    ATM("ATM", UserType.EMPLOYEE);

    private final GrantedAuthority authority;
    private final UserType type;

    UserRole(String authority, UserType type) {
        this.authority = new SimpleGrantedAuthority("ROLE_" + authority);
        this.type = type;
    }

}
