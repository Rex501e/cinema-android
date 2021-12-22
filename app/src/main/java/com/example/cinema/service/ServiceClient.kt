package com.example.cinema.service

import com.example.cinema.domain.Client
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ServiceClient {

// requête d'appel des clients  GET
@GET("client/getClients")
fun getClients(): Call<List<Client>>

    // requête d'ajout
    @POST("client/ajout")
    fun ajouterClient(@Body unCl: Client): Call<Client>

    // requête de mise à jour
    @POST("client/modification")
    fun updateClient(@Body unCl: Client): Call<Client>

    // requête de suppression
    @POST("client/deleteClient")
    fun deleteClient(@Body unCl: Client): Call<Client>
}
