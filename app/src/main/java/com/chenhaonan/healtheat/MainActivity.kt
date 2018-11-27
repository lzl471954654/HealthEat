package com.chenhaonan.healtheat

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.chenhaonan.healtheat.fragments.MeFragment
import com.chenhaonan.healtheat.fragments.StepCountFragment
import com.chenhaonan.healtheat.utils.getBluetoothAdapter
import com.chenhaonan.healtheat.utils.sendData
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.*
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var step : StepCountFragment
    lateinit var me : MeFragment
    var targetDevice : BluetoothDevice? = null
    var isLoop = true

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
        initBluetooth()
        val json = intent.getStringExtra("user")
        me.updateData(json)
    }

    private fun initBluetooth(){
        var filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(mReceiver,filter)
        filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(mReceiver,filter)
        filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        registerReceiver(mReceiver,filter)

        val adapter = getBluetoothAdapter()

        if (!adapter.isEnabled){
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(intent, BLUETOOTH_REQUEST_CODE)
        }else{
            checkBondBluetoothDevices()
        }
        Log.e(TAG,"-蓝牙已经打开")
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver)
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

        const val UUID_STRING = "00001101-0000-1000-8000-00805F9B34FB"
        const val BLUETOOTH_REQUEST_CODE = 100
        val TAG = this::class.java.name

        fun startMainActivity(context: Context){
            val intent = Intent(context,MainActivity::class.java)
            context.startActivity(intent)
        }

        fun startMainActivityWithExtra(context: Context, json : String){
            val intent = Intent(context,MainActivity::class.java)
            intent.putExtra("user",json)
            context.startActivity(intent)
        }
    }

    fun checkBondBluetoothDevices(){
        val adapter = getBluetoothAdapter()
        val set = adapter.bondedDevices
        for (bluetoothDevice in set) {
            Log.e(TAG,"name = ${bluetoothDevice.name}\taddress = ${bluetoothDevice.address}")
            if (bluetoothDevice.name == "XueTang"){
                targetDevice = bluetoothDevice
                Snackbar.make(navigation,"搜索到蓝牙设备 XueTang 开始链接",Snackbar.LENGTH_SHORT).show()
                startConnectDeivce(targetDevice!!)
                return
            }
        }
        val result = adapter.startDiscovery()
        Log.e(TAG,"startDiscovery = $result")
    }

    fun startConnectDeivce(device: BluetoothDevice){
        if (device.bondState == BluetoothDevice.BOND_NONE){
            device.createBond()
        }

        Thread{
            try{
                val blueSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(UUID_STRING))
                blueSocket.connect()
                runOnUiThread { Snackbar.make(navigation,"XueTang 链接成功",Snackbar.LENGTH_SHORT).show() }
                manageConnect(blueSocket)
            }catch (e:IOException){
                e.printStackTrace()
            }
        }.start()
    }

    // this method should be called on SubThread
    fun manageConnect(bluetoothSocket: BluetoothSocket){
        val reader = BufferedReader(InputStreamReader(bluetoothSocket.inputStream))
        val builder = StringBuilder()
        var start_time = System.currentTimeMillis()
        while (isLoop){
            val c = reader.read().toChar()
            builder.append(c)
            if ( c == '}'){
                val data = builder.toString()
                //println(data)
                step.updateData(data)
                val currentTime = System.currentTimeMillis()
                if (currentTime - start_time > 2*60*1000){
                    start_time = currentTime
                    sendData(data,JSONObject(intent.getStringExtra("user")).getString("id"))
                }
                builder.delete(0,builder.length)
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == BLUETOOTH_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                Log.e(TAG,"蓝牙已经打开")
                checkBondBluetoothDevices()
            }else{
                Log.e(TAG,"蓝牙打开失败")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    // Create a BroadcastReceiver for ACTION_FOUND
    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND == action) {
                // Get the BluetoothDevice object from the Intent
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                // Add the name and address to an array adapter to show in a ListView
                if ( device.name != null && device.name.contains("XueTang")){
                    getBluetoothAdapter().cancelDiscovery()
                    targetDevice = device
                    Snackbar.make(navigation,"搜索到蓝牙设备 XueTang 开始链接",Snackbar.LENGTH_SHORT).show()
                    startConnectDeivce(targetDevice!!)
                    return
                }
                Log.e(TAG,device.name + "\n" + device.address)
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED == action){
                println("scan start")
                Snackbar.make(navigation,"开始搜索蓝牙设备",Snackbar.LENGTH_SHORT).show()
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action){
                println("scan finish")
                if (targetDevice == null){
                    Snackbar.make(navigation,"没有搜索到合适的蓝牙设备",Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

}
