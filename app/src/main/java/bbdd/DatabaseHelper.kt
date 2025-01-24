package bbdd

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.bookrank.libro.Libro
import com.example.bookrank.ui.EstadisticaActivity
import java.time.LocalDate


class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "biblioteca.db"
        private const val DATABASE_VERSION = 4
        private const val TABLE_LIBROS = "estanteria"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "titulo"
        private const val COLUMN_AUTHOR = "autor"
        private const val COLUMN_COVER = "portada"
        private const val COLUMN_TYPE = "tipoLista"
        private const val COLUMN_DATE = "fechaIngreso"

        private var instance: DatabaseHelper? = null
        fun getInstance(context: Context): DatabaseHelper {
            if (instance == null) {
                instance = DatabaseHelper(context.applicationContext)
            }
            return instance!!
        }

        /**
         * METODO READ -> Leer el registro
         * @param listadoLibros
         * @return el listado de libros
         * Para poder acceder desde otras funciones, se debe hacer que esta función sea un Método Estático (Companion Object)
         */
        fun getLibroPorLista(context: Context, tipoLista: String): Any {
            val db = getInstance(context).readableDatabase
            val listadoLibros = mutableListOf<Libro>()

            try {
                val consulta = "SELECT * FROM estanteria WHERE tipoLista=? ORDER BY id DESC"
                db.rawQuery(consulta, arrayOf(tipoLista)).use { cursor ->
                    if (cursor.moveToFirst()) {
                        do {
                            val titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"))
                            val autor = cursor.getString(cursor.getColumnIndexOrThrow("autor"))
                            val portada = cursor.getString(cursor.getColumnIndexOrThrow("portada"))
                            val tipoLista =
                                cursor.getString(cursor.getColumnIndexOrThrow("tipoLista"))
                            listadoLibros.add(Libro(titulo, autor, portada, tipoLista))
                        } while (cursor.moveToNext())
                    }
                }
            } catch (e: Exception) {
                throw RuntimeException(
                    "Error al obtener la lista de libros con esta lista: ${e.message}",
                    e
                )
            } finally {
                db.close()
            }

            return listadoLibros
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val crearTablaEstanteria = """

        CREATE TABLE estanteria (
            id INTEGER PRIMARY KEY AUTOINCREMENT, 
            titulo TEXT NOT NULL, 
            autor TEXT NOT NULL, 
            portada TEXT,
            tipoLista TEXT NOT NULL,
            fechaIngreso DATE 
        )
           
        """.trimIndent()
        db?.execSQL(crearTablaEstanteria)
    }

    /*
    * METODO UPDATE -> Actualización de la versión de la BBDD
    */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS estanteria")
        onCreate(db)
    }

    /**
     * METODO INSERT -> Insertar un libro en la BBDD
     * @param estanteria -> datos del nuevo libro
     * @return booleano (si ha ido bien o no)
     */
    fun insertarLibro(
        title: String,
        author: String,
        cover_i: String?,
        tipoLista: String
    ): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("titulo", title)
            put("autor", author)
            put("portada", cover_i)
            put("tipoLista", tipoLista)
            put("fechaIngreso", LocalDate.now().toString())
        }
        val result = db.insert(/* table = */ "estanteria", null, values)
        db.close()
        return result != -1L // Devuelve true si la inserción fue exitosa
    }

    fun getEstadisticaMensual(estadisticaActivity: EstadisticaActivity): Map<String, Int> {
        val db = readableDatabase
        val estadisticas = mutableMapOf<String, Int>()
        try {
            val consultaFecha = """
                    SELECT strftime('%Y-%m', fechaIngreso) AS mes, -- Obtiene el año y mes en formato 'YYYY-MM'
                    COUNT(*) AS cantidad
                    FROM estanteria
                    WHERE tipoLista IN ('leído', 'pendiente') -- Filtra solo los libros leídos y pendientes
                    GROUP BY mes -- Agrupa por mes y por tipoLista
                    ORDER BY mes ASC, -- Ordena por mes en orden ascendente
                    tipoLista;
                """

            db.rawQuery(consultaFecha, null).use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val mes = cursor.getString(cursor.getColumnIndexOrThrow("mes"))
                        val cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"))
                        estadisticas[mes] = cantidad

                    } while (cursor.moveToNext())
                }
            }
        } catch (e: Exception) {
            throw RuntimeException(
                "Error al obtener la lista de libros con esta fecha: ${e.message}",
                e
            )
        } finally {
            db.close()
        }
        return estadisticas
    }

    fun logTableData(){
        val db = readableDatabase
        try{
            val cursor = db.rawQuery("SELECT * FROM estanteria", null)
            if (cursor.moveToFirst()){
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    val titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"))
                    val autor = cursor.getString(cursor.getColumnIndexOrThrow("autor"))
                    val portada = cursor.getString(cursor.getColumnIndexOrThrow("portada"))
                    val tipoLista = cursor.getString(cursor.getColumnIndexOrThrow("tipoLista"))
                    val fechaIngreso = cursor.getString(
                        cursor.getColumnIndexOrThrow("fechaIngreso")
                    )
                } while (cursor.moveToNext())
            }else {
                Log.d("DatabaseHelper", "No hay datos en la tabla")
            }
            cursor.close()
        } catch (e: Exception){
            Log.e("DatabaseHelper", "Error al obtener los datos de la tabla", e)
        } finally {
            db.close()
        }
    }
}
    /*
    /**
    METODO DELETE -> Borrar un libro de la BBDD
    @ idLibro es el indentificador del libro
    @ return booleano (si ha ido bien o no)
     */
    fun borrarLibro(idLibro: Long): Boolean{
        val db = writableDatabase
        val affectedRows = db.delete("estanterias", "id = ?", arrayOf(idLibro.toString()))
        db.close()
        return affectedRows != -1 //Devuelve true si se borró de manera correcta
    }*/
