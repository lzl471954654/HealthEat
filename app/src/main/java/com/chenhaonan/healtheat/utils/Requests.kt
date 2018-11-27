package com.chenhaonan.healtheat.utils

import android.os.Looper
import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

private val client = OkHttpClient()

public fun login(username:String, password:String,callback:Callback){
    val url = "http://132.232.60.56:8080/HealthyEat/LoginValidation"
    val body = FormBody.Builder()
        .add("emall",username)
        .add("userPassword",password)
        .build()
    val request = Request.Builder()
        .url(url)
        .post(body)
        .build()
    val call = client.newCall(request)
    call.enqueue(callback)
}

public fun sendData(data : String, userID : String){
    val url = "http://132.232.60.56:8080/HealthyEat/date"
    val jsonObject = JSONObject(data)
    val hr = jsonObject.get("heartrate")
    val us = jsonObject.get("step")
    val newJson = JSONObject()
    newJson.put("userID",userID)
    newJson.put("userHeartRate",hr)
    newJson.put("userStep",us)
    val finalData = newJson.toString()
    val body = FormBody.Builder()
        .add("date",finalData)
        .build()
    val request = Request.Builder()
        .url(url)
        .post(body)
        .build()
    val call = client.newCall(request)
    call.enqueue(object : Callback{
        override fun onFailure(call: Call, e: IOException) {
            Log.e("sendData","error")
        }

        override fun onResponse(call: Call, response: Response) {
            Log.e("sendData","success")
            response.close()
        }

    })
}