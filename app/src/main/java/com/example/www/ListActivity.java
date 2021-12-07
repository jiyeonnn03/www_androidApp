package com.example.www;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;

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

        FILENAME = year + month + day + ".txt";
        Toast.makeText(this, FILENAME, Toast.LENGTH_LONG).show();

        TextView tv_result_date = (TextView) findViewById(R.id.tv_result_date);
        TextView tv_result_weather = (TextView) findViewById(R.id.tv_result_weather);
        TextView tv_result_memo = (TextView) findViewById(R.id.tv_result_memo);
        tv_result_date.setText(year + month + day);

        try {
            FileInputStream fis = openFileInput(FILENAME);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            tv_result_weather.setText(new String(buffer));
            tv_result_memo.setText(new String(buffer));
            fis.close();
        } catch (IOException e) {
        }

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