package com.proj.www.bluetooth_chat_app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import static com.proj.www.bluetooth_chat_app.Contants.STATE_CONNECTED;
import static com.proj.www.bluetooth_chat_app.Contants.STATE_CONNECTING;
import static com.proj.www.bluetooth_chat_app.MainActivity.APP_NAME;
import static com.proj.www.bluetooth_chat_app.MainActivity.MY_UUID;

public class ServerThread2 extends Thread {




        private BluetoothServerSocket serverSocket;
    private SendRecieveThread2 sendReecieve;
    public Handler handler;

    public ServerThread2(Handler handler1, BluetoothAdapter bluetoothAdapter){

        handler = handler1;
            try {
                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME, MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            BluetoothSocket socket = null;

            while ((socket ==null)){

                try{
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTING;
                    handler.sendMessage(message);
                    socket = serverSocket.accept();

                } catch (IOException e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTING;
                    handler.sendMessage(message);

                }

                if(socket !=null){

                    Message message = Message.obtain();
                    message.what = STATE_CONNECTED;
                    handler.sendMessage(message);
                    sendReecieve = new SendRecieveThread2(socket, handler);
                    sendReecieve.start();

                    break;
                }
            }
        }

}
