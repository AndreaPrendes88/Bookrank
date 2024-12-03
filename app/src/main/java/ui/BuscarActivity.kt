package ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookrank.R


open class BuscarActivity : AppCompatActivity() {

    private lateinit var resultsRecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var booksAdapter: BooksAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar)

        //Iniciar los elementos
        searchView = findViewById(R.id.searchLibro)
        resultsRecyclerView = findViewById(R.id.resultLibros)

        //Configuración RecyclerView
        booksAdapter = BooksAdapter(listOf()) // <- Revisar el nombre, quizás sea BuscarLibroSQLite
        resultsRecyclerView.layoutManager = LinearLayoutManager(this)
        resultsRecyclerView.adapter = booksAdapter

        //Configuración del searchView (searchLibro)
        searchView.setOnClickListener {
            val query = searchView.text.toString()
            performSearch(query)
        }
    }

    private fun performSearch(query: String){
        //Lógica para buscar los libros
        // Aqui debe hacer la búsqueda, con la API
        //Actualizar el adaptador con los resultados
        val results = searchView(query) // <-- Esta sería la función de búsqueda
        booksAdapter.updateBooks(results)
    }
}



