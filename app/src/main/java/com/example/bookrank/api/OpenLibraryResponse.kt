package com.example.bookrank.api

import com.example.bookrank.ui.BookAdapter

data class OpenLibraryResponse(
    val docs: List<BookAdapter> //Listado de libros devueltos por la API
)

