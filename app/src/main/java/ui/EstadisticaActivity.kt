package ui

import android.os.Bundle
import com.example.bookrank.R

class EstadisticaActivity : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estadistica)
        setupNavigationButtons() //Llamamos al método para iniciar los botones

    }
}