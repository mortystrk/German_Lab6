package bstu.by.lab6k.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_DB_TABLE)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL(DROP_DB_TABLE)
        onCreate(sqLiteDatabase)
    }

    companion object {
        internal const val DATABASE_NAME = "Test"
        private const val TEST_TABLE_NAME = "test"
        internal const val DATABASE_VERSION = 1;
        internal const val CREATE_DB_TABLE = ("CREATE TABLE "
                + TEST_TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "value TEXT NOT NULL);")
        internal const val DROP_DB_TABLE = "DROP TABLE IF EXISTS $TEST_TABLE_NAME"

        internal fun addValue(value: String, database: SQLiteDatabase) {
            val putValue = ContentValues()
            putValue.put("value", value)

            database.insert(TEST_TABLE_NAME, "", putValue)
        }

        internal fun getAllValues(database: SQLiteDatabase): ArrayList<String> {
            val listOfValues = ArrayList<String>()
            val cursor = database.query(TEST_TABLE_NAME, arrayOf("id", "value"), null, null, null, null, null)

            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                listOfValues.add(cursor.getString(1))
                cursor.moveToNext()
            }
            cursor.close()

            return listOfValues
        }

        internal fun updateValue(textOld: String, textNew: String, database: SQLiteDatabase) {
            val value = ContentValues()
            value.put("value", textNew)

            database.update(DBHelper.TEST_TABLE_NAME, value, "value = ?", arrayOf(textOld))
        }

        internal fun deleteValue(deleteValue: String, database: SQLiteDatabase) {
            database.delete(DBHelper.TEST_TABLE_NAME, "value = ?", arrayOf(deleteValue))
        }
    }
}
