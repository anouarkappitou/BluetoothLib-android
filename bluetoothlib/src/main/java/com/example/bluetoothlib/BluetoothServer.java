package com.example.bluetoothlib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import static com.example.bluetoothlib.Bluetooth.BLUETOOTH_PERMISSION;

/**
 * Created by anouarkappitou on 7/28/17.
 */

public class BluetoothServer {

    public static int CHANNEL_NOT_CREATED = 1;
    public static int SOCKET_NOT_ACCEPTED = 2;
    public static int NULL_SOCKET = 3;

    private BluetoothAdapter _adapter;
    private Context _context;
    private IConnection _callback;
    private IError _errCallback;

    public BluetoothServer( BluetoothAdapter adapter , Context context )
    {
        _adapter = adapter;
        _context =  context;
    }

    public void set_connection_callback( IConnection callback )
    {
        _callback = callback;
    }

    public void set_error_callback( IError errorCallback )
    {
        _errCallback = errorCallback;
    }

    public void listen()
    {
        new ListenTread().start();
    }

    private class ListenTread extends Thread
    {
        private BluetoothServerSocket _socket;
        private String channel_name;
        private UUID _ID;
        public ListenTread()
        {
            channel_name = getName();
            _ID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            // open RF communication channel
            try
            {
                if( !BluetoothUtils.check_bluetooth_permission( _context ) )
                {
                    if( _errCallback != null )
                    {
                        _errCallback.onError( BLUETOOTH_PERMISSION );
                    }

                    return;
                }

                _socket = _adapter.listenUsingInsecureRfcommWithServiceRecord( channel_name , _ID );

                if( _socket == null )
                {
                    if( _errCallback != null )
                    {
                        _errCallback.onError( CHANNEL_NOT_CREATED );
                    }
                }
            }catch( IOException e )
            {
                if( _errCallback != null )
                {
                    _errCallback.onError( CHANNEL_NOT_CREATED );
                }
            }
        }

        public void run()
        {
            // Nothing to do
            if( _socket == null ) {
                if( _errCallback != null )
                    _errCallback.onError( NULL_SOCKET );
                return;
            }
            try {
                BluetoothSocket socket = _socket.accept();

                Log.d("Bluetooth" , "Accepted");

                if( socket == null )
                {
                    Log.d("Bluetooth" , "Null socket");
                }

                if( _callback != null )
                {
                        _callback.onConnect( socket );
                }

            } catch (IOException e) {

                Log.e( "Bluetooth" , "[BluetoothSev] : " + e.getMessage() );

                if( _errCallback != null )
                {
                    _errCallback.onError( SOCKET_NOT_ACCEPTED );
                }
            }
        }
    }


}
