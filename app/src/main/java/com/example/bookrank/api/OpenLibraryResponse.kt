package com.example.bookrank.api

import com.example.bookrank.ui.BookAdapter

data class OpenLibraryResponse(
    val docs: List<BookAdapter.BookData> //Listado de libros devueltos por la API
    //docs contiene una lista de libros corretamente formateados.

)