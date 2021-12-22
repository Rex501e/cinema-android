package com.example.cinema.presentation

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cinema.MainActivity
import com.example.cinema.domain.LoginParam
import com.example.cinema.meserreurs.MonException
import com.example.cinema.service.IntConnexion
import com.example.cinema.service.RetrofitConnexion
import com.example.cinema.ui.theme.CinemaTheme
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ActivityConnexion : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CinemaTheme {
                loginForm()
            }
        }

        /*
            username = edLogin.getText().toString()
            password edPwd.getText().toString()

            val login = login(nom!!,pwd!!)
            controleUtilisateur(login)
         */
    }


    @Throws(MonException::class)
    fun login(login: LoginParam) {
        val retour = false

        // On construit le client
        val retrofit: Retrofit? = RetrofitConnexion.getClientRetrofit(this)
        // On appelle l'interface de connexion
        val uneConnexionService = retrofit!!.create(IntConnexion::class.java)
        try {
            var rep = uneConnexionService.getConnexion(login)
                // appel asynchrone
                .enqueue(object : Callback<Object> {
                    override fun onResponse(call: Call<Object>, uneReponse: Response<Object>) {
                        if (uneReponse.isSuccessful) { //Recupérer le corps de la reponse que Retrofit
                            // s'est chargé de désérialiser
                            //  à notre place l'aide du convertor Gson
                            if (uneReponse.body() != null) {
                                val unObjet: Any? = uneReponse.body()
                                val jsonString = Gson().toJson(unObjet)
                                var unJSO: JSONObject? = null
                                try {
                                    unJSO = JSONObject(jsonString)
                                    val unToken = unJSO.getString("token")
                                    Toast.makeText(
                                        this@ActivityConnexion,
                                        "Authentification réussie !!!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    save(unToken)
                                } catch (e: JSONException) {
                                    MonException(e.message, "Erreur Appel WS Connexion")
                                }
                                Toast.makeText(
                                    this@ActivityConnexion,
                                    "Authentification réussie !!!",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@ActivityConnexion,
                                    "Erreur d'appel!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else { //Toast.makeText(MainActivity.this, "Erreur rencontrée", Toast.LENGTH_LONG).show();
                            Log.d("CONNEXION", "onResponse =>>> code = " + uneReponse.code())
                        }
                    }

                    override fun onFailure(call: Call<Object?>, t: Throwable) {
                        Toast.makeText(
                            this@ActivityConnexion, "Erreur de connexion",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        } catch (exception: IllegalStateException) {
            MonException(exception.message, "Erreur Appel WS Connexion")
        } catch (exception: JsonSyntaxException) {
            MonException(exception.message, "Erreur Appel WS Connexion")
        } catch (e: Exception) {
            MonException(e.message, "Erreur Appel WS Connexion")
        }
    }

    private fun save(token: String) {
        val retour = 1
        val intent = Intent(this@ActivityConnexion, MainActivity::class.java)
        intent.putExtra("token", token)
        setResult(Activity.RESULT_OK, intent)
        super.finish()
    }

}

@Preview(showBackground = true, widthDp = 320, uiMode = Configuration.UI_MODE_NIGHT_YES,)
@Composable
fun loginForm() {
    val context = LocalContext.current
    val username = remember { mutableStateOf(TextFieldValue()) }
    val usernameErrorState = remember { mutableStateOf(false) }
    val passwordErrorState = remember { mutableStateOf(false) }
    val password = remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Red)) {
                append("C")
            }
            withStyle(style = SpanStyle(color = Color.White)) {
                append("inéma")
            }

        }, fontSize = 30.sp)
        Spacer(Modifier.size(16.dp))
        OutlinedTextField(
            value = username.value,
            onValueChange = {
                if (usernameErrorState.value) {
                    usernameErrorState.value = false
                }
                username.value = it
            },
            isError = usernameErrorState.value,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Nom d'utilisateur", color = Color.Gray)
            },
            colors = TextFieldDefaults.textFieldColors(textColor = Color.White)
        )
        if (usernameErrorState.value) {
            Text(text = "Champs obligatoire", color = Color.Red)
        }
        Spacer(Modifier.size(16.dp))
        val passwordVisibility = remember { mutableStateOf(true) }
        OutlinedTextField(
            value = password.value,
            onValueChange = {
                if (passwordErrorState.value) {
                    passwordErrorState.value = false
                }
                password.value = it;
            },
            isError = passwordErrorState.value,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Mot de passe", color = Color.Gray);
            },
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
            colors = TextFieldDefaults.textFieldColors(textColor = Color.White)
        )
        if (passwordErrorState.value) {
            Text(text = "Champs obligatoire", color = Color.Red)
        }
        Spacer(Modifier.size(16.dp))
        Button(
            onClick = {
                when {
                    username.value.text.isEmpty() -> {
                        usernameErrorState.value = true
                    }
                    password.value.text.isEmpty() -> {
                        passwordErrorState.value = true
                    }
                    else -> {
                        passwordErrorState.value = false
                        usernameErrorState.value = false
                        Toast.makeText(
                            context,
                            "Logged in successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            },
            content = {
                Text(text = "Connexion", color = Color.White)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
        )
    }
}


