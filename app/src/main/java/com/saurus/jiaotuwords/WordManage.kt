package com.saurus.jiaotuwords

import android.content.Context
import android.database.Cursor
import android.util.Log
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
    var score = _score
    val recite_times = _recite_times
    var last_time = _last_time
    val translate = _translate
    override fun toString(): String {
        return word_id + " " + word + " " + score.toString() + " " + recite_times + " " +
                last_time.toString() + " " + translate
    }
}

class WordManage (context: Context) {
    var db = DataBase(context, "my_database")
    fun parseWord(cursor : Cursor) : Word {
        var word = Word(cursor.getString(cursor.getColumnIndex("word_id")),
                cursor.getString(cursor.getColumnIndex("word")),
                cursor.getString(cursor.getColumnIndex("translate")),
                cursor.getInt(cursor.getColumnIndex("recite_times")),
                cursor.getLong(cursor.getColumnIndex("score")),
                cursor.getLong(cursor.getColumnIndex("last_time"))
        )
        Log.e("word", word.toString())
        return word
    }
    fun importStringWord(string : String?){
        Log.e("string", string)
        val input = StringReader(string)
        val buffer = BufferedReader(input)
        var wordLine : String? = null
        wordLine = buffer.readLine()
        while(wordLine != null) {
            val temp = wordLine.split(' ')
            db.insert_new_word(temp[0], temp[1])
            wordLine = buffer.readLine()
        }
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
            wordLine = buffer.readLine()
        }
    }
    fun updateWords(words : Array<Word>) {
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
    fun updateWord(word : Word, status : Int) {
        // 0 : not Understand   1 : Understand
        if(status == 0){
            word.score = word.score / 2
        } else {
            word.score += 2000
        }
        word.score = Math.max(word.score, 1000)
        val pairArray = ArrayList<Pair<String, String>>()
        pairArray.add(Pair("word", word.word))
        pairArray.add(Pair("last_time", word.last_time.toString()))
        pairArray.add(Pair("recite_times", word.recite_times.toString()))
        pairArray.add(Pair("score", word.score.toString()))
        pairArray.add(Pair("translate", word.translate))
        db.update_word(word.word_id, pairArray)
    }
    private fun selectOldWord() : ArrayList<Word> {
        val cursor = db.select_word("score != 0", null, "last_time")
        val arrayList = ArrayList<Word>()
        if(cursor?.count == 0) return arrayList
        while(cursor!!.moveToNext()){
            var word = parseWord(cursor)
            arrayList.add(parseWord(cursor))
        }
        return arrayList
    }
    private fun selectNewWord() : ArrayList<Word> {
        val cursor = db.select_word("score == 0", null, null)
        val arrayList = ArrayList<Word> ()
        Log.e("debug", cursor?.count.toString())
        if(cursor?.count == 0) return arrayList
        while(cursor!!.moveToNext()){
            arrayList.add(parseWord(cursor))
        }
        return arrayList
    }
    fun getOldWordRecite(n : Int) : ArrayList<Word> {
        val arrayList = selectOldWord()
        val reciteList = ArrayList<Word>()
        for(item in arrayList) {
            if(reciteList.size > n) break
            if(item.score >= 12000) continue
            if(item.last_time + item.score*3600*24 < Date().time){
                item.last_time = Date().time
                reciteList.add(item)
            }
        }
        return reciteList
    }
    fun getNewWord(n : Int) : ArrayList<Word> {
        val arrayList = selectNewWord()
        val newList = ArrayList<Word>()
        for(item in arrayList){
            if(newList.size > n) break
            item.last_time = Date().time
            newList.add(item)
        }
        return newList
    }
    fun getNewWordSize() : Int{
        return selectNewWord().size
    }
    fun getOldWordSize() : Int{
        val arrayList = selectOldWord()
        var ans = 0
        for(item in arrayList) {
            if(item.score >= 12000) continue
            if(item.last_time + item.score*3600*24 < Date().time){
                item.last_time = Date().time
                ans++
            }
        }
        return ans
    }
    fun getMyWordSize() : Int{
        val arrayList = selectOldWord()
        var ans = 0
        for(item in arrayList) {
            if(item.score >= 12000) ans++
        }
        return ans
    }
}