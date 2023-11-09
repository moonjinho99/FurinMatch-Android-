package com.example.pc.furnimatch;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImagePagerAdapter extends PagerAdapter {

    private Context mContext;
   // private int[] mImageIds;
    private  String[] ImageURI;

    public ImagePagerAdapter(Context context, String[] imageuri) {
        mContext = context;
        ImageURI = imageuri;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(mContext).load(ImageURI[position]).into(imageView);
        container.addView(imageView);
        return imageView;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return ImageURI.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

