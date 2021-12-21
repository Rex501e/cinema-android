package com.example.cinema.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cinema.MainActivity
import com.example.cinema.R
import com.example.cinema.domain.LoginParam
import com.example.cinema.domain.Utilisateur
import com.example.cinema.meserreurs.MonException
import com.example.cinema.service.IntConnexion
import com.example.cinema.service.RetrofitClient
import com.example.cinema.service.RetrofitConnexion
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ActivityConnexion : AppCompatActivity() {
    private lateinit var edLogin: EditText;
    private lateinit var edPwd: EditText
    private lateinit var btnQuitter: Button
    private lateinit var btnValider: Button
    private var unUtilisateur: Utilisateur? = null
    private var unlogin: LoginParam? = null
    private val TAG = "Connexion"
    private var data: Intent? = null
    private var nom: String? = null
    private var pwd: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        event()
    }

    fun event() {
        btnQuitter.setOnClickListener {
            finish()
        }
        btnValider.setOnClickListener {
            nom= edLogin.getText().toString()
            pwd = edPwd.getText().toString()

           val  unLogin= LoginParam(nom!!,pwd!!)
            controleUtilisateur(unLogin)
        }
    }

    @Throws(MonException::class)
    fun controleUtilisateur(unLogin:LoginParam) {
        val retour = false

        // On construit le client
        val retrofit: Retrofit? = RetrofitConnexion.getClientRetrofit(this)
       // On appelle l'interface de connexion
        val uneConnexionService = retrofit!!.create(IntConnexion::class.java)
        try {
            var rep = uneConnexionService.getConnexion(unLogin)
                // appel asynchrone
                .enqueue(object : Callback<Object> {
                    override fun onResponse( call: Call<Object>,uneReponse: Response<Object>) {
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
                                    stocke(unToken)
                                } catch (e: JSONException) {
                                    MonException(e.message, "Erreur Appel WS Connexion")
                                }
           Toast.makeText( this@ActivityConnexion, "Authentification réussie !!!",Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this@ActivityConnexion,"Erreur d'appel!",Toast.LENGTH_LONG ) .show()
                            }
                        } else { //Toast.makeText(MainActivity.this, "Erreur rencontrée", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onResponse =>>> code = " + uneReponse.code())
                        }
                    }
                    override fun onFailure( call: Call<Object?>, t: Throwable ) {
                        Toast.makeText(
                            this@ActivityConnexion,"Erreur de connexion",
                            Toast.LENGTH_LONG ) .show()
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

    // retour vers  les informations à la fenêtre principale

    private fun stocke(unTk: String) {
        val retour = 1
        val intent = Intent(this@ActivityConnexion, MainActivity::class.java)
        intent.putExtra("unToken", unTk)
        setResult(Activity.RESULT_OK, intent)
        super.finish()
    }

}


