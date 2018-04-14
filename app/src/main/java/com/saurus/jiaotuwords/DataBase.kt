package com.saurus.jiaotuwords

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by sao on 2018/4/12.
 */

class DataBase(context : Context, name : String) : SQLiteOpenHelper(context, name, null, 9) {
    override fun onUpgrade(db: SQLiteDatabase?, ver0: Int, p2: Int) {
        db?.execSQL("DROP TABLE words")
        onCreate(db)
    }

    fun insert_new_word(word : String, translation: String){
        writableDatabase.insert("words", null,
                ContentValues().apply {
                    put("word", word)
                    put("translate", translation)
                })
    }

    fun select_word(selectWord : String?, selectValue : Array<String>?, orderWord: String?) : Cursor? {
        return readableDatabase.query("words", null, selectWord, selectValue, null,
                null, orderWord)
    }

    fun update_word(word_id: String, update_pair : ArrayList<Pair<String, String>> ){
        var content = ContentValues()
        for(item in update_pair){
            content.put(item.first, item.second)
        }
        writableDatabase.update("words", content, "word_id = ?",
                arrayOf(word_id))
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_SENTENCE)
    }

    companion object {
        private val CREATE_SENTENCE = "CREATE TABLE words( " +
                "word_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "word TEXT UNIQUE," +
                "translate TEXT," +
                "recite_times INTEGER DEFAULT 0," +
                "score INTEGER DEFAULT 0," +
                "last_time INTEGER DEFAULT 0" +
                ");"
    }
}