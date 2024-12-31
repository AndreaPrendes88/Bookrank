package ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookrank.R
import com.example.bookrank.api.RetrofitClient
import com.example.bookrank.ui.BookAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BuscarActivity: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
 //   private lateinit var bookAdapter: BookAdapter
  //  private var booksList: List<BookAdapter.BookData> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar)
        showLog("Iniciando los componentes")

        //Declaramos los componentes del Activity
        val resultLibros = findViewById<RecyclerView>(R.id.resultLibros)
        val searchLibro = findViewById<SearchView>(R.id.searchLibro)

        //Configuración RecyclerView
        recyclerView = resultLibros
        recyclerView.layoutManager = LinearLayoutManager(this)

        searchLibro.setOnQueryTextListener(createSearchListener(searchLibro))
    }

    //Creamos el listener del SearchView
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
        //Registro para verificar el texto de búsqueda
        showLog("Buscando: $query")
        fetchBooks(query)
    }

    private fun fetchBooks(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Llama al método suspend
                val response = RetrofitClient.api.searchBooks(query)

                //Procesa la respuesta y genera la lista de libros
                val books = response.docs.map {
                    BookAdapter.BookData(
                        title = it.title,
                        author_name = it.author_name?.firstOrNull() ?: "Autor desconocido"
                       // coverUrl = "https://covers.openlibrary.org/b/id/${it.cover_i}-L.jpg"
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
                    }
                }
            } catch (e: Exception) {
                // Maneja el error
                withContext(Dispatchers.Main) {
                    showLog("Error al buscar libros: ${e.message}")
                }
            }
        }

    }

    // Función para actualizar el RecyclerView con los datos obtenidos
    private fun updateRecyclerView(books: List<BookAdapter.BookData>) {
        val resultLibros = findViewById<RecyclerView>(R.id.resultLibros)
        val adapter = BookAdapter(books)
        resultLibros.adapter = adapter
    }

    private fun showLog(message: String) {
        Log.d("BuscarActivity", message)
    }
}


