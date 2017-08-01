package com.example.anouarkappitou.bluetoothlib_demo;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.bluetoothlib.Bluetooth;
import com.example.bluetoothlib.BluetoothClient;
import com.example.bluetoothlib.IConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Device extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        final TextView tv = (TextView) findViewById( R.id.client_tv );
        final EditText edit = (EditText) findViewById( R.id.client_edit );
        final Button send = (Button) findViewById( R.id.client_send );

        BluetoothDevice device = (BluetoothDevice) getIntent().getExtras().get("device");

        Bluetooth bt = new Bluetooth( this );

        BluetoothClient client = bt.client_create();

        client.set_connection_callback(new IConnection() {
            @Override
            public void onConnect(final BluetoothSocket socket) {


                try {
                    final OutputStream out = socket.getOutputStream();
                    InputStream input = socket.getInputStream();


                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String text =  edit.getText().toString();
                            send_string( out , text );
                        }
                    });


                    BufferedReader reader = new BufferedReader( new InputStreamReader(input) );
                    String line;

                    StringBuffer buffer = new StringBuffer();
                    while( ( line = reader.readLine() ) != null )
                    {
                        final String finalLine = line;
                        runOnUiThread(new Thread(){
                            @Override
                            public void run() {
                                tv.setText(finalLine);
                            }
                        });
                        Log.d("Bluetooth",line);
                    }

                    //tv.setText( buffer.toString() );




                } catch (IOException e) {
                    Log.e("Bluetooth", e.getMessage() );
                }
            }
        });

        client.device_connect( device );

    }


     public void send_string( OutputStream os , String str )
    {
        PrintWriter writer = new PrintWriter( os );
        str += "\n";
        writer.write( str );
        writer.flush();
    }

}
