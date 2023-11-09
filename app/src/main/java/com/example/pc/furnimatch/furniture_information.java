package com.example.pc.furnimatch;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

import me.relex.circleindicator.CircleIndicator;
class GetARTask extends AsyncTask<String,Void,String> {
    String sendMsg, receiveMsg;
    @Override
    protected String doInBackground(String... strings) {
        try{
            String str;
            URL url = new URL("http://"+MainActivity.ip+":8000/furnimatch/get_ar.jsp");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            sendMsg = "code="+strings[0];
            osw.write(sendMsg);
            Log.i("ar코드값 : ",sendMsg);
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
        Log.e("모델명",receiveMsg);
        return receiveMsg;
    }
}

class ProductProfileImg extends AsyncTask<String,Void,String> {
    String sendMsg, receiveMsg;
    @Override
    protected String doInBackground(String... strings) {
        try{
            String str;
            URL url = new URL("http://"+MainActivity.ip+":8000/furnimatch/product_inf_profileimg.jsp");
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
        Log.e("이름",receiveMsg);
        return receiveMsg;
    }
}

class ProductInfTask extends AsyncTask<String,Void,String> {
    String sendMsg, receiveMsg;
    @Override
    protected String doInBackground(String... strings) {
        try{
            String str;
            URL url = new URL("http://"+MainActivity.ip+":8000/furnimatch/product_information.jsp");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            sendMsg = "code="+strings[0];
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

        Log.e("물품정보 값: ",receiveMsg);
        return receiveMsg;
    }
}

public class furniture_information extends AppCompatActivity {


    static String chat_seller_name;
    String product_inf_items[];
    String armodel="";
    String code="";
    ImageButton ar_btn;
    ViewPager viewPager;
    String seller_id="";

    ImageView seller_profile_image;
    TextView furni_sell_title, product_width, product_length, product_height, product_content, product_price, seller_name, seller_add,product_category;

    Button seller_chat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furniture_information);



        seller_name = (TextView) findViewById(R.id.sellername);
        seller_add = (TextView) findViewById(R.id.seller_add);
        furni_sell_title = (TextView) findViewById(R.id.furni_sell_title);
        product_length = (TextView) findViewById(R.id.product_length);
        product_width = (TextView) findViewById(R.id.product_width);
        product_height = (TextView) findViewById(R.id.product_height);
        product_content = (TextView) findViewById(R.id.product_content);
        product_price = (TextView) findViewById(R.id.product_price);
        seller_chat = (Button) findViewById(R.id.seller_chat);
        ar_btn = (ImageButton) findViewById(R.id.ar_btn);
        viewPager = findViewById(R.id.view_pager);
        product_category = (TextView)findViewById(R.id.product_inf_category);
        seller_profile_image = (ImageView)findViewById(R.id.seller_profile_image);

        Intent codeintent = getIntent();
        String gcode = codeintent.getStringExtra("code");

        try{
           product_inf_items =  (new ProductInfTask().execute(gcode).get()).split("#");
           seller_name.setText(product_inf_items[0].trim());
           seller_add.setText(product_inf_items[1]);
           furni_sell_title.setText(product_inf_items[2]);
           product_width.setText(product_inf_items[3]);
           product_length.setText(product_inf_items[4]);
           product_height.setText(product_inf_items[5]);
           product_content.setText(product_inf_items[6]);
           product_price.setText(product_inf_items[7]+"원");

           ImagePagerAdapter adapter = new ImagePagerAdapter(this,
                   new String[]{"http://"+MainActivity.ip+":8000/furnimatch/"+product_inf_items[8],
                           "http://"+MainActivity.ip+":8000/furnimatch/"+product_inf_items[9],
                           "http://"+MainActivity.ip+":8000/furnimatch/"+product_inf_items[10],
                           "http://"+MainActivity.ip+":8000/furnimatch/"+product_inf_items[11]});
           viewPager.setAdapter(adapter);

            product_category.setText(product_inf_items[12]);
            seller_id = product_inf_items[13];
        } catch (Exception e) {}

        try{

            String img = new ProductProfileImg().execute(seller_id).get().trim();
            Log.e("물품정보 이미지",img);

            if(img.contains("null"))
            {
                seller_profile_image.setImageResource(R.drawable.user_profile);
            }else{
                Glide.with(this).load("http://"+MainActivity.ip+":8000/furnimatch/"+img).into(seller_profile_image);

            }

        } catch(Exception e)
        {

        }


        Intent intent;
        intent = getIntent();

        code = furni_sell_title.getText().toString().substring(0,1)+seller_add.getText().toString().substring(0,1)+product_price.getText().toString().substring(0,1);

        try{
            armodel = new GetARTask().execute(code).get().trim();
        }catch(Exception e){}

        Log.e("모델값:",armodel);
        ar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(armodel.equals("null"))
                {
                    Toast.makeText(getApplicationContext(),"아직 AR모델이 추가되지 않았습니다",Toast.LENGTH_SHORT).show();
                } else{
                    furni_ar_view.name=armodel;
                    Intent intent01 = new Intent(furniture_information.this,furni_ar_view.class);
                    startActivity(intent01);
                }

            }
        });


        seller_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent02 = new Intent(furniture_information.this,Chatting.class);
                startActivity(intent02);
            }
        });

        chat_seller_name = seller_name.getText().toString();
    }

}
