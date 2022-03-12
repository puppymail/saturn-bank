package com.epam.saturn.operator.dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardEntityDao {

    private Long id;
    private String account;
    private String user;
    private String pinCode;
}
