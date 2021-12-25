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

object RetrofitMovie {
    private var retrofit: Retrofit? = null
    private val intercepteur = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)

    fun getMovieRetrofit(c: Context? ): Retrofit? {
        val session = SessionManager(c!!)
        val token = session.fetchAuthToken()
        if (retrofit == null) {
             val httpClient = OkHttpClient.Builder()
                    .addInterceptor(object : Interceptor {
                        @Throws(IOException::class)
                        override fun intercept(chain: Interceptor.Chain): Response {
                            val newRequest = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer $token")
                                .build()
                            return chain.proceed(newRequest)
                        }
                    })
                    .build()

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
