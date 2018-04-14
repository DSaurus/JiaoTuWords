package com.saurus.jiaotuwords

import android.app.Fragment
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_file.view.*
import android.provider.MediaStore
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.util.Log
import android.R.attr.data
import android.app.Activity
import android.widget.Toast
import okhttp3.*
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FileFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FileFragment : Fragment() {

    fun onBrowse() {
        val chooseFile = Intent(Intent.ACTION_GET_CONTENT)
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE)
        chooseFile.type = "*/*"
        val intent = Intent.createChooser(chooseFile, "Choose a file")
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode != RESULT_OK) return
        var path = ""
        if (requestCode == 0) {
            val uri = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = activity.contentResolver.query(uri, filePathColumn, null, null, null)
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                path = cursor.getString(columnIndex)
            } else {
                //boooo, cursor doesn't have rows ...
            }
            cursor.close()
            Log.e("Path", path)
        }
    }

    val importLisner = View.OnClickListener {
        Toast.makeText(fragActivity, "Loading!", Toast.LENGTH_LONG).show()
        val client = OkHttpClient()
        val request = Request.Builder().url("https://raw.githubusercontent.com/DSaurus/JiaoTuWords/master/assets/" + view.editText.text + ".txt").build()
        val response = client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                val resString = response?.body()?.string()
                if(resString!!.contains("404") || resString.contains("400")) {
                    fragActivity!!.runOnUiThread({
                        Toast.makeText(fragActivity, "404 Error!", Toast.LENGTH_SHORT).show()
                    })
                    return
                }
                WordManage(fragActivity!!).importStringWord(resString)
                fragActivity!!.runOnUiThread({
                    Toast.makeText(fragActivity, "Finished!", Toast.LENGTH_SHORT).show()
                })
            }

            override fun onFailure(call: Call?, e: IOException?) {
                Toast.makeText(fragActivity, "404 Error!", Toast.LENGTH_SHORT).show()
            }

        })
    }
    var fragActivity : Activity? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_file, container, false)
        fragActivity = activity
        view.relativeLayout.setOnClickListener(importLisner)
        view.importButton.setOnClickListener(importLisner)
        return view
    }

}// Required empty public constructor
