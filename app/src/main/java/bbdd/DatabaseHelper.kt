package bbdd

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.bookrank.libro.Libro
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "biblioteca.db"
        private const val DATABASE_VERSION = 3
        private const val TABLE_LIBROS = "estanteria"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "titulo"
        private const val COLUMN_AUTHOR = "autor"
        private const val COLUMN_COVER = "portada"
        private const val COLUMN_TYPE = "tipoLista"
        private const val COLUMN_DATE = "fechaIngreso"

        private var instance:DatabaseHelper? = null
            fun getInstance(context: Context): DatabaseHelper{
                if (instance == null){
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
                            val tipoLista = cursor.getString(cursor.getColumnIndexOrThrow("tipoLista"))
                            listadoLibros.add(Libro(titulo, autor, portada, tipoLista))
                        } while (cursor.moveToNext())
                    }
                }
            } catch (e: Exception) {
                throw RuntimeException("Error al obtener la lista de libros con esta lista: ${e.message}", e)
            } finally {
                db.close()
            }

            return listadoLibros
        }
        /**
         * METODO READ -> Leer el registro
         * @param librosFecha
         * @return los libros leidos y favoritos por fecha
         * Para poder acceder desde otras funciones, se debe hacer que esta función sea un Método Estático (Companion Object)
         */
        fun getLibroPorFecha(context: Context, fechaIngreso: String): Any {
            val db = getInstance(context).readableDatabase
            val librosFecha = mutableListOf<Libro>()

            try {
                val consultaFecha = "SELECT titulo, autor, portada, tipoLista, fechaIngreso FROM estanteria WHERE tipoLista IN ('leído', 'favorito') ORDER BY fechaIngreso ASC"
                db.rawQuery(consultaFecha, arrayOf(fechaIngreso)).use { cursor ->
                    if (cursor.moveToFirst()) {
                        do {
                            val titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"))
                            val autor = cursor.getString(cursor.getColumnIndexOrThrow("autor"))
                            val portada = cursor.getString(cursor.getColumnIndexOrThrow("portada"))
                            val tipoLista =
                                cursor.getString(cursor.getColumnIndexOrThrow("tipoLista"))
                            val fechaIngresoString = cursor.getString(cursor.getColumnIndexOrThrow("fechaIngreso"))

                            //Convertir String a LocalDate
                            val fechaIngreso = LocalDate.parse(fechaIngresoString, DateTimeFormatter.ISO_LOCAL_DATE)

                            //Crear el objeto Libro y agregarlo a la lista
                            librosFecha.add(Libro(titulo, autor, portada, tipoLista, fechaIngreso))
                        } while (cursor.moveToNext())
                    }
                }
            } catch (e: Exception) {
                throw RuntimeException("Error al obtener la lista de libros con esta fecha: ${e.message}", e)
            } finally {
                db.close()
            }

            return librosFecha
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
        onCreate(db) }

    /**
    * METODO INSERT -> Insertar un libro en la BBDD
    * @param estanteria -> datos del nuevo libro
    * @return booleano (si ha ido bien o no)
    */
    fun insertarLibro(title: String, author: String, cover_i: String?, tipoLista: String): Boolean{
        val db = writableDatabase
        val values = ContentValues().apply {
            put("titulo", title)
            put("autor", author)
            put("portada", cover_i)
            put("tipoLista", tipoLista)
        }

        val result = db.insert(/* table = */ "estanteria", null, values)
        db.close()
        return result != -1L // Devuelve true si la inserción fue exitosa
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
}