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

        //Ejemplo de datos : Meses y actividad
        val data = listOf(
            "Enero" to 5,
            "Febrero" to 3,
            "Marzo" to 7,
            "Abril" to 2,
            "Mayo" to 6,
            "Junio" to 4,
            "Julio" to 8,
            "Agosto" to 1,
            "Septiembre" to 9,
            "Octubre" to 2,
            "Noviembre" to 6,
            "Diciembre" to 3
        )
        barChartView.setData(data)

     /*   //Actualizar los datos del grafico
        val newData = floatArrayOf(10f, 20f, 30f, 40f, 50f)
        val newLabels = arrayOf("Enero", "Febrero", "Marzo", "Abril", "Mayo")
        barChartView.setData(newData, newLabels)
        */
    }
}