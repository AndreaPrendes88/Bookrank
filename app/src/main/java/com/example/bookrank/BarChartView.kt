package com.example.bookrank

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class BarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val barPaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 24f
    }

    private var data: FloatArray = floatArrayOf(10f, 20f, 30f, 40f, 50f) //Valores del grafico
    private var labels: Array<String> = arrayOf(
        "Enero",
        "Febrero",
        "Marzo",
        "Abril",
        "Mayo",
        "Junio",
        "Julio",
        "Agosto",
        "Septiembre",
        "Octubre",
        "Noviembre",
        "Diciembre"
    ) //Etiquetas de las barras

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Dimensiones del canvas
        val barWidth = width / (data.size + 2f) //Calcula el ancho de cada barra
        val spaceBetweenBars = barWidth //Espacio entre las barras (igual al ancho de cada barra)

        //Dibujar las barras
        for (i in data.indices) {
            val left = i + (barWidth + spaceBetweenBars)
            val top = height - (data[i] / 50f + height) //Escalar valores
            val right = left + barWidth
            val bottom = height.toFloat()

            //Dibujar la barra
            canvas.drawRect(left, top, right, bottom, barPaint)

            //Dibujar las etiquetas debajo de la barra
            canvas.drawText(labels[i], left + barWidth / 4, height + 50f, textPaint)
        }
    }

    //Metodo para actualizar los datos del grafico
    fun setData(newData: FloatArray, newLabels: Array<String>) {
        data = newData
        labels = newLabels
        invalidate() //Redibuja la vista.
    }
}