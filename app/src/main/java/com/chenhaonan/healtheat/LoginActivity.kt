package com.chenhaonan.healtheat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.chenhaonan.healtheat.utils.login
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

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
                val username = user_input.text.toString()
                val password = pass_input.text.toString()
                login(username,password, object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                        runOnUiThread {
                            Snackbar.make(progressBar,"登录失败，请重试",Snackbar.LENGTH_SHORT).show()
                            progressBar.visibility = View.GONE
                        }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        runOnUiThread { progressBar.visibility = View.GONE }
                        val data = response.body()?.string()
                        if (data == null){
                            runOnUiThread {
                                Snackbar.make(progressBar,"服务器无返回，请重试",Snackbar.LENGTH_SHORT).show()
                            }
                            response.close()
                            return
                        }
                        val json = JSONObject(data)
                        val code = json.getInt("code")
                        if (code != 1){
                            runOnUiThread {
                                Snackbar.make(progressBar,"密码或账号错误，请重试",Snackbar.LENGTH_SHORT).show()
                            }
                            response.close()
                            return
                        }
                        runOnUiThread {
                            MainActivity.startMainActivityWithExtra(this@LoginActivity,data)
                            finish()
                        }
                    }
                })
            }
        }
    }



}