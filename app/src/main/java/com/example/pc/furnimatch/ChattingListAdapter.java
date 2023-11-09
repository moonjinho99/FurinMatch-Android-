package com.example.pc.furnimatch;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class ChattingListAdapter extends BaseAdapter{

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;

    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase database;

    String sql;
    Cursor cursor;


    private ArrayList<ChattingItem> chatmsgList ;


    public ChattingListAdapter(Context context, ArrayList<ChattingItem> data)
    {
        mContext = context;
        chatmsgList = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return chatmsgList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatmsgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate((R.layout.chatting_msg_list),null);


        ConstraintLayout chat_list_layout = (ConstraintLayout)view.findViewById(R.id.chat_list_layout);
        TextView selller_chat_msg_txt = (TextView)view.findViewById(R.id.seller_chat_msg_txt);
        TextView my_chat_msg_txt = (TextView)view.findViewById(R.id.my_chat_msg_txt);


        if(chatmsgList.get(position).getChat_msg().contains(Mainpage.username))
        {
            selller_chat_msg_txt.setVisibility(View.INVISIBLE);
            my_chat_msg_txt.setVisibility(View.VISIBLE);
            my_chat_msg_txt.setText(chatmsgList.get(position).getChat_msg());

        }
        else{
            selller_chat_msg_txt.setVisibility(View.VISIBLE);
            my_chat_msg_txt.setVisibility(View.INVISIBLE);
            selller_chat_msg_txt.setText( chatmsgList.get(position).getChat_msg());
        }
        return view;
    }

}
