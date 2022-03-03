package com.example.cinema.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cinema.data.CategoryViewModel
import com.example.cinema.data.RealisatorViewModel
import com.example.cinema.domain.Movie
import com.example.cinema.exception.Exception
import com.example.cinema.service.ServiceMovie
import com.example.cinema.ui.theme.CinemaTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import com.example.cinema.service.RetrofitToken
import com.example.cinema.ui.bottomnav.BottomNavActivity
import com.example.cinema.validator.UsernameState
import retrofit2.Retrofit

class ActorAdminActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val cvm = CategoryViewModel()
        val rvm = RealisatorViewModel()

        var movie = intent.getSerializableExtra("movie") as? Movie

        var created = false;
        if (movie == null) {
            created = true;
            movie = Movie()
        }
        super.onCreate(savedInstanceState)
        setContent {
            CinemaTheme {
                FormMovie(cvm, rvm, movie, created)
            }
        }
    }
}

// @Preview(showBackground = true, widthDp = 320, uiMode = UI_MODE_NIGHT_YES)
@Composable()
fun FormActor(cvm: CategoryViewModel, rvm: RealisatorViewModel, movie: Movie, created: Boolean){
    val context = LocalContext.current

    LaunchedEffect(Unit, block = {
        cvm.getCategoryList(context)
        rvm.getRealisatorList(context)
    })

    val strCreated = if(created) "Ajouter" else "Modifer"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        verticalArrangement = Arrangement.Center,
    ){
        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.White)) {
                append("$strCreated un film")
            }
        }, fontSize = 30.sp)
        Column(
            modifier = Modifier
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
        ){

        }
    }
}

@Throws(Exception::class)
fun createActor(movie: Movie, context: Context) {
    val retrofit: Retrofit? = RetrofitToken.getRetrofit(context)
    val movieService = retrofit!!.create(ServiceMovie::class.java)

    try {
        movieService.createMovie(movie).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val intent = Intent(context, BottomNavActivity::class.java)
                        context.startActivity(intent)
                        Toast.makeText(context, "Film ajouté avec succé", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context,"Erreur d'appel!", Toast.LENGTH_LONG ) .show()
                    }
                }
            }
            override fun onFailure(call: Call<Movie>, t: Throwable ) {
                Toast.makeText(context,"Erreur de connexion", Toast.LENGTH_LONG ) .show()
            }
        })
    } catch (e: Exception) {
        Exception(
            e.message,
            "Erreur Appel WS Connexion"
        )
    }
}

@Throws(Exception::class)
fun updateActor(movie: Movie, context: Context) {
    val retrofit: Retrofit? = RetrofitToken.getRetrofit(context)
    val movieService = retrofit!!.create(ServiceMovie::class.java)

    try {
        movieService.editMovie(movie.noFilm ?: "-1", movie).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val intent = Intent(context, BottomNavActivity::class.java)
                        context.startActivity(intent)
                        Toast.makeText(context, "Film modifié avec succès", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context,"Erreur d'appel!", Toast.LENGTH_LONG ) .show()
                    }
                }
            }
            override fun onFailure(call: Call<Movie>, t: Throwable ) {
                Toast.makeText(context,"Erreur de connexion", Toast.LENGTH_LONG ) .show()
            }
        })
    } catch (e: Exception) {
        Exception(
            e.message,
            "Erreur Appel WS Connexion"
        )
    }
}