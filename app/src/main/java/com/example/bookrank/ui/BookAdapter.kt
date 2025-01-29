package com.example.bookrank.ui

import android.content.Context
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import bbdd.DatabaseHelper
import com.bumptech.glide.Glide
import com.example.bookrank.R
import kotlinx.parcelize.Parcelize


/*
* Esta clase se encarga de gestionar y mostrar los datos de los libros en la interfaz de usuario
* Incluye el btnAnadir para añadir libros a la base de datos
 */

class BookAdapter(private val books: List<BookData>, private val context: Context) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {
    // Valores que se pasan a fetchBook -> No borrar
    val author_name: List<String>? = null
    val title: String = ""
    val cover_i: String? = null


    //ViewHolder: Asocia las vistas al layaout con las propiedades del modelo(la API)
    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView: TextView = itemView.findViewById(R.id.bookTitle)
        val authorNameView: TextView = itemView.findViewById(R.id.bookAuthor)
        val bookCoverView: ImageView = itemView.findViewById(R.id.bookCover)
        val btnAnadir: ImageButton = itemView.findViewById(R.id.btnAnadir)


        //Enlaza los datos de la clase libro con las vistas del ViewHolder
        fun bind(book: BookData) {
            // Asigna los datos a las vistas
            titleView.text = book.title
            authorNameView.text = book.author_name.toString()

            //Configuración de el click listener para el botón de añadir (este está en ItemBookActivity)
            itemView.setOnClickListener {
                //onBookSelected(book)
            }
            //Carga la imagen de portada
            if (book.cover_i.isNullOrEmpty()) {
                //Usar la imagen de portada por defecto
                bookCoverView.setImageResource(R.drawable.placeholder_book_cover)
            } else {
                val coverURL = "https://covers.openlibrary.org/b/id/${book.cover_i}-M.jpg"
            }
        }
    }

    //Es un adaptador de RecyclerView, esencial para saber cuantas veces debe llamar a onBindViewHolder y onCreateViewHolder
    override fun getItemCount(): Int = books.size

    //AQUI CARGA LA VISTA DE LOS LIBROS (ITEM_BOOK)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    //RECOGE LOS VALORES DE LA API Y LOS TRANSFORMA PARA QUE SE MUESTREN EN LA VISTA
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val libroAPI = books[position]
        holder.bind(libroAPI)

        //Configurar los textos
        holder.titleView.text = libroAPI.title
        holder.authorNameView.text = libroAPI.author_name.toString()
        //Construir la URL de la portada
        val coverUrl = libroAPI.cover_i?.let {
            "https://covers.openlibrary.org/b/id/$it-M.jpg"
        }

        //Cargar la imagen de portada con manejo de valores nulos o vacios
        if (libroAPI.cover_i.isNullOrEmpty()) {
            //Configurar la imagen y usar una por defecto
            holder.bookCoverView.setImageResource(R.drawable.placeholder_book_cover)
        } else {
            //Construir la URL de la portada y cargarla con Glide
            Glide.with(holder.itemView.context)
                .load(coverUrl)
                .placeholder(R.drawable.placeholder_book_cover) // Imagen de reemplazo
                .error(R.drawable.error_book_cover)
                .into(holder.bookCoverView)
        }
        
        holder.btnAnadir.setOnClickListener {
            val libroSeleccionado = libroAPI.copy()
            val dbHelper = DatabaseHelper(context)
            val exito = dbHelper.insertarLibro(
                libroSeleccionado.title,
                libroSeleccionado.author_name,
                libroSeleccionado.cover_i,
            )
            if (exito) {
                showLog("Libro añadido correctamente")
                Toast.makeText(holder.itemView.context, "Libro añadido correctamente", Toast.LENGTH_SHORT).show()

                //Obtener el ID del libro recién insertado.
                val libroId = dbHelper.getIdLibro(libroSeleccionado.title, libroSeleccionado.author_name, libroSeleccionado.cover_i)
                if (libroId != null) {
                    //Mostrar el cuadro de díalogo para elegir la categoría.
                    mostrarDialogoSeleccion(holder.itemView.context, dbHelper, libroId)
                } else {
                    showLog("Error al obtener el ID del libro")
                }
            } else {
                showLog("Error al añadir el libro")
                Toast.makeText(holder.itemView.context, "Error al añadir el libro", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Función para mostrar el cuadro de diálogo con las diferentes categorias.
    private fun mostrarDialogoSeleccion(context: Context, dbHelper: DatabaseHelper, libroId: Int) {
        val opciones = arrayOf("Leído", "Pendiente", "Favorito")
        val builder = AlertDialog.Builder(context)
        builder.setTitle("¿A qué lista quieres añadir el libro?")
        builder.setItems(opciones) { _, which ->
            when (which) {
                0 -> {
                    dbHelper.insertarLibroLeido(libroId)
                    Toast.makeText(context, "Libro añadido a la lista de leídos", Toast.LENGTH_SHORT).show()
                }
                1 -> {
                    dbHelper.insertarLibroPendiente(libroId)
                    Toast.makeText(context, "Libro añadido a la lista de pendientes", Toast.LENGTH_SHORT
                    ).show()
                }
                2 -> {
                    dbHelper.insertarLibroFavorito(libroId)
                    Toast.makeText(context, "Libro añadido a la lista de favoritos", Toast.LENGTH_SHORT).show()
                }
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    @Parcelize
    data class BookData( //<- Tiene que tener los mismos nombres que en la API
        val title: String,
        val author_name: String,
        val cover_i: String?,
        val tipoLista: String,

    ) : Parcelable
}
    fun showLog(message: String) {
    Log.d("BuscarActivity", message)
}




