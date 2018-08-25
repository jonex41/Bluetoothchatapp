package com.proj.www.bluetooth_chat_app;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import static com.proj.www.bluetooth_chat_app.Contants.STATE_MESSAGE_RECIEVE;

public class SendRecieveThread2 extends Thread {


    private final BluetoothSocket bluetoothSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    public Handler handler;

    public SendRecieveThread2(BluetoothSocket socket, Handler handler1){

        bluetoothSocket = socket;
        InputStream tempIn = null;
        OutputStream tempOut = null;
        handler = handler1;

        try {

            tempIn = bluetoothSocket.getInputStream();
            tempOut = bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputStream = tempIn;
        outputStream = tempOut;

    }
    @Override
    public void run(){

        byte[] buffer = new byte[1024];

        int byes;

        while (true){

            try {


                byes = inputStream.read(buffer);
                handler.obtainMessage(STATE_MESSAGE_RECIEVE, byes, -1, buffer).sendToTarget();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    public void write(byte[] bytes){

        try{
            outputStream.write(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
