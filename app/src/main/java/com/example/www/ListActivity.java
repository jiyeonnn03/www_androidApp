package com.example.www;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    String FILENAME;

    static final int listCount = 15;
    ListView list_view;
    Button btn_goToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent getIntent = getIntent();
        String year = getIntent.getStringExtra("year");
        String month = getIntent.getStringExtra("month");
        String day = getIntent.getStringExtra("day");

        FILENAME = year + month + day + ".txt";

        ImageView iv_picture2 = (ImageView) findViewById(R.id.iv_picture2);
        TextView tv_result_date = (TextView) findViewById(R.id.tv_result_date);
        TextView tv_result_temperMax = (TextView) findViewById(R.id.tv_result_temperMax);
        TextView tv_result_temperMin = (TextView) findViewById(R.id.tv_result_temperMin);
        TextView tv_result_user_weather = (TextView) findViewById(R.id.tv_result_user_weather);
        TextView tv_result_memo = (TextView) findViewById(R.id.tv_result_memo);


        File directory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"");
        File[] files = directory.listFiles();

        List<String> imgfileList = new ArrayList<>();
        List<String> txtfileList = new ArrayList<>();

        ArrayList<DataList> listData = new ArrayList<>();

//        String date = year + month + day;
        //파일명 비교해서 가져오기
        for (int i=0; i< files.length; i++) {
//            tv_result_date.setText(year + ". " + month +  ". " + day + ".");
            if (files[i].getName().contains(".jpg")) {
                imgfileList.add(files[i].getName());
                txtfileList.add(files[i].getName().substring(0,8)+".txt");
            }
        }

        try {
            Uri uri;
            FileInputStream fis;
            BufferedReader buffer;
            String line = null;

            for(int i = 0 ; i < imgfileList.size() ; i++) {
                uri = Uri.parse(directory + "/"+ imgfileList.get(i));
                fis = openFileInput(txtfileList.get(i));
                buffer = new BufferedReader(new InputStreamReader(fis));

//                if ((line = buffer.readLine()) != null) {
                    listData.add(new DataList(uri, buffer.readLine(), buffer.readLine()+"º", buffer.readLine()+"º", buffer.readLine(), buffer.readLine()));
//                } else
                    buffer.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //개발자가 만든 아답터를 연결하기 위해서
        MyListAdapter myListAdapter = new MyListAdapter(listData);

        list_view = (ListView)findViewById(R.id.list_view);
        list_view.setAdapter(myListAdapter);

        // 리스트에 들어갈 데이터를 만드는 과정.

//
//        try {
//            Uri uri = Uri.parse(directory + "/"+ filesNameList.get(0));
//            iv_picture2.setImageURI(uri);
//
//            FileInputStream fis = openFileInput(FILENAME);
//            String data = "";
//            BufferedReader buffer = new BufferedReader(new InputStreamReader(fis));
//            List<String> filelist = new ArrayList<>();
//
////            if((line = buffer.readLine()) == null) {
////                data += line;
////                data += "\n";
//
//            tv_result_temperMax.setText(buffer.readLine());
//            tv_result_temperMin.setText(buffer.readLine());
//            tv_result_weather.setText(buffer.readLine());
//            tv_result_memo.setText(buffer.readLine());
//            buffer.close();
//
//            Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
//        }catch (Exception e){
//            Toast.makeText(this, "txt 파일 불러오기 실패", Toast.LENGTH_LONG).show();
//
////            Toast.makeText(this, "uri 불러오기 실패", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }








        //처음으로 버튼 클릭 시, 메인 화면으로 전환
        btn_goToMain = findViewById(R.id.btn_goToMain);
        btn_goToMain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(ListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}