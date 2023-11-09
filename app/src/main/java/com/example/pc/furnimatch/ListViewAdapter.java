package com.example.pc.furnimatch;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;


class InsertFavoriteTask extends AsyncTask<String,Void,String> {
    String sendMsg, receiveMsg;
    @Override
    protected String doInBackground(String... strings) {
        try{
            String str;
            URL url = new URL("http://"+MainActivity.ip+":8000/furnimatch/insert_favorite_list.jsp");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

            sendMsg = "favorite_code="+strings[0]+"&id="+strings[1]+"&type="+strings[2];

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


class FindFavoriteCodeTask extends AsyncTask<String,Void,String> {
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

            sendMsg = "favorite_code="+strings[0]+"&id="+strings[1]+"&type="+strings[2];

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
        Log.e("결과",receiveMsg);
        return receiveMsg;
    }
}

public class ListViewAdapter extends BaseAdapter{

    //static ArrayList<String> click_heart_list;

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;

    int heart_press = 0;

    static ListViewItem favoriteListitem;
    private ArrayList<ListViewItem> listViewItemList ;

    public ListViewAdapter(Context context, ArrayList<ListViewItem> data)
    {
            mContext = context;
            listViewItemList = data;
            mLayoutInflater = LayoutInflater.from(mContext);


    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        View view = mLayoutInflater.inflate((R.layout.listview_furniture_item),null);

        ImageView furni_img =(ImageView) view.findViewById(R.id.funitureImage);
        TextView furni_name = (TextView)view.findViewById(R.id.furni_name);
        TextView furni_price = (TextView)view.findViewById(R.id.furni_price);
        TextView furni_add = (TextView) view.findViewById(R.id.furni_add);
        final ToggleButton heart_btn = (ToggleButton)view.findViewById(R.id.heart_btn);
        LinearLayout furni_list = (LinearLayout)view.findViewById(R.id.furni_list);



        Glide.with(mContext).load("http://"+MainActivity.ip+":8000/furnimatch/"+listViewItemList.get(position).getFurniture_img()).into(furni_img);


        furni_name.setText(listViewItemList.get(position).getFurni_name());
        furni_price.setText(listViewItemList.get(position).getFurni_price());
        furni_add.setText(listViewItemList.get(position).getFurni_add());

        furni_img.setClipToOutline(true);

        Log.e("카테고리",Mainpage.clicked_category);

        String name = furni_name.getText().toString();
        String price = furni_price.getText().toString();
        String addr = furni_add.getText().toString();
        String image = "";

        String f_code = name.substring(0,1)+addr.substring(0,1)+price.substring(0,1);

        heart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(heart_btn.isChecked())
                {
                    heart_btn.setBackgroundDrawable(view.getResources().getDrawable(R.drawable.heart_red));

                    Log.e("코드",f_code);
                    try{
                        String res = new InsertFavoriteTask().execute(f_code,MainActivity.id,"insert").get();

                        if(res.equals("ok"))
                        {
                            Toast toast = Toast.makeText(mContext, "관심상품 등록", Toast.LENGTH_SHORT);
                            toast.show();
                            Log.e("등록","성공");
                        } else if(res.equals("no"))
                        {
                            Log.e("등록","실패");
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        Log.e("전송 오류","전송 실패");
                    }

                }
                else{
                    heart_btn.setBackgroundDrawable(view.getResources().getDrawable(R.drawable.heart_empty));

                   try{
                       String res = new InsertFavoriteTask().execute(f_code,MainActivity.id,"delete").get();

                       if(res.equals("ok"))
                       {
                           Toast toast = Toast.makeText(mContext, "관심상품 해제", Toast.LENGTH_SHORT);
                           toast.show();
                           Log.e("삭제","성공");
                       } else if(res.equals("no"))
                       {
                           Log.e("삭제","실패");
                       }
                   }catch (Exception e)
                   {
                       e.printStackTrace();
                       Log.e("전송 오류","전송 실패");
                   }

                }
            }
        });

        return view;
    }


}
