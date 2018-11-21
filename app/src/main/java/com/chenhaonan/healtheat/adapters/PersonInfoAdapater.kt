package com.chenhaonan.healtheat.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chenhaonan.healtheat.R
import kotlinx.android.synthetic.main.item_person_info.view.*

class PersonInfoAdapater(context: Context,data : ArrayList<Pair<String,String>>) : RecyclerView.Adapter<PersonInfoAdapater.InfoHolder>() {

    var ctx : Context = context
    var datas : ArrayList<Pair<String,String>> = data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonInfoAdapater.InfoHolder {
        val view = LayoutInflater.from(ctx).inflate(R.layout.item_person_info,parent,false)
        return InfoHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: PersonInfoAdapater.InfoHolder, position: Int) {
        holder.onBind(position)
    }

    inner class InfoHolder(base : View) : RecyclerView.ViewHolder(base){
        val root : View = base

        fun onBind(position: Int){
            val left = datas[position].first
            val right = datas[position].second
            root.first.text = left
            root.second.text = right
        }
    }
}