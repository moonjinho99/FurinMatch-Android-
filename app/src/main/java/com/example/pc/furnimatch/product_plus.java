package com.example.pc.furnimatch;

import android.Manifest;
import android.app.AppComponentFactory;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


class FileUploadTasks extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
        String apiUrl = "http://"+MainActivity.ip+":8000/furnimatch/insert_product.jsp";

        String img1 = params[0];
        String img2 = params[1];
        String img3 = params[2];
        String img4 = params[3];
        String title = params[4];
        String sellerName = params[5];
        String price = params[6];
        String width = params[7];
        String length = params[8];
        String height = params[9];
        String content = params[10];
        String address = params[11];
        String category = params[12];
        String sellerId = params[13];

        HttpFileUpload(apiUrl, "", img1, img2, img3, img4, title, sellerName, price, width, length, height, content, address, category,sellerId);

        return null;
    }

    private void HttpFileUpload(String urlString, String params, String img1, String img2, String img3, String img4, String title, String sellerName, String price, String width, String length, String height, String content, String address, String category, String sellerId) {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            File sourceFile1 = new File(img1);
            File sourceFile2 = new File(img2);
            File sourceFile3 = new File(img3);
            File sourceFile4 = new File(img4);

            DataOutputStream dos;
            if (!sourceFile1.isFile()) {
                Log.e("uploadFile1", "Source File not exist :" + img1);
            } else if (!sourceFile2.isFile()) {
                Log.e("uploadFile2", "Source File not exist :" + img2);
            } else if (!sourceFile3.isFile()) {
                Log.e("uploadFile3", "Source File not exist :" + img3);
            } else if (!sourceFile4.isFile()) {
                Log.e("uploadFile4", "Source File not exist :" + img4);
            } else {
                FileInputStream mFileInputStream1 = new FileInputStream(sourceFile1);
                FileInputStream mFileInputStream2 = new FileInputStream(sourceFile2);
                FileInputStream mFileInputStream3 = new FileInputStream(sourceFile3);
                FileInputStream mFileInputStream4 = new FileInputStream(sourceFile4);

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
                conn.setRequestProperty("uploaded_file1", img1);
                conn.setRequestProperty("uploaded_file2", img2);
                conn.setRequestProperty("uploaded_file3", img3);
                conn.setRequestProperty("uploaded_file4", img4);

                // 1번 이미지
                dos = new DataOutputStream(conn.getOutputStream());
                OutputStreamWriter writer = new OutputStreamWriter(dos, "UTF-8");

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file1\";filename=\"" + img1 + "\"" + lineEnd);
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

                //2번 이미지
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file2\";filename=\"" + img2 + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                int bytesAvailable2 = mFileInputStream2.available();
                int maxBufferSize2 = 1024 * 1024;
                int bufferSize2 = Math.min(bytesAvailable2, maxBufferSize2);
                byte[] buffer2 = new byte[bufferSize2];
                int bytesRead2 = mFileInputStream2.read(buffer2, 0, bufferSize2);
                // read image
                while (bytesRead2 > 0) {
                    dos.write(buffer2, 0, bufferSize2);
                    bytesAvailable2 = mFileInputStream2.available();
                    bufferSize2 = Math.min(bytesAvailable2, maxBufferSize2);
                    bytesRead2 = mFileInputStream2.read(buffer2, 0, bufferSize2);
                }
                dos.writeBytes(lineEnd);

                //3번 이미지
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file3\";filename=\"" + img3 + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                int bytesAvailable3 = mFileInputStream3.available();
                int maxBufferSize3 = 1024 * 1024;
                int bufferSize3 = Math.min(bytesAvailable3, maxBufferSize3);
                byte[] buffer3 = new byte[bufferSize3];
                int bytesRead3 = mFileInputStream3.read(buffer3, 0, bufferSize3);
                // read image
                while (bytesRead3 > 0) {
                    dos.write(buffer3, 0, bufferSize3);
                    bytesAvailable3 = mFileInputStream3.available();
                    bufferSize3 = Math.min(bytesAvailable3, maxBufferSize3);
                    bytesRead3 = mFileInputStream3.read(buffer3, 0, bufferSize3);
                }
                dos.writeBytes(lineEnd);

                //4번 이미지
                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file4\";filename=\"" + img4 + "\"" + lineEnd);
                dos.writeBytes(lineEnd);
                int bytesAvailable4 = mFileInputStream4.available();
                int maxBufferSize4 = 1024 * 1024;
                int bufferSize4 = Math.min(bytesAvailable4, maxBufferSize4);
                byte[] buffer4 = new byte[bufferSize4];
                int bytesRead4 = mFileInputStream4.read(buffer4, 0, bufferSize4);
                // read image
                while (bytesRead4 > 0) {
                    dos.write(buffer4, 0, bufferSize4);
                    bytesAvailable4 = mFileInputStream4.available();
                    bufferSize4 = Math.min(bytesAvailable4, maxBufferSize4);
                    bytesRead4 = mFileInputStream4.read(buffer4, 0, bufferSize4);
                }
                dos.writeBytes(lineEnd);

                //판매자명
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"sellerName\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.write(sellerName.getBytes("UTF-8"));
                dos.writeBytes(lineEnd);

                //제목
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"title\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.write(title.getBytes("UTF-8"));
                dos.writeBytes(lineEnd);

                //주소
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"address\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.write(address.getBytes("UTF-8"));
                dos.writeBytes(lineEnd);

                //가격
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"price\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(price);
                dos.writeBytes(lineEnd);

                //가로
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"width\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(width);
                dos.writeBytes(lineEnd);

                //세로
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"length\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(length);
                dos.writeBytes(lineEnd);

                //높이
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"height\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(height);
                dos.writeBytes(lineEnd);

                //내용
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"content\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.write(content.getBytes("UTF-8"));
                dos.writeBytes(lineEnd);

                //종류
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"category\"" + lineEnd);
                dos.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.write(category.getBytes("UTF-8"));
                dos.writeBytes(lineEnd);

                //아이디
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"sellerId\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(sellerId);
                dos.writeBytes(lineEnd);


                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                mFileInputStream1.close();
                mFileInputStream2.close();
                mFileInputStream3.close();
                mFileInputStream4.close();
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
                mFileInputStream2.close();
                mFileInputStream3.close();
                mFileInputStream4.close();
                dos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    public class product_plus extends AppCompatActivity {

        static String output_addr = "";

        private LocationManager locationManager;
        private LocationListener locationListener;

        double latitude = 0;
        double longitude = 0;


        Uri uri;

        ImageView add_img_view1, add_img_view2, add_img_view3, add_img_view4;

        EditText tran_title_edt, tran_price_edt, width_edt, length_edt, height_edt, content_edt;

        Spinner tran_category_spinner;

        RadioGroup addr_radio_group;
        RadioButton current_addr,insert_addr;
        EditText seller_addr;

        Button reg_btn;

        String img_path1 = "", img_path2="", img_path3="", img_path4="";

        //static ListViewItem sellListitem;
        private ArrayList<ListViewItem> listViewItemList;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.product_plus);

            add_img_view1 = (ImageView) findViewById(R.id.add_img_imageView1);
            add_img_view2 = (ImageView) findViewById(R.id.add_img_imageView2);
            add_img_view3 = (ImageView) findViewById(R.id.add_img_imageView3);
            add_img_view4 = (ImageView) findViewById(R.id.add_img_imageView4);

            tran_title_edt = (EditText) findViewById(R.id.tran_title_edt);
            tran_price_edt = (EditText) findViewById(R.id.tran_price);
            width_edt = (EditText) findViewById(R.id.width_edt);
            length_edt = (EditText) findViewById(R.id.length_edt);
            height_edt = (EditText) findViewById(R.id.height_edt);
            content_edt = (EditText) findViewById(R.id.content_edt);
            tran_category_spinner = (Spinner) findViewById(R.id.tran_category_spinner);

            addr_radio_group = (RadioGroup) findViewById(R.id.addr_radio_group);
            current_addr = (RadioButton) findViewById(R.id.current_addr);
            insert_addr = (RadioButton) findViewById(R.id.insert_addr);
            seller_addr = (EditText) findViewById(R.id.seller_addr);


            reg_btn = (Button) findViewById(R.id.reg_btn);

            add_img_view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 1);
                }
            });

            add_img_view2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 2);
                }
            });

            add_img_view3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 3);
                }
            });

            add_img_view4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 4);
                }
            });


            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }

            try{
                Location current = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                latitude = current.getLatitude();
                longitude = current.getLongitude();
                Log.e("위도 : ",Double.toString(latitude));
                Log.e("경도 : ",Double.toString(longitude));

            }catch (Exception e)
            {
                e.printStackTrace();
            }

            String current_address = getAddress(product_plus.this,latitude,longitude);
            String addrs[] = current_address.split(" ");

            output_addr = "";
            for(int i=1; i<addrs.length; i++)
            {
                output_addr += addrs[i]+" ";
            }

    //주소 입력 부분

            addr_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                    switch (checkedId)
                    {
                        case R.id.current_addr:
                            seller_addr.setText(output_addr);
                            break;
                        case R.id.insert_addr:
                            seller_addr.setText("");
                            break;
                    }

                }
            });


            reg_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String title = tran_title_edt.getText().toString();
                    String price = tran_price_edt.getText().toString();
                    String width = width_edt.getText().toString();
                    String length = length_edt.getText().toString();
                    String height = height_edt.getText().toString();
                    String content = content_edt.getText().toString();
                    String addr = seller_addr.getText().toString();

                    String category = tran_category_spinner.getSelectedItem().toString();

                    Drawable img1 = add_img_view1.getDrawable();
                    Drawable img2 = add_img_view2.getDrawable();
                    Drawable img3 = add_img_view3.getDrawable();
                    Drawable img4 = add_img_view4.getDrawable();

                    if (title.equals("")) {
                        Toast.makeText(product_plus.this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show();

                    } else if (price.equals("")) {
                        Toast.makeText(product_plus.this, "가격을 입력해주세요", Toast.LENGTH_SHORT).show();
                    } else if (width.equals("") || length.equals("") || height.equals("")) {
                        Toast.makeText(product_plus.this, "면적을 모두 입력해주세요", Toast.LENGTH_SHORT).show();
                    } else if (content.equals("")) {
                        Toast.makeText(product_plus.this, "부가 설명을 입력해주세요", Toast.LENGTH_SHORT).show();
                    } else if(img1.getConstantState().equals(img2.getConstantState()) || img1.getConstantState().equals(img3.getConstantState()) || img1.getConstantState().equals(img4.getConstantState()) || img2.getConstantState().equals(img3.getConstantState()) || img2.getConstantState().equals(img4.getConstantState()) || img3.getConstantState().equals(img4.getConstantState()))
                    {
                        Toast.makeText(product_plus.this, "이미지를 모두 첨부해주새요.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        DecimalFormat format_price = new DecimalFormat("###,###");
                        String f_price = format_price.format(Integer.parseInt(price));
                        new FileUploadTasks().execute(img_path1, img_path2, img_path3, img_path4, title, Mainpage.username, f_price, width, length, height, content, addr, category,MainActivity.id);
                        Log.e("1번이미지",img_path1);

                        recreate();
                        Toast.makeText(product_plus.this, "물품등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(product_plus.this, Mainpage.class);
                        startActivity(intent);
                    }

                }
            });

        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            switch (requestCode) {
                case 1:
                    if (resultCode == RESULT_OK) {
                        if (data != null) {
                            uri = data.getData();
                            add_img_view1.setImageURI(uri);
                            img_path1 = getRealPathFromURI(uri);
                        }
                    }
                    break;
                case 2:
                    if (resultCode == RESULT_OK) {
                        if (data != null) {
                            uri = data.getData();
                            add_img_view2.setImageURI(uri);
                            img_path2 = getRealPathFromURI(uri);
                        }
                    }
                    break;
                case 3:
                    if (resultCode == RESULT_OK) {
                        if (data != null) {
                            uri = data.getData();
                            add_img_view3.setImageURI(uri);
                            img_path3 = getRealPathFromURI(uri);
                        }
                    }
                    break;
                case 4:
                    if (resultCode == RESULT_OK) {
                        if (data != null) {
                            uri = data.getData();
                            add_img_view4.setImageURI(uri);
                            img_path4 = getRealPathFromURI(uri);
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

        //실제 주소 가져오기
        public String getAddress(Context mContext, double lat, double lng)
        {
            String nowAddr ="현재 위치를 확인 할 수 없습니다.";
            Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
            List<Address> address;

            try
            {
                if (geocoder != null)
                {
                    address = geocoder.getFromLocation(lat, lng, 1);
                    if (address != null && address.size() > 0)
                    {
                        nowAddr = address.get(0).getAddressLine(0).toString();
                    }
                }
            }
            catch (IOException e)
            {
                Toast.makeText(mContext, "주소를 가져 올 수 없습니다.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            return nowAddr;
        }
}
