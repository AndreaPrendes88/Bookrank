package com.example.bookrank.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookrank.R
import com.example.bookrank.api.RetrofitClient
import com.example.bookrank.libro.Libro
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BuscarActivity: MainActivity() {
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar)
        setupNavigationButtons() //Llamamos al método para iniciar los botones
        showLog("Iniciando los componentes")

        //Declaramos los componentes del Activity
        val resultLibros = findViewById<RecyclerView>(R.id.resultLibros)
        val searchLibro = findViewById<SearchView>(R.id.searchLibro)

        //Configuración RecyclerView
        recyclerView = resultLibros
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        searchLibro.setOnQueryTextListener(createSearchListener(searchLibro))
    }

    //Creamos el listener del SearchView (buscador)
    private fun createSearchListener(searchLibro: SearchView): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    showLog(it) //Registra el texto en el log
                    realizarBusqueda(query) //Realiza la busqueda
                }
                searchLibro.clearFocus() //Oculta el teclado y quita el foco
                return true
            }

            //Aparecen resultados a medida que se escribe el texto en la barra de busqueda
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        }
    }

    //Función para realizar la búsqueda en la API
    private fun realizarBusqueda(query: String) {
        //Registro para verificar el texto de busqueda
        showLog("Realizando búsqueda: $query")
        fetchBooks(query)
    }

    private var isFetching = false

    private fun fetchBooks(query: String) {
        if(isFetching) return
        isFetching = true

        lifecycleScope.launch{
            try {
                // Llama al método suspend
                val response = RetrofitClient.api.searchBooks(query)
                showLog("Respuesta de la API: $response")

                //Procesa la respuesta y genera la lista de libros
                val books = response.docs.map {
                    BookAdapter.BookData(
                        title = it.title,
                        author_name = it.author_name,
                        cover_i = it.cover_i,
                        tipoLista = "",
                    )
                }
                    // Procesa el resultado en el hilo principal
                withContext(Dispatchers.Main) {
                    if (response.docs.isNotEmpty()) {
                        // Actualiza la interfaz de usuario con los resultados
                        updateRecyclerView(books)
                        showLog("Libros encontrados: ${response.docs}")
                    } else {
                        showLog("No se encontraron libros para la consulta: $query")
                        Toast.makeText(this@BuscarActivity, "No se ha encontrado libros con el texto introducido", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // Maneja el error
                showLog("Error al buscar libros: ${e.message}")
                Toast.makeText(this@BuscarActivity, "Error al buscar el libro", Toast.LENGTH_SHORT).show()
            } finally {
                    isFetching = false //Resetea el estado de la llamada
            }
        }
    }

    //Variable para el "libro selecionado" con el btnAnadir (BookAdapter)
    private var selectedBook: Libro? = null

    // Función para recibir la lista procesada de BookAdapter.BookData y actualizar el RecyclerView
    private fun updateRecyclerView(books: List<BookAdapter.BookData>) {
        val resultLibros = findViewById<RecyclerView>(R.id.resultLibros)

        //Pasa el listener al adaptador para manejar la selección
        val adapter = BookAdapter(books, this)
        resultLibros.adapter = adapter
    }
}




