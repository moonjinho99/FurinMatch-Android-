package com.example.pc.furnimatch;



import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


class DeleteSellListTask extends AsyncTask<String,Void,String> {
    String sendMsg, receiveMsg;
    @Override
    protected String doInBackground(String... strings) {
        try{
            String str;
            URL url = new URL("http://"+MainActivity.ip+":8000/furnimatch/delete_sell_list.jsp");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

            sendMsg = "sell_code="+strings[0]+"&id="+strings[1];

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

public class SellListViewAdapter extends BaseAdapter{

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;

    private ArrayList<ListViewItem> sellItemList ;

    public SellListViewAdapter(Context context, ArrayList<ListViewItem> data)
    {
        mContext = context;
        sellItemList = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sellItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return sellItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate((R.layout.sell_list_item),null);
        ImageView sell_img =(ImageView) view.findViewById(R.id.sell_list_Image);
        TextView sell_title = (TextView)view.findViewById(R.id.sell_list_title);
        TextView sell_price = (TextView)view.findViewById(R.id.sell_list_price);
        TextView sell_addr = (TextView) view.findViewById(R.id.sell_list_add);
        ImageButton sell_delete = (ImageButton)view.findViewById(R.id.sell_list_delete);
        LinearLayout sell_list = (LinearLayout)view.findViewById(R.id.sell_list);

        Glide.with(mContext).load("http://"+MainActivity.ip+":8000/furnimatch/"+sellItemList.get(position).getFurniture_img()).into(sell_img);

        sell_title.setText(sellItemList.get(position).getFurni_name());
        sell_price.setText(sellItemList.get(position).getFurni_price());
        sell_addr.setText(sellItemList.get(position).getFurni_add());


        sell_delete.requestFocus();
        sell_delete.setFocusable(false);

        String code = sell_title.getText().toString().substring(0,1)+sell_addr.getText().toString().substring(0,1)+sell_price.getText().toString().substring(0,1);


        sell_delete.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                DialogInterface.OnClickListener confirm = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try{

                            new DeleteSellListTask().execute(code,MainActivity.id).get();
                            sellItemList.clear();
                            Intent intent = ((Activity)mContext).getIntent();
                            ((Activity)mContext).finish();
                            mContext.startActivity(intent);
                            ((Activity)mContext).recreate();

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        Toast.makeText(mContext,"등록된 매물이 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                    }
                };

                DialogInterface.OnClickListener cancle = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(mContext,"삭제를 취소 하셨습니다.",Toast.LENGTH_SHORT).show();
                    }
                };

                new AlertDialog.Builder(mContext)
                        .setTitle("등록하신 매물을 삭제 하시겠습니까?")
                        .setPositiveButton("삭제",confirm)
                        .setNegativeButton("취소",cancle)
                        .show();
                Log.e("삭제코드",code);
            }
        });


        return view;
    }

}

