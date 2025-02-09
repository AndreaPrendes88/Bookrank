package com.example.bookrank.libro

import java.time.LocalDate

data class Libro(
    val id: Int,
    val title: String,
    val author_name: String,
    val bookCover: String,
    val fechaIngreso: LocalDate = LocalDate.now() //Mas moderno y no almacena hora exacta.
    )


