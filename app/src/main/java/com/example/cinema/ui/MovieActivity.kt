package com.example.cinema.ui;

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cinema.data.MovieViewModel
import com.example.cinema.domain.Movie
import com.example.cinema.exception.Exception
import com.example.cinema.service.RetrofitMovie
import com.example.cinema.service.ServiceMovie
import com.example.cinema.ui.theme.CinemaTheme
import retrofit2.*

class MovieActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val vm = MovieViewModel()
        super.onCreate(savedInstanceState)
        setContent {
            CinemaTheme {
                Greetings(vm)
            }
        }
    }
}


@Composable
fun Greetings(vm: MovieViewModel) {
    val context = LocalContext.current

    LaunchedEffect(Unit, block = {
        vm.getMovieList(context)
    })


    Scaffold(
        topBar = {
            topBar()
        }
    ) {
        LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
            item {
                Surface(
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 24.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            SearchTxtField() {}
                        }
                    }
                }
            }
            items(items = vm.movieList) { movie ->
                Greeting(movie = movie)
            }
        }
    }
}

@Composable
fun topBar(){
    TopAppBar(
        title = {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Red)) {
                        append("C")
                    }
                    withStyle(style = SpanStyle(color = Color.White)) {
                        append("inéma")
                    }
                }, fontSize = 30.sp
            )
        }
    )
}

@Composable
fun SearchTxtField(onSearchChanged: (String) -> Unit) {
    OutlinedTextField(
        value = "",
        onValueChange = { value -> onSearchChanged(value) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = "Rechercher", color = Color.Gray) },
        colors = TextFieldDefaults.textFieldColors(textColor = Color.White),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "search",
                tint = Color.Red
            )
        },
    )
}

@Composable
fun Greeting(movie: Movie) {
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
                Text(text = movie.dateSortie.toString(), color = Color.White, fontSize = 13.sp)
                Text(
                    text = movie.titre!!,
                    style = MaterialTheme.typography.h4.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = Color.White,
                    fontSize = 25.sp
                )
            }
            OutlinedButton(
                onClick = { expanded = !expanded },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            ) {
                Text(if (expanded) "Voir moins" else "Voir plus", color = Color.White)
            }
        }
    }
}

