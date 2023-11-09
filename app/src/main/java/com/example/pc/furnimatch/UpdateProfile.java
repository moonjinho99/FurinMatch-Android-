package com.example.pc.furnimatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


class ProfileUpdateImgTasks extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
        String apiUrl = "http://"+MainActivity.ip+":8000/furnimatch/update_profile_img.jsp";
        String img = params[0];
        String id = params[1];
        HttpFileUpload(apiUrl, "", img,id);

        return null;
    }

    private void HttpFileUpload(String urlString, String params, String img, String id) {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            File sourceFile1 = new File(img);


            DataOutputStream dos;
            if (!sourceFile1.isFile()) {
                Log.e("uploadFile1", "Source File not exist :" + img);
            } else {
                FileInputStream mFileInputStream1 = new FileInputStream(sourceFile1);

                URL connectUrl = new URL(urlString);
                // open connection
                HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("Charset", "UTF-8");
                conn.setRequestProperty("uploaded_file1", img);

                // 이미지
                dos = new DataOutputStream(conn.getOutputStream());
                OutputStreamWriter writer = new OutputStreamWriter(dos, "UTF-8");

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + img + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                int bytesAvailable1 = mFileInputStream1.available();
                int maxBufferSize1 = 1024 * 1024;
                int bufferSize1 = Math.min(bytesAvailable1, maxBufferSize1);
                byte[] buffer1 = new byte[bufferSize1];
                int bytesRead1 = mFileInputStream1.read(buffer1, 0, bufferSize1);
                // read image
                while (bytesRead1 > 0) {
                    dos.write(buffer1, 0, bufferSize1);
                    bytesAvailable1 = mFileInputStream1.available();
                    bufferSize1 = Math.min(bytesAvailable1, maxBufferSize1);
                    bytesRead1 = mFileInputStream1.read(buffer1, 0, bufferSize1);
                }
                dos.writeBytes(lineEnd);

                //아이디
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"id\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.write(id.getBytes("UTF-8"));
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                mFileInputStream1.close();
                dos.flush(); // finish upload...
                if (conn.getResponseCode() == 200) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer stringBuffer = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                }
                mFileInputStream1.close();
                dos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class UserInf extends AsyncTask<String,Void,String> {
    String sendMsg, receiveMsg;
    @Override
    protected String doInBackground(String... strings) {
        try{
            String str;
            URL url = new URL("http://"+MainActivity.ip+":8000/furnimatch/find_user_inf.jsp");
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

        return receiveMsg;
    }
}

class UpdateUserTask extends AsyncTask<String,Void,String> {
    String sendMsg, receiveMsg;
    @Override
    protected String doInBackground(String... strings) {
        try{
            String str;
            URL url = new URL("http://"+MainActivity.ip+":8000/furnimatch/update_user_inf.jsp");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            if(strings[2].equals("username"))
            {
                sendMsg = "username="+strings[0]+"&id="+strings[1]+"&type="+strings[2];
            }
            else if(strings[2].equals("phonenum"))
            {
                sendMsg = "phonenum="+strings[0]+"&id="+strings[1]+"&type="+strings[2];
            }
            else if(strings[2].equals("password"))
            {
                sendMsg = "password="+strings[0]+"&id="+strings[1]+"&type="+strings[2];
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

public class UpdateProfile extends AppCompatActivity {

    Uri uri;

    String img_path="";

    static String attribute = "";

    int check_pw=0;

    ImageView update_profileimg,back_btn;

    TextView update_userid, update_username, update_userphonenum;

    Button update_username_btn, update_userphonenum_btn,update_userpw,update_profileimg_btn;

    String user_inf[];

    String name="";
    String id="";
    String phonenum="";
    String password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        update_profileimg = (ImageView) findViewById(R.id.update_profileimg);
        update_profileimg_btn = (Button)findViewById(R.id.update_profile_img_btn);

        update_userid = (TextView) findViewById(R.id.update_userid);
        update_username = (TextView) findViewById(R.id.update_profile_username_txt);
        update_userphonenum = (TextView) findViewById(R.id.update_profile_phonnum_txt);

        update_username_btn = (Button) findViewById(R.id.update_profile_username_update_btn);
        update_userphonenum_btn = (Button) findViewById(R.id.update_profile_phonenum_update_btn);
        update_userpw = (Button) findViewById(R.id.update_profile_password_btn);

        back_btn = (ImageButton) findViewById(R.id.back_btn);

        if(Mainpage.profile_img.contains("null"))
        {
            update_profileimg.setImageResource(R.drawable.user_profile);
        }else{
            Glide.with(this).load("http://"+MainActivity.ip+":8000/furnimatch/"+Mainpage.profile_img).into(update_profileimg);

        }


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateProfile.this,Mainpage.class);
                startActivity(intent);
            }
        });

        update_profileimg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });


        try{
            user_inf = (new UserInf().execute(MainActivity.id).get()).split("#");

            name = user_inf[0];
            id= user_inf[1];
            phonenum = user_inf[2];
            password = user_inf[3];

            update_userid.setText(id);
            update_username.setText(name.trim());
            update_userphonenum.setText(phonenum.trim());

        }catch (Exception e) {}

       update_username_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               attribute = "name";
               LinearLayout linear = (LinearLayout) View.inflate(UpdateProfile.this, R.layout.update_dialog_name_phonenum, null);
               EditText update_name = (EditText) linear.findViewById(R.id.update_edt);
               TextView dialog_update_inf = (TextView) linear.findViewById(R.id.dialog_update_inf);
               TextView update_title = (TextView) linear.findViewById(R.id.update_title);
               update_title.setText("사용자명 변경");
               dialog_update_inf.setText("변경할 사용자명 입력");
               update_name.setHint("변경할 사용자명");
               new AlertDialog.Builder(UpdateProfile.this)
                       .setView(linear)
                       .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int whichButton) {
                               dialog.dismiss();
                           }
                       })
                       .setPositiveButton("변경", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int whichButton) {
                                   String value = update_name.getText().toString();
                                   String res="";
                               try{
                                   res = new UpdateUserTask().execute(value,MainActivity.id,"username").get();
                               }catch(Exception e){}

                               if(res.equals("ok"))
                               {
                                   Toast.makeText(getApplicationContext(),"사용자명이 변경되었습니다.",Toast.LENGTH_SHORT).show();
                                   recreate();

                               }
                               else{
                                   Toast.makeText(getApplicationContext(),"변경중에 오류가 발생했습니다.",Toast.LENGTH_SHORT).show();
                               }

                              // ((Mainpage)Mainpage.con).recreate();
                               dialog.dismiss();
                           }
                       })
                       .show();
           }
       });

       update_userphonenum_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //사용자 핸드폰 번호 갱신
               attribute = "phonenum";
               LinearLayout linear = (LinearLayout) View.inflate(UpdateProfile.this, R.layout.update_dialog_name_phonenum, null);
               EditText update_phone = (EditText) linear.findViewById(R.id.update_edt);
               TextView dialog_update_inf = (TextView) linear.findViewById(R.id.dialog_update_inf);
               TextView update_title = (TextView) linear.findViewById(R.id.update_title);
               update_title.setText("전화번호 변경");
               dialog_update_inf.setText("변경할 전화번호 입력");
               update_phone.setHint("변경할 전화번호(-없이 입력해주세요)");


               new AlertDialog.Builder(UpdateProfile.this)
                       .setView(linear)
                       .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int whichButton) {
                               dialog.dismiss();
                           }
                       })

                       .setPositiveButton("변경", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int whichButton) {
                               String value = update_phone.getText().toString();
                               String res="";
                               try{
                                   res = new UpdateUserTask().execute(value,MainActivity.id,"phonenum").get();
                               }catch(Exception e){}

                               if(res.equals("ok"))
                               {
                                   Toast.makeText(getApplicationContext(),"핸드폰 번호가 변경되었습니다.",Toast.LENGTH_SHORT).show();
                                   recreate();

                               }
                               else{
                                   Toast.makeText(getApplicationContext(),"변경중에 오류가 발생했습니다.",Toast.LENGTH_SHORT).show();
                               }

                               //((Mainpage)Mainpage.con).recreate();
                               dialog.dismiss();
                           }
                       })
                       .show();

           }
       });

       update_userpw.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //사용자 패스워드 변경
               attribute = "pw";
               LinearLayout linear = (LinearLayout) View.inflate(UpdateProfile.this, R.layout.update_dialog, null);
               EditText update_pw = (EditText) linear.findViewById(R.id.update_edt);
               TextView dialog_update_inf = (TextView) linear.findViewById(R.id.dialog_update_inf);
               EditText dialog_pw = (EditText) linear.findViewById(R.id.update_pw_edt);
               Button check_btn = (Button) linear.findViewById(R.id.check_btn);
               TextView update_check_msg = (TextView) linear.findViewById(R.id.update_check_pw_msg);
               TextView update_title = (TextView) linear.findViewById(R.id.update_title);
               update_title.setText("비밀번호 변경");
               dialog_update_inf.setText("변경할 비밀번호 입력");
               update_pw.setHint("변경할 비밀번호");
               new AlertDialog.Builder(UpdateProfile.this)
                       .setView(linear)
                       .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int whichButton) {
                               dialog.dismiss();
                           }
                       })
                       .setPositiveButton("변경", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int whichButton) {
                               String value = update_pw.getText().toString();
                               String res="";
                               try{
                                   res = new UpdateUserTask().execute(value,MainActivity.id,"password").get();
                               }catch(Exception e){}

                               if(res.equals("ok"))
                               {
                                   Toast.makeText(getApplicationContext(),"비밀번호가 변경되었습니다.",Toast.LENGTH_SHORT).show();
                                   recreate();

                               }
                               else{
                                   Toast.makeText(getApplicationContext(),"변경중에 오류가 발생했습니다.",Toast.LENGTH_SHORT).show();
                               }

                               //((Mainpage)Mainpage.con).recreate();
                               dialog.dismiss();
                           }
                       })

                       .show();

               check_btn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       if(dialog_pw.getText().toString().equals(password))
                       {
                           check_btn.setEnabled(false);
                           update_check_msg.setText("비밀번호가 일치합니다");
                           update_check_msg.setTextColor(Color.GREEN);
                           check_pw=1;
                       }
                       else{
                           update_check_msg.setText("비밀번호가 일치하지 않습니다");
                           update_check_msg.setTextColor(Color.RED);
                           check_pw=0;
                       }
                   }
               });
           }
       });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UpdateProfile.this,Mainpage.class);
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        uri = data.getData();
                        update_profileimg.setImageURI(uri);
                        img_path = getRealPathFromURI(uri);

                        try{
                            new ProfileUpdateImgTasks().execute(img_path, MainActivity.id);
                            Toast.makeText(getApplicationContext(),"프로필 이미지가 변경되었습니다.",Toast.LENGTH_SHORT).show();
                        }catch(Exception e)
                        {

                        }
                    }
                }
                break;
        }
    }

    //실제 경로
    private String getRealPathFromURI(Uri contentURI) {

        String result;

        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);

        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}
