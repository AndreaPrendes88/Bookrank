package bbdd

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "biblioteca.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_LIBROS = "estanteria"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "titulo"
        private const val COLUMN_AUTHOR = "autor"
        private const val COLUMN_COVER = "portada"
        /*TODO -> añadir tipo listaprivate const val COLUMN_TYPE = "tipoLista"*/
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val crearTablaEstanteria = """

        CREATE TABLE estanteria (
            id INTEGER PRIMARY KEY AUTOINCREMENT, 
            titulo TEXT NOT NULL, 
            autor TEXT NOT NULL, 
            portada TEXT,
            tipoLista TEXT NOT NULL
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
    * METODO READ -> Leer el registro
    * @param nuevoLibro
    * @return el listado de libros
    */
    fun getLibroPorLista(tipoLista: String): MutableList<Libro> {
        val db = readableDatabase
        val listadoLibros = mutableListOf<Libro>()

        //Recorrer los valores de la consulta
        try {
            val consulta = "SELECT * FROM estanteria WHERE tipoLista=? ORDER BY id DESC"
            db.rawQuery(consulta, arrayOf(tipoLista)).use { cursor ->
                //Recorre los valores de la consulta
                if (cursor.moveToFirst()) {
                    do {
                        //Valores extraidos de la base de datos
                        val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
                        val titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"))
                        val autor = cursor.getString(cursor.getColumnIndexOrThrow("autor"))
                        val portada = cursor.getString(cursor.getColumnIndexOrThrow("portada"))
                        val tipoLista = cursor.getString(cursor.getColumnIndexOrThrow("tipoLista"))
                        //Crear el objeto nuevoLibro con los datos anteriores y los añadimos a la lista
                        val nuevoLibro = Libro(
                            id,
                            titulo,
                            autor,
                            portada,
                            tipoLista
                        )
                        listadoLibros.add(nuevoLibro)
                    } while (cursor.moveToNext())
                }
            }
        }catch (e: Exception){
            throw RuntimeException("Error al obtener la lista de libros con esta lista: ${e.message}", e)
        } finally {
            db.close()
        }
        return listadoLibros
    }


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