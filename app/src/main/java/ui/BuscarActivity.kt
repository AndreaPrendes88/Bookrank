package ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bookrank.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.bookrank.api.OpenLibraryApi
import com.example.bookrank.api.OpenLibraryResponse

open class BuscarActivity : AppCompatActivity() {

    //Añadimos los eventos de la Activity
    lateinit var btnBuscar: Button
    lateinit var resultLibros: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar)

        btnBuscar = findViewById(R.id.btnBuscar)
        resultLibros = findViewById(R.id.resultLibros)

        fun onCreateOptionsMenu(menu: Menu?): Boolean {
            //Inflar el menú desde el archivo XML
            val searchItem = menu?.findItem(R.id.searchMenuLibro)
            val searchLibro = searchItem?.actionView as SearchView

            //Configurar el comportamiento del searchView
            searchLibro.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                //Se ejecuta cuando el usuacio envía la búsqueda
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                // se ejecuta mientras el usuario escribe
                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
            // Este return es necesario para indicar que el menú ha sido correctamente creado e inflado
            return true
        }
        //Añadimos el listener del botón btnBuscar
           btnBuscar.setOnClickListener {
            try {
                // Crear Intent para iniciar la actividad de búsqueda
                val intent = Intent(this, OpenLibraryResponse::class.java)
                startActivity(intent)
                val BASE_URL = "https://openlibrary.org/"
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val api = retrofit.create(OpenLibraryApi::class.java)

                // Realizar la llamada
                val call = api.searchBooks("Grey")

            } catch (e: Exception) {
                // Si hay un error al intentar iniciar la actividad, lo mostramos
                Toast.makeText(
                    this,
                    "Error al iniciar la busqueda: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}


