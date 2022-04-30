package com.saturn_bank.operator.dao;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum UserRole {

    CLIENT("USER"), OPERATOR("STAFF"), ADMIN("ADMIN"), ATM("ATM");

    private final GrantedAuthority authority;

    UserRole(String authority) {
        this.authority = new SimpleGrantedAuthority("ROLE_" + authority);
    }

    public GrantedAuthority getAuthority() {
        return authority;
    }

}
