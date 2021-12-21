package com.example.cinema.service

import android.content.Context
import android.content.SharedPreferences
import com.example.cinema.R


class SessionManager(context: Context) {
    private val prefs: SharedPreferences
    val USER_TOKEN = "user_token"

    /**
     * on sauve le token
     */
    fun saveAuthToken(token: String?) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Fon récupère le token
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    init {
        prefs = context.getSharedPreferences(
            context.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }
}