package com.libinggen.javadocker.javaapp.validator;

import java.util.regex.Pattern;

public class PasswordValidator {

    private static final int MAX_LENGTH = 14;
    private static final int MIN_LENGTH = 8;
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("\\d");
    private static final Pattern SPECIAL_CHARACTER_PATTERN =
            Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");
    private static final String[] COMMON_WORDS = {"password", "123456", "qwerty", "admin"};

    public static void validatePasswordComplexity(String password) throws Exception {
        // Maximum length check
        if (password.length() > MAX_LENGTH) {
            throw new Exception("Password should be 14 characters or less.");
        }

        // Minimum length check
        if (password.length() < MIN_LENGTH) {
            throw new Exception("Password must be at least 8 characters long.");
        }

        // Uppercase and lowercase letters check
        if (!LOWERCASE_PATTERN.matcher(password).find()
                || !UPPERCASE_PATTERN.matcher(password).find()) {
            throw new Exception("Password must include both uppercase and lowercase letters.");
        }

        // Numeric character check
        if (!NUMERIC_PATTERN.matcher(password).find()) {
            throw new Exception("Password must include at least one numeric character.");
        }

        // Special character check
        if (!SPECIAL_CHARACTER_PATTERN.matcher(password).find()) {
            throw new Exception("Password must include at least one special character.");
        }

        // Common words check
        for (String word : COMMON_WORDS) {
            if (password.toLowerCase().contains(word)) {
                throw new Exception(
                        "Password should not contain common words like '" + word + "'.");
            }
        }
    }
}

