package com.example.cinema.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cinema.domain.Login
import com.example.cinema.exception.Exception
import com.example.cinema.service.RetrofitLogin
import com.example.cinema.service.ServiceLogin
import com.example.cinema.service.SessionManager
import com.example.cinema.ui.bottomnav.BottomNavActivity
import com.example.cinema.validator.PasswordState
import com.example.cinema.validator.UsernameState
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

@Composable
fun SignInScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ){
        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Red)) {
                append("C")
            }
            withStyle(style = SpanStyle(color = Color.White)) {
                append("in??ma")
            }
        }, fontSize = 30.sp)
        Spacer(Modifier.size(16.dp))
        val usernameState = remember { UsernameState() }
        Username(usernameState.text, usernameState.error) {
            usernameState.text = it
            usernameState.validate()
        }
        Spacer(Modifier.size(16.dp))
        val passwordState = remember { PasswordState() }
        Password(passwordState.text, passwordState.error) {
            passwordState.text = it
            passwordState.validate()
        }
        Spacer(Modifier.size(16.dp))
        ValidatorButton("Connexion", enabled = usernameState.isValid() && passwordState.isValid()) {
            signin(usernameState.text, passwordState.text, context)
        }
    }
}

@Throws(Exception::class)
fun signin(username: String, password: String, context: Context) {
    val login= Login(username, password)
    val retrofit: Retrofit? = RetrofitLogin.getTokenRetrofit(context)
    val connexionService = retrofit!!.create(ServiceLogin::class.java)

    try {
        connexionService.getConnexion(login).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, uneReponse: Response<Any>) {
                if (uneReponse.isSuccessful) {
                    if (uneReponse.body() != null) {
                        val unObjet: Any? = uneReponse.body()
                        val jsonString = Gson().toJson(unObjet)
                        var json: JSONObject? = null
                        try {
                            json = JSONObject(jsonString)
                            val token = json.getString("token")
                            Toast.makeText(context, "Authentification r??ussie !!!", Toast.LENGTH_LONG).show()
                            SessionManager(context).saveAuthToken(token)
                            val intent = Intent(context, BottomNavActivity::class.java)
                            context.startActivity(intent)
                        } catch (e: JSONException) {
                            Exception(e.message, "Erreur Appel WS Connexion")
                        }
                    } else {
                        Toast.makeText(context,"Erreur d'appel!", Toast.LENGTH_LONG ) .show()
                    }
                } else {
                    Toast.makeText(context, "Utilisateur non trouv??", Toast.LENGTH_LONG).show();
                    Log.d("CONNEXION", "onResponse =>>> code = " + uneReponse.code())
                }
            }
            override fun onFailure(call: Call<Any?>, t: Throwable ) {
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
fun Username(username: String, error: String?, onUsernameChanged: (String) -> Unit){
    TextField(
        value = username,
        onValueChange = { value -> onUsernameChanged(value) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = "Nom d'utilisateur", color = Color.Gray) },
        colors = TextFieldDefaults.textFieldColors(textColor = Color.White),
        isError = error != null
    )

   error?.let { ErrorField(it) }
}

@Composable
fun Password(password: String, error: String?, onPasswordChanged: (String) -> Unit){
    val passwordVisibility = remember { mutableStateOf(true) }

    TextField(
        value = password,
        onValueChange = { value -> onPasswordChanged(value) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = "Mot de passe", color = Color.Gray) },
        trailingIcon = {
            IconButton(onClick = {
                passwordVisibility.value = !passwordVisibility.value
            }) {
                Icon(
                    imageVector = if (passwordVisibility.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = "visibility",
                    tint = Color.Red
                )
            }
        },
        visualTransformation = if (passwordVisibility.value) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.textFieldColors(textColor = Color.White),
        isError = error != null
    )

    error?.let { ErrorField(it) }
}

@Composable
fun ValidatorButton(text: String, enabled: Boolean, onClick: () -> Unit){
    Button(
        onClick = { onClick() },
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
        enabled = enabled
    ) {
        Text(text = text, color = Color.White)
    }
}

@Composable
fun ErrorField(error: String) {
    Text(
        text = error,
        modifier = Modifier.fillMaxWidth(),
        style = TextStyle(color = MaterialTheme.colors.error)
    )
}