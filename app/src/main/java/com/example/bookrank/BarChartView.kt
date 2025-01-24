package com.example.bookrank

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat

class BarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val barPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.bluegrey)
        style = Paint.Style.FILL_AND_STROKE
        //strokeWidth = 2f //TODO Cambiar el grosor de la linea para que se pueda ver mejor
    }
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 30f
    }
// FloatArray -> estructura de datos primitiva que usa menos memoria y es más rapido porque almacena valores primitivos en lugar de objetos.
    private var data: FloatArray = floatArrayOf() //Valores del grafico
    private var labels: Array<String> = arrayOf() //Etiquetas de las barras

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if(data.isEmpty()) return

        // Dimensiones del canvas
        val chartWidth = width.toFloat()
        val chartHeight = height.toFloat()

        //Configuración de las barras
        val totalBarSpace = chartWidth * 0.8f //Reservar un 80% del ancho para las barras
        val totalSpaceBetweenBars = chartWidth * 0.2f //Reservar un 20% del ancho para los espacios entre las barras

        val barWidth = totalBarSpace/data.size //Ancho de cada barra
        val spaceBetweenBars = totalSpaceBetweenBars / (data.size + 1) //Espacio entre las barras

        //Altura máxima para escalar las barras
        val maxDataValue = data.maxOrNull() ?: 1f //Evitar division por cero

        //Dibujar las barras
        for (i in data.indices) {
            //Coordenadas de las barras
            val left = spaceBetweenBars + i * (barWidth + spaceBetweenBars)
            val top = chartHeight - (data[i] / maxDataValue * chartHeight * 0.8f) //Escalado al 80% del Canvas
            val right = left + barWidth
            val bottom = chartHeight

            //Dibujar la barra
            canvas.drawRect(left, top, right, bottom, barPaint)

            //Dibujar la etiqueta
            val label = labels.getOrNull(i) ?: ""
            val textX = left + (barWidth / 2) - (textPaint.measureText(label) / 2) //Centrar el texto
            val textY = chartHeight + 40f //Un poco debajo del gráfico
            canvas.drawText(label, textX, textY, textPaint)

            //Dibujar el valor encima de la barra
            val valueText = data[i].toInt().toString()
            val valueX = left + (barWidth / 2) - (textPaint.measureText(valueText) / 2) //Centrar el texto
            val valueY = top - 10f //Justo encima de la barra
            canvas.drawText(valueText, valueX, valueY, textPaint)
        }
    }

    //Metodo para actualizar los datos del grafico
    fun setData(newData: Map<String, Int>) {
        //Convertir los datos (cantidad libros leidos) a FloatArray
        val floatArray = newData.values.map { it.toFloat() }.toFloatArray() //newData es la lista com el nombre del mes y el numero de libros leidos

        //Guarda los datos en la propiedad correspondiente
        data = floatArray

        labels = newData.keys.toTypedArray()

        //Redibuja la vista
        invalidate()
    }

    private fun showLog(message: String) {
        Log.d("BuscarActivity", message)
    }
}
