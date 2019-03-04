package com.sneakyscout.n30b4rt.sneakyscout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothServerSocket ss;
    private Thread t;

    public Button listen;
    public Button send;
    public EditText data;

    private static final UUID MY_UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //startBluetooth();
        bluetoothAdapter.startDiscovery();

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 600);
        startActivity(discoverableIntent);

        listen = (Button) this.findViewById(R.id.listen);
        send = (Button) this.findViewById(R.id.Send);
        data = (EditText) this.findViewById(R.id.textStuff);

        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClientData();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothDevice[] devices = (BluetoothDevice[]) (bluetoothAdapter.getBondedDevices().toArray());
                for (BluetoothDevice a : devices) {
                    Toast.makeText(getApplicationContext(), "'" + a.getName() + "'", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*public void startBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBT);
        }
    }*/

    public void getClientData() {
        t = new Thread() {
            @Override
            public void run() {
                boob();
            }
        };
        t.start();
    }

    public void boob() {

        ss = null;

        try {
            ss = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("Spartan-Scout", MY_UUID_SECURE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                BluetoothSocket s = ss.accept();
                Toast.makeText(getApplicationContext(), s.getRemoteDevice().getName(), Toast.LENGTH_LONG).show();
                s.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
