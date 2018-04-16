package com.saurus.jiaotuwords


import android.annotation.SuppressLint
import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_recite.*
import kotlinx.android.synthetic.main.fragment_recite.view.*


/**
 * A simple [Fragment] subclass.
 */
class ReciteFragment : Fragment(){
    val wordList = ArrayList<Word>()
    val waList = ArrayList<Word> ()
    var wordIndex = 0
    var wordStatus = 0
    @SuppressLint("SetTextI18n")
    fun getWords(wordManage : WordManage, n : Int) {
        val oldList = wordManage.getOldWordRecite(n)
        var newList = ArrayList<Word>()
        val m = n - oldList.size
        if(m > 0){
            newList = wordManage.getNewWord(m)
        }
        wordList += oldList
        wordList += newList
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_recite, container, false)
        val wordManage = WordManage(activity)
        getWords(wordManage, 40)
        if(wordList.isEmpty()){
            view.word.text = "没有单词！"
            return view
        }
        wordIndex = 0
        view.word.text = wordList[0].word
        view.wordsinfo.visibility = View.GONE
        view.understandtext.setOnTouchListener {
            v: View, event: MotionEvent ->
            if (event.getAction() == MotionEvent.AXIS_PRESSURE) {
                view.wordsinfo.text = wordList[wordIndex].translate
                view.wordsinfo.visibility = View.VISIBLE
                return@setOnTouchListener false
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                view.wordsinfo.visibility = View.GONE
                return@setOnTouchListener false
            }
            return@setOnTouchListener false
        }
        view.understandtext.setOnClickListener {
            wordManage.updateWord(wordList[wordIndex], 1-wordStatus)
            wordIndex++
            wordStatus = 0
            view.progressBar.max = wordList.size
            view.progressBar.progress = wordIndex
            if(wordIndex >= wordList.size) {
                (activity as MainActivity).endLearn()
            } else {
                view.word.text = wordList[wordIndex].word
                view.wordsinfo.visibility = View.GONE
            }
        }
        view.notunderstandtext.setOnClickListener {
            view.wordsinfo.text = wordList[wordIndex].translate
            view.wordsinfo.visibility = View.VISIBLE
            if(wordStatus == 0) wordList.add(wordList[wordIndex])
            wordStatus = 1
        }
        return view
    }

}// Required empty public constructor
