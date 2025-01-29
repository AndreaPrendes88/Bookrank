package bbdd

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.bookrank.libro.Libro
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        //CREAMOS LAS TABLAS
        @SuppressLint("SuspiciousIndentation")
        override fun onCreate(db: SQLiteDatabase?) {
            val crearTablaEstanteria = """
                CREATE TABLE estanteria (
                    id INTEGER PRIMARY KEY AUTOINCREMENT, 
                    titulo TEXT NOT NULL, 
                    autor TEXT NOT NULL, 
                    portada TEXT
                )
             """.trimIndent()
                db?.execSQL(crearTablaEstanteria)

            val crearTablaEstanteriaLeidos = """
                CREATE TABLE estanteriaLeidos (
                    id INTEGER PRIMARY KEY, 
                    fechaIngreso DATE NOT NULL,
                    FOREIGN KEY (id) REFERENCES estanteria(id) ON DELETE CASCADE
                )
            """.trimIndent()
                db?.execSQL(crearTablaEstanteriaLeidos)

            val crearTablaEstanteriaPendientes = """
                CREATE TABLE estanteriaPendientes (
                    id INTEGER PRIMARY KEY, 
                    fechaIngreso DATE NOT NULL,
                    FOREIGN KEY (id) REFERENCES estanteria(id) ON DELETE CASCADE
                )
            """.trimIndent()
                db?.execSQL(crearTablaEstanteriaPendientes)

            val crearTablaEstanteriaFavoritos = """
                CREATE TABLE estanteriaFavoritos (
                    id INTEGER PRIMARY KEY, 
                    fechaIngreso DATE NOT NULL,
                    FOREIGN KEY (id) REFERENCES estanteria(id) ON DELETE CASCADE
                )
            """.trimIndent()
                db?.execSQL(crearTablaEstanteriaFavoritos)
        }

        /**
         * METODO UPDATE -> Actualización de la versión de la BBDD
         */
        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL("DROP TABLE IF EXISTS estanteriaLeidos")
            db?.execSQL("DROP TABLE IF EXISTS estanteriaPendientes")
            db?.execSQL("DROP TABLE IF EXISTS estanteriaFavoritos")
            db?.execSQL("DROP TABLE IF EXISTS estanteria")
            onCreate(db)
        }

        /**
         * METODO INSERT -> Insertar un libro en la BBDD
         * @return booleano (si ha ido bien o no)
         */
        fun insertarLibro(title: String, author: String, cover_i: String?): Boolean {
            val db = writableDatabase
            Log.d("DatabaseHelper", "Intentando insertar libro en la base de datos...Titulo=$title, autor=$author, portada=$cover_i")
            val values = ContentValues().apply {
                put("titulo", title)
                put("autor", author)
                put("portada", cover_i)
            }
            val result = db.insert(/* table = */ "estanteria", null, values)
            db.close()

            if (result == -1L) {
                Log.e("DatabaseHelper", "Error al insertar el libro en la base de datos")
            } else {
                Log.d("DatabaseHelper", "Libro insertado correctamente con ID: $result")
            }
            return result != -1L // Devuelve true si la inserción fue exitosa
        }

        fun insertarLibroLeido(id: Int): Boolean {
            val db = writableDatabase
            val values = ContentValues().apply {
                put("id", id)
                put("fechaIngreso", LocalDate.now().toString())
            }
            val result = db.insert(/* table = */ "estanteriaLeidos", null, values)
            db.close()
            return result != -1L // Devuelve true si la inserción fue exitosa
        }

        fun insertarLibroPendiente(id: Int): Boolean {
            val db = writableDatabase
            val values = ContentValues().apply {
                put("id", id)
                put("fechaIngreso", LocalDate.now().toString())
            }
            val result = db.insert(/* table = */ "estanteriaPendientes", null, values)
            db.close()
            return result != -1L // Devuelve true si la inserción fue exitosa
        }

        fun insertarLibroFavorito(id: Int): Boolean {
            val db = writableDatabase
            val values = ContentValues().apply {
                put("id", id)
                put("fechaIngreso", LocalDate.now().toString())
            }
            val result = db.insert(/* table = */ "estanteriaFavoritos", null, values)
            db.close()
            return result != -1L // Devuelve true si la inserción fue exitosa
        }

    /**
     * METODO READ -> Obtener el ID de la tabla Libro
     * @return el ID del libro para pasarlo a las otras listas.
     */
    fun getIdLibro(title: String, author: String, cover_i: String?): Int? {
        val db = readableDatabase
        val query = "SELECT id FROM estanteria WHERE titulo = ? AND autor = ? AND portada = ?"
        val cursor = db.rawQuery(query, arrayOf(title, author, cover_i ?: ""))

        return if (cursor.moveToFirst()) {
            val id = cursor.getInt(0)
            cursor.close()
            db.close()
            id
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    fun getLibrosLeidos(context: Context): List<Libro> {
        return getLibrosPorCategoria(context, "estanteriaLeidos")
    }

    fun getLibrosPendientes(context: Context): List<Libro> {
        return getLibrosPorCategoria(context, "estanteriaPendientes")
    }

    fun getLibrosFavoritos(context: Context): List<Libro> {
        return getLibrosPorCategoria(context, "estanteriaFavoritos")
    }

    private fun getLibrosPorCategoria(context: Context, tabla: String): List<Libro> {
        val libros = mutableListOf<Libro>()
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase
         // e. es el alias asignado para estantería y el. es el alias asignado para estanteriaLeidos
        val query = """
            SELECT estanteria.id, estanteria.titulo, estanteria.autor, estanteria.portada, $tabla.fechaIngreso 
            FROM $tabla 
            JOIN estanteria ON $tabla.id = estanteria.id
        """.trimIndent()

        Log.d("SQL_DEBUG", "Ejecutando consulta: $query")

        val cursor = db.rawQuery(query, null)
        Log.d("SQL_DEBUG", "Numero de filas obtenidas: ${cursor.count}")

        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val titulo = cursor.getString(1)
            val autor = cursor.getString(2)
            val portada = cursor.getString(3)
            val fechaIngreso = cursor.getString(4)

            //Convertir la fecha de String a LocalDate
            val fechaIngresoToString = LocalDate.parse(fechaIngreso, DateTimeFormatter.ISO_LOCAL_DATE)
            libros.add(Libro(id, titulo, autor, portada, fechaIngresoToString))
        }
        cursor.close()
        db.close()

        return libros
    }

    fun getEstadisticaMensual(): Map<String, Int> {
        val db = readableDatabase
        val estadisticas = mutableMapOf<String, Int>()
        try{
            val cursor = """
                SELECT strftime('%Y-%m', fechaIngreso) as mes, COUNT(*) as cantidad
                FROM (
                    SELECT fechaIngreso FROM estanteriaLeidos
                    UNION ALL
                    SELECT fechaIngreso FROM estanteriaFavoritos
                )
                GROUP BY mes
            """.trimIndent()

            db.rawQuery(cursor, null).use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val mes = cursor.getString(cursor.getColumnIndexOrThrow("mes"))
                        val cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"))
                        estadisticas[mes] = cantidad
                    } while (cursor.moveToNext())
                }
            }
        } catch (e: Exception) {
            throw RuntimeException("Error al obtener las estadísticas mensuales", e)
        } finally {
            db.close()
        }
        return estadisticas
    }

    /**
     * METODO DELETE -> Eliminar un libro de la BBDD
     */
    fun eliminarLibro(id: Int): Boolean {
        val db = writableDatabase

        //Eliminar de las tablas relacionadas primero para evitar violaciones de clave foránea.
        db.delete("estanteriaLeidos", "id = ?", arrayOf(id.toString()))
        db.delete("estanteriaPendientes", "id = ?", arrayOf(id.toString()))
        db.delete("estanteriaFavoritos", "id = ?", arrayOf(id.toString()))

        //Eliminar el libro de la tabla principal
        val resultado = db.delete("estanteria", "id = ?", arrayOf(id.toString()))

        db.close()
        return resultado > 0 //Devuelve true si la eliminación fue exitosa
    }

    companion object {
        private const val DATABASE_NAME = "biblioteca.db"
        private const val DATABASE_VERSION = 4
    }
}

