package com.example.cinema.service

import com.example.cinema.domain.LoginParam
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface IntConnexion {

    // requête de contrôle

    @POST("authentification/login")
    fun getConnexion(@Body unL: LoginParam): Call<Object>

}