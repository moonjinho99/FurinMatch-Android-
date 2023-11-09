package com.example.pc.furnimatch;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ARListViewAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;

    private ArrayList<ListViewItem> arItemList;

    public ARListViewAdapter(Context context, ArrayList<ListViewItem> data)
    {
        mContext = context;
        arItemList = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return arItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return arItemList.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate((R.layout.ar_list_item),null);
        ImageView ar_img =(ImageView) view.findViewById(R.id.ar_list_Image);
        TextView ar_title = (TextView)view.findViewById(R.id.ar_list_title);
        TextView ar_price = (TextView)view.findViewById(R.id.ar_list_price);
        TextView ar_addr = (TextView) view.findViewById(R.id.ar_list_add);
        Button move_product_inf = (Button)view.findViewById(R.id.move_product_inf);
        Button move_ar = (Button)view.findViewById(R.id.move_ar);


        Glide.with(mContext).load("http://"+MainActivity.ip+":8000/furnimatch/"+arItemList.get(position).getFurniture_img()).into(ar_img);

        ar_title.setText(arItemList.get(position).getFurni_name());
        ar_price.setText(arItemList.get(position).getFurni_price());
        ar_addr.setText(arItemList.get(position).getFurni_add());


        move_product_inf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "물품정보 화면으로 이동", Toast.LENGTH_SHORT).show();
                String code = ar_title.getText().toString().substring(0, 1) + ar_addr.getText().toString().substring(0, 1)
                        + ar_price.getText().toString().substring(0, 1);
                Intent intent = new Intent(mContext, furniture_information.class);
                intent.putExtra("code", code);
                mContext.startActivity(intent);
            }
        });

        move_ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "AR화면으로 이동", Toast.LENGTH_SHORT).show();
                String code = ar_title.getText().toString().substring(0, 1) + ar_addr.getText().toString().substring(0, 1)
                        + ar_price.getText().toString().substring(0, 1);
                String armodel="";
                try{
                    armodel = new GetARTask().execute(code).get().trim();
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                furni_ar_view.name = armodel;
                Intent intent = new Intent(mContext, furni_ar_view.class);
                mContext.startActivity(intent);
            }
        });

        return view;
    }
}
