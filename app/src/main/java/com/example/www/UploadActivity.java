package com.example.www;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import com.bumptech.glide.Glide;

public class UploadActivity extends AppCompatActivity {
    String FILENAME;

    Button btn_cancel, btn_complete;
    ImageView iv_picture;
    TextView tv_selectdate;

    final static int TAKE_PICTURE = 1;
    String mCurrentPhotoPath;
    final static int REQUEST_TAKE_PHOTO = 1;
    final private static String TAG = "tag";

    int year;
    int month;
    int day;
    String imagePath;
    int id;
    static int CHANGE_SIZE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        tv_selectdate = (TextView) findViewById(R.id.tv_selectdate);
        EditText temperMax = (EditText) findViewById(R.id.temperMax);
        EditText temperMin = (EditText) findViewById(R.id.temperMin);
        EditText memo = (EditText) findViewById(R.id.et_memo);

        Intent getIntent = getIntent();
        String year = getIntent.getStringExtra("year");
        String month = getIntent.getStringExtra("month");
        String day = getIntent.getStringExtra("day");

        tv_selectdate.setText(year + month + day);
        FILENAME = year + month + day + ".txt";
        Toast.makeText(this, FILENAME, Toast.LENGTH_LONG).show();

        //취소 버튼 클릭 시, 이전 화면인 메인으로 돌아가기
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        final String[] result_date = new String[1];

        //완료 버튼 클릭 시, 리스트 화면으로 전환
        btn_complete = (Button) findViewById(R.id.btn_complete);
        btn_complete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    FileOutputStream fos = openFileOutput(FILENAME,
                            Context.MODE_PRIVATE);

                    fos.write(temperMax.getText().toString().getBytes());
                    fos.write(temperMin.getText().toString().getBytes());
                    fos.write(memo.getText().toString().getBytes());

                    fos.close();
                } catch (IOException e) {
                }

                Intent intent = new Intent(UploadActivity.this, ListActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);
                startActivity(intent);

                startActivity(intent);
            }
        });

        //카메라 접근 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한 설정 완료");
            } else {
                Log.d(TAG, "권한 설정 요청");
                ActivityCompat.requestPermissions(UploadActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        //카메라 버튼 클릭 시, 기본 카메라 실행
        iv_picture = (ImageView) findViewById(R.id.iv_picture);
        iv_picture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.iv_picture:
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
    }

       public void saveBitmapToJpeg(Bitmap bitmap) {   // 선택한 이미지 내부 저장소에 저장
        String imgName = null;
        File tempFile = new File(getCacheDir(), imgName);    // 파일 경로와 이름 넣기
        try {
            tempFile.createNewFile();   // 자동으로 빈 파일을 생성하기
            FileOutputStream out = new FileOutputStream(tempFile);  // 파일을 쓸 수 있는 스트림을 준비하기
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);   // compress 함수를 사용해 스트림에 비트맵을 저장하기
            out.close();    // 스트림 닫아주기
            Toast.makeText(getApplicationContext(), "파일 저장 성공", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "파일 저장 실패", Toast.LENGTH_SHORT).show();
        }
    }
}