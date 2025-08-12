package com.example.test.ui.theme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {

    val api:FinkApi by lazy{
        Retrofit.Builder()
            .baseUrl("https://api.fink-portal.org/")  // Fink API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FinkApi::class.java)
    }

}