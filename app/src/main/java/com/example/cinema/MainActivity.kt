package com.example.cinema

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cinema.ui.signin.SignInScreen
import com.example.cinema.ui.theme.CinemaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CinemaTheme {
                SignInScreen ()
            }
        }
    }
}