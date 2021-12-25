package com.example.cinema.validator

import java.util.regex.Pattern

private const val PASSWORD_VALIDATION_REGEX = "^(?=.*[a-z]).{3,32}\$";

class PasswordState: TextFieldState(
    validator =  ::isPasswordValid,
    errorMessage = ::passwordErrorMessage,
)

private fun isPasswordValid(password: String): Boolean {
    return Pattern.matches(PASSWORD_VALIDATION_REGEX, password);
}

private fun passwordErrorMessage(password: String): String { return "Mot de passe: secret"; }