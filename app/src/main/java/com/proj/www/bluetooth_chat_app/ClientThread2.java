package com.proj.www.bluetooth_chat_app;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import static com.proj.www.bluetooth_chat_app.Contants.STATE_CONNECTED;
import static com.proj.www.bluetooth_chat_app.Contants.STATE_CONNECTION_FAILED;
import static com.proj.www.bluetooth_chat_app.MainActivity.MY_UUID;

public class ClientThread2 extends Thread {



        private BluetoothDevice device;
        private BluetoothSocket socket;
    private SendRecieveThread2 sendReecieve;
    private Handler handler;

    public ClientThread2(BluetoothDevice device1, Handler handler1){

            device= device1;
            handler= handler1;

            try{
                socket = device.createRfcommSocketToServiceRecord(MY_UUID);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try{
                socket.connect();
                Message message = Message.obtain();
                message.what = STATE_CONNECTED;
                handler.sendMessage(message);
                sendReecieve = new SendRecieveThread2(socket, handler);
                sendReecieve.start();
            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }
        }
    }

