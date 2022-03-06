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
import com.example.cinema.exception.Exception
import com.example.cinema.ui.theme.CinemaTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.cinema.data.MovieViewModel
import com.example.cinema.domain.Actor
import com.example.cinema.domain.Character
import com.example.cinema.service.RetrofitToken
import com.example.cinema.service.ServiceCharacter
import com.example.cinema.ui.bottomnav.BottomNavActivity
import retrofit2.Retrofit

class CharacterAdminActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val mvm = MovieViewModel()
        val actor = intent.getSerializableExtra("actor") as? Actor
        val character = Character()
        character.noAct = actor!!.noAct

        super.onCreate(savedInstanceState)
        setContent {
            CinemaTheme {
                FormCharacter(mvm, character)
            }
        }
    }
}

@Throws(Exception::class)
fun createCharacter(character: Character, context: Context) {
    val retrofit: Retrofit? = RetrofitToken.getRetrofit(context)
    val characterService = retrofit!!.create(ServiceCharacter::class.java)

    try {
        characterService.create(character).enqueue(object : Callback<Character> {
            override fun onResponse(call: Call<Character>, response: Response<Character>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val intent = Intent(context, BottomNavActivity::class.java)
                        context.startActivity(intent)
                        Toast.makeText(context, "Personnage ajouté avec succée", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context,"Erreur d'appel!", Toast.LENGTH_LONG ) .show()
                    }
                }
            }
            override fun onFailure(call: Call<Character>, t: Throwable ) {
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

@Composable()
fun FormCharacter(mvm: MovieViewModel, character: Character){
    val context = LocalContext.current

    LaunchedEffect(Unit, block = {
        mvm.getMovieList(context)
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        verticalArrangement = Arrangement.Center,
    ){
        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.White)) {
                append("Ajouter un personnage")
            }
        }, fontSize = 30.sp)
        Column(
            modifier = Modifier
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
        ){
            var nomPersState by mutableStateOf(character.nomPers)
            StringField(nomPersState, "Nom du personnage"){
                nomPersState = it
                character.nomPers = it
            }
            Spacer(Modifier.size(10.dp))
            MovieField(character, mvm)
            Spacer(Modifier.size(10.dp))
            ValidatorButton("Ajouter",
                enabled = true
            ) {
                createCharacter(character, context)
            }
        }
    }
}

@Composable
fun MovieField(character: Character, mvm: MovieViewModel){
    val movies = mvm.movieList

    if (mvm.movieList.isNotEmpty()) {
        val default = movies[0].noFilm
        var expanded by remember { mutableStateOf(false) }
        var selectedText by remember { mutableStateOf(default) }

        character.noFilm = default!!

        val icon = if (expanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown

        val current = movies.filter { it.noFilm == selectedText }.first()
        Column() {
            OutlinedTextField(
                value = current.titre,
                onValueChange = { },
                modifier = Modifier.fillMaxWidth().clickable{ expanded = !expanded },
                label = { current.titre },
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
                movies.forEach{ item ->
                    DropdownMenuItem(onClick = {
                        character.noFilm= item.noFilm!!
                        selectedText = item.noFilm
                        expanded = false
                    }) {
                        Text(text = item.titre)
                    }
                }
            }
        }
    }
}