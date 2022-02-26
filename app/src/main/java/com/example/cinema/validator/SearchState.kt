package com.example.cinema.validator

class SearchState: TextFieldState(
    validator = ::isSearchValid,
    errorMessage = ::searchErrorMessage,
)

fun searchErrorMessage(search: String): String {
    return ""
}

fun isSearchValid(search: String): Boolean {
    return true
}

