package com.example.bookrank.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://openlibrary.org/"

    //Configuración de Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Expone la API configurada
    val api: OpenLibraryApi by lazy {
        retrofit.create(OpenLibraryApi::class.java)
    }
}