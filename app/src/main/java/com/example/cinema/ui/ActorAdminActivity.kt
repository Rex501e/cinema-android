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
import com.example.cinema.domain.Actor
import com.example.cinema.service.RetrofitToken
import com.example.cinema.service.ServiceActor
import com.example.cinema.ui.bottomnav.BottomNavActivity
import com.example.cinema.validator.UsernameState
import retrofit2.Retrofit

class ActorAdminActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var actor = intent.getSerializableExtra("actor") as? Actor

        var created = false;
        if (actor == null) {
            created = true;
            actor = Actor()
        }
        super.onCreate(savedInstanceState)
        setContent {
            CinemaTheme {
                FormActor(actor, created)
            }
        }
    }
}

@Composable()
fun FormActor(actor: Actor, created: Boolean){
    val context = LocalContext.current
    val strCreated = if(created) "Ajouter" else "Modifier"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp),
        verticalArrangement = Arrangement.Center,
    ){
        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.White)) {
                append("$strCreated un acteur")
            }
        }, fontSize = 30.sp)
        Column(
            modifier = Modifier
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
        ){
            var nomState by mutableStateOf(actor.nomAct)
            StringField(nomState, "Nom"){
                nomState = it
                actor.nomAct = it
            }
            Spacer(Modifier.size(10.dp))
            var prenState by mutableStateOf(actor.prenAct)
            StringField(prenState, "Prenom"){
                prenState = it
                actor.prenAct = it
            }
            Spacer(Modifier.size(10.dp))
            var dateNaissState by mutableStateOf(if (actor.dateNaiss != null) actor.dateNaiss else "")
            StringField(dateNaissState!!, "Date de naissance"){
                dateNaissState = it
                actor.dateNaiss = it
            }
            Spacer(Modifier.size(10.dp))
            var dateDecesState by mutableStateOf(if (actor.dateDeces != null) actor.dateDeces else "")
            StringField(dateDecesState!!, "Date deces"){
                dateDecesState = it
                actor.dateDeces = it
            }
            Spacer(Modifier.size(10.dp))
            ValidatorButton(strCreated,
                enabled = true
            ) {
                if (created) {
                    createActor(actor, context)
                }else{
                    updateActor(actor, context)
                }

            }
        }
    }
}

@Throws(Exception::class)
fun createActor(actor: Actor, context: Context) {
    val retrofit: Retrofit? = RetrofitToken.getRetrofit(context)
    val actorService = retrofit!!.create(ServiceActor::class.java)

    try {
        actorService.createActor(actor).enqueue(object : Callback<Actor> {
            override fun onResponse(call: Call<Actor>, response: Response<Actor>) {
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
            override fun onFailure(call: Call<Actor>, t: Throwable ) {
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
fun updateActor(actor: Actor, context: Context) {
    val retrofit: Retrofit? = RetrofitToken.getRetrofit(context)
    val serviceActor = retrofit!!.create(ServiceActor::class.java)

    try {
        serviceActor.editActor(actor.noAct ?: "-1", actor).enqueue(object : Callback<Actor> {
            override fun onResponse(call: Call<Actor>, response: Response<Actor>) {
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
            override fun onFailure(call: Call<Actor>, t: Throwable ) {
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