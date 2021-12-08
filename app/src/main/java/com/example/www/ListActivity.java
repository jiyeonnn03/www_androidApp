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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    String FILENAME;

    Button btn_goToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Intent getIntent = getIntent();
        String year = getIntent.getStringExtra("year");
        String month = getIntent.getStringExtra("month");
        String day = getIntent.getStringExtra("day");
        String line = null;

        FILENAME = year + month + day + ".txt";
        Toast.makeText(this, FILENAME, Toast.LENGTH_LONG).show();

        ImageView iv_picture2 = (ImageView) findViewById(R.id.iv_picture2);
        TextView tv_result_date = (TextView) findViewById(R.id.tv_result_date);
        TextView tv_result_weather = (TextView) findViewById(R.id.tv_result_weather);
        TextView tv_result_memo = (TextView) findViewById(R.id.tv_result_memo);
        TextView tv_result_temperMax = (TextView) findViewById(R.id.tv_result_temperMax);
        TextView tv_result_temperMin = (TextView) findViewById(R.id.tv_result_temperMin);

        tv_result_date.setText(year + ". " + month +  ". " + day + ".");
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/files/Picture";

        File directory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"");
        File[] files = directory.listFiles();

        List<String> filesNameList = new ArrayList<>();
        String date = year + month + day;
        //파일명 비교해서 가져오기
        for (int i=0; i< files.length; i++) {
            if (files[i].getName().contains(date)) {
                if (files[i].getName().contains(".jpg"))
                    filesNameList.add(files[i].getName());
                break;
            }
        }

        try {
            Uri uri = Uri.parse(directory + "/"+ filesNameList.get(0));
            iv_picture2.setImageURI(uri);

            FileInputStream fis = openFileInput(FILENAME);
            String data = "";
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fis));
            List<String> filelist = new ArrayList<>();

//            if((line = buffer.readLine()) == null) {
//                data += line;
//                data += "\n";

            tv_result_temperMax.setText(buffer.readLine());
            tv_result_temperMin.setText(buffer.readLine());
            tv_result_memo.setText(buffer.readLine());
            buffer.close();

            Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "txt 파일 불러오기 실패", Toast.LENGTH_LONG).show();

//            Toast.makeText(this, "uri 불러오기 실패", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        String str[] = new String[3];
//        try {
//            FileInputStream fis = openFileInput(FILENAME);

//                str[i] = line;
//                i += 1;
//            }
//            for (int j = 0; j < i; j++) {
//
//            }
//            fis.read(buffer);
//            tv_result_weather.setText(new String(buffer));
//            tv_result_memo.setText(new String(buffer));
//            fis.close();
//        } catch (IOException e) {
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

//        DateFormat formatter = new SimpleDateFormat("yyyy년MM월dd일");
//        Date date = new Date(calendarView.getDate());
//        today.setText(formatter.format(date));
//
//
//        final CharSequence[] date = {null};
//
//        goToMain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
//
//                intent.putExtra("selectedDate", date[0]);
//                startActivities(intent);
//            }
//        });

    }
}