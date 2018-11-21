package com.chenhaonan.healtheat

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() , View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //supportActionBar?.hide()
        sign_in.setOnClickListener(this)
    }

    companion object {
        fun startLoginActivity(context : Context){
            val intent = Intent(context,LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            sign_in.id ->{
                progressBar.visibility = View.VISIBLE
                Thread{
                    Thread.sleep(3000)
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        MainActivity.startMainActivity(this)
                        finish()
                    }
                }.start()
            }
        }
    }



}