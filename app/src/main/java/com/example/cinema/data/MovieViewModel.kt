package com.example.cinema.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinema.domain.Movie
import com.example.cinema.service.RetrofitToken
import com.example.cinema.service.ServiceMovie
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MovieViewModel: ViewModel() {
    private val _movieList = mutableStateListOf<Movie>()
    var errorMessage: String by mutableStateOf("")
    val movieList: List<Movie>
    get() = _movieList

    fun getMovieList(context: Context, search: String = "") {
        val retrofit: Retrofit? = RetrofitToken.getRetrofit(context)
        val serviceMovie: ServiceMovie = retrofit!!.create(ServiceMovie::class.java)

        val call: Call<List<Movie>> = if (search === "") {
            serviceMovie.getMovies()
        } else {
            serviceMovie.searchMovies(search)
        }

        viewModelScope.launch {
            try {
                call.enqueue(object : Callback<List<Movie>> {
                    override fun onResponse(call: Call<List<Movie>>, uneReponse: Response<List<Movie>>) {
                        if (uneReponse.isSuccessful) {
                            if (uneReponse.body() != null) {
                                var movies = uneReponse.body();
                                _movieList.clear()
                                if (movies != null) {
                                    _movieList.addAll(movies)
                                }
                            }
                        } else {
                            errorMessage = "Erreur d'appel!";
                        }
                    }
                    override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
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