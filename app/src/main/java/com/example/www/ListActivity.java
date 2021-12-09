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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    ListView list_view;
    Button btn_goToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        File directory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"");
        File[] files = directory.listFiles();
        sort(files); //파일명 내림차순 정렬

        List<String> imgfileList = new ArrayList<>();
        List<String> txtfileList = new ArrayList<>();

        ArrayList<DataList> listData = new ArrayList<>();

        //이미지&텍스트 파일 각각 파일명 비교해서 가져오기(날짜 기준)
        for (int i=0; i< files.length; i++) {
            if (files[i].getName().contains(".jpg")) {
                imgfileList.add(files[i].getName());
                txtfileList.add(files[i].getName().substring(0,8)+".txt");
            }
        }

        //이미지 파일 및 텍스트 파일 접근
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

        //Custom Adapter 연결
        MyListAdapter myListAdapter = new MyListAdapter(listData);

        list_view = (ListView)findViewById(R.id.list_view);
        list_view.setAdapter(myListAdapter);

        //달력보기 버튼 클릭 시, 메인 화면으로 전환
        btn_goToMain = findViewById(R.id.btn_goToMain);
        btn_goToMain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void sort(File[] filterResult){
        // 파일명으로 정렬한다.
        Arrays.sort(filterResult, new Comparator() {
            public int compare(Object arg0, Object arg1) {
                File file1 = (File)arg0;
                File file2 = (File)arg1;
                return file2.getName().compareToIgnoreCase(file1.getName());
            }
        });
    }
}