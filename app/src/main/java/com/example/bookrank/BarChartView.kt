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

    private val axisPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 4f
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 30f
    }
// FloatArray -> estructura de datos primitiva que usa menos memoria y es más rapido porque almacena valores primitivos en lugar de objetos.
    private var data: FloatArray = floatArrayOf() //Valores del grafico
    private var labels: Array<String> = arrayOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ag", "Sept", "Oct", "Nov", "Dic") //Etiquetas de las barras

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if(data.isEmpty()) return

        // Dimensiones del canvas
        val chartWidth = width.toFloat()
        val chartHeight = height.toFloat()

        //Margenes
        val margin = 50f
        val axisOffSet = 10f //Espacio para los ejes
        val graphWidth = chartWidth - margin * 2
        val graphHeight = chartHeight - margin * 2

        //Configuración de las barras
        val barWidth = 50f //Ancho fijo de las barras
        val totalSpace = graphWidth - (barWidth * data.size) //Espacio total restante
        val spaceBetweenBars = totalSpace / (data.size + 1) //Espacio dinámico entre barras

        //Altura máxima para escalar las barras
        val maxDataValue = data.maxOrNull() ?: 1f //Evitar division por cero

        //Dibujar los ejes
        val xAxisY = chartHeight - margin //Posición del eje X
        val yAxisX = margin //Posición del eje Y
        canvas.drawLine(yAxisX, margin, yAxisX, xAxisY, axisPaint) //Eje Y
        canvas.drawLine(yAxisX, xAxisY, chartWidth - margin, xAxisY, axisPaint) //Eje X

        //Dibujar las barras
        for (i in data.indices) {
            //Coordenadas de las barras
            val left = yAxisX + spaceBetweenBars * (i + 1) + barWidth * i
            val top = xAxisY - (data[i] / maxDataValue * graphHeight) //Escalado
            val right = left + barWidth
            val bottom = xAxisY

            //Dibujar la barra
            canvas.drawRect(left, top, right, bottom, barPaint)

            //Dibujar la etiqueta (nombre del mes)
            val label = labels[i]
            val textX = left + (barWidth / 2) - (textPaint.measureText(label) / 2) //Centrar el texto
            val textY = xAxisY + 40f //Un poco debajo del gráfico
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
        val monthMapping = mapOf(
            "2025-01" to 0, "2025-02" to 1, "2025-03" to 2, "2025-04" to 3, "2025-05" to 4, "2025-06" to 5, "2025-07" to 6, "2025-08" to 7, "2025-09" to 8, "2025-10" to 9, "2025-11" to 10, "2025-12" to 11
        )

        //Convertir los datos (cantidad libros leidos) a FloatArray
        val completeData = FloatArray(12) { 0f } //Inicializar con 0 libros para cada mes

        for ((key, value) in newData) {
            val index = monthMapping[key] ?: continue //Obtiene el indice del mes
            completeData[index] = value.toFloat()
        }

        //Actualizar datos y dedibujar
        data = completeData
        invalidate()
    }

    private fun showLog(message: String) {
        Log.d("BuscarActivity", message)
    }
}
