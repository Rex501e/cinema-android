package com.example.cinema.service

import com.example.cinema.domain.Character
import retrofit2.Call
import retrofit2.http.*

interface ServiceCharacter {

    @GET("personnage/")
    fun getCharacters(): Call<List<Character>>

    @GET("personnage/acteur/{id}")
    fun getCharactersByActor(@Path(value="id") id: Int): Call<List<Character>>

}