package com.example.cinema.validator

import java.util.regex.Pattern

private const val USERNAME_VALIDATION_REGEX = "^(?=.{3,20}\$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])";

class UsernameState: TextFieldState(
    validator =  ::isUsernameValid,
    errorMessage = ::usernameErrorMessage,
)

private fun isUsernameValid(username: String): Boolean {
    return Pattern.matches(USERNAME_VALIDATION_REGEX, username);
}

private fun usernameErrorMessage(username: String): String { return "Nom d'utilisateur: Merlot"; }