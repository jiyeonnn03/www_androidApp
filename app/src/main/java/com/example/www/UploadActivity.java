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

        toolbar_title.setText(year + "??? " + month + "??? " + day + "?????? ??????");

        FILENAME = year + month + day + ".txt";

        ImageView iv_picture = (ImageView) findViewById(R.id.iv_picture);
        EditText et_temperMax = (EditText) findViewById(R.id.et_temperMax);
        EditText et_temperMin = (EditText) findViewById(R.id.et_temperMin);
        EditText et_user_weather = (EditText) findViewById(R.id.et_user_weather);
        EditText et_memo = (EditText) findViewById(R.id.et_memo);

        File directory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "");
        File[] files = directory.listFiles();

        String date = year + month + day;

        //????????? ???????????? ???????????? ????????? ????????????
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().contains(date)) {
                    if (files[i].getName().contains(".jpg")) {
                        try {
                            //????????? ????????????
                            Uri uri = Uri.parse(directory + "/" + files[i].getName());
                            iv_picture.setImageURI(uri);

                            FileInputStream fis = openFileInput(FILENAME);
                            BufferedReader buffer = new BufferedReader(new InputStreamReader(fis));

                            buffer.readLine(); //select date ??? ??? ??????
                            et_temperMax.setText(buffer.readLine());
                            et_temperMin.setText(buffer.readLine());
                            et_user_weather.setText(buffer.readLine());
                            et_memo.setText(buffer.readLine());
                            buffer.close();
                            break;
                        } catch (Exception e) {
                            Log.d(TAG, "????????? ?????? ???????????? ??????");
                        }
                    }
                } else
                    continue;
            }
        }

        //????????? ?????? ?????? ??????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "????????? ?????? ?????? ?????? ??????");
            } else {
                Log.d(TAG, "????????? ?????? ?????? ?????? ??????");
                ActivityCompat.requestPermissions(UploadActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }

        //????????? ?????? ?????? ???, ?????? ????????? ??????
        iv_picture = (ImageView) findViewById(R.id.iv_picture);
        iv_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] date = intent_date();
                String today = date[0] + date[1] + date[2];

                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("?????? ????????? ?????? ??????");

                alert.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToAlbum(today);
                    }
                });

                alert.setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dispatchTakePictureIntent(today);
                    }
                });

                alert.show();
            }
        });
    }

    //intent??? ????????? date ??????
    public String[] intent_date() {

        String[] date = new String[3];

        Intent getIntent = getIntent();
        date[0] = getIntent.getStringExtra("year");
        date[1] = getIntent.getStringExtra("month");
        date[2] = getIntent.getStringExtra("day");

        return date;
    }

    //?????? ?????? ?????? ??? ??????
    public void click_save() {
        String[] date = intent_date();

        EditText et_temperMax = (EditText) findViewById(R.id.et_temperMax);
        EditText et_temperMin = (EditText) findViewById(R.id.et_temperMin);
        EditText et_user_weather = (EditText) findViewById(R.id.et_user_weather);
        EditText et_memo = (EditText) findViewById(R.id.et_memo);

        if (et_temperMax.getText().toString().length() == 0) {
            Toast.makeText(this, "?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
            et_temperMax.requestFocus();
            return;
        } if (et_temperMin.getText().toString().length() == 0) {
            Toast.makeText(this, "?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
            et_temperMin.requestFocus();
            return;
        } if (et_user_weather.getText().toString().length() == 0) {
            Toast.makeText(this, "?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
            et_user_weather.requestFocus();
            return;
        } if (Integer.parseInt(et_temperMax.getText().toString()) < Integer.parseInt(et_temperMin.getText().toString())) { //?????? ????????? ?????? ???????????? ?????? ??????
            Toast.makeText(this, "?????? ????????? ?????? ???????????? ????????????."+"\n"+"?????? ???????????????.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, date[0] + "???" + date[1] + "???" + date[2] + "?????? ?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                finish();
            } catch (IOException e) {
                Toast.makeText(this, date[0] + "???" + date[1] + "???" + date[2] + "?????? ?????? ?????? ??????", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //????????? ????????? Uri??? ?????? ??????(?????? ??????)
    public Uri findUri() {
        String[] date = intent_date();

        String today = date[0] + date[1] + date[2];

        File directory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "");
        File[] files = directory.listFiles();

        Uri uri = null;

        //????????? ???????????? ????????????
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(today) && files[i].getName().contains(".jpg")) {
                uri = Uri.parse(directory + "/" + files[i].getName());
//                break; //?????? ????????? ????????? ????????? ???????????? ?????? break ?????? ??????
            }
        }
        return uri;
    }

    //?????? ?????? ?????? ??? ??????
    public void click_remove() {
        String[] date = intent_date();

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("??????");
        alert.setMessage("?????? ?????? ???????????????????");

        alert.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean imgFlag = remove_imgFile();
                boolean txtFlag = remove_txtFile();

                if (imgFlag && txtFlag) {
                    Toast.makeText(alert.getContext(), date[0] + "??? " + date[1] + "??? " + date[2] + "?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(alert.getContext(), "????????? ????????? ????????????.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alert.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        alert.show();
    }

    //????????? ?????? ?????? ??????
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

    //????????? ?????? ?????? ??????
    public boolean remove_txtFile() {
        String[] date = intent_date();
        String today = date[0] + date[1] + date[2];

        String txtName = today + ".txt";
        boolean isDelete = false;

        if (deleteFile(txtName))
            isDelete = true;

        return isDelete;
    }

    //(????????? ?????? ?????? ??? ???????????? ???????????? ?????? ????????? ?????? ??????)
    public void click_home() {
        EditText et_temperMax = (EditText) findViewById(R.id.et_temperMax);
        EditText et_temperMin = (EditText) findViewById(R.id.et_temperMin);
        EditText et_user_weather = (EditText) findViewById(R.id.et_user_weather);

        if (et_temperMax.getText().toString().length() == 0 || et_temperMin.getText().toString().length() == 0 || et_user_weather.getText().toString().length() == 0) {
            remove_imgFile();
        }
        finish();
    }

    ////////////////////////////////// ????????? ??????
    // ?????? ??????
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        }
    }

    // ???????????? ????????? ????????? ???????????? ??????
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
                                        FileOutputStream out = new FileOutputStream(file);  // ????????? ??? ??? ?????? ???????????? ????????????
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);   // compress ????????? ????????? ???????????? ???????????? ????????????
                                        out.close();    // ????????? ????????????
                                        Log.d(TAG, "REQUEST_TAKE_PHOTO VER 29 ?????? ?????? ??????");
                                    } catch (Exception e) {
                                        Log.d(TAG, "REQUEST_TAKE_PHOTO VER 29 ?????? ?????? ??????");
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
                                        FileOutputStream out = new FileOutputStream(file);  // ????????? ??? ??? ?????? ???????????? ????????????
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);   // compress ????????? ????????? ???????????? ???????????? ????????????
                                        out.close();    // ????????? ????????????
                                        Log.d(TAG, "REQUEST_TAKE_PHOTO ?????? ?????? ??????");
                                        Log.d(TAG, mCurrentPhotoPath);
                                    } catch (Exception e) {
                                        Log.d(TAG, "REQUEST_TAKE_PHOTO ?????? ?????? ??????");
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
                                FileOutputStream out = new FileOutputStream(file);  // ????????? ??? ??? ?????? ???????????? ????????????
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);   // compress ????????? ????????? ???????????? ???????????? ????????????
                                out.close();    // ????????? ????????????
                                Log.d(TAG, "REQUEST_TAKE_ALBUM ?????? ?????? ??????");
                                Log.d(TAG, mCurrentPhotoPath);
                            } catch (Exception e) {
                                Log.d(TAG, "REQUEST_TAKE_ALBUM ?????? ?????? ??????");
                            }
                        } catch (Exception e) {

                        }
                    } else if (resultCode == RESULT_CANCELED) {
                        Toast.makeText(this, "?????? ?????? ??????", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "?????? ?????? ??????");
                    }
                }
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    // ?????? ?????? ??? ???????????? ?????????. ???????????? ????????? ???????????? ???
    private File createImageFile(String date) throws IOException {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(date, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    // ????????? ????????? ???????????? ??????
    private void dispatchTakePictureIntent(String filename) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Boolean remove = remove_imgFile(); //???????????? ????????? ?????? ?????? ??? ????????? ????????? ??????
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
        Boolean remove = remove_imgFile(); //???????????? ????????? ?????? ?????? ??? ????????? ????????? ??????
        if (takeAlbumIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile(filename);
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
                Log.d(TAG, "photoURI : "+photoURI);
                Log.d(TAG, "photoURI.getPath : "+photoURI.getPath());

                takeAlbumIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                takeAlbumIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takeAlbumIntent, REQUEST_TAKE_ALBUM);
            }
        }
    }

    ////////////////////////////////// ?????? ??????
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