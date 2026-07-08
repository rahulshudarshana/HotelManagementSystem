package com.hotelmanagement.util;

import com.hotelmanagement.exception.ValidationException;

public final class ValidationUtils {
    private ValidationUtils() {}

    public static boolean isNullOrBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (isNullOrBlank(email)) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public static boolean isValidPhone(String phone) {
        if (isNullOrBlank(phone)) return false;
        return phone.matches("^\\+?[0-9]{7,15}$");
    }

    public static boolean isPositiveInt(String str) {
        if (isNullOrBlank(str)) return false;
        try {
            return Integer.parseInt(str.trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isPositiveInt(int value) {
        return value > 0;
    }

    public static void requireNonBlank(String value, String fieldName) {
        if (isNullOrBlank(value)) {
            throw new ValidationException(fieldName + " is required.");
        }
    }

    public static void requirePositive(int value, String fieldName) {
        if (value <= 0) {
            throw new ValidationException(fieldName + " must be greater than zero.");
        }
    }
}
