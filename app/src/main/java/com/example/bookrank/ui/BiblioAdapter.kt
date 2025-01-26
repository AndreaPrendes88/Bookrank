package com.example.bookrank.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import bbdd.DatabaseHelper
import com.bumptech.glide.Glide
import com.example.bookrank.R
import com.example.bookrank.libro.Libro

class BiblioAdapter(private val librosBiblio: List<Libro>): RecyclerView.Adapter<BiblioAdapter.BiblioViewHolder>() {

    //Definimos como se enlazan los datos con la vista
    inner class BiblioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tituloB: TextView = itemView.findViewById(R.id.bookTitleAdd)
        val autorB: TextView = itemView.findViewById(R.id.bookAuthorAdd)
        val coverB: ImageView = itemView.findViewById(R.id.bookCoverAdd)
    }

    //Infla el diseño del book_biblio para cada libro
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BiblioViewHolder {
        try{
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.book_biblio, parent, false)
            return BiblioViewHolder(view)
        } catch (e: Exception) {
            throw IllegalStateException("Error al inflar el diseño del book_biblio", e)
        }
    }

    //Devuelve el número de libros
    override fun getItemCount(): Int = librosBiblio.size

    //Vincula los datos de la clase Libro con los elementos de la vista
    override fun onBindViewHolder(holder: BiblioAdapter.BiblioViewHolder, position: Int) {
        val libroB = librosBiblio[position]
        holder.tituloB.text = libroB.title
        holder.autorB.text = libroB.author_name
        Log.d("BiblioAdapter","Lista de libros: $librosBiblio")

        //Construir la URL de la portada
        val coverUrl = libroB.bookCover?.let {
            "https://covers.openlibrary.org/b/id/$it-M.jpg"
        }

        //Es para verificar que pasa el valor a Glide
        Log.d("GlideURL", "Cargando imagen para el libro: ${libroB.bookCover}")
        if (!coverUrl.isNullOrEmpty()){
            Glide.with(holder.itemView.context)
                .load(coverUrl) // Campo "cover" que contiene la URL de la imagen
                .into(holder.coverB) // ImageView donde se mostrará la imagen
            //holder.coverB.text = libroB.bookCover //TODO cambiar text por imagen
        } else{
            holder.coverB.setImageResource(R.drawable.placeholder_book_cover)
        }
    }
}

fun cargarLibroPortipo(tipoLista: String, recyclerView: RecyclerView, context: Context) {
    //Usamos esa instancia para llamar al metodo
    val libros = DatabaseHelper.getLibroPorLista(context, tipoLista) as? List<Libro> ?: emptyList()

    Log.d("cargarLibroPortipo", "Cargando libros para $tipoLista: $libros")
    //Configurar el adaptador y asignarlo al RecyclerView
    val adapter = BiblioAdapter(libros)
    recyclerView.adapter = adapter
}