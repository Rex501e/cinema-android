package com.example.cinema.service

import com.example.cinema.domain.Login
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ServiceLogin {

    @POST("authentification/login")
    fun getConnexion(@Body unL: Login): Call<Any>

}