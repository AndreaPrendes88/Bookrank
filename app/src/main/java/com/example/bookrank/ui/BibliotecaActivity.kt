package com.example.bookrank.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bbdd.DatabaseHelper
import com.example.bookrank.R
import com.example.bookrank.libro.Libro


open class BibliotecaActivity : MainActivity() {
    private lateinit var recyclerViewLeidos: RecyclerView
    private lateinit var recyclerViewPend: RecyclerView
    private lateinit var recyclerViewFav: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biblio)
        setupNavigationButtons() //Llamamos al método para iniciar los botones

        //Declaración de los recyclerview
        val rcvLibrosLeidos = findViewById<RecyclerView>(R.id.rcvLibrosLeidos)
        val rcvLibrosPend = findViewById<RecyclerView>(R.id.rcvLibrosPend)
        val rcvLibrosFav = findViewById<RecyclerView>(R.id.rcvLibrosFav)

        //Inicialización de los recyclerview
        recyclerViewLeidos = rcvLibrosLeidos
        recyclerViewPend = rcvLibrosPend
        recyclerViewFav = rcvLibrosFav

        //Configuración RecyclerView
        recyclerViewLeidos.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewPend.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewFav.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        //Cargar datos en los RecyclerView
        cargarLibroPortipo("leido", recyclerViewLeidos, this)
        Log.d("cargarLibroPortipo", "Cargando libros leídos :$rcvLibrosLeidos")
        cargarLibroPortipo("pendiente", recyclerViewPend, this)
        cargarLibroPortipo("favorito", recyclerViewFav, this)
    }
}