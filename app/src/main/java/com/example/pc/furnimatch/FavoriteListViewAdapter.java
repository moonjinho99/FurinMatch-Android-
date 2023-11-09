package com.example.pc.furnimatch;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.support.graphics.drawable.VectorDrawableCompat;
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

import java.util.ArrayList;

public class FavoriteListViewAdapter extends BaseAdapter{

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;

    int heart_press = 0;

    private ArrayList<ListViewItem> favoriteItemList ;


    public FavoriteListViewAdapter(Context context, ArrayList<ListViewItem> data)
    {
            mContext = context;
            favoriteItemList = data;
            mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return favoriteItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return favoriteItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate((R.layout.listview_favorite_item),null);


        ImageView favorite_img =(ImageView) view.findViewById(R.id.favoriteImage);
        TextView favorite_name = (TextView)view.findViewById(R.id.favorite_name);
        TextView favorite_price = (TextView)view.findViewById(R.id.favorite_price);
        TextView favorite_add = (TextView) view.findViewById(R.id.favorite_add);
        LinearLayout favorite_list = (LinearLayout)view.findViewById(R.id.favorite_list);

        Glide.with(mContext).load("http://"+MainActivity.ip+":8000/furnimatch/"+favoriteItemList.get(position).getFurniture_img()).into(favorite_img);


        favorite_name.setText(favoriteItemList.get(position).getFurni_name());
        favorite_price.setText(favoriteItemList.get(position).getFurni_price());
        favorite_add.setText(favoriteItemList.get(position).getFurni_add());


        return view;
    }

}
