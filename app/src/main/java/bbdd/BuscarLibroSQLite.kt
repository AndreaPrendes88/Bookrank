package bbdd

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Aquí se creará el CRUD de la Base de datos
 * C - Create - Crear registro/insertar registro
 * R - Read - Leer el registro
 * U - Update - actualizar un registro
 * D - Delete - borrar un registro
*/
abstract class BuscarLibroSQLite(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "buscarLibro.db"
        private const val DATABASE_VERSION = 1
    }

    /**
     *  METODO CREATE -> Establece la estructura de las tablas
     */
    override fun onCreate(db: SQLiteDatabase?) {
        val crearTablaLibros = """
            CREATE TABLE libros(
                id INTEGER PRIMARY KEY AUTOINCREMENT, 
                titulo TEXT NOT NULL, 
                autor TEXT NOT NULL, 
                editorial TEXT NOT NULL, 
                isbn TEXT NOT NULL
            )
        """.trimIndent()

        //Creamos la tabla
        db?.execSQL(crearTablaLibros)
    }

    /**
     * METODO READ
     *      @param libroTitulo
     *      @return el listado de libros con ese título
     */
    fun getListaPorTitulo(libroTitulo: String): List<LibroNuevoRegistro> { //<- Preguntar si esta sería la lista
        val db = readableDatabase // Accedemos en modo lectura a la BBDD
        val listaLibros = mutableListOf<LibroNuevoRegistro>()

        //Recorrer los valores de la consulta
        try {
            val consulta = "SELECT * FROM libros WHERE libroTitulo=? ORDER BY id DESC"
            db.rawQuery(consulta, arrayOf(libroTitulo)).use { cursor ->
                //Recorre los valores de la consulta
                if (cursor.moveToFirst()) {
                    do {
                        //Valores de un libro
                        val titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"))
                        val autor = cursor.getString(cursor.getColumnIndexOrThrow("autor"))
                        val editorial = cursor.getString(cursor.getColumnIndexOrThrow("editorial"))
                        val isbn = cursor.getString(cursor.getColumnIndexOrThrow("isbn"))

                        //Crear el objeto nuevoLibro con los datos anteriores y los añadimos a la lista
                        val nuevoLibro = LibroNuevoRegistro(
                            titulo,
                            autor,
                            editorial,
                            isbn
                        )
                        listaLibros.add(nuevoLibro)
                    } while (cursor.moveToNext())
                }
            }
        } catch (e: Exception) {
            throw RuntimeException(
                "Error al obtener la lista de libros con este título: ${e.message}",
                e
            )
        } finally {
            //Cerrar la BBDD
            db.close()
        }
        return listaLibros
    }

    /**
     * METODO UPDATE
     *      * "LibroNuevoRegistro -> tiene que ir incorporado en la UI "BuscarActivity"
     *      * Será el botón de añadir libro de la interfaz
     */
    // Actualización de la versión de la BBDD
    override fun onUpgrade(db: SQLiteDatabase?, viejaVersion: Int, nuevaVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS libros")
        onCreate(db)
    }

    // Inserta un libro en la BBDD
    fun insertarLibro(libro: LibroNuevoRegistro): Long {
        val db = writableDatabase
        val values =  ContentValues().apply {
            put("titulo", libro.titulo)
            put("autor", libro.autor)
            put("editorial", libro.editorial)
            put("isbn", libro.isbn)
        }
        val numRowId = db.insert("libros", null, values)
        return numRowId
    }

    /**
     * METODO DELETE
     *      @param idLibro es el indentificador del libro
     *      @param libros los datos del libro
     */
    fun borrarLibro(idLibro: Long): Int {
        val db = writableDatabase
        val affectedRows = db.delete("libros", "id = ?", arrayOf(idLibro.toString()))
        db.close()
        return affectedRows
    }
}
