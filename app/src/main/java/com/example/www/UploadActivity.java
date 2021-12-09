package com.example.www;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class UploadActivity extends AppCompatActivity {
    String FILENAME;
    int flag = 0;

    String mCurrentPhotoPath;
    final static int REQUEST_TAKE_PHOTO = 1;
    final static int REQUEST_TAKE_ALBUM = 2;
    final private static String TAG = "tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent getIntent = getIntent();
        String year = getIntent.getStringExtra("year");
        String month = getIntent.getStringExtra("month");
        String day = getIntent.getStringExtra("day");

        toolbar_title.setText(year + "년 " + month + "월 " + day + "일의 기록");

        FILENAME = year + month + day + ".txt";

        ImageView iv_picture = (ImageView) findViewById(R.id.iv_picture);
        EditText et_temperMax = (EditText) findViewById(R.id.et_temperMax);
        EditText et_temperMin = (EditText) findViewById(R.id.et_temperMin);
        EditText et_user_weather = (EditText) findViewById(R.id.et_user_weather);
        EditText et_memo = (EditText) findViewById(R.id.et_memo);

        File directory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "");
        File[] files = directory.listFiles();

        String date = year + month + day;

        //파일명 비교해서 이미지와 텍스트 가져오기
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(date)) {
                if (files[i].getName().contains(".jpg")) {
                    try {
                        //이미지 가져오기
                        Uri uri = Uri.parse(directory + "/" + files[i].getName());
                        iv_picture.setImageURI(uri);
                        flag = 1;

                        FileInputStream fis = openFileInput(FILENAME);
                        BufferedReader buffer = new BufferedReader(new InputStreamReader(fis));

                        buffer.readLine(); //select date 한 줄 제거
                        et_temperMax.setText(buffer.readLine());
                        et_temperMin.setText(buffer.readLine());
                        et_user_weather.setText(buffer.readLine());
                        et_memo.setText(buffer.readLine());
                        buffer.close();
                        break;
                    } catch (Exception e) {
                        Log.d(TAG, "이미지 파일 불러오기 실패");

                    }
                }
            }
        }

        //카메라 접근 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "카메라 접근 권한 설정 완료");
            } else {
                Log.d(TAG, "카메라 접근 권한 설정 요청");
                ActivityCompat.requestPermissions(UploadActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        //카메라 버튼 클릭 시, 기본 카메라 실행
        iv_picture = (ImageView) findViewById(R.id.iv_picture);
        iv_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] date = intent_date();
                String today = date[0] + date[1] + date[2];

                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("사진 가져올 방법 선택");

                alert.setPositiveButton("앨범", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToAlbum(today);
                    }
                });

                alert.setNegativeButton("카메라", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dispatchTakePictureIntent(today);
                    }
                });

                alert.show();
            }
        });
    }

    //intent로 넘어온 date 반환
    public String[] intent_date() {

        String[] date = new String[3];

        Intent getIntent = getIntent();
        date[0] = getIntent.getStringExtra("year");
        date[1] = getIntent.getStringExtra("month");
        date[2] = getIntent.getStringExtra("day");

        return date;
    }

    //저장 버튼 클릭 시 호출
    public void click_save() {
        String[] date = intent_date();

        EditText et_temperMax = (EditText) findViewById(R.id.et_temperMax);
        EditText et_temperMin = (EditText) findViewById(R.id.et_temperMin);
        EditText et_user_weather = (EditText) findViewById(R.id.et_user_weather);
        EditText et_memo = (EditText) findViewById(R.id.et_memo);

        if (et_temperMax.getText().toString().length() == 0) {
            Toast.makeText(this, "최고 기온을 입력하세요.", Toast.LENGTH_SHORT).show();
            et_temperMax.requestFocus();
            return;
        } if (et_temperMin.getText().toString().length() == 0) {
            Toast.makeText(this, "최저 기온을 입력하세요.", Toast.LENGTH_SHORT).show();
            et_temperMin.requestFocus();
            return;
        } if (et_user_weather.getText().toString().length() == 0) {
            Toast.makeText(this, "체감 날씨를 입력하세요.", Toast.LENGTH_SHORT).show();
            et_user_weather.requestFocus();
            return;
        } if (Integer.parseInt(et_temperMax.getText().toString()) < Integer.parseInt(et_temperMin.getText().toString())) { //최고 기온이 최저 기온보다 작은 경우
            Toast.makeText(this, "최고 기온이 최저 기온보다 낮습니다."+"\n"+"다시 입력하세요.", Toast.LENGTH_SHORT).show();
            et_temperMax.requestFocus();
            return;
        } else {
            try {
                FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(fos));
                buf.append(date[0] + "." + date[1] + "." + date[2] + ".");
                buf.newLine();
                buf.append(et_temperMax.getText().toString());
                buf.newLine();
                buf.append(et_temperMin.getText().toString());
                buf.newLine();
                buf.append(et_user_weather.getText().toString());
                buf.newLine();
                buf.append(et_memo.getText().toString());
                buf.close();

                fos.close();
                Toast.makeText(this, date[0] + "년" + date[1] + "월" + date[2] + "일의 기록 저장 성공", Toast.LENGTH_SHORT).show();
                finish();
            } catch (IOException e) {
                Toast.makeText(this, date[0] + "년" + date[1] + "월" + date[2] + "일의 기록 저장 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //이미지 파일의 Uri를 찾는 함수(날짜 기준)
    public Uri findUri() {
        String[] date = intent_date();

        String today = date[0] + date[1] + date[2];

        File directory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "");
        File[] files = directory.listFiles();

        Uri uri = null;

        //파일명 비교해서 가져오기
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(today) && files[i].getName().contains(".jpg")) {
                uri = Uri.parse(directory + "/" + files[i].getName());
//                break; //제일 마지막 이미지 파일을 가져오기 위해 break 걸지 않음
            }
        }
        return uri;
    }

    //삭제 버튼 클릭 시 호출
    public void click_remove() {
        String[] date = intent_date();

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("삭제");
        alert.setMessage("정말로 삭제 하시겠습니까?");

        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean imgFlag = remove_imgFile();
                boolean txtFlag = remove_txtFile();

                if (imgFlag && txtFlag) {
                    Toast.makeText(alert.getContext(), date[0] + "년 " + date[1] + "월 " + date[2] + "일의 기록 삭제", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(alert.getContext(), "삭제할 기록이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        alert.show();
    }

    //이미지 파일 삭제 함수
    public boolean remove_imgFile() {
        Uri uri = findUri();
        boolean isDelete = false;

        if (uri != null) {
            File imgFile = new File(uri.getPath());
            if (imgFile.exists()) {
                isDelete = imgFile.delete();
            }
        }
        return isDelete;
    }

    //텍스트 파일 삭제 함수
    public boolean remove_txtFile() {
        String[] date = intent_date();
        String today = date[0] + date[1] + date[2];

        String txtName = today + ".txt";
        boolean isDelete = false;

        if (deleteFile(txtName))
            isDelete = true;

        return isDelete;
    }

    //(텍스트 입력 안한 채 처음으로 돌아가려 하면 이미지 파일 삭제)
    public void click_home() {
        EditText et_temperMax = (EditText) findViewById(R.id.et_temperMax);
        EditText et_temperMin = (EditText) findViewById(R.id.et_temperMin);
        EditText et_user_weather = (EditText) findViewById(R.id.et_user_weather);

        if (et_temperMax.getText().toString().length() == 0 || et_temperMin.getText().toString().length() == 0 || et_user_weather.getText().toString().length() == 0) {
            remove_imgFile();
        }
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        ImageView iv_picture = (ImageView) findViewById(R.id.iv_picture);

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
                                        Log.d(TAG, "REQUEST_TAKE_PHOTO VER 29 파일 저장 성공");
                                    } catch (Exception e) {
                                        Log.d(TAG, "REQUEST_TAKE_PHOTO VER 29 파일 저장 실패");
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
                                        FileOutputStream out = new FileOutputStream(file);  // 파일을 쓸 수 있는 스트림을 준비하기
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);   // compress 함수를 사용해 스트림에 비트맵을 저장하기
                                        out.close();    // 스트림 닫아주기
                                        Log.d(TAG, "REQUEST_TAKE_PHOTO 파일 저장 성공");
                                    } catch (Exception e) {
                                        Log.d(TAG, "REQUEST_TAKE_PHOTO 파일 저장 실패");
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                }
                case REQUEST_TAKE_ALBUM: {
                    if (resultCode == RESULT_OK) {
                        File file = new File(mCurrentPhotoPath);

                        try {
                            InputStream in = getContentResolver().openInputStream(intent.getData());

                            Bitmap bitmap = BitmapFactory.decodeStream(in);
//                            Bitmap bitmap 	= MediaStore.Images.Media.getBitmap(getContentResolver(), intent.getData());
                            in.close();

                            iv_picture.setImageBitmap(bitmap);
                            try {
                                FileOutputStream out = new FileOutputStream(file);  // 파일을 쓸 수 있는 스트림을 준비하기
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);   // compress 함수를 사용해 스트림에 비트맵을 저장하기
                                out.close();    // 스트림 닫아주기
                                Log.d(TAG, "REQUEST_TAKE_ALBUM 파일 저장 성공");
                            } catch (Exception e) {
                                Log.d(TAG, "REQUEST_TAKE_ALBUM 파일 저장 실패");
                            }
                        } catch (Exception e) {

                        }
                    } else if (resultCode == RESULT_CANCELED) {
                        Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
                    }
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
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // 카메라 인텐트 실행하는 부분
    private void dispatchTakePictureIntent(String filename) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Boolean remove = remove_imgFile(); //존재하는 이미지 파일 삭제 후 새로운 이미지 저장
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile(filename);
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void goToAlbum(String filename) {
        Intent takeAlbumIntent = new Intent(Intent.ACTION_PICK);
        Boolean remove = remove_imgFile(); //존재하는 이미지 파일 삭제 후 새로운 이미지 저장
        if (takeAlbumIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile(filename);
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
                takeAlbumIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                takeAlbumIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takeAlbumIntent, REQUEST_TAKE_ALBUM);
            }
        }
    }

    ////////////////////////////////// 옵션 메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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