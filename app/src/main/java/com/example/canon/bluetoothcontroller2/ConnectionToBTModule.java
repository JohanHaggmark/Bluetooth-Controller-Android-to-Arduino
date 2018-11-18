package com.example.canon.bluetoothcontroller2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class ConnectionToBTModule extends Thread{

    private static Context context;

    private final String DEVICE_ADDRESS = "98:D3:33:80:C6:DA";
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothDevice device;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private InputStream inputStream;

    private DataInputStream in;
    private InputStreamReader aReader;
    private BufferedReader mBufferedReader;

    private Handler messageHandler;

    private TextView textView;


    public boolean BTinit(){
        boolean found = false;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            //Doesn't support bluetooth
        }
        if(!bluetoothAdapter.isEnabled()){
            found = false;
        } else {
            Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();

            if(bondedDevices.isEmpty()){
                //no device is paried
            } else {

                for (BluetoothDevice iterator : bondedDevices) {
                    if(iterator.getAddress().equals(DEVICE_ADDRESS)) {
                        device = iterator;
                        found = true;
                        break;
                    }
                }
            }
        }
        return found;
    }

    public boolean BTconnect(){
        boolean connected = true;

        try {
         //   bluetoothSocket = device.createRfcommSocketToServiceRecord(PORT_UUID); //Creates a socket to handle the outgoing connection
            bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(PORT_UUID);
            bluetoothSocket.connect();

        } catch(IOException e){
            e.printStackTrace();
            connected = false;
        }

        try {
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();


            in = new DataInputStream(inputStream);
            aReader = new InputStreamReader(in);
            mBufferedReader = new BufferedReader(aReader);
        } catch(IOException e){
            e.printStackTrace();
        }
        return connected;
    }

    public void setOutputStream(String command){

            try {
                for(int i = 0; i < command.length(); i++) {
                   char achar = command.charAt(i);
                    outputStream.write((byte)achar);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
    }




    public String readInputStream(){

        String data = "";

        try {
            if (inputStream.available() > 0) {
                byte[] inBuffer = new byte[1024];
                int bytesRead = inputStream.read(inBuffer);
                // data = new String(inBuffer, "ASCII");
                data = new String(inBuffer, 0, bytesRead);
                //data = data.substring(0, bytesRead);
            }
        } catch(IOException e){
            Log.e(TAG, "Error reading from btInputStream");
        }
        return data;
    }

    public void setContext(Context context){
        this.context = context;
    }

    @Override
    public void run() {




        while(!this.isInterrupted()){
           String message = readInputStream();
           if(message.length() > 0){
            //   Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
               try {
                       textView.setText(message);

               } catch(Exception e){
                   e.printStackTrace();
               }

           }
        }
    }

    public void setTextView(TextView textView){
        this.textView = textView;
    }
}
