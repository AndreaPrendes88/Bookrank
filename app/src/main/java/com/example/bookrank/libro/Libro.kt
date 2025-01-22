package com.example.bookrank.libro

import java.time.LocalDate

data class Libro(
    val title: String,
    val author_name: String,
    val bookCover: String,
    val tipoLista: String,//Aqui definimos si es Leido, Pendiente, Favorito
    val fechaIngreso : LocalDate = LocalDate.now()  //Mas moderno y no almacena hora exacta.
    )
   // val editorial: String,
   // val isbn: String

