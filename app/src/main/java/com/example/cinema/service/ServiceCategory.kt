package com.example.cinema.service

import com.example.cinema.domain.Category
import com.example.cinema.domain.Login
import com.example.cinema.domain.Movie
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ServiceCategory {

    @GET("categorie/")
    fun getCategories(): Call<List<Category>>

}