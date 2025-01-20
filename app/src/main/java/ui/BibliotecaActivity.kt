package ui

import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import bbdd.DatabaseHelper
import com.bumptech.glide.Glide
import com.example.bookrank.R
import com.example.bookrank.libro.Libro
import com.example.bookrank.ui.BookAdapter


open class BibliotecaActivity : MainActivity() {

    private lateinit var bookCoverAdd: ImageView
    private lateinit var book: Libro
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var rcvLibrosLeidos: RecyclerView
    private lateinit var rcvLibrosPend: RecyclerView
    private lateinit var rcvLibrosFav: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biblio)
        setupNavigationButtons() //Llamamos al método para iniciar los botones

        val bookCoverAdd = findViewById<ImageView>(R.id.bookCoverAdd)
        val rcvLibrosLeidos = findViewById<RecyclerView>(R.id.rcvLibrosLeidos)
        val rcvLibrosPend = findViewById<RecyclerView>(R.id.rcvLibrosPend)
        val rcvLibrosFav = findViewById<RecyclerView>(R.id.rcvLibrosFav)
        //recyclerView.layoutManager = LinearLayoutManager(this)

        databaseHelper = DatabaseHelper(this)

    }
}

