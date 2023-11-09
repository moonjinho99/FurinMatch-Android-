package com.example.pc.furnimatch;

import android.net.Uri;

class ListViewItem {

    private String furniture_img;
    private String furni_name;
    private String furni_price;
    private String furni_add;

    public ListViewItem(String furniture_img, String furni_name, String furni_price, String furni_add)
    {
        this.furniture_img = furniture_img;
        this.furni_name = furni_name;
        this.furni_price = furni_price;
        this.furni_add = furni_add;
    }

    public String getFurniture_img() {
        return this.furniture_img;
    }

    public String getFurni_name() {
        return this.furni_name;
    }

    public String getFurni_price() {
        return this.furni_price;
    }

    public String getFurni_add() {
        return furni_add;
    }
}
