package com.example.anouarkappitou.bluetoothlib_demo;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bluetoothlib.Bluetooth;
import com.example.bluetoothlib.BluetoothServer;
import com.example.bluetoothlib.IConnection;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Server extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_server);

        final TextView tv = (TextView) findViewById( R.id.server_tv );
        final Button send = (Button) findViewById( R.id.serv_send );
        final EditText edit = (EditText) findViewById( R.id.serv_edit );

        Bluetooth bt = new Bluetooth( this );
        bt.enable();
        bt.make_discoverable( 500 );

        BluetoothServer server = bt.server_create();

        server.set_connection_callback(new IConnection() {
            @Override
            public void onConnect(BluetoothSocket socket) {
                Log.d("Bluetooth" , "Connected");

                try {
                    final OutputStream out = socket.getOutputStream();
                    PrintWriter writer = new PrintWriter( out );

                    while( true )
                    {
                        String line = edit.getText().toString();

                        if( !line.isEmpty() )
                        {
                            writer.println( line );
                            writer.flush();
                        }

                        Thread.sleep( 50 );
                    }

                    //writer.close();
                } catch (IOException e) {

                } catch (InterruptedException e) {
                }
            }
        });

        server.listen();
    }

}
