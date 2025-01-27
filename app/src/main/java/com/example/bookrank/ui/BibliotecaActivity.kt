package com.example.bookrank.ui

import android.os.Bundle
import android.util.Log

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookrank.R


open class BibliotecaActivity : MainActivity() {
    private lateinit var recyclerViewLeido: RecyclerView
    private lateinit var recyclerViewPend: RecyclerView
    private lateinit var recyclerViewFav: RecyclerView
    //private lateinit var btnPapelera: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biblio)
        setupNavigationButtons() //Llamamos al método para iniciar los botones

        //Declaración de los recyclerview
        val rcvLibrosLeido = findViewById<RecyclerView>(R.id.rcvLibrosLeido)
        val rcvLibrosPend = findViewById<RecyclerView>(R.id.rcvLibrosPend)
        val rcvLibrosFav = findViewById<RecyclerView>(R.id.rcvLibrosFav)

        //Inicialización de los recyclerview
        recyclerViewLeido = rcvLibrosLeido
        recyclerViewPend = rcvLibrosPend
        recyclerViewFav = rcvLibrosFav

        //Configuración RecyclerView
        recyclerViewLeido.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewPend.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewFav.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        //Inicializacion del botón papelera
      //  btnPapelera = findViewById(R.id.btnPapelera)

        //Configurar inicial de los recyclerView
        configurarRecyclerView()

    }

    private fun configurarRecyclerView() {
        //Cargar datos en los RecyclerView
        cargarLibroPortipo("leído", recyclerViewLeido, this)
        Log.d("cargarLibroPortipo", "Cargando libros leídos :$recyclerViewLeido")
        cargarLibroPortipo("pendiente", recyclerViewPend, this)
        cargarLibroPortipo("favorito", recyclerViewFav, this)
    }
}