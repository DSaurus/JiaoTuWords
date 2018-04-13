package com.saurus.jiaotuwords


import android.annotation.SuppressLint
import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_recite.*
import kotlinx.android.synthetic.main.fragment_recite.view.*


/**
 * A simple [Fragment] subclass.
 */
class ReciteFragment : Fragment() {
    
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_recite, container, false)

        view.wordsinfo.visibility = View.GONE
        view.understandtext.setOnClickListener {
            _ ->
            wordsinfo.text = "${wordsinfo.text}1"
        }
        view.notunderstandtext.setOnClickListener {
            wordsinfo.visibility = View.VISIBLE
        }
        return view
    }

}// Required empty public constructor
