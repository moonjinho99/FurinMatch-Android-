package com.example.pc.furnimatch;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public static final String tableName = "Users";
    public static final String favoritetableName = "favorite_list";
    public static final String producttableName = "Products";

    public static String username="";

    public DatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context,name,factory,version);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("tag","db 생성_db가 없을때만 최초로 실행함");
        createTable(db);
        createFavoriteTable(db);
        createProductTable(db);
    }

    //회원정보 테이블
    public void createTable(SQLiteDatabase db) {
        String sql ="CREATE TABLE "+ tableName + "(id text, pw text,  name text, phonenum text)";
        try{
            db.execSQL(sql);
        } catch (SQLException e){

        }
    }


    public void insertUser(SQLiteDatabase db, String id, String pw, String name,String phonenum){
        Log.i("tag","회원가입을 했을때 생성함");
        db.beginTransaction();
        try{
            String sql = "INSERT INTO "+tableName + "(id, pw, name,phonenum)" +"values('"
                    +id+"', '"+pw+"', '"+name+"', '"+phonenum+"')";

            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void UpdateUser(SQLiteDatabase db, String value){
        db.beginTransaction();
        try{
            String sql = "UPDATE "+tableName+" SET "+UpdateProfile.attribute+"= '"+value+"' WHERE "+ "id = '"+MainActivity.id+"'";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }


    //관심목록 테이블
    public void createFavoriteTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + favoritetableName + "(image text,  title text, price text, addr text)";
        try {
            db.execSQL(sql);
        } catch (SQLException e) {

        }
    }

    //물품등록 테이블
    public void createProductTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + producttableName + "(username text, image1 text,Image2 text,Image3 text,Image4 text ,title text, " +
                "price text,width text,length text,height text,content text ,addr text,category text,id text)";
        try {
            db.execSQL(sql);
        } catch (SQLException e) {

        }
    }

    public void insertProduct(SQLiteDatabase db,String username,String image1,String image2,String image3,String image4,String title, String price,
                              String width, String length, String height,  String addr, String category, String content,String id){
        Log.i("tag","물품등록을 했을때 생성함");
        db.beginTransaction();
        try{
            String sql = "INSERT INTO "+ producttableName + "(username, image1,image2,image3,image4, title, price,width,length,height,addr,category,content,id)"+
                    "values('" +username+"', '"+image1+"', '"+image2+"', '"+image3+"', '"+image4+"', '"+title+"', '"+
                    price+"', '"+width+"', '"+length+"', '"+height+"', '"+addr+"', '"+category+"', '"+content+"', '"+id+"')";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void insertFavorite(SQLiteDatabase db,String image, String title, String price, String addr){
        Log.i("tag","관심물품등록을 했을때 생성함");
        db.beginTransaction();
        try{
            String sql = "INSERT INTO "+favoritetableName + "(image, title, price, addr)" +"values('"
                    +image+"', '"+title+"', '"+price+"', '"+addr+"')";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
