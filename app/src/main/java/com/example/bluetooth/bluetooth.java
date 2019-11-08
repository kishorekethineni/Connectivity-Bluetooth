package com.example.bluetooth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Set;

public class bluetooth extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        IntentFilter filter1 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(broadcastReceiver1, filter1);

        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(broadcastReceiver2, filter2);

    }
    BroadcastReceiver broadcastReceiver1=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            Toast.makeText(context,"Inside on Receive of receiver1", Toast.LENGTH_SHORT).show();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {

                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Toast.makeText(bluetooth.this, "Bluetooth Off", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Toast.makeText(bluetooth.this, "Turning Bluetooth off...", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Toast.makeText(bluetooth.this, "Bluetooth on", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Toast.makeText(bluetooth.this, "Turning Bluetooth on...", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

        }
    };
    private final BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"Inside on Receive of receiver2", Toast.LENGTH_SHORT).show();
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                Toast.makeText(bluetooth.this,deviceName,Toast.LENGTH_SHORT).show();
                Toast.makeText(bluetooth.this, deviceHardwareAddress, Toast.LENGTH_LONG).show();
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver1);
        bluetoothAdapter.cancelDiscovery();
        unregisterReceiver(broadcastReceiver2);
    }

    public void bton(View view)
    {
        if(bluetoothAdapter == null)
        {
            Toast.makeText(this, "Device not supported buletooth", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if(!bluetoothAdapter.isEnabled())
            {
                Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(i,1);
            }
            if(bluetoothAdapter.isEnabled())
            {
                Toast.makeText(this, "Bluetooth is enabled", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void paireddevices(View view)
    {
        if(bluetoothAdapter != null)
        {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice device : pairedDevices)
                {
                    String deviceName = device.getName();
                    String deviceHardwareAddress = device.getAddress(); // MAC address

                    Toast.makeText(this,"Paired Devices are:- "+ deviceName, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void btoff(View view)
    {
        if(bluetoothAdapter != null)
        {
            bluetoothAdapter.disable();
            Toast.makeText(getApplicationContext(), "Bluetooth Turned off" ,Toast.LENGTH_LONG).show();
        }
    }
    public void Discoverdevices(View view)
    {
        if (bluetoothAdapter!=null)

            bluetoothAdapter.startDiscovery();
        //Toast.makeText(this,"Start Discovery"+bluetoothAdapter.startDiscovery(),Toast.LENGTH_SHORT).show();
    }
    public void visibility(View view)
    {
        if (bluetoothAdapter != null) {

            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);

            startActivityForResult(discoverableIntent,2);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode ==1)
        {
            if(resultCode == RESULT_OK)
            {
                Toast.makeText(this, "Bluetooth turned On", Toast.LENGTH_SHORT).show();
            }
            if(resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this, "Bluetooth turned on Failed", Toast.LENGTH_SHORT).show();
            }
        }

        else if (requestCode == 2)
        {
            if (resultCode != RESULT_CANCELED)
            {
                Toast.makeText(this, "Device Discoverablility Start", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Device Discoverablility Canceled", Toast.LENGTH_SHORT).show();
        }
    }
public void intent(View V)
{
    Intent i=new Intent(bluetooth.this,conectivity.class);
    startActivity(i);
}
}
