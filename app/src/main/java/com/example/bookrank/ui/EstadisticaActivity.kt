package com.example.bookrank.ui

import android.os.Bundle
import android.util.Log
import bbdd.DatabaseHelper
import com.example.bookrank.BarChartView
import com.example.bookrank.R

class EstadisticaActivity : MainActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estadistica)
        setupNavigationButtons() //Llamamos al método para iniciar los botones

        //Declaramos los componentes del Activity
        val barChartView = findViewById<BarChartView>(R.id.barChartView)

        //Inicializa la base de datos y obtiene los datos
        databaseHelper = DatabaseHelper(this)
        val estadisticasPorMes = databaseHelper.getEstadisticaMensual()
        Log.d("Estadisticas", "Datos obtenidos: $estadisticasPorMes")
        showLog("Iniciando la base de datos y obtenidos los datos")

        //Actualiza los datos del grafico
        barChartView.setData(estadisticasPorMes)
        showLog("Actualizados los datos del grafico")
    }
}