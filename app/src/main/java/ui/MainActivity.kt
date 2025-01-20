package ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookrank.R


open class MainActivity : AppCompatActivity() {

    private lateinit var btnLupa: ImageButton
    private lateinit var btnLibro: ImageButton
    private lateinit var btnEstadisticas: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        setupNavigationButtons() //Llamamos al método para iniciar los botones

    }

    fun setupNavigationButtons(){
        //Añadimos los botones de la Activity
        btnLupa = findViewById(R.id.btnLupa)
        btnLibro = findViewById(R.id.btnLibros)
        btnEstadisticas = findViewById(R.id.btnEstadisticas)

        //Añadimos listener para btnLupa que nos lleva a BuscarActivity
        btnLupa.setOnClickListener {
            Toast.makeText(this, "Botón lupa presionado", Toast.LENGTH_SHORT).show()
            navigateToActivity(BuscarActivity::class.java)
        }

        //Añadimos listener para btnLibro que nos lleva a BibliotecaActivity
        btnLibro.setOnClickListener {
            Toast.makeText(this,"Boton libro presionado", Toast.LENGTH_SHORT).show()
            navigateToActivity(BibliotecaActivity::class.java)
        }

        //Añadimos listener para btnEstadisticas que nos lleva a EstadisticaActivity
        btnEstadisticas.setOnClickListener {
            Toast.makeText(this,"Boton estadisticas presionado", Toast.LENGTH_SHORT).show()
            navigateToActivity(EstadisticaActivity::class.java)
        }
    }

    private fun navigateToActivity(targetActivity: Class<*>) {
        try {
            val intent = Intent(this, targetActivity)
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Error al iniciar la actividad: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}

