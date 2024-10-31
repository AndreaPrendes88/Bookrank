package ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.bookrank.R

class MainActivity :AppCompatActivity() {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle ?) {
        setTheme(R.style.Theme_Bookrank)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Añadimos botón de la lupa
        val btnlupa = findViewById<ImageButton>(R.id.btn_lupa)
        // Establecer el OnclickListener para el botón de la lupa
        btnlupa.setOnClickListener {

     //      //Crea un Intent para iniciar la busqueda
     //       val intent = Intent (this, buscarLibro::class.java)
            // Con la lupa tiene que ir a la activity donde salga la opción de busqueda
     //       startActivity(intent)
        }

    }
}