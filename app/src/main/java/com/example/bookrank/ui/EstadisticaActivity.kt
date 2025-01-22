package com.example.bookrank.ui

import android.os.Bundle
import com.example.bookrank.BarChartView
import com.example.bookrank.R

class EstadisticaActivity : MainActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estadistica)
        setupNavigationButtons() //Llamamos al método para iniciar los botones

        //Referenciamos el gráfico
        val barChartView = findViewById<BarChartView>(R.id.barChartView)

        //Actualizar los datos del grafico
        val newData = floatArrayOf(10f, 20f, 30f, 40f, 50f)
        val newLabels = arrayOf("Enero", "Febrero", "Marzo", "Abril", "Mayo")
        barChartView.setData(newData, newLabels)

    }
}