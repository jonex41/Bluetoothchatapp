package com.proj.www.bluetooth_chat_app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.proj.www.bluetooth_chat_app.Contants.STATE_CONNECTED;
import static com.proj.www.bluetooth_chat_app.Contants.STATE_CONNECTING;
import static com.proj.www.bluetooth_chat_app.Contants.STATE_CONNECTION_FAILED;
import static com.proj.www.bluetooth_chat_app.Contants.STATE_LISTENING;
import static com.proj.www.bluetooth_chat_app.Contants.STATE_MESSAGE_RECIEVE;
import static com.proj.www.bluetooth_chat_app.MainActivity.APP_NAME;
import static com.proj.www.bluetooth_chat_app.MainActivity.MY_UUID;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    public ClientThread clientThread;
    public ServerThread serverThread;
    public SendRecieveThread sendRecieveThread;

    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice device;
    private ArrayList<Manss> manssList= new ArrayList<>();


    int REQUEST_ENABLE_BLUETOOTH= 1;
    private View textView;
    String name;
    private EditText editText;
    private ImageView button;

    private Button send_connection, wait_for_connection;



    private static  final  String APP_NAME = "Bluetooth";
    private static  final UUID MY_UUID = UUID.fromString("6667f2aa-a3f2-11e8-98d0-529269fb1459");
    private RecyclerView recyclerView;

    private MessageAdapterYes messageAdater;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
      //  recyclerView.setHasFixedSize(true);
        messageAdater = new MessageAdapterYes(ChatActivity.this, manssList);
         linearLayoutManager = new LinearLayoutManager(getApplicationContext());



        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(messageAdater);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                wait_for_connection = findViewById(R.id.waitforconnection);
                send_connection=findViewById(R.id.sendforconnection);





        BluetoothDevice[] btarray;
        device =  getIntent().getExtras().getParcelable("device");
        ClientThread clientThread = new ClientThread(device);
        clientThread.start();
        getSupportActionBar().setSubtitle("Connecting");
        name = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(name);
        button = findViewById(R.id.sendImage);
        editText = findViewById(R.id.edittext);
        wait_for_connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServerThread serverClass  =new ServerThread();
                serverClass.start();
            }
        });

        send_connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClientThread clientClass = new ClientThread(device);
                clientClass.start();

               handler.obtainMessage(STATE_CONNECTING);
            }
        });



    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){

                case STATE_LISTENING:
                getSupportActionBar().setSubtitle("Listening...");
                    break;

                case STATE_CONNECTING:
                    getSupportActionBar().setSubtitle("Connecting...");

                    break;

                case STATE_CONNECTED:
                    getSupportActionBar().setSubtitle("Connected");
                    button.setOnClickListener(ChatActivity.this);
                    wait_for_connection.setVisibility(View.GONE);
                    send_connection.setVisibility(View.GONE);
                    break;
                case STATE_CONNECTION_FAILED:
                    getSupportActionBar().setSubtitle("Connection failed...");

                    break;

                case STATE_MESSAGE_RECIEVE:
                    byte[] readstuff = (byte[]) message.obj;
                    String messageq = new String(readstuff, 0, message.arg1);

                    Manss manss = new Manss(messageq, String.valueOf(System.currentTimeMillis()),String.valueOf(23) );

                    manssList.add(manss);
                    messageAdater.notifyDataSetChanged();
                    recyclerView.scrollToPosition(manssList.size() - 1 );

                    break;

            }

            return true;
        }
    });

    @Override
    public void onClick(View view) {
        String string =String.valueOf(editText.getText()); //getText from edittext
        String unicNumber = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("numss", "1234");
        Manss manss = new Manss(string, String.valueOf(System.currentTimeMillis()),unicNumber );

        manssList.add(manss);
        messageAdater.notifyDataSetChanged();
        editText.setText(" ");
        recyclerView.scrollToPosition(manssList.size() - 1 );

        sendRecieveThread.write(string.getBytes());

    }


    public class ServerThread extends Thread {




        private BluetoothServerSocket serverSocket;



        public ServerThread( ){


            try {
                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME, MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            BluetoothSocket socket = null;

            while (socket ==null){

                try{
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTING;
                    handler.sendMessage(message);
                    socket = serverSocket.accept();

                } catch (IOException e) {
                    e.printStackTrace();
                    Message message = Message.obtain();
                    message.what = STATE_CONNECTION_FAILED;
                    handler.sendMessage(message);

                }

                if(socket !=null){

                    Message message = Message.obtain();
                    message.what = STATE_CONNECTED;
                    handler.sendMessage(message);
                    sendRecieveThread = new SendRecieveThread(socket);
                    sendRecieveThread.start();

                    break;
                }
            }
        }

    }
    public class ClientThread extends Thread {



        private BluetoothDevice device;
        private BluetoothSocket socket;



        public ClientThread(BluetoothDevice device1){

            device= device1;


            try{
                socket = device.createRfcommSocketToServiceRecord(MY_UUID);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void run() {
            try{
                socket.connect();
                Message message = Message.obtain();
                message.what = STATE_CONNECTED;
                handler.sendMessage(message);
                sendRecieveThread = new SendRecieveThread(socket);
                sendRecieveThread.start();
            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = STATE_CONNECTION_FAILED;
                handler.sendMessage(message);
            }
        }
    }

    public class SendRecieveThread extends Thread {


        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;


        public SendRecieveThread(BluetoothSocket socket){

            bluetoothSocket = socket;
            InputStream tempIn = null;
            OutputStream tempOut = null;


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
}
