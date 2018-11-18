package com.example.canon.bluetoothcontroller2;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    BluetoothConnectionService bluetoothConnectionService;
    private ConnectionToBTModule connectionToBTModule;
    private boolean ledIsOn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bluetoothConnectionService = new BluetoothConnectionService();
        connectionToBTModule = new ConnectionToBTModule();
        //Thread connectionToBTModule = new Thread(new ConnectionToBTModule());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.append("no text here");


        Button activateBluetoothBtn = (Button) findViewById(R.id.activateBluetoothBtn);
        final Button connectToBTModuleBtn = (Button) findViewById(R.id.connectToBTModuleBtn);
        Button lightLedBtn = (Button) findViewById(R.id.lightLedBtn);
        SeekBar motorSpeedBar = (SeekBar) findViewById(R.id.motorSpeedBar);
        motorSpeedBar.setMax(99);

        activateBluetoothBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(!bluetoothConnectionService.checkIfBluetoothIsEnabled()){
                  bluetoothConnectionService.enableBluetooth(getApplicationContext());
               }
            }
        });

        connectToBTModuleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(connectionToBTModule.BTinit()){
                connectionToBTModule.BTconnect();
                connectionToBTModule.setContext(getApplicationContext());
                connectionToBTModule.setTextView(textView2);
                connectionToBTModule.start();
            }
            }
        });

        lightLedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!ledIsOn){
                    connectionToBTModule.setOutputStream("LONE");
                    ledIsOn = true;
                } else if(ledIsOn){
                    connectionToBTModule.setOutputStream("LOFE");
                    ledIsOn = false;
                }

            }
        });

        motorSpeedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                 String valueOfMotor = String.valueOf(i);
                 connectionToBTModule.setOutputStream("M"+ valueOfMotor+ "E");
                 Toast.makeText(getApplicationContext(), "motorsBar" + valueOfMotor, Toast.LENGTH_SHORT);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
