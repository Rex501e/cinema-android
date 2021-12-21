package com.example.cinema.service


import android.content.Context
import com.example.cinema.config.MyConstants
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


val intercepteur : HttpLoggingInterceptor = HttpLoggingInterceptor().
apply {
    this.level = HttpLoggingInterceptor.Level.BASIC
}

object RetrofitConnexion {
    private var retrofit: Retrofit? = null
    private val intercepteur = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BASIC)

    fun getClientRetrofit(c: Context? ): Retrofit? {
        val uneSession = SessionManager(c!!)
        val unToken = uneSession.fetchAuthToken()
        if (retrofit == null) {
            // si le code est zéro, on n'envoie pas de token  -> login
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(object : Interceptor {
                    @Throws(IOException::class)
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val newRequest = chain.request().newBuilder()
                            .build()
                        return chain.proceed(newRequest)
                    }
                })
                .build()

            // on définit l'entête de l'appel
            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .setLenient()
                .create()
            retrofit = Retrofit.Builder()
                .client(httpClient)
                .baseUrl(MyConstants.url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return retrofit
    }
}

