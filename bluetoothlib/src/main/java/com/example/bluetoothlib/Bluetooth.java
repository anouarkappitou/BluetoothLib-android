package com.example.bluetoothlib;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by anouarkappitou on 7/28/17.
 */

public class Bluetooth {


    private Context _context;
    private BluetoothAdapter _adapter;
    private BroadcastReceiver _reciver;
    private IFoundCallback _callback;
    private IError _errCallback;

    public static final int ENABLE_REQUEST = 1009;
    public static final int NO_ADAPTER_FOUND = 1008;
    public static final int BLUETOOTH_ADAPTER_NOT_FOUND = 1010;

    public static final int BLUETOOTH_PERMISSION = 500;

    public Bluetooth(Context context )
    {
       _context = context;
       _adapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void enable()
    {
        if( _adapter == null )
        {
            if( _errCallback  != null )
            {
                _errCallback.onError( BLUETOOTH_ADAPTER_NOT_FOUND );
            }
            return;
        }

        if( !check_for_bluetooth_permission() )
        {
            if( _errCallback != null )
                _errCallback.onError( BLUETOOTH_PERMISSION );
        }

        if( !_adapter.isEnabled() )
        {
            Intent intent = new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE );

            if( _context instanceof Activity )
            {
                Activity ac = ( Activity ) _context;
                ac.startActivityForResult( intent , ENABLE_REQUEST );
            }else
            {
                _context.startActivity( intent );
            }
        }
    }

    public void start_discovery()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        _reciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                BluetoothDevice device = intent.getParcelableExtra( BluetoothDevice.EXTRA_DEVICE );
                // TODO: call onFound

                if( _callback != null )
                {
                    if( device != null )
                        _callback.onDevice( device );
                    Log.d( "Bluetooth" , "Callback called" );
                }else
                {
                    Log.d( "Bluetooth" , "Callback not called" );
                }
            }
        };

        _context.registerReceiver( _reciver , filter );
        _adapter.startDiscovery();
    }

    public boolean cancel_discovery()
    {
        if( _reciver == null )
            return false;
        _context.unregisterReceiver( _reciver );
        _adapter.cancelDiscovery();
        return true;
    }

    public void make_discoverable( int duration )
    {
        Intent discoverableIntent = new Intent( BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE );
        discoverableIntent.putExtra( BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION , duration );
        _context.startActivity( discoverableIntent );
    }

    public BluetoothServer server_create()
    {
        if( _adapter == null )
        {
            if( _errCallback  != null )
            {
                _errCallback.onError( BLUETOOTH_ADAPTER_NOT_FOUND );
            }
            return null;
        }

        BluetoothServer server = new BluetoothServer( _adapter , _context );
        return server;
    }

    public BluetoothClient client_create()
    {
       if( _adapter == null )
        {
            if( _errCallback  != null )
            {
                _errCallback.onError( BLUETOOTH_ADAPTER_NOT_FOUND );
            }

            return null;
        }

        cancel_discovery();
        BluetoothClient client = new BluetoothClient( _adapter , _context );
        return client;
    }

    public void set_found_callback( IFoundCallback callback )
    {
        _callback = callback;
    }

    public void set_error_callback( IError error )
    {
        _errCallback = error;
    }


    public boolean check_for_bluetooth_permission()
    {
        String permission = "android.permission.BLUETOOTH";
        int res = _context.checkCallingOrSelfPermission( permission );
        return ( res == PackageManager.PERMISSION_GRANTED );
    }

    public boolean check_for_bluetooth_admin_persmission()
    {
         String permission = "android.permission.BLUETOOTH_ADMIN";
        int res = _context.checkCallingOrSelfPermission( permission );
        return ( res == PackageManager.PERMISSION_GRANTED );
    }
}
