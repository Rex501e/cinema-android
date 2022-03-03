package com.example.cinema.service

import com.example.cinema.domain.Actor
import com.example.cinema.domain.Character
import retrofit2.Call
import retrofit2.http.*

interface ServiceActor {

    @GET("acteur/")
    fun getActors(): Call<List<Actor>>

}