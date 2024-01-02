package com.gm.cw
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
class DB_class(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 2
        private val DATABASE_NAME = "CWDataBase"
        private val TABLE_CONTACTS = "user"
        private val KEY_PROF = "profissao"
        private val KEY_UNAME = "username"
        private val KEY_PSWD = "pswd"

        private val TABLE_COMMENTS = "posts"
        private val KEY_COMENTARIO = "comentario"
        private val KEY_DATA = "data"
        private val KEY_NAME = "name"
        private val KEY_CURTIDAS = "curtidas"
        private val LIST_CURTIDAS = "quemcurtiu"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val newtb = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_PROF + " TEXT," + KEY_UNAME + " TEXT,"
                + KEY_PSWD + " TEXT" + ")")
        db?.execSQL(newtb)
        val createCommentsTable = ("CREATE TABLE IF NOT EXISTS " + TABLE_COMMENTS + "("
                + "ROWID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$KEY_COMENTARIO TEXT, $KEY_DATA TEXT, $KEY_NAME TEXT, $KEY_CURTIDAS INT, $LIST_CURTIDAS TEXT )")

        db?.execSQL(createCommentsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS)
        onCreate(db)
    }

    fun inserirComentario(comentario: String, data: String, name: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_COMENTARIO, comentario)
        values.put(KEY_DATA, data)
        values.put(KEY_NAME, name)

        values.put(KEY_CURTIDAS, 0)

        // Insira os dados na tabela de comentários
        db.insert(TABLE_COMMENTS, null, values)

        // Feche o banco de dados após a inserção
        db.close()
    }
    fun updateLikeStatus(commentId: Int, userName: String) {
        writableDatabase.use { db ->
            // SQL query to update the likes and liked users
            val sql = "UPDATE $TABLE_COMMENTS " +
                    "SET $KEY_CURTIDAS = $KEY_CURTIDAS + 1, " +
                    "$LIST_CURTIDAS = CASE " +
                    "WHEN $LIST_CURTIDAS IS NULL THEN '$userName' " +
                    "ELSE $LIST_CURTIDAS || ',$userName' END " +
                    "WHERE ROWID = $commentId"

            // Execute the SQL query
            db.execSQL(sql)
        }
    }

    @SuppressLint("Range")
    fun getComments(): List<Comment> {
        val comments = mutableListOf<Comment>()
        val selectQuery = "SELECT * FROM $TABLE_COMMENTS ORDER BY $KEY_DATA DESC"


        val db = this.readableDatabase

        readableDatabase.use { db ->
            db.rawQuery(selectQuery, null).use { cursor ->
                if (cursor.moveToFirst()) {
                    do {
                        val commentId = cursor.getInt(cursor.getColumnIndex("ROWID"))
                        val date = cursor.getString(cursor.getColumnIndex(KEY_DATA))
                        val people = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                        val message = cursor.getString(cursor.getColumnIndex(KEY_COMENTARIO))
                        val heartCount = cursor.getInt(cursor.getColumnIndex(KEY_CURTIDAS))
                        val peopleHeartIndex = cursor.getColumnIndex(LIST_CURTIDAS)
                        val peopleHeart = if (cursor.isNull(peopleHeartIndex)) "" else cursor.getString(peopleHeartIndex)

                        val comment = Comment(commentId, date, people, message, heartCount,peopleHeart)
                        comments.add(comment)
                    } while (cursor.moveToNext())
                }
            }
        }

        return comments
    }

}