package com.example.canon.bluetoothcontroller2;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class BluetoothConnectionService extends AppCompatActivity {

    public static Context context;
    private BluetoothAdapter mBluetoothAdapter;
    private final int REQUEST_ENABLE_BT = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get the systems bluetooth device

    }




    public boolean checkIfBluetoothIsEnabled(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return  mBluetoothAdapter.isEnabled();
    }

    public void enableBluetooth(Context context){

             mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            //this opens the dialog to enable bluetooth
            if(!mBluetoothAdapter.isEnabled()) {

                 Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                 context.startActivity(enableBtIntent);
               // startActivity(enableBtIntent);
            }
            if(mBluetoothAdapter.isEnabled()){
                mBluetoothAdapter.disable();
            }


    }
}
