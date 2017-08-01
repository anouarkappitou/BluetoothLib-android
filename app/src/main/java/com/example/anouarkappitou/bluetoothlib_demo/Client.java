package com.example.anouarkappitou.bluetoothlib_demo;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bluetoothlib.Bluetooth;
import com.example.bluetoothlib.IFoundCallback;

import java.util.ArrayList;
import java.util.List;

public class Client extends AppCompatActivity {


    private ListView _listView;
    private List<BluetoothDevice> _devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        _devices = new ArrayList<>();

        _listView = (ListView) findViewById( R.id.list_devices );

        ArrayList<String> list = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>( this , android.R.layout.simple_list_item_1 , list );

        _listView.setAdapter( adapter );

        Bluetooth bluetooth = new Bluetooth( this );
        bluetooth.enable();

        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BluetoothDevice device = _devices.get( i );

                Intent in = new Intent( Client.this , Device.class );
                in.putExtra("device" , device );

                startActivity( in );
            }
        });

        //final BluetoothClient client = bluetooth.makeBluetoothClient();

        bluetooth.set_found_callback(new IFoundCallback() {
            @Override
            public void onDevice(BluetoothDevice device) {
                adapter.add( device.getAddress() );
                adapter.notifyDataSetChanged();
                _devices.add( device );
                Toast.makeText( Client.this , "new device with name : " + device.getName() ,Toast.LENGTH_LONG ).show();
            }
        });

        bluetooth.start_discovery();
    }


}
