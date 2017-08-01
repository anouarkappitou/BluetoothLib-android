package com.example.bluetoothlib;

import android.bluetooth.BluetoothSocket;

/**
 * Created by anouarkappitou on 7/28/17.
 */

public interface IConnection {
    void onConnect(BluetoothSocket socket);
}
