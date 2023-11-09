package com.example.pc.furnimatch;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.Buffer;
import java.util.ArrayList;

public class Chatting extends AppCompatActivity {
    private Handler mHandler;
    InetAddress serverAddr;
    Socket socket;
    PrintWriter sendWriter;
    private String ip = MainActivity.ip;
    private int port = 8888;

    TextView chat_room_title;
    ListView chat_listView;
    ChattingListAdapter chat_listAdapter;
    ArrayList<ChattingItem> chat_arrayList;

    String sendmsg;
    String read;

    EditText chat_msg;
    Button chat_trans;

    @Override
    protected void onStop() {
        super.onStop();
        try {
            sendWriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        mHandler = new Handler();
        chat_room_title = (TextView) findViewById(R.id.chat_room_title);
        chat_listView = (ListView) findViewById(R.id.chatting_listView);
        chat_arrayList = new ArrayList<ChattingItem>();
        chat_listAdapter = new ChattingListAdapter(this,chat_arrayList);
        chat_listView.setAdapter(chat_listAdapter);

        chat_msg = (EditText) findViewById(R.id.chat_msg);
        chat_trans = (Button) findViewById(R.id.chat_trans);

        String username = Mainpage.username;


        new Thread() {
            public void run() {
                try {
                    InetAddress serverAddr = InetAddress.getByName(ip);
                    socket = new Socket(serverAddr, port);
                    sendWriter = new PrintWriter(socket.getOutputStream());
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while(true){
                        read = input.readLine();

                        System.out.println("TTTTTTTT"+read);
                        if(read!=null){
                            mHandler.post(new msgUpdate(read));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } }}.start();

        chat_trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmsg = chat_msg.getText().toString();
                chat_msg.setText("");
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            sendWriter.println("  "+username +" : "+ sendmsg);
                            //sendWriter.println(sendmsg);
                            sendWriter.flush();
                            chat_msg.setText("");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }

    class msgUpdate implements Runnable{
        private String msg;
        public msgUpdate(String str) {this.msg=str;}

        @Override
        public void run() {
            //chat_view.setText(chat_view.getText().toString()+msg+"\n\n");
            chat_listAdapter.notifyDataSetChanged();
            chat_arrayList.add(new ChattingItem(msg));
        }
    }
}
