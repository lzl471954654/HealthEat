package com.chenhaonan.healtheat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.chenhaonan.healtheat.fragments.MeFragment
import com.chenhaonan.healtheat.fragments.StepCountFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var step : StepCountFragment
    lateinit var me : MeFragment

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val trans = supportFragmentManager.beginTransaction()
                trans.hide(me)
                trans.show(step)
                trans.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                val trans = supportFragmentManager.beginTransaction()
                trans.hide(step)
                trans.show(me)
                trans.commit()
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
        me = MeFragment()
        step = StepCountFragment()
        val trans = supportFragmentManager.beginTransaction()
        trans.add(R.id.content_frame, me)
        trans.add(R.id.content_frame, step)
        trans.hide(me)
        trans.commit()
    }

    companion object {
        fun startMainActivity(context: Context){
            val intent = Intent(context,MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}
