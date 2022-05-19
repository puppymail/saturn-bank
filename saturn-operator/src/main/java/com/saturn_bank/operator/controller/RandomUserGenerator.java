package com.saturn_bank.operator.controller;

import com.saturn_bank.operator.dao.User;
import com.saturn_bank.operator.dao.UserRole;
import com.saturn_bank.operator.dao.UserType;
import com.saturn_bank.operator.supplier.util.RandomData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.function.Supplier;

@Slf4j
@Component
public class RandomUserGenerator implements Supplier<User> {

    @Override
    public User get(){
        User user = new User();
        Random random = new Random();
        RandomData rd = new RandomData();
        user.setFirstName(rd.getRandomName());
        user.setLastName(rd.getRandomName());
        user.setMiddleName(rd.getRandomName());
        user.setPhoneNumber(rd.getRandomPhone());
        user.setEmail(rd.getRandomEmail());
        user.setBirthDate(rd.getRandomDate());
        user.setRegistrationDate(rd.getRandomDateTime());
        user.setType(UserType.values()[random.nextInt(UserType.values().length)]);
        user.setRole(UserRole.values()[random.nextInt(UserRole.values().length)]);

        return user;
    }

}
