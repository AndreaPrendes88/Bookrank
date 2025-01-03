package com.example.bookrank.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookrank.R

class BookAdapter(val books: List<BookData>) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {
        // Valores que se pasan a fetchBook -> No borrar
    val author_name: List<String>? = null
    val title: String = ""
    val cover_i: String = ""

    //ViewHolder: Asocia las vistas del layaout con las propiedades del modelo
    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.bookTitle)
        val author_name: TextView = itemView.findViewById(R.id.bookAuthor)
        val bookCover: ImageView = itemView.findViewById(R.id.bookCover)
    }

    //Es un adaptador de RecyclerView, esencial para saber cuantas veces debe llamar a onBindViewHolder y onCreateViewHolder
        override fun getItemCount(): Int = books.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

        override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.title.text = book.title
        holder.author_name.text = book.author_name.toString()

        //Construye la URL de la portada <- Maneja el caso en el caos de que sea null
        val coverUrl = book.cover_i?.let {
            "https://covers.openlibrary.org/b/id/$it-M.jpg"
        }

        //Cargar imagen si se tiene la URL de la portada
        Glide.with(holder.itemView.context)
            .load(coverUrl)
            .placeholder(R.drawable.placeholder_book_cover) // Imagen de reemplazo
            //.error(R.drawable.error_book_cover)
            .into(holder.bookCover)
        }

    data class BookData( //<- Tiene que tener los mismos nombres que en la API
        val title: String,
        val author_name: String,
        val cover_i: String?
    )
    }



