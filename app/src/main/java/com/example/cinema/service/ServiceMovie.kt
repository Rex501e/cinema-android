package com.example.cinema.service

import com.example.cinema.domain.Login
import com.example.cinema.domain.Movie
import retrofit2.Call
import retrofit2.http.*

interface ServiceMovie {

    @GET("film/")
    fun getMovies(): Call<List<Movie>>

    @POST("film/")
    fun createMovie(@Body movie: Movie): Call<Movie>

    @PATCH("film/{id}")
    fun editMovie(@Path(value="id") id: String, @Body movie: Movie): Call<Movie>

}