package com.example.cinema.ui

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
import com.example.cinema.data.ActorViewModel
import com.example.cinema.data.CharacterViewModel
import com.example.cinema.data.MovieViewModel
import com.example.cinema.domain.Actor
import com.example.cinema.domain.Character
import com.example.cinema.domain.Movie
import com.example.cinema.validator.SearchState


@Composable
fun Actors() {
    val vm = ActorViewModel()
    val context = LocalContext.current

    LaunchedEffect(Unit, block = {
        vm.getCharactersList(context)
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
                        SearchTxtFieldForCharacter(searchState.text, searchState.error, context, vm) {
                            searchState.text = it
                            searchState.validate()
                        }
                    }
                }
            }
        }
        items(items = vm.actorsList) { actor ->
            Character(actor = actor, context)
        }
    }
}

@Composable
fun SearchTxtFieldForCharacter(
    search: String,
    error: String?,
    context: Context,
    vm: ActorViewModel,
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
                vm.getCharactersList(context, search)
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
fun Character(actor: Actor, context: Context) {
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
                        text = actor.nomAct +" "+ actor.prenAct,
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
                CharacterDetail(actor, context);
            }
        }
    }
}

@Composable
fun CharacterDetail(actor: Actor, context: Context) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "ID: " + actor.noAct,
                color = Color.White,
                fontSize = 15.sp
            )
            Text(
                text = "Date de naissance: " + actor.dateNaiss,
                color = Color.White,
                fontSize = 15.sp
            )
            if (actor.dateDeces != null) {
                Text(
                    text = "Date de sortie: " + actor.dateDeces,
                    color = Color.White,
                    fontSize = 15.sp
                )
            }
            Spacer(Modifier.size(16.dp))
            Text(
                text = "Personnage joué:",
                color = Color.White,
                fontSize = 15.sp
            )
            for (item in actor.characters){
                Text(
                    text = " - "+ item.film?.titre+ ": "+ item.nomPers
                )
            }

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
                intent.putExtra("actor", actor);
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