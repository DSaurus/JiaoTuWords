package com.saurus.jiaotuwords

import android.content.Context
import android.database.Cursor
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by sao on 2018/4/12.
 */


class Word (_word_id : String, _word : String, _translate: String,
            _recite_times : Int, _score : Long, _last_time : Long){
    val word_id = _word_id
    val word = _word
    val score = _score
    val recite_times = _recite_times
    val last_time = _last_time
    val translate = _translate
}

class WordManage (context : Context) {
    var db = DataBase(context, "my_database")
    fun parseWord(cursor : Cursor) : Word {
        var word = Word(cursor.getString(cursor.getColumnIndex("word_id")),
                cursor.getString(cursor.getColumnIndex("word")),
                cursor.getString(cursor.getColumnIndex("translate")),
                cursor.getInt(cursor.getColumnIndex("recite_times")),
                cursor.getLong(cursor.getColumnIndex("score")),
                cursor.getLong(cursor.getColumnIndex("last_time"))
        )
        return word
    }

    fun importFileWord(filename : String){
        val file = File(filename)
        val input = FileReader(file)
        val buffer = BufferedReader(input)
        var wordLine : String? = null
        wordLine = buffer.readLine()
        while(wordLine != null) {
            val temp = wordLine.split(' ')
            db.insert_new_word(temp[0], temp[1])
        }
    }
    fun updateWord(words : Array<Word>) {
        for(item in words) {
            val pairArray = ArrayList<Pair<String, String>>()
            pairArray.add(Pair("word", item.word))
            pairArray.add(Pair("last_time", item.last_time.toString()))
            pairArray.add(Pair("recite_times", item.recite_times.toString()))
            pairArray.add(Pair("score", item.score.toString()))
            pairArray.add(Pair("translate", item.translate))
            db.update_word(item.word_id, pairArray)
        }
    }
    private fun selectOldWord() : ArrayList<Word> {
        val cursor = db.select_word("score != 0", null, "last_time")
        val arrayList = ArrayList<Word>()
        while(!cursor!!.isLast){
            arrayList.add(parseWord(cursor))
        }
        arrayList.add(parseWord(cursor))
        return arrayList
    }
    private fun selectNewWord() : ArrayList<Word> {
        val cursor = db.select_word("score == 0", null, null)
        val arrayList = ArrayList<Word> ()
        do {
            arrayList.add(parseWord(cursor!!))
        } while(cursor!!.moveToNext())
        return arrayList
    }
    fun getOldWordRecite(n : Int) : ArrayList<Word> {
        val arrayList = selectOldWord()
        val reciteList = ArrayList<Word>()
        for(item in arrayList) {
            if(reciteList.size > n) break
            if(item.last_time + item.score*3600*24 > Date().time){
               reciteList.add(item)
            }
        }
        return reciteList
    }
    fun getNewWord(n : Int) : ArrayList<Word> {
        val arraryList = selectNewWord()
        val newList = ArrayList<Word>()
        for(item in newList){
            if(newList.size > n) break
            newList.add(item)
        }
        return newList
    }
}