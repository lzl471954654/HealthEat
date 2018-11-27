package com.chenhaonan.healtheat.utils

import android.bluetooth.BluetoothAdapter
import com.chenhaonan.healtheat.exceptions.DoNotSupportException

fun getBluetoothAdapter(): BluetoothAdapter {
    val adapter = BluetoothAdapter.getDefaultAdapter()
    adapter ?: throw DoNotSupportException("This device do not support bluetooth")
    return adapter
}

