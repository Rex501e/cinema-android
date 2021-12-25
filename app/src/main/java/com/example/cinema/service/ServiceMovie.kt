package com.example.cinema.service

import com.example.cinema.domain.Movie
import retrofit2.Call
import retrofit2.http.GET

interface ServiceMovie {

    @GET("film/")
    fun getMovies(): Call<List<Movie>>

}