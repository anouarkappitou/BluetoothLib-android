package com.example.bluetoothlib;

import android.bluetooth.BluetoothDevice;

/**
 * Created by anouarkappitou on 7/28/17.
 */

public interface IFoundCallback {
    void onDevice(BluetoothDevice device);
}
