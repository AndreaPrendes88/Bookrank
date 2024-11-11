package ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.bookrank.R
import com.example.bookrank.databinding.ActivityIntroBinding

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
            //Crear Intent
            val intent = Intent(this, BuscarActivity::class.java)
            startActivity(intent)
        }
        }
    }
