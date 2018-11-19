package com.chenhaonan.healtheat

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.chenhaonan.healtheat.fragments.StepCountFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        initFragment()
    }

    private fun initFragment(){
        val trans = supportFragmentManager.beginTransaction()
        trans.add(R.id.content_frame, StepCountFragment())
        trans.commit()
    }
}
