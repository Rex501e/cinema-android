package com.example.cinema.data;

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinema.domain.Character
import com.example.cinema.service.RetrofitToken
import com.example.cinema.service.ServiceCharacter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class CharacterViewModel: ViewModel() {
    private val _charactersList = mutableStateListOf<Character>()
    var errorMessage: String by mutableStateOf("")
    val charactersList: List<Character>
    get() = _charactersList

    fun getCharactersList(context: Context, search: String = "") {
        val retrofit: Retrofit? = RetrofitToken.getRetrofit(context)
        val servicePerson: ServiceCharacter = retrofit!!.create(ServiceCharacter::class.java)
        val call: Call<List<Character>> = servicePerson.getCharacters()

        viewModelScope.launch {
            try {
                call.enqueue(object : Callback<List<Character>> {
                    override fun onResponse(call: Call<List<Character>>, uneReponse: Response<List<Character>>) {
                        if (uneReponse.isSuccessful) {
                            if (uneReponse.body() != null) {
                                var persons = uneReponse.body();
                                _charactersList.clear()
                                if (persons != null) {
                                    _charactersList.addAll(persons)
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
