package com.chenhaonan.healtheat.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chenhaonan.healtheat.R
import com.chenhaonan.healtheat.interfaces.Updatable
import kotlinx.android.synthetic.main.fragment_tody_steps.*
import kotlinx.android.synthetic.main.fragment_tody_steps.view.*
import org.json.JSONObject

class StepCountFragment : Fragment() ,Updatable{
    override fun updateData(data: String?) {
        Log.e("LOGIN_DATA",data)
        val json = JSONObject(data)
        val step = json.getInt("step")
        val s = step.toString() + "步"
        rootView.post {
            rootView.steps_count.text = s
        }
    }

    lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.rootView = inflater.inflate(R.layout.fragment_tody_steps,container,false)
        return rootView
    }
}