package com.epam.saturn.operator.service.validation;

public class Validator {

    public static boolean validate (String prototype, String s){
        return s.matches(prototype);
    }

}
