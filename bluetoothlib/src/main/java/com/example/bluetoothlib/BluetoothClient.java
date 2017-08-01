package com.example.bluetoothlib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import static com.example.bluetoothlib.Bluetooth.BLUETOOTH_PERMISSION;

/**
 * Created by anouarkappitou on 7/28/17.
 */

public class BluetoothClient {

    public static final int SOCKET_NOT_CREATED   = 3;
    public static final int SOCKET_NOT_CONNECTED = 4;

    private BluetoothAdapter _adapter;
    private IError _errCallback;
    private IConnection _callback;
    private Context _context;

    public BluetoothClient( BluetoothAdapter adapter , Context context )
    {
        _adapter = adapter;
        _context = context;
    }

    public void mac_connect( String address )
    {
        BluetoothDevice device = _adapter.getRemoteDevice( address );
        new ConnectionThread( device ).start();
    }

    public void device_connect( BluetoothDevice device )
    {
        new ConnectionThread( device ).start();
    }

    public void set_connection_callback( IConnection connectCallback )
    {
        _callback = connectCallback;
    }

    public void set_error_callback( IError errorCallback )
    {
        _errCallback = errorCallback;
    }

    private class ConnectionThread extends Thread
    {
        private BluetoothSocket _socket;
        private UUID _UID;

        public ConnectionThread( BluetoothDevice device )
        {
            _UID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            try {
                if( BluetoothUtils.check_bluetooth_permission( _context ) )
                {
                    if( _errCallback != null )
                    {
                        _errCallback.onError( BLUETOOTH_PERMISSION );
                    }
                    return;
                }

                _socket = device.createInsecureRfcommSocketToServiceRecord( _UID );
                if( _socket == null )
                {
                    if( _errCallback != null )
                    {
                        _errCallback.onError( SOCKET_NOT_CREATED );
                    }
                }
            } catch (IOException e) {

                Log.e("Bluetooth" , "[ChanelBluetoothCli] : " + e.getMessage() );
                if( _errCallback != null )
                    {
                        _errCallback.onError( SOCKET_NOT_CREATED );
                    }

            }
        }

        public void run()
        {
            try {
                _socket.connect();

                Log.d("Bluetooth" , "Client connected");
               _callback.onConnect( _socket );

            } catch (IOException e) {
                Log.e("Bluetooth" , "[ConnBluetoothCli] : " + e.getMessage() );
                    if( _errCallback != null )
                    {
                        _errCallback.onError( SOCKET_NOT_CONNECTED );
                    }
            }
        }
    }

}
