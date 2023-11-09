package com.example.pc.furnimatch;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


class CustomTask extends AsyncTask<String,Void,String> {
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
           if(strings.length == 2)
            {
                sendMsg = "id="+strings[0]+"&type="+strings[1];
            }
            else {
                sendMsg = "id="+strings[0]+"&pwd="+strings[1]+"&name="+strings[2]+"&phonenum="+strings[3]+"&type="+strings[4];
            }


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


public class Memberin extends AppCompatActivity {



    int passpw = 0;
    int passid = 0;


    EditText idEditText;
    EditText pwEditText;
    EditText nameEditText;
    EditText phoneEditText;
    EditText chkpw;

    TextView chkid_txt;
    TextView chkpw_txt;

    Button btnJoin,chkbtn,chkidbtn;

    String sql;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memberin);

        idEditText = (EditText) findViewById(R.id.input_newid);
        pwEditText = (EditText) findViewById(R.id.input_newpw);
        nameEditText = (EditText) findViewById(R.id.input_username);
        phoneEditText = (EditText) findViewById(R.id.input_userphone);
        chkpw = (EditText) findViewById(R.id.input_chkpw);

        btnJoin = (Button) findViewById(R.id.memberjoin);
        chkbtn =(Button) findViewById(R.id.chkbtn);
        chkidbtn = (Button) findViewById(R.id.chk_reid);



        chkid_txt = (TextView) findViewById(R.id.checkid_txt);
        chkpw_txt = (TextView) findViewById(R.id.checkpw_txt);


        //아이디 중복확인
        chkidbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = idEditText.getText().toString();



                try{
                    String result = new CustomTask().execute(id,"checkid").get();

                    if(result.equals("ok"))
                    {
                        passid = 1;
                        chkid_txt.setTextColor(Color.GREEN);
                        chkid_txt.setText("사용가능한 아이디 입니다.");
                        chkidbtn.setEnabled(false);
                    }
                    else {
                        chkid_txt.setTextColor(Color.RED);
                        chkid_txt.setText("존재하는 아이디 입니다.");
                        passid = 0;
                    }

                }catch(Exception e)
                {

                }

            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = idEditText.getText().toString();
                String pw = pwEditText.getText().toString();
                String name =nameEditText.getText().toString();
                String phonenum = phoneEditText.getText().toString();

                if(id.length() == 0 || pw.length() == 0 || name.length() == 0)
                {
                    Toast toast = Toast.makeText(Memberin.this, "아이디, 비밀번호, 사용자 이름은 필수 입력사항 입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                else{
                    if(passid == 1 && passpw == 1)
                    {
                        try{
                            String result = new CustomTask().execute(id,pw,name,phonenum,"join").get();
                            if(result.equals("joinok"))
                            {
                                Toast toast = Toast.makeText(Memberin.this,"가입이 완료되었습니다. 로그인 해주세요.", Toast.LENGTH_SHORT);
                                toast.show();
                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                Toast toast = Toast.makeText(Memberin.this,"아이디, 비밀번호, 사용자명, 이메일이 형식에 맞는지 확인해주세요.", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }catch(Exception e){}

                    } else if(passid != 1 && passpw == 1)
                    {
                        Toast toast = Toast.makeText(Memberin.this,"아이디 중복확인을 해주세요.", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if(passid == 1 && passpw != 1)
                    {
                        Toast toast = Toast.makeText(Memberin.this,"비밀번호 확인을 해주세요.", Toast.LENGTH_SHORT);
                        toast.show();
                    } else
                    {
                        Toast toast = Toast.makeText(Memberin.this,"아이디와 비밀번호 확인을 해주세요.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

            }
        });

        chkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkpw.getText().toString().equals(pwEditText.getText().toString()) && !chkpw.getText().toString().equals(""))
                {
                    chkpw_txt.setTextColor(Color.GREEN);
                    chkpw_txt.setText("비밀번호가 확인되었습니다.");
                    passpw=1;
                    chkbtn.setEnabled(false);
                }
                else if(pwEditText.getText().toString().length() == 0 || chkpw.getText().toString().length() == 0 )
                {
                    chkpw_txt.setTextColor(Color.RED);
                    chkpw_txt.setText("비밀번호를 입력해주세요.");
                    passpw = 0;
                }
                else
                {
                    chkpw_txt.setTextColor(Color.RED);
                    chkpw_txt.setText("입력한 비밀번호와 같지 않습니다. 다시 입력해주세요.");
                    passpw=0;
                }
            }
        });

    }
}
