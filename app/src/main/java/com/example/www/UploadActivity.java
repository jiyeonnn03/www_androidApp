package com.example.www;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class UploadActivity extends AppCompatActivity {
    String FILENAME;

    Button btn_cancel, btn_complete;
    ImageView iv_picture;
    TextView tv_selectdate;

    String mCurrentPhotoPath;
    final static int REQUEST_TAKE_PHOTO = 1;
    final private static String TAG = "tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Toolbar toolbar = findViewById(R.id.toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);

        Intent getIntent = getIntent();
        String year = getIntent.getStringExtra("year");
        String month = getIntent.getStringExtra("month");
        String day = getIntent.getStringExtra("day");

        toolbar_title.setText(year + "년 "+month + "월 " + day + "일의 기록");

//        tv_selectdate = (TextView) findViewById(R.id.tv_selectdate);
//        EditText temperMax = (EditText) findViewById(R.id.temperMax);
//        EditText temperMin = (EditText) findViewById(R.id.temperMin);
//        EditText memo = (EditText) findViewById(R.id.et_memo);
//
//        tv_selectdate.setText(year + month + day);
//        FILENAME = year + month + day + ".txt";
////        Toast.makeText(this, FILENAME, Toast.LENGTH_SHORT).show();
//
//        //취소 버튼 클릭 시, 이전 화면인 메인으로 돌아가기
//        btn_cancel = (Button) findViewById(R.id.btn_cancel);
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        final String[] result_date = new String[1];

//        //완료 버튼 클릭 시, 리스트 화면으로 전환
//        btn_complete = (Button) findViewById(R.id.btn_complete);
//        btn_complete.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                try {
//                    FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
//                    BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(fos));
//
//                    buf.append(temperMax.getText().toString());buf.newLine();
//                    buf.append(temperMin.getText().toString());buf.newLine();
//                    buf.append(memo.getText().toString());
//
//                    buf.close();
//                    Toast.makeText(UploadActivity.this, FILENAME + "텍스트 파일 쓰기 성공", Toast.LENGTH_SHORT);
//
//                    fos.close();
//                } catch (IOException e) {
//                    Toast.makeText(getApplicationContext(), "파일 쓰기 실패", Toast.LENGTH_SHORT).show();
//                }
//
//                Intent intent = new Intent(UploadActivity.this, ListActivity.class);
//                intent.putExtra("year", year);
//                intent.putExtra("month", month);
//                intent.putExtra("day", day);
//                startActivity(intent);
//            }
//        });
//
//        //카메라 접근 권한 요청
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                Log.d(TAG, "권한 설정 완료");
//            } else {
//                Log.d(TAG, "권한 설정 요청");
//                ActivityCompat.requestPermissions(UploadActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//            }
//        }
//
//        //카메라 버튼 클릭 시, 기본 카메라 실행
//        iv_picture = (ImageView) findViewById(R.id.iv_picture);
//        iv_picture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.iv_picture:
//                        String date = year + month + day;
//                        dispatchTakePictureIntent(date);
//                        break;
//                }
//            }
//        });
    }

    public void click_save() {
        Intent getIntent = getIntent();
        String year = getIntent.getStringExtra("year");
        String month = getIntent.getStringExtra("month");
        String day = getIntent.getStringExtra("day");

        tv_selectdate = (TextView) findViewById(R.id.tv_selectdate);
        EditText et_temperMax = (EditText) findViewById(R.id.et_temperMax);
        EditText et_temperMin = (EditText) findViewById(R.id.et_temperMin);
        EditText memo = (EditText) findViewById(R.id.et_memo);

        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(fos));

            buf.append(et_temperMax.getText().toString());
            buf.newLine();
            buf.append(et_temperMin.getText().toString());
            buf.newLine();
            buf.append(memo.getText().toString());

            buf.close();
            Toast.makeText(UploadActivity.this, FILENAME + "텍스트 파일 쓰기 성공", Toast.LENGTH_SHORT);

            fos.close();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "파일 쓰기 실패", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(UploadActivity.this, ListActivity.class);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        intent.putExtra("day", day);
        startActivity(intent);

        finish();
    }

    public void click_remove() {

    }

    public void click_home() {
        finish();
    }

    ////////////////////////////////// 이미지 관련
    // 권한 요청
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }

    // 카메라로 촬영한 영상을 가져오는 부분
//    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO: {
                    if (resultCode == RESULT_OK) {
                        File file = new File(mCurrentPhotoPath);
                        Bitmap bitmap;
                        if (Build.VERSION.SDK_INT >= 29) {
                            ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), Uri.fromFile(file));
                            try {
                                bitmap = ImageDecoder.decodeBitmap(source);
                                if (bitmap != null) {
                                    iv_picture.setImageBitmap(bitmap);

                                    try {
                                        FileOutputStream out = new FileOutputStream(file);  // 파일을 쓸 수 있는 스트림을 준비하기
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);   // compress 함수를 사용해 스트림에 비트맵을 저장하기
                                        out.close();    // 스트림 닫아주기
                                        Toast.makeText(this, "파일 저장 성공", Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "파일 저장 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                                if (bitmap != null) {
                                    iv_picture.setImageBitmap(bitmap);


                                    try {
//                                        file.createNewFile();   // 자동으로 빈 파일을 생성하기
                                        FileOutputStream out = new FileOutputStream(file);  // 파일을 쓸 수 있는 스트림을 준비하기
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);   // compress 함수를 사용해 스트림에 비트맵을 저장하기
                                        out.close();    // 스트림 닫아주기
                                        Toast.makeText(this, file + "파일 저장 성공", Toast.LENGTH_LONG).show();
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "파일 저장 실패", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                }
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    // 사진 촬영 후 썸네일만 띄워줌. 이미지를 파일로 저장해야 함
    private File createImageFile(String date) throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(date, ".jpg", storageDir);
        Toast.makeText(this, getExternalFilesDir(Environment.DIRECTORY_PICTURES) + date + "경로(createImageFile)", Toast.LENGTH_SHORT);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // 카메라 인텐트 실행하는 부분
    private void dispatchTakePictureIntent(String filename) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile(filename);
                Toast.makeText(this, "createImageFile 통과", Toast.LENGTH_SHORT);
            } catch (IOException ex) {
                Toast.makeText(this, "createImageFile 통과못함", Toast.LENGTH_SHORT);
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    ////////////////////////////////// 옵션 메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                click_save();
                break;
            case R.id.remove:
                click_remove();
                break;
            case R.id.home:
                click_home();
                break;
        }

        return true;

    }


}