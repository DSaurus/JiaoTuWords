package com.saurus.jiaotuwords

import android.app.Fragment
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_home, container, false)
        val wordManage = WordManage(activity)
        view.newWord.text = wordManage.getNewWordSize().toString()
        view.oldWord.text = wordManage.getOldWordSize().toString()
        view.myWord.text = wordManage.getMyWordSize().toString()
        view.btn_startlearn.setOnClickListener {
            (activity as MainActivity).startLearn()
        }
        return view
    }
}// Required empty public constructor
