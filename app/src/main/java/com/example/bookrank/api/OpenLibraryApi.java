package com.example.bookrank.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenLibraryApi{
    @GET("search.json")

    //Definir la llamada HTTP
    Call<OpenLibraryResponse> searchBooks(@Query("q") String title);
}

