package com.example.cinema.data;

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinema.domain.Actor
import com.example.cinema.domain.Character
import com.example.cinema.service.RetrofitToken
import com.example.cinema.service.ServiceActor
import com.example.cinema.service.ServiceCharacter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ActorViewModel : ViewModel() {
    private val _actorsList = mutableStateListOf<Actor>()
    var errorMessage: String by mutableStateOf("")
    val actorsList: List<Actor>
    get() = _actorsList

    fun getCharactersList(context: Context, search: String = "") {
        val retrofit: Retrofit? = RetrofitToken.getRetrofit(context)
        val serviceActor: ServiceActor = retrofit!!.create(ServiceActor::class.java)

        val call: Call<List<Actor>> = if (search === "") {
            serviceActor.getActors()
        } else {
            serviceActor.searchActor(search)
        }

        viewModelScope.launch {
            try {
                call.enqueue(object : Callback<List<Actor>> {
                    override fun onResponse(call: Call<List<Actor>>, uneReponse: Response<List<Actor>>) {
                        if (uneReponse.isSuccessful) {
                            if (uneReponse.body() != null) {
                                val actors = uneReponse.body();
                                _actorsList.clear()
                                if (actors != null) {
                                    _actorsList.addAll(actors)
                                    _actorsList.forEach {
                                        getCharacterByActId(context, it.noAct!!)
                                    }
                                }
                            }
                        } else {
                            errorMessage = "Erreur d'appel!";
                        }
                    }
                    override fun onFailure(call: Call<List<Actor>>, t: Throwable) {
                        errorMessage = "Erreur rencontrée";
                        call.cancel()
                    }
                })
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }

    fun getCharacterByActId(context: Context, actId: String){
        val retrofit: Retrofit? = RetrofitToken.getRetrofit(context)
        val serviceCharacter: ServiceCharacter = retrofit!!.create(ServiceCharacter::class.java)
        val call: Call<List<Character>> = serviceCharacter.getCharactersByActor(actId)

        viewModelScope.launch {
            try {
                call.enqueue(object : Callback<List<Character>> {
                    override fun onResponse(call: Call<List<Character>>, uneReponse: Response<List<Character>>) {
                        if (uneReponse.isSuccessful) {
                            if (uneReponse.body() != null) {
                                val characters = uneReponse.body();
                                if (characters != null) {
                                    val actorTmp = _actorsList.find { it.noAct == actId }
                                    actorTmp?.characters = characters
                                }
                            }
                        } else {
                            errorMessage = "Erreur d'appel!";
                        }
                    }
                    override fun onFailure(call: Call<List<Character>>, t: Throwable) {
                        errorMessage = "Erreur rencontrée";
                        call.cancel()
                    }
                })
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }
}
