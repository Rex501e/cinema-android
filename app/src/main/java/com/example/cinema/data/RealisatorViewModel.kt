package com.example.cinema.data;

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinema.domain.Realisator;
import com.example.cinema.service.RetrofitToken
import com.example.cinema.service.ServiceRealisator
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class RealisatorViewModel: ViewModel() {
    private val _realisatorList = mutableStateListOf<Realisator>()
    var errorMessage: String by mutableStateOf("")
    val realisatorList: List<Realisator>
        get() = _realisatorList

    fun getRealisatorList(context: Context) {
        val retrofit: Retrofit? = RetrofitToken.getRetrofit(context)
        val serviceRealisator: ServiceRealisator = retrofit!!.create(ServiceRealisator::class.java)
        val call: Call<List<Realisator>> = serviceRealisator.getRealisators()

        viewModelScope.launch {
            try {
                call.enqueue(object : Callback<List<Realisator>> {
                    override fun onResponse(call: Call<List<Realisator>>, uneReponse: Response<List<Realisator>>) {
                        if (uneReponse.isSuccessful) {
                            if (uneReponse.body() != null) {
                                var movies = uneReponse.body();
                                _realisatorList.clear()
                                if (movies != null) {
                                    _realisatorList.addAll(movies)
                                }
                            }
                        } else {
                            errorMessage = "Erreur d'appel!";
                        }
                    }
                    override fun onFailure(call: Call<List<Realisator>>, t: Throwable) {
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
