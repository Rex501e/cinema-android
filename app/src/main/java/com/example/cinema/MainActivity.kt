package com.example.cinema

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.cinema.presentation.ActivityConnexion
import com.example.cinema.service.SessionManager

class MainActivity : ComponentActivity() {

    val MyPREFERENCES = "MyPrefs"
    private val sharedpreferences: SharedPreferences? = null
    private var token: String? = null
    private var session: SessionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    if (result.data!!.hasExtra("token")) {
                        val token = result.data!!.getSerializableExtra("token") as String
                        session!!.saveAuthToken(token)
                    }
                    else
                        super.finish()
                }
            }

        }

        startForResult.launch(Intent(this, ActivityConnexion::class.java))

        this.token = null
        this.session =  SessionManager(this)
    }
}