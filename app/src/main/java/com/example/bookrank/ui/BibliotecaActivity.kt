package com.example.bookrank.ui


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bbdd.DatabaseHelper
import com.example.bookrank.R
import com.example.bookrank.libro.Libro


open class BibliotecaActivity : MainActivity() {
    private lateinit var recyclerViewLeido: RecyclerView
    private lateinit var recyclerViewPend: RecyclerView
    private lateinit var recyclerViewFav: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biblio)
        setupNavigationButtons() //Llamamos al método para iniciar los botones

        //Declaración de los recyclerview y el botón
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

    private fun cargarLibroPortipo(tipoLista: String, recyclerView: RecyclerView, context: Context) {
        val dbHelper = DatabaseHelper(context)
        //Usamos esa instancia para llamar al metodo
        val libros = when (tipoLista) {
            "leído" -> dbHelper.getLibrosLeidos(context)
            "pendiente" -> dbHelper.getLibrosPendientes(context)
            "favorito" -> dbHelper.getLibrosFavoritos(context)
            else -> emptyList()
        }

        Log.d("cargarLibroPortipo", "Cargando libros para $tipoLista: $libros")

        val adapter = BiblioAdapter(libros)
        recyclerView.adapter = adapter

        adapter.onPapeleraClick = { libro ->
            libroSeleccionado = libro
            mostrarDialogoBorrado()
        }
    }

    private var libroSeleccionado: Libro? = null

    private fun obtenerLibroSeleccionado(): Libro? {
        return libroSeleccionado
    }

    private fun mostrarDialogoBorrado() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Libro")
        builder.setMessage("¿Estás seguro de que deseas eliminar este libro?")
        builder.setPositiveButton("Sí") { dialog, _ ->
            borrarLibroSeleccionado()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()

    }

   //Metodo para borrar el libro de la base de datos y de la lista
    private fun borrarLibroSeleccionado(){
        val libroSeleccionado = obtenerLibroSeleccionado()
        if (libroSeleccionado != null) {
            val dbHelper = DatabaseHelper(this)
            val exito = dbHelper.eliminarLibro(libroSeleccionado.id) // Usar el ID del libro

            if (exito) {
                Toast.makeText(this, "Libro eliminado correctamente", Toast.LENGTH_SHORT).show()
                // Actualiza la lista y el RecyclerView
                configurarRecyclerView()
            } else {
                Toast.makeText(this, "Error al eliminar el libro", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No se ha seleccionado ningún libro", Toast.LENGTH_SHORT).show()
        }
    }
}
