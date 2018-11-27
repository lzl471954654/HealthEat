package com.chenhaonan.healtheat.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chenhaonan.healtheat.R
import com.chenhaonan.healtheat.adapters.PersonInfoAdapater
import com.chenhaonan.healtheat.interfaces.Updatable
import kotlinx.android.synthetic.main.fragment_me.view.*
import org.json.JSONObject

class MeFragment : Fragment(),Updatable {
    override fun updateData(data: String?) {
        Log.e("ME",data)
        val json = JSONObject(data)
        val list = ArrayList<Pair<String,String>>()
        list.add("身高" to json.getString("hight"))
        list.add("体重" to json.getString("weight"))
        list.add("性别" to json.getString("sex"))
        list.add("基础代谢" to "0 kcal")
        list.add("运动消耗" to "0 kcal")
        //list.add("今日摄入" to "0 kcal")
        tempList = list
        if (isAttach){
            root.person_info_list.layoutManager = LinearLayoutManager(ctx)
            val adapter = PersonInfoAdapater(ctx,list)
            root.person_info_list.adapter = adapter
        }
    }

    var isAttach = false
    var tempList : ArrayList<Pair<String,String>>? = null
    lateinit var root : View
    lateinit var ctx : Context

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        ctx = context!!
        isAttach = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.root = inflater.inflate(R.layout.fragment_me,container,false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    fun initData(){
        root.person_info_list.layoutManager = LinearLayoutManager(ctx)
        val list = if (tempList == null){
            val list = ArrayList<Pair<String,String>>()
            list.add("身高" to "188 cm")
            list.add("体重" to "80 kg")
            list.add("性别" to "男")
            list.add("基础代谢" to "2000 kcal")
            list.add("运动消耗" to "1500 kcal")
            //list.add("今日摄入" to "1800 kcal")
            list
        }else{
            tempList!!
        }
        val adapter = PersonInfoAdapater(ctx,list)
        root.person_info_list.adapter = adapter
    }
}