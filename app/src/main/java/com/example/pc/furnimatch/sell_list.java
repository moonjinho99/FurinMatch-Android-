package com.example.pc.furnimatch;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

class FindSellListTask extends AsyncTask<String,Void,String> {
    String sendMsg, receiveMsg;
    @Override
    protected String doInBackground(String... strings) {
        try{
            String str;
            URL url = new URL("http://"+MainActivity.ip+":8000/furnimatch/find_sell_list.jsp");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

            sendMsg = "id="+strings[0];

            osw.write(sendMsg);
            Log.i("값 : ",sendMsg);
            osw.flush();
            if(conn.getResponseCode() == conn.HTTP_OK) {
                Log.i("연결 ","연결은 됨");
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(),"UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while((str = reader.readLine()) != null)
                {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
            } else {
                Log.i("통신결과",conn.getResponseCode()+"에러");
                Log.i("통신오류",conn.getErrorStream().toString());

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        Log.e("관심목록값",receiveMsg);
        return receiveMsg;
    }
}

public class sell_list extends AppCompatActivity {

    static ArrayList<ListViewItem> sell_data_list = new ArrayList<ListViewItem>();
    ListView sell_list;
    SellListViewAdapter sell_adapter;
    String sellitems[];
    String sell_str="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_list);

        sell_list = (ListView) findViewById(R.id.sell_listview);


        try{
            sell_str = new FindSellListTask().execute(MainActivity.id).get();
            sellitems = (sell_str).split("#");
        }catch (Exception e)
        {

        }

        sell_adapter = new SellListViewAdapter(this,sell_data_list);
        sell_list.setAdapter(sell_adapter);


        if(!(sell_str.trim().equals("")))
        {
            sell_data_list.clear();
            for(int i=0; i<sellitems.length; i+=4)
            {
                String title = sellitems[i].trim();
                String addr = sellitems[i+1];
                String price = sellitems[i+2]+"원";
                String img = sellitems[i+3];

                sell_data_list.add(new ListViewItem(img,title,price,addr));
            }
            sell_adapter.notifyDataSetChanged();
        }


        sell_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(sell_list.this,furniture_information.class);
                String code = sell_data_list.get(i).getFurni_name().substring(0,1)+sell_data_list.get(i).getFurni_add().substring(0,1)+sell_data_list.get(i).getFurni_price().substring(0,1);
                intent.putExtra("code",code);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(sell_list.this,Mainpage.class);
        startActivity(intent);

    }
}
