package com.example.bookrank.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bbdd.DatabaseHelper
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
        //holder.coverB.text = libroB.bookCover //TODO cambiar text por imagen
    }
}

fun cargarLibroPortipo(tipoLista: String, recyclerView: RecyclerView, context: Context) {
    //Usamos esa instancia para llamar al metodo
    val libros = DatabaseHelper.getLibroPorLista(context, tipoLista)


    //Configurar el adaptador y asignarlo al RecyclerView
        val adapter = BiblioAdapter(libros as List<Libro>)
        recyclerView.adapter = adapter
    }

