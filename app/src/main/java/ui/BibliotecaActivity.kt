package ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookrank.R
import com.example.bookrank.ui.cargarLibroPortipo


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
        recyclerViewLeidos.layoutManager = LinearLayoutManager(this)
        recyclerViewPend.layoutManager = LinearLayoutManager(this)
        recyclerViewFav.layoutManager = LinearLayoutManager(this)

        //Cargar datos en los RecyclerView
        cargarLibroPortipo("leido", recyclerViewLeidos,this)
        cargarLibroPortipo("pendiente", recyclerViewPend, this)
        cargarLibroPortipo("favorito", recyclerViewFav, this)
    }

}

