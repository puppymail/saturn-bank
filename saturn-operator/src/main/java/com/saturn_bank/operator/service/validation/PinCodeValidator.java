package com.saturn_bank.operator.service.validation;

public class PinCodeValidator {

    public static void validatePinCode(String existingPinCode, String oldPinCode, String newPinCode) {
        validateOldPinCode(existingPinCode, oldPinCode);
        validatePinCodesForEquals(oldPinCode, newPinCode);
        validatePinCodeForDigits(newPinCode);
    }

    private static void validateOldPinCode(String existingPinCode, String oldPinCode) {
        if (!existingPinCode.equals(oldPinCode)) {
            throw new IllegalArgumentException("Old pincode doesn't match existing pincode");
        }
    }

    private static void validatePinCodesForEquals(String oldPinCode, String newPinCode) {
        if (oldPinCode.equals(newPinCode)) {
            throw new IllegalArgumentException("New pincode and old pincode can't be same");
        }
    }

    private static void validatePinCodeForDigits(String pinCode) {
        if (!pinCode.matches("^\\d{4}$")) {
            throw new IllegalArgumentException("Pincode should be 4 digit");
        }
    }
}
