package com.example.cinema.service

import android.content.Context
import android.content.SharedPreferences
import com.example.cinema.R

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.app_name),
        Context.MODE_PRIVATE
    )
    private val USER_TOKEN = "user_token"

    fun saveAuthToken(token: String?) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

}