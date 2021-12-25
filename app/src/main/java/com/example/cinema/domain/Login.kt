package com.example.cinema.domain

import java.io.Serializable

class Login (username: String, password: String ) : Serializable {
    var nomUtil: String = username
    var motPasse: String = password
}