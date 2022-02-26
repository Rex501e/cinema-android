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
import com.example.cinema.validator.UsernameState
import retrofit2.Retrofit

class AdminActivity : ComponentActivity() {
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
fun FormMovie(cvm: CategoryViewModel, rvm: RealisatorViewModel, movie: Movie, created: Boolean){
    val context = LocalContext.current

    LaunchedEffect(Unit, block = {
        cvm.getCategoryList(context)
        rvm.getRealisatorList(context)
    })

    val strCreated = if(created) "Ajouter" else "Modifer"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
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
            var titreState by mutableStateOf(movie.titre)
            StringField(titreState, "Titre"){
                titreState = it
                movie.titre = it
            }
            Spacer(Modifier.size(16.dp))
            var dureeState by mutableStateOf(movie.duree)
            StringField(dureeState, "Durée (en min)"){
                dureeState = it
                movie.duree = it
            }
            Spacer(Modifier.size(16.dp))
            var budgetState by mutableStateOf(movie.budget)
            StringField(budgetState, "Budget"){
                budgetState = it
                movie.budget = it
            }
            Spacer(Modifier.size(16.dp))
            var amountState by mutableStateOf(movie.montantRecette)
            StringField(amountState, "Revenue"){
                amountState = it
                movie.montantRecette = it
            }
            Spacer(Modifier.size(16.dp))
            val realisator = if(movie.noRea != "") movie.noRea else null
            RealisatorField(rvm, realisator)
            Spacer(Modifier.size(16.dp))
            val category = if(movie.codeCat != "") movie.codeCat else null
            CategoryField(cvm, category)
            Spacer(Modifier.size(16.dp))
            var urlImageState by mutableStateOf(movie.urlImage)
            StringField(urlImageState, "Url image"){
                urlImageState = it
                movie.urlImage = it
            }
            Spacer(Modifier.size(16.dp))
            var urlTrailerState by mutableStateOf(movie.urlTrailer)
            StringField(urlTrailerState, "Url trailer"){
                urlTrailerState = it
                movie.urlTrailer = it
            }
            Spacer(Modifier.size(16.dp))
            ValidatorButton(strCreated,
                enabled = true
            ) {
                if (created) {
                    createMovie(movie, context)
                }else{
                    updateMovie(movie, context)
                }

            }
        }
    }
}

@Throws(Exception::class)
fun createMovie(movie: Movie, context: Context) {
    val retrofit: Retrofit? = RetrofitToken.getRetrofit(context)
    val movieService = retrofit!!.create(ServiceMovie::class.java)

    try {
        movieService.createMovie(movie).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val intent = Intent(context, MovieActivity::class.java)
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
fun updateMovie(movie: Movie, context: Context) {
    val retrofit: Retrofit? = RetrofitToken.getRetrofit(context)
    val movieService = retrofit!!.create(ServiceMovie::class.java)

    try {
        movieService.editMovie(movie.noFilm, movie).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val intent = Intent(context, MovieActivity::class.java)
                        context.startActivity(intent)
                        Toast.makeText(context, "Film modifié avec succé", Toast.LENGTH_LONG).show()
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

@Composable
fun StringField(strValue: String, placeHolder: String, onChanged: (String) -> Unit){
    TextField(
        value = strValue,
        onValueChange = { value -> onChanged(value) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = placeHolder, color = Color.Gray) },
        colors = TextFieldDefaults.textFieldColors(textColor = Color.White),
    )
}

@Composable
fun RealisatorField(rvm: RealisatorViewModel, realisator: String?){
    val realisators = rvm.realisatorList

    if (rvm.realisatorList.isNotEmpty()) {
        val default = realisator ?: realisators[0].noRea
        var expanded by remember { mutableStateOf(false) }
        var selectedText by remember { mutableStateOf(default) }

        val icon = if (expanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown

        val current = realisators.filter{ it.noRea == selectedText }.first()
        Column() {
            OutlinedTextField(
                value = current.nomRea + " "+ current.prenRea,
                onValueChange = {  },
                modifier = Modifier.fillMaxWidth().clickable{ expanded = !expanded },
                label = { current.nomRea + " "+ current.prenRea },
                colors = TextFieldDefaults.textFieldColors(textColor = Color.White),
                singleLine = true,
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = "contentDescription",
                        tint = Color.White,
                        modifier = Modifier.clickable { expanded = !expanded }
                    )
                }

            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                realisators.forEach{ item ->
                    DropdownMenuItem(onClick = {
                        selectedText = item.noRea
                        expanded = false
                    }) {
                        Text(text = item.nomRea +" "+ item.prenRea)
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryField(cvm: CategoryViewModel, category: String?){
    val categories = cvm.categoryList

    if (cvm.categoryList.isNotEmpty()) {
        val default = category ?: categories[0].codeCat
        var expanded by remember { mutableStateOf(false) }
        var selectedText by remember { mutableStateOf(default) }

        val icon = if (expanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown

        val current = categories.filter { it.codeCat == selectedText }.first()
        Column() {
            OutlinedTextField(
                value = current.libelleCat!!,
                onValueChange = { },
                modifier = Modifier.fillMaxWidth().clickable{ expanded = !expanded },
                label = { current.libelleCat },
                colors = TextFieldDefaults.textFieldColors(textColor = Color.White),
                singleLine = true,
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = "contentDescription",
                        tint = Color.White,
                        modifier = Modifier.clickable { expanded = !expanded }
                    )
                }

            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                categories.forEach{ item ->
                    DropdownMenuItem(onClick = {
                        selectedText = item.codeCat
                        expanded = false
                    }) {
                        Text(text = item.libelleCat!!)
                    }
                }
            }
        }
    }
}