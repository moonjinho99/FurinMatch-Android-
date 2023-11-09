package com.example.pc.furnimatch;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class LoginCustomTask extends AsyncTask<String,Void,String> {
    String sendMsg, receiveMsg;
    @Override
    protected String doInBackground(String... strings) {
        try{
            String str;
            URL url = new URL("http://"+MainActivity.ip+":8000/furnimatch/join_login.jsp");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            sendMsg = "id="+strings[0]+"&pwd="+strings[1]+"&type="+strings[2];
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

        return receiveMsg;
    }
}

public class MainActivity extends AppCompatActivity {

    public static String ip ="192.168.0.16";

    ImageView back_img;

    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase database;

    EditText idEditText,pwEditText;

    static String id;

    String sql;
    Cursor cursor;

    Button login_btn,memberin_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_btn = (Button) findViewById(R.id.loginbtn);
        memberin_btn = (Button) findViewById(R.id.memberinbtn);

        idEditText = (EditText) findViewById(R.id.id_edt);
        pwEditText = (EditText) findViewById(R.id.pw_edt);

        back_img = (ImageView)findViewById(R.id.main_back_img);

        GlideDrawableImageViewTarget gifimage = new GlideDrawableImageViewTarget(back_img);
        Glide.with(this).load(R.drawable.main_page).into(back_img);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = idEditText.getText().toString();
                String pw = pwEditText.getText().toString();

                if (id.length() == 0 || pw.length() == 0)
                {
                    Toast toast = Toast.makeText(MainActivity.this,"아이디와 비밀번호는 필수 입력사항입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                try{
                    String result = new LoginCustomTask().execute(id,pw,"login").get();

                    if(result.equals("success"))
                    {
                        Toast toast = Toast.makeText(MainActivity.this,"로그인 성공", Toast.LENGTH_SHORT);
                        toast.show();

                        Intent intent = new Intent(MainActivity.this,Mainpage.class);
                        startActivity(intent);

                        //finish();
                    }
                    else
                    {
                        Toast toast = Toast.makeText(MainActivity.this, "아이디 또는 비밀번호가 존재하지 않습니다.", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }catch (Exception e){}



            }

        });


        memberin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast toast = Toast.makeText(MainActivity.this, "회원가입 화면으로 이동", Toast.LENGTH_SHORT);
                toast.show();
                Intent intent = new Intent(getApplicationContext(),Memberin.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onBackPressed() {
        finishAffinity();
        System.runFinalization();
        System.exit(0);

    }
}
