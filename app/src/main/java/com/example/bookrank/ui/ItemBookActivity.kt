package com.example.bookrank.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.bookrank.R


/*
 En esta clase se maneja el funcionamiento del botón para
 añadir libros a la base de datos
 */
class ItemBookActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_book)

        //Declaramos los componentes de item_book (layout)
        val btnAnadir = findViewById<ImageButton>(R.id.btnAnadir)

      /*  //Agregar listener para que al pulsarlo añada el libro a la BD
        btnAnadir.setOnClickListener {
            val selectedBook: BookAdapter.BookData? = intent.getParcelableExtra("selectedBook")

            if(selectedBook != null){
                val dbHelper = DatabaseHelper(this)
                val result = dbHelper.insertarLibro(
                    selectedBook.title,
                    selectedBook.author_name.toString(),
                    selectedBook.cover_i ?: ""
                )
                if (result) {
                    showLog("Libro añadido correctamente")
                    Toast.makeText(this, "Libro añadido correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    showLog("Error al añadir el libro")
                    Toast.makeText(this, "Error al añadir el libro", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No se ha seleccionado ningún libro", Toast.LENGTH_SHORT).show()
            }
        }*/
    }
    private fun showLog(message: String) {
        Log.d("BuscarActivity", message)
    }}
