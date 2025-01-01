package com.example.bookrank.ui

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookrank.R

class BookAdapter(private val books: List<BookData>) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    val author_name: List<String>? = null
    val title: String = ""

    //ViewHolder: Asocia las vistas del layaout con las propiedades del modelo
            class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
                val title: TextView = itemView.findViewById(R.id.bookTitle)
                val author: TextView = itemView.findViewById(R.id.bookAuthor)
              //  val bookCover: ImageView = itemView.findViewById(R.id.bookCover)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_book, parent, false)
                return BookViewHolder(view)
            }

            override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
                val book = books[position]
                holder.title.text = book.title
                holder.author.text = book.author_name

             /*   // Cargar imagen si se tiene la URL de la portada
                Glide.with(holder.itemView.context)
                    .load(book.covers)
                    .placeholder(R.drawable.placeholder_book_cover) // Imagen de reemplazo
                    .into(holder.bookCover) */
            }

            override fun getItemCount(): Int = books.size

            data class BookData(
                val title: String,
                val author_name: String,
                //val covers: String? // URL de la portada del libro
              )
    }

