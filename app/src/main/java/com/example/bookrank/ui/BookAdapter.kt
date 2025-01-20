package com.example.bookrank.ui

import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import bbdd.DatabaseHelper
import com.bumptech.glide.Glide
import com.example.bookrank.R
import kotlinx.parcelize.Parcelize


/*
* Esta clase se encarga de gestionar y mostrar los datos de los libros en la interfaz de usuario
* Incluye el btnAnadir para añadir libros a la base de datos
 */

class BookAdapter(private val books: List<BookData>, private val onBookSelected: (BookData) -> Unit) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {
    // Valores que se pasan a fetchBook -> No borrar
    val author_name: List<String>? = null //Tiene que ser una Lista porque así está en la API - Si lo pongo como String crashea
    val title: String = ""
    val cover_i: String? = null

    //Lista de libros seleccionados (necesaria para poder añadir libros con btnAnadir)
    private var libroSeleccionado: MutableList<BookData> = mutableListOf()

   /* //Función para obtener el libro seleccionado
    fun getSelectedBook(): MutableList<Libro> {
        return selectedBook
    }*/

    //ViewHolder: Asocia las vistas al layaout con las propiedades del modelo(la API)
    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.bookTitle)
        val author_name: TextView = itemView.findViewById(R.id.bookAuthor)
        val bookCover: ImageView = itemView.findViewById(R.id.bookCover)
        val btnAnadir: ImageView = itemView.findViewById(R.id.btnAnadir)

        //Enlaza los datos de la clase libro con las vistas del ViewHolder
        fun bind(book: BookData) {
            // Asigna los datos a las vistas
            title.text = book.title
            author_name.text = book.author_name.toString()

            //Configuración de el click listener para el botón de añadir (este está en ItemBookActivity)
            itemView.setOnClickListener {
                //onBookSelected(book)
            }
            //Carga la imagen de portada
            if (book.cover_i.isNullOrEmpty()) {
                //Usar la imagen de portada por defecto
                bookCover.setImageResource(R.drawable.placeholder_book_cover)
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
        holder.title.text = libroAPI.title
        holder.author_name.text = libroAPI.author_name.toString()
        //Construir la URL de la portada
        val coverUrl = libroAPI.cover_i?.let {
            "https://covers.openlibrary.org/b/id/$it-M.jpg"
        }

        //Cargar la imagen de portada con manejo de valores nulos o vacios
        if (libroAPI.cover_i.isNullOrEmpty()) {
            //Usar la imagen de portada por defecto
            holder.bookCover.setImageResource(R.drawable.placeholder_book_cover)
        } else {
            //Construir la URL de la portada y cargarla con Glide
           // val coverUrl = "https://covers.openlibrary.org/b/id/${libroAPI.cover_i}-M.jpg"
            Glide.with(holder.itemView.context)
                .load(coverUrl)
                .placeholder(R.drawable.placeholder_book_cover) // Imagen de reemplazo
                .error(R.drawable.error_book_cover)
                .into(holder.bookCover)
        }

        //Declaramos los componentes de item_book (layout)
        val btnAnadir = holder.itemView.findViewById<ImageButton>(R.id.btnAnadir)

          //FUNCION PARA HACER QUE AL PULSAR EL BTNAÑADIR, APAREZCAN LOS OTROS TRES BOTONES (LEIDO, PENDIENTE, FAVORITO) Y SU FUNCION
          // Botón Añadir principal
            val btnLeido: Button = holder.itemView.findViewById<Button>(R.id.btnLeido)
            val btnPendiente: Button = holder.itemView.findViewById<Button>(R.id.btnPendiente)
            val btnFavorito: Button = holder.itemView.findViewById<Button>(R.id.btnFavorito)


            // Inicialmente oculta los botones secundarios
            btnLeido.visibility = View.GONE
            btnPendiente.visibility = View.GONE
            btnFavorito.visibility = View.GONE

            btnAnadir.setOnClickListener {
                // Mostrar los botones secundarios al hacer clic en "Añadir"
                btnLeido.visibility = View.VISIBLE
                btnPendiente.visibility = View.VISIBLE
                btnFavorito.visibility = View.VISIBLE
            }

            // Manejo de boton para insertar en la lista LEIDOS
            btnLeido.setOnClickListener {
                val libroSeleccionado = libroAPI.copy(tipoLista ="leído")

                val dbHelper = DatabaseHelper(holder.itemView.context)
                val result = dbHelper.insertarLibro(
                    libroSeleccionado.title,
                    libroSeleccionado.author_name,
                    libroSeleccionado.cover_i,
                    libroSeleccionado.tipoLista
                     )
                    if (result) {
                         showLog("Libro añadido correctamente")
                            Toast.makeText(holder.itemView.context, "Libro añadido correctamente", Toast.LENGTH_SHORT).show()

                    } else {
                    showLog("Error al añadir el libro")
                    Toast.makeText(holder.itemView.context, "Error al añadir el libro", Toast.LENGTH_SHORT).show()
                     }
            }

            //Manejo de botón para insertar en la lista PENDIENTE
            btnPendiente.setOnClickListener {
                val libroSeleccionado = libroAPI.copy(tipoLista ="pendiente")

                val dbHelper = DatabaseHelper(holder.itemView.context)
                val result = dbHelper.insertarLibro(
                    libroSeleccionado.title,
                    libroSeleccionado.author_name,
                    libroSeleccionado.cover_i,
                    libroSeleccionado.tipoLista
                )
                if (result) {
                    showLog("Libro añadido correctamente")
                    Toast.makeText(holder.itemView.context, "Libro añadido correctamente", Toast.LENGTH_SHORT).show()

                } else {
                    showLog("Error al añadir el libro")
                    Toast.makeText(holder.itemView.context, "Error al añadir el libro", Toast.LENGTH_SHORT).show()
                }
            }

            //Manejo de botón para insertar en la lista FAVORITO
            btnFavorito.setOnClickListener {
                val libroSeleccionado = libroAPI.copy(tipoLista ="favorito")

                val dbHelper = DatabaseHelper(holder.itemView.context)
                val result = dbHelper.insertarLibro(
                    libroSeleccionado.title,
                    libroSeleccionado.author_name,
                    libroSeleccionado.cover_i,
                    libroSeleccionado.tipoLista
                )
                if (result) {
                    showLog("Libro añadido correctamente")
                    Toast.makeText(holder.itemView.context, "Libro añadido correctamente", Toast.LENGTH_SHORT).show()

                } else {
                    showLog("Error al añadir el libro")
                    Toast.makeText(holder.itemView.context, "Error al añadir el libro", Toast.LENGTH_SHORT).show()
                }
            }
         }
    @Parcelize
    data class BookData( //<- Tiene que tener los mismos nombres que en la API
        val title: String,
        val author_name: String,
        val cover_i: String?,
        val tipoLista: String
    ) : Parcelable
}
    fun showLog(message: String) {
    Log.d("BuscarActivity", message)
}




