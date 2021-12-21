package com.example.cinema.domain

import java.io.Serializable

class LoginParam (login:String , pwd: String ) : Serializable {
    var nomUtil: String = login
    var motPasse: String = pwd
}