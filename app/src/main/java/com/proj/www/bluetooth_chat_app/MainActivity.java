package com.proj.www.bluetooth_chat_app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements ListofBluetoothAdapter.ItemClickListener {


    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice[] btArray;



    int REQUEST_ENABLE_BLUETOOTH = 34;
   public RecyclerView recyclerView;

    public static final String APP_NAME = "Bluetooth";
    public static final UUID MY_UUID = UUID.fromString("6667f2aa-a3f2-11e8-98d0-529269fb1459");

    //public  String[] strings;
    private boolean firstStart;
    public static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static Random RANDOM = new Random();
    private List<String> stringList = new ArrayList<String>();
    private ListofBluetoothAdapter listofBluetoothAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView =  (RecyclerView) findViewById(R.id.listfoviews);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        listofBluetoothAdapter = new ListofBluetoothAdapter(MainActivity.this, stringList);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> bt = bluetoothAdapter.getBondedDevices();

        String[] stringuy = new String[bt.size()];
        btArray = new BluetoothDevice[bt.size()];
        int index = 0;
        Log.d("TAG", "a");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Log.d("TAG", "b");
        SharedPreferences settingd = getSharedPreferences("PREFS", MODE_PRIVATE);
        firstStart = settingd.getBoolean("justInstalled", true);
        if (firstStart) {
            SharedPreferences.Editor editor = settingd.edit();
            editor.putBoolean("justInstalled", false);

            String ramdomgenerated = ramdomString(10);
            editor.putString("numss", ramdomgenerated);
            editor.commit();


        }
        Log.d("TAG", "b");
        if (!bluetoothAdapter.isEnabled()) {

            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        }



        if (bt.size() > 0) {
                String you = "";
            for (BluetoothDevice device : bt) {
                btArray[index] = device;
                stringuy[index] = device.getName();
                index++;


                String name = device.getName();
                stringList.add(name);
               // you += device.getName();
                listofBluetoothAdapter.notifyDataSetChanged();

            }

            listofBluetoothAdapter.setClickListener(this);

                recyclerView.setAdapter(listofBluetoothAdapter);
        }


    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + listofBluetoothAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        Log.d("TAG", "w");
        //  intent.putExtra("name", strings[i]);

        BluetoothDevice bluetoothDevice = btArray[position];
        intent.putExtra("name", listofBluetoothAdapter.getItem(position));
        intent.putExtra("device", bluetoothDevice);
        startActivity(intent);

    }
        public static String ramdomString ( int len){

            StringBuilder sb = new StringBuilder(len);
            for (int i = 1; i < len; i++) {
                sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
            }
            return sb.toString();
        }


}

