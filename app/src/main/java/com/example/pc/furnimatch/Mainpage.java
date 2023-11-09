package com.example.pc.furnimatch;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.function.LongFunction;


class MainpageCustomTask extends AsyncTask<String,Void,String> {
    String sendMsg, receiveMsg;
    @Override
    protected String doInBackground(String... strings) {
        try{
            String str;
            URL url = new URL("http://"+MainActivity.ip+":8000/furnimatch/find_name_product_list.jsp");
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
        Log.e("이름",receiveMsg);
        return receiveMsg;
    }
}


class ProductListTask extends AsyncTask<String,Void,String> {
        String receiveMsg;
@Override
protected String doInBackground(String... strings) {
        try{
        String str;
        URL url = new URL("http://"+MainActivity.ip+":8000/furnimatch/main_product_list.jsp");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        if(conn.getResponseCode() == conn.HTTP_OK) {
            Log.i("연결 ","연결은 됐어유");
            InputStreamReader tmp = new InputStreamReader(conn.getInputStream(),"UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuffer buffer = new StringBuffer();
            while((str = reader.readLine()) != null)
            {
            buffer.append(str);
            }
            receiveMsg = buffer.toString();
            Log.e("리스트값",receiveMsg);
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


class ARListTask extends AsyncTask<String,Void,String> {
    String receiveMsg;
    @Override
    protected String doInBackground(String... strings) {
        try{
            String str;
            URL url = new URL("http://"+MainActivity.ip+":8000/furnimatch/main_ar_list.jsp");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            if(conn.getResponseCode() == conn.HTTP_OK) {
                Log.i("연결 ","연결은 됐어유");
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(),"UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while((str = reader.readLine()) != null)
                {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
                Log.e("리스트값",receiveMsg);
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


public class Mainpage extends AppCompatActivity {

    static String code = "";

    String product_list_item[];

    static Context con;
    static String username;
    static String profile_img = "";
    String name_img_list[];

    static String main_clicked_list = "";
    //홈 화면
    static String click_list = "";

    private ListView chair_list, table_list, bed_list, sofa_list, another_list;
    private ListViewAdapter chair_adapter, table_adapter, bed_adapter, sofa_adapter, another_adapter;

    String product_list_str = "";

    ImageView mypage_profile_img;

    TextView chair_btn_txt, table_btn_txt, sofa_btn_txt, bed_btn_txt, ano_btn_txt;

    ArrayList<ListViewItem> chair_furnidataList, table_furnidataList, bed_furnidataList, sofa_furnidataList, another_furnidataList;


    TextView maintext;


    LinearLayout chair_layout, table_layout, bed_layout, sofa_layout, another_layout, mypage_layout, home_layout, ar_list_layout;
    ImageButton chair_btn, table_btn, bed_btn, sofa_btn, another_btn;
    ImageButton home_btn, ar_btn, product_plus, mypage_btn;


    //AR화면
    static ArrayList<ListViewItem> ar_data_list = new ArrayList<ListViewItem>();
    ListView ar_list;
    ARListViewAdapter ar_adapter;
    String aritems[];
    String ar_str = "";


    public static String clicked_category = "의자";


    //마이페이지
    TextView mypage_username;
    Button mypage_profile_update_btn, mypage_sell_list_btn, mypage_favorite_list_btn, mypage_logout_btn;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);


        //외부 저장소 권한
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }

        maintext = (TextView) findViewById(R.id.maintext);
        mypage_username = (TextView) findViewById(R.id.mypage_username);
        mypage_profile_img = (ImageView) findViewById(R.id.profile_picture);


        try {
            name_img_list = (new MainpageCustomTask().execute(MainActivity.id, "findname").get()).split("#");
            username = name_img_list[0].trim();
            profile_img = name_img_list[1];

        } catch (Exception e) {
        }

        try {

            product_list_str = new ProductListTask().execute().get();
            product_list_item = product_list_str.split("#");

        } catch (Exception e) {
            Log.e("오류", e.getMessage());
        }


        if (profile_img.contains("null")) {
            mypage_profile_img.setImageResource(R.drawable.user_profile);
        } else {
            Glide.with(this).load("http://" + MainActivity.ip + ":8000/furnimatch/" + profile_img).into(mypage_profile_img);

        }


        maintext.setText("FurniMatch");
        mypage_username.setText(username + "님");


        chair_btn_txt = (TextView) findViewById(R.id.chair_btn_txt);
        table_btn_txt = (TextView) findViewById(R.id.table_btn_txt);
        bed_btn_txt = (TextView) findViewById(R.id.bed_btn_txt);
        sofa_btn_txt = (TextView) findViewById(R.id.sofa_btn_txt);
        ano_btn_txt = (TextView) findViewById(R.id.ano_btn_txt);


        chair_layout = (LinearLayout) findViewById(R.id.chair_layout);
        table_layout = (LinearLayout) findViewById(R.id.table_layout);
        bed_layout = (LinearLayout) findViewById(R.id.bed_layout);
        sofa_layout = (LinearLayout) findViewById(R.id.sofa_layout);
        another_layout = (LinearLayout) findViewById(R.id.another_layout);
        mypage_layout = (LinearLayout) findViewById(R.id.mypage_layout);
        home_layout = (LinearLayout) findViewById(R.id.home_layout);

        ar_list_layout = (LinearLayout) findViewById(R.id.ar_list_layout);


        chair_btn = (ImageButton) findViewById(R.id.chair_btn);
        table_btn = (ImageButton) findViewById(R.id.table_btn);
        bed_btn = (ImageButton) findViewById(R.id.bed_btn);
        sofa_btn = (ImageButton) findViewById(R.id.sofa_btn);
        another_btn = (ImageButton) findViewById(R.id.another_btn);


        chair_list = (ListView) findViewById(R.id.chair_list);
        table_list = (ListView) findViewById(R.id.table_list);
        sofa_list = (ListView) findViewById(R.id.sofa_list);
        bed_list = (ListView) findViewById(R.id.bed_list);
        another_list = (ListView) findViewById(R.id.another_list);


        home_btn = (ImageButton) findViewById(R.id.home_btn);
        ar_btn = (ImageButton) findViewById(R.id.ar_btn);
        product_plus = (ImageButton) findViewById(R.id.product_plus);
        mypage_btn = (ImageButton) findViewById(R.id.mypage_btn);


        //AR리스트
        ar_list = (ListView) findViewById(R.id.ar_listview);


        //메인화면 홈,채팅,물품등록,마이페이지 전환코드
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home_layout.setVisibility(View.VISIBLE);
                mypage_layout.setVisibility(View.INVISIBLE);
                ar_list_layout.setVisibility(View.INVISIBLE);

            }
        });


        mypage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home_layout.setVisibility(View.INVISIBLE);
                mypage_layout.setVisibility(View.VISIBLE);
                ar_list_layout.setVisibility(View.INVISIBLE);

            }
        });

        product_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Mainpage.this, product_plus.class);
                startActivity(intent);
            }
        });

        ar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home_layout.setVisibility(View.INVISIBLE);
                mypage_layout.setVisibility(View.INVISIBLE);
                ar_list_layout.setVisibility(View.VISIBLE);
            }
        });
        //홈화면의 동작 코드
        chair_btn.setBackgroundColor(Color.parseColor("#000000"));

        chair_btn.setOnClickListener(new View.OnClickListener() {
                 @Override
                public void onClick(View view) {

                    clicked_category = "의자";

                    chair_layout.setVisibility(View.VISIBLE);
                    table_layout.setVisibility(View.INVISIBLE);
                    bed_layout.setVisibility(View.INVISIBLE);
                    sofa_layout.setVisibility(View.INVISIBLE);
                    another_layout.setVisibility(View.INVISIBLE);

                    chair_btn.setBackgroundColor(Color.parseColor("#000000"));

                    table_btn.setBackgroundColor(Color.parseColor("#ffffff"));

                    bed_btn.setBackgroundColor(Color.parseColor("#ffffff"));

                    sofa_btn.setBackgroundColor(Color.parseColor("#ffffff"));

                    another_btn.setBackgroundColor(Color.parseColor("#ffffff"));

                }
            }
        );

        table_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clicked_category = "책상";

                chair_layout.setVisibility(View.INVISIBLE);
                table_layout.setVisibility(View.VISIBLE);
                bed_layout.setVisibility(View.INVISIBLE);
                sofa_layout.setVisibility(View.INVISIBLE);
                another_layout.setVisibility(View.INVISIBLE);

                chair_btn.setBackgroundColor(Color.parseColor("#ffffff"));

                table_btn.setBackgroundColor(Color.parseColor("#000000"));

                bed_btn.setBackgroundColor(Color.parseColor("#ffffff"));

                sofa_btn.setBackgroundColor(Color.parseColor("#ffffff"));

                another_btn.setBackgroundColor(Color.parseColor("#ffffff"));

            }
        });

        bed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clicked_category = "침대";


                chair_layout.setVisibility(View.INVISIBLE);
                table_layout.setVisibility(View.INVISIBLE);
                bed_layout.setVisibility(View.VISIBLE);
                sofa_layout.setVisibility(View.INVISIBLE);
                another_layout.setVisibility(View.INVISIBLE);

                chair_btn.setBackgroundColor(Color.parseColor("#ffffff"));

                table_btn.setBackgroundColor(Color.parseColor("#ffffff"));

                bed_btn.setBackgroundColor(Color.parseColor("#000000"));

                sofa_btn.setBackgroundColor(Color.parseColor("#ffffff"));

                another_btn.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        });

        sofa_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clicked_category = "소파";


                chair_layout.setVisibility(View.INVISIBLE);
                table_layout.setVisibility(View.INVISIBLE);
                bed_layout.setVisibility(View.INVISIBLE);
                sofa_layout.setVisibility(View.VISIBLE);
                another_layout.setVisibility(View.INVISIBLE);

                chair_btn.setBackgroundColor(Color.parseColor("#ffffff"));

                table_btn.setBackgroundColor(Color.parseColor("#ffffff"));

                bed_btn.setBackgroundColor(Color.parseColor("#ffffff"));

                sofa_btn.setBackgroundColor(Color.parseColor("#000000"));

                another_btn.setBackgroundColor(Color.parseColor("#ffffff"));

            }
        });

        another_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clicked_category = "기타";

                chair_layout.setVisibility(View.INVISIBLE);
                table_layout.setVisibility(View.INVISIBLE);
                bed_layout.setVisibility(View.INVISIBLE);
                sofa_layout.setVisibility(View.INVISIBLE);
                another_layout.setVisibility(View.VISIBLE);

                chair_btn.setBackgroundColor(Color.parseColor("#ffffff"));

                table_btn.setBackgroundColor(Color.parseColor("#ffffff"));

                bed_btn.setBackgroundColor(Color.parseColor("#ffffff"));

                sofa_btn.setBackgroundColor(Color.parseColor("#ffffff"));

                another_btn.setBackgroundColor(Color.parseColor("#000000"));
            }
        });


        chair_furnidataList = new ArrayList<ListViewItem>();
        table_furnidataList = new ArrayList<ListViewItem>();
        sofa_furnidataList = new ArrayList<ListViewItem>();
        bed_furnidataList = new ArrayList<ListViewItem>();
        another_furnidataList = new ArrayList<ListViewItem>();

        chair_adapter = new ListViewAdapter(this, chair_furnidataList);
        chair_list.setAdapter(chair_adapter);

        table_adapter = new ListViewAdapter(this, table_furnidataList);
        table_list.setAdapter(table_adapter);

        sofa_adapter = new ListViewAdapter(this, sofa_furnidataList);
        sofa_list.setAdapter(sofa_adapter);

        bed_adapter = new ListViewAdapter(this, bed_furnidataList);
        bed_list.setAdapter(bed_adapter);

        another_adapter = new ListViewAdapter(this, another_furnidataList);
        another_list.setAdapter(another_adapter);

        //등록된 매물 조회
//        String furni_list;
//
//        try {
//            furni_list = new MainpageCustomTask().execute(MainActivity.id, "find_list").get();
//
//        } catch (Exception e) {
//        }


        if (!(product_list_str.equals(""))) {
            for (int i = 0; i < product_list_item.length; i += 5) {
                String product_title = product_list_item[i];
                String product_address = product_list_item[i + 1];
                String product_price = product_list_item[i + 2] + "원";
                String product_img1 = product_list_item[i + 3];
                String product_category = product_list_item[i + 4];

                switch (product_category) {
                    case "의자":
                        chair_furnidataList.add(new ListViewItem(product_img1, product_title, product_price, product_address));
                        break;
                    case "책상":
                        table_furnidataList.add(new ListViewItem(product_img1, product_title, product_price, product_address));
                        break;
                    case "침대":
                        bed_furnidataList.add(new ListViewItem(product_img1, product_title, product_price, product_address));
                        break;
                    case "소파":
                        sofa_furnidataList.add(new ListViewItem(product_img1, product_title, product_price, product_address));
                        break;
                    case "기타":
                        another_furnidataList.add(new ListViewItem(product_img1, product_title, product_price, product_address));
                        break;
                }

            }
        }







        /*리스트 클릭시 매물 정보 화면*/
        chair_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                code = chair_furnidataList.get(i).getFurni_name().substring(0, 1) + chair_furnidataList.get(i).getFurni_add().substring(0, 1)
                        + chair_furnidataList.get(i).getFurni_price().substring(0, 1);
                Intent intent = new Intent(Mainpage.this, furniture_information.class);
                intent.putExtra("code", code);
                startActivity(intent);
            }
        });

        table_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                code = table_furnidataList.get(i).getFurni_name().substring(0, 1) + table_furnidataList.get(i).getFurni_add().substring(0, 1)
                        + table_furnidataList.get(i).getFurni_price().substring(0, 1);

                Intent intent = new Intent(Mainpage.this, furniture_information.class);
                intent.putExtra("code", code);
                startActivity(intent);
            }
        });
        sofa_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                code = sofa_furnidataList.get(i).getFurni_name().substring(0, 1) + sofa_furnidataList.get(i).getFurni_add().substring(0, 1)
                        + sofa_furnidataList.get(i).getFurni_price().substring(0, 1);
                Intent intent = new Intent(Mainpage.this, furniture_information.class);
                intent.putExtra("code", code);
                startActivity(intent);
            }
        });

        bed_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                code = bed_furnidataList.get(i).getFurni_name().substring(0, 1) + bed_furnidataList.get(i).getFurni_add().substring(0, 1)
                        + bed_furnidataList.get(i).getFurni_price().substring(0, 1);
                Intent intent = new Intent(Mainpage.this, furniture_information.class);
                intent.putExtra("code", code);
                startActivity(intent);
            }
        });

        another_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                code = another_furnidataList.get(i).getFurni_name().substring(0, 1) + another_furnidataList.get(i).getFurni_add().substring(0, 1)
                        + another_furnidataList.get(i).getFurni_price().substring(0, 1);
                Intent intent = new Intent(Mainpage.this, furniture_information.class);
                intent.putExtra("code", code);
                startActivity(intent);
            }
        });


        //AR내부 코드
        try {
            ar_str = new ARListTask().execute(MainActivity.id).get();
            aritems = (ar_str).split("#");
        } catch (Exception e) {

        }

        ar_adapter = new ARListViewAdapter(this, ar_data_list);
        ar_list.setAdapter(ar_adapter);

        if(!(ar_str.trim().equals("")))
        {
            ar_data_list.clear();
            for(int i=0; i<aritems.length; i+=4)
            {
                String title = aritems[i].trim();
                String addr = aritems[i+1];
                String price = aritems[i+2]+"원";
                String img = aritems[i+3];

                ar_data_list.add(new ListViewItem(img,title,price,addr));
            }
            ar_adapter.notifyDataSetChanged();
        }



        //마이페이지 내부 코드
        mypage_profile_update_btn = (Button) findViewById(R.id.mypage_profile_update_btn);
        mypage_sell_list_btn = (Button) findViewById(R.id.mypage_sell_list_btn);
        mypage_favorite_list_btn = (Button) findViewById(R.id.mypage_favorite_btn);
        mypage_logout_btn = (Button) findViewById(R.id.mypage_logout_btn);

        //프로필 수정화면으로 이동
        mypage_profile_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Mainpage.this, UpdateProfile.class);
                startActivity(intent);
            }
        });

        //판매내역 화면으로 이동
        mypage_sell_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent01 = new Intent(Mainpage.this, sell_list.class);
                intent01.putExtra("username", username);
                startActivity(intent01);
            }
        });

        //관심목록 화면으로 이동
        mypage_favorite_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Mainpage.this, Favorite_list.class);
                startActivity(intent);
            }
        });
    }

        //뒤로가기 버튼 오버라이드
        private final long finishtimeed = 1000;
        private long presstime = 0;

        @Override
        public void onBackPressed () {
            long tempTime = System.currentTimeMillis();
            long intervalTime = tempTime - presstime;

            if (0 <= intervalTime && finishtimeed >= intervalTime) {
                finishAffinity();
                System.runFinalization();
                System.exit(0);
            } else {
                presstime = tempTime;
                Toast.makeText(getApplicationContext(), "한번더 누르시면 앱이 종료됩니다", Toast.LENGTH_SHORT).show();
            }
        }

        public void OnClickHandler (View view)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("로그아웃 확인").setMessage("정말 로그아웃 하시겠습니까?");

            builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Mainpage.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

}

