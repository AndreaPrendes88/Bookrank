package ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookrank.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        //Añadimos los botones de la Activity
        val btnLupa = findViewById<ImageButton>(R.id.btnLupa)
        val btnLibro = findViewById<ImageButton>(R.id.btnLibros)
        val btnEstadisticas = findViewById<ImageButton>(R.id.btnEstadisticas)

        //Añadimos listener para btnLupa
        btnLupa.setOnClickListener {
            Toast.makeText(this, "Botón lupa presionado", Toast.LENGTH_SHORT).show()

            try {
                // Crear Intent para iniciar la actividad de búsqueda
                val intent = Intent(this, BuscarActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                // Si hay un error al intentar iniciar la actividad, lo mostramos
                Toast.makeText(this, "Error al iniciar la actividad: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}

