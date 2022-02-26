package com.example.cinema.service

import com.example.cinema.domain.Category
import com.example.cinema.domain.Login
import com.example.cinema.domain.Movie
import com.example.cinema.domain.Realisator
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ServiceRealisator {

    @GET("realisateur/")
    fun getRealisators(): Call<List<Realisator>>


}