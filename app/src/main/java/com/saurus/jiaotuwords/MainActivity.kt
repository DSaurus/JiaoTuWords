package com.saurus.jiaotuwords

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var isLearn = false
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer,
                        HomeFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer,
                        FileFragment()).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    fun startLearn() {
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, ReciteFragment()).commit()
        navigation.visibility = View.GONE
        isLearn = true
    }
    fun endLearn() {
        Toast.makeText(this, "Study Finished!", Toast.LENGTH_SHORT).show()
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, HomeFragment()).commit()
        isLearn = false
        navigation.visibility = View.VISIBLE
    }
    override fun onBackPressed() {
        if(isLearn) {
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, HomeFragment()).commit()
            navigation.visibility = View.VISIBLE
            isLearn = false
        } else {
            super.onBackPressed()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentManager.beginTransaction().replace(R.id.fragmentContainer,
                HomeFragment()).commit()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
