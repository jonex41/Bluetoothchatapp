package com.proj.www.bluetooth_chat_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ListofBluetoothAdapter extends RecyclerView.Adapter<ListofBluetoothAdapter.ListOfBluetooth> {
private Context context;
private List<String> stringList;
    private ItemClickListener mClickListener;


    public ListofBluetoothAdapter(Context context, List<String> stringList) {
        this.context = context;
        this.stringList = stringList;
    }


    @NonNull
    @Override
    public ListOfBluetooth onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_list, parent, false);
        return new ListOfBluetooth(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfBluetooth holder, int position) {
        String namew= stringList.get(position);
        holder.name.setText(namew);
        getItem(position);
    }



    @Override
    public int getItemCount() {
        return (stringList != null)?stringList.size():0;
    }

    public class ListOfBluetooth extends RecyclerView.ViewHolder implements View.OnClickListener {
       public TextView name ;


        ListOfBluetooth(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.name);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

        }





        // parent activity will implement this method to respond to click events

    }

    String getItem(int id) {
        return stringList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
