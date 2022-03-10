package com.example.cinema.data

import android.content.Context
import android.util.*
import android.util.Log.ERROR
import android.widget.Toast
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
import retrofit2.*

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

    fun deleteMovie(context: Context, movie: Movie)
    {
        val retrofit: Retrofit? = RetrofitToken.getRetrofit(context)
        val serviceMovie: ServiceMovie = retrofit!!.create(ServiceMovie::class.java)

        try {
            movie.noFilm?.let {
                serviceMovie.deleteMovie(it).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Film bien supprimé", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "Erreur durant la suppression du film", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable ) {
                        Toast.makeText(context,"Erreur au niveau de la réponse", Toast.LENGTH_LONG ) .show()
                    }
                })
            }
            _movieList.remove(movie)
        } catch (e: com.example.cinema.exception.Exception) {
            com.example.cinema.exception.Exception(
                e.message,
                "Erreur Appel WS Connexion"
            )
        }
    }
}