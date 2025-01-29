package com.example.bookrank.api

import retrofit2.http.GET;
import retrofit2.http.Query;

interface OpenLibraryApi {;
    @GET("search.json")
    suspend fun searchBooks(@Query("q") title: String): OpenLibraryResponse
}