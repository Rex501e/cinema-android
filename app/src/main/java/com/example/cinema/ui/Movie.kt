package com.example.cinema.ui;

import android.content.Context
import android.content.Intent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.cinema.data.MovieViewModel
import com.example.cinema.domain.Movie
import com.example.cinema.validator.SearchState


@Composable
fun Movies() {
    val vm = MovieViewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit, block = {
        vm.getMovieList(context)
    })


    LazyColumn() {
        item {
            Surface(
                modifier = Modifier.padding(horizontal = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        val searchState = remember { SearchState() }
                        SearchTxtFieldForMovie(searchState.text, searchState.error, context, vm) {
                            searchState.text = it
                            searchState.validate()
                        }
                    }
                }
            }
        }
        items(items = vm.movieList) { movie ->
            Movie(movie = movie, context)
        }
    }
}

@Composable
fun SearchTxtFieldForMovie(
    search: String,
    error: String?,
    context: Context,
    vm: MovieViewModel,
    onSearchChanged: (String) -> Unit
) {
    TextField(
        value = search,
        onValueChange = { value -> onSearchChanged(value) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = "Rechercher", color = Color.Gray) },
        colors = TextFieldDefaults.textFieldColors(textColor = Color.White),
        trailingIcon = {
            IconButton(onClick = {
                vm.getMovieList(context, search)
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search",
                    tint = Color.Red
                )
            }
        },
        isError = error != null
    )

    error?.let { ErrorField(it) }
}

@Composable
fun Movie(movie: Movie, context: Context) {
    var expanded by remember { mutableStateOf(false) }

    val extraPadding by animateDpAsState(
        if (expanded) 5.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Surface(
        color = Color.Gray,
        modifier = Modifier.padding(vertical = 5.dp, horizontal = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.padding(15.dp)) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = extraPadding.coerceAtLeast(0.dp))
                ) {
                    Text(
                        text = movie.titre!!,
                        style = MaterialTheme.typography.h4.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = Color.White,
                        fontSize = 26.sp
                    )
                }
                OutlinedButton(
                    onClick = { expanded = !expanded },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                ) {
                    Text(if (expanded) "Voir moins" else "Voir plus", color = Color.White)
                }
            }
            if (expanded) {
                MovieDetail(movie, context);
            }
        }
    }
}

@Composable
fun MovieDetail(movie: Movie, context: Context) {
    val url = if (movie.urlImage == "")
        "https://media.istockphoto.com/vectors/no-image-available-sign-vector-id922962354?k=20&m=922962354&s=612x612&w=0&h=f-9tPXlFXtz9vg_-WonCXKCdBuPUevOBkp3DQ-i0xqo="
    else
        movie.urlImage

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box() {
            Image(
                painter = rememberImagePainter(url),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )
        }
        Column {
            Text(
                text = "ID: " + movie.noFilm,
                color = Color.White,
                fontSize = 15.sp
            )
            Text(
                text = "Durée: " + movie.duree + " min",
                color = Color.White,
                fontSize = 15.sp
            )
            Text(
                text = "Date de sortie: " + movie.dateSortie.toString(),
                color = Color.White,
                fontSize = 15.sp
            )
            Text(
                text = "Catégorie: " + movie.categorie?.libelleCat,
                color = Color.White,
                fontSize = 15.sp
            )
            Text(
                text = "Réalisateur: " + movie.realisateur?.nomRea + " " + movie.realisateur?.prenRea,
                color = Color.White,
                fontSize = 15.sp
            )
            Text(
                text = "Budget: " + movie.budget + " €",
                color = Color.White,
                fontSize = 15.sp
            )
            Text(
                text = "Montant recette: " + movie.montantRecette + " €",
                color = Color.White,
                fontSize = 15.sp
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Button(
            onClick = {
                val intent = Intent(context, MovieAdminActivity::class.java)
                intent.putExtra("movie", movie);
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
        ) {
            Text(text = "Modifier", color = Color.White)
        }
        Spacer(Modifier.size(10.dp))
        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
        ) {
            Text(text = "Supprimer", color = Color.White)
        }
    }
}