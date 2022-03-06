package com.example.cinema.service

import com.example.cinema.domain.Actor
import com.example.cinema.domain.Character
import com.example.cinema.domain.Movie
import retrofit2.Call
import retrofit2.http.*

interface ServiceActor {

    @GET("acteur/")
    fun getActors(): Call<List<Actor>>

    @POST("acteur/")
    fun createActor(@Body actor: Actor): Call<Actor>

    @PATCH("acteur/{id}")
    fun editActor(@Path(value="id") id: String, @Body actor: Actor): Call<Actor>
}