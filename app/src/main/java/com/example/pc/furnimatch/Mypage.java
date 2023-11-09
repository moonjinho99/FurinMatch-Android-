package com.example.pc.furnimatch;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Mypage extends AppCompatActivity {

    TextView mypage_username;

    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase database;

    String sql;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        mypage_username = (TextView) findViewById(R.id.mypage_username);

        helper = new DatabaseOpenHelper(Mypage.this, DatabaseOpenHelper.tableName, null, version);
        database = helper.getWritableDatabase();

        sql = "SELECT name FROM "+helper.tableName + " WHERE id ='"+MainActivity.id+"'";
        cursor = database.rawQuery(sql,null);
        cursor.moveToNext();

        String username = cursor.getString(0);

        mypage_username.setText(username+"님");

    }
}
