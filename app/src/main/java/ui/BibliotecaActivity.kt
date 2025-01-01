package ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bookrank.R

open class BibliotecaActivity : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listas)
        setupNavigationButtons() //Llamamos al método para iniciar los botones
    }
}