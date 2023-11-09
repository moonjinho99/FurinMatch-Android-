package com.example.pc.furnimatch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

class FindFavoriteListTask extends AsyncTask<String,Void,String> {
    String sendMsg, receiveMsg;
    @Override
    protected String doInBackground(String... strings) {
        try{
            String str;
            URL url = new URL("http://"+MainActivity.ip+":8000/furnimatch/find_favorite_list.jsp");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

            sendMsg = "id="+strings[0]+"&type="+strings[1];

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

public class Favorite_list extends AppCompatActivity {

    static ArrayList<ListViewItem> favorite_data_list = new ArrayList<ListViewItem>();
    private FavoriteListViewAdapter favorite_adapter;
    private ListView favorite_list;
    static int cnt=0;
    String favoriteItems[];
    String favorite_str="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        favorite_list = (ListView) findViewById(R.id.favorite_listview);


        favorite_adapter = new FavoriteListViewAdapter(this,favorite_data_list);
        favorite_list.setAdapter(favorite_adapter);




        try{
            favorite_str = new FindFavoriteListTask().execute(MainActivity.id,"findfavorite").get();
            favoriteItems = favorite_str.split("#");
            Log.e("값 전송","성공");
        } catch(Exception e)
        {

        }



        favorite_data_list.clear();
        if(!(favorite_str.trim().equals("")))
        {

            for(int i=0; i<favoriteItems.length; i+=4)
            {
                String title = favoriteItems[i].trim();
                String addr = favoriteItems[i+1];
                String price = favoriteItems[i+2]+"원";
                String img = favoriteItems[i+3];

                favorite_data_list.add(new ListViewItem(img,title,price,addr));

            }

        }

        favorite_adapter.notifyDataSetChanged();



        favorite_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView favorite_title = (TextView) findViewById(R.id.favorite_name);
                TextView favorite_addr=(TextView)findViewById(R.id.favorite_add);
                TextView favorite_price=(TextView) findViewById(R.id.favorite_price);

                String code  = favorite_data_list.get(i).getFurni_name().substring(0,1)+favorite_data_list.get(i).getFurni_add().substring(0,1)
                        +favorite_data_list.get(i).getFurni_price().substring(0,1);
                Intent intent = new Intent(Favorite_list.this,furniture_information.class);
                intent.putExtra("code",code);
                startActivity(intent);

            }
        });



    }


}
