package ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookrank.R


class MainActivity : AppCompatActivity() {

    private lateinit var btnLupa: ImageButton
    private lateinit var btnLibro: ImageButton
    private lateinit var btnEstadisticas: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        //Añadimos los botones de la Activity
        btnLupa = findViewById(R.id.btnLupa)
        btnLibro = findViewById(R.id.btnLibros)
        btnEstadisticas = findViewById(R.id.btnEstadisticas)

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

        btnLibro.setOnClickListener {
            Toast.makeText(this,"Boton libro presionado", Toast.LENGTH_SHORT).show()

            try{
                //Crear Intent para iniciar actividad de Biblioteca
                val intent = Intent(this, BibliotecaActivity::class.java)
                startActivity(intent)
            } catch(e:Exception) {
                Toast.makeText(this, "Error al entrar a tu biblioteca: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}

