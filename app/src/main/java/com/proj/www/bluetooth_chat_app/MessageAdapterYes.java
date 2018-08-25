package com.proj.www.bluetooth_chat_app;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;


/**
 * Created by MR AGUDA on 19-Apr-18.
 */

    public class MessageAdapterYes extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_TEXT = 1;




    private static final int VIEW_TYPE_TEXT2 = 2;



        private Context ctx;
        private List<Manss> mMessageList;


    public MessageAdapterYes(Context context, List<Manss> messageList) {
            ctx = context;
            mMessageList = messageList;
        }

        @Override
        public int getItemCount() {
            return mMessageList.size();
        }




   @Override
    public int getItemViewType(int position) {
       Manss messagess = (Manss) mMessageList.get(position);


       String unicNumber = PreferenceManager.getDefaultSharedPreferences(ctx).getString("numss", "1234");

       String type = messagess.getMyIdNumber().toString();

       if (type.equals(unicNumber)) {


               return VIEW_TYPE_TEXT;



       } else {

               return VIEW_TYPE_TEXT2;


       }

       }







        // Inflates the appropriate layout according to the ViewType.
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;


               if( viewType==VIEW_TYPE_TEXT) {

                   view = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_layout, parent, false);
                   return new SentMessageHolder(view);

               }

                 if (viewType==VIEW_TYPE_TEXT2) {
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recieve_layout, parent, false);
                    return new ReceivedMessageHolder(view);
                }




                return null;
        }

        // Passes the message object to a ViewHolder so that the contents can be bound to UI.
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Manss message = (Manss) mMessageList.get(position);

            switch (holder.getItemViewType()) {
                case VIEW_TYPE_TEXT:
                    ((SentMessageHolder) holder).bind(message);
                    break;
                case VIEW_TYPE_TEXT2:
                    ((ReceivedMessageHolder) holder).bind(message);
                    break;



            }


        }






        private class SentMessageHolder extends RecyclerView.ViewHolder {
            TextView messageText, timeText;


            SentMessageHolder(View itemView) {
                super(itemView);


                messageText = (TextView) itemView.findViewById(R.id.message);
                timeText = (TextView) itemView.findViewById(R.id.time);
            }


            void bind(Manss message) {

                    String messagesd = message.getMessages().toString();
                    if(!TextUtils.isEmpty(messagesd)) {
                        messageText.setText(messagesd);
                    }


               timeText.setText(message.getTime());
            }
        }

        private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
            TextView messageText, timeText, nameText;


            ReceivedMessageHolder(View itemView) {
                super(itemView);

                messageText = (TextView) itemView.findViewById(R.id.message);
                  timeText = (TextView) itemView.findViewById(R.id.time);
            }

            void bind(Manss message) {

                String messagesd = message.getMessages().toString();
                if(!TextUtils.isEmpty(messagesd)) {
                    messageText.setText(messagesd);
                }

                timeText.setText(DateUtils.getRelativeTimeSpanString(Long.valueOf(message.getTime())));


            }
        }


}
