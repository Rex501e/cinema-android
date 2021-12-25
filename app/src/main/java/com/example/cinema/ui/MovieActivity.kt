package com.example.cinema.ui;

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cinema.domain.Movie
import com.example.cinema.exception.Exception
import com.example.cinema.service.RetrofitMovie
import com.example.cinema.service.ServiceMovie
import com.example.cinema.ui.theme.CinemaTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class MovieActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CinemaTheme {
                Greetings()
            }
        }
    }
}


@Throws(Exception::class)
private fun loadMovies(context: Context) {
    val retrofit: Retrofit? = RetrofitMovie.getMovieRetrofit(context)
    val serviceMovie: ServiceMovie = retrofit!!.create(ServiceMovie::class.java)
    val call: Call<List<Movie>> = serviceMovie.getMovies()
    try {
        call.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(
                call: Call<List<Movie>>,
                uneReponse: Response<List<Movie>>
            ) {
                if (uneReponse.isSuccessful) {
                    if (uneReponse.body() != null) {
                        var clients = uneReponse.body()
                        Toast.makeText(context, "Appel reussie", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Erreur d'appel!", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context, "Erreur rencontrée", Toast.LENGTH_LONG).show();
                    Log.d(
                        "LOAD DATA", "onResponse =>>> code = " + uneReponse.code()
                    )
                }
            }
            override fun onFailure(
                call: Call<List<Movie>>, t: Throwable
            ) {
                Toast.makeText(context, "Erreur d'appel!", Toast.LENGTH_LONG).show()
                call.cancel()
            }
        })
    } catch (e: java.lang.Exception) {
        Exception(e.message, "Erreur Appel WS Connexion")
    }
}



@Preview(showBackground = true, widthDp = 320)
@Composable
private fun Greetings(names: List<String> = List(100) { "$it" }) {
    val context = LocalContext.current
    loadMovies(context)

    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            Greeting(name = name)
        }
    }
}

@Composable
fun Greeting(name: String) {
    var expanded by remember { mutableStateOf(false) }

    val extraPadding by animateDpAsState(
        if (expanded) 48.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Surface(
        color = Color.Gray,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = extraPadding.coerceAtLeast(0.dp))
            ) {
                Text(text = "Hello,", color = Color.White)
                Text(
                    text = name,
                    style = MaterialTheme.typography.h4.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = Color.White
                )
            }
            OutlinedButton(
                onClick = { expanded = !expanded },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            ) {
                Text(if (expanded) "Show less" else "Show more", color = Color.White)
            }
        }
    }
}