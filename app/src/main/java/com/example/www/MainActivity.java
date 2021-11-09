package com.example.www;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ImageButton btn_cam;
    TextView today;
    CalendarView calendarView;
    Button btn_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        final EditText eText; // 내부 클래스에서 사용하려면, final 타입이어야 함
//        Button btn;
//
//        eText = (EditText) findViewById(R.id.eText1);

        btn_cam = (ImageButton) findViewById(R.id.btn_cam);
        btn_view = (Button) findViewById(R.id.btn_view);
        today = findViewById(R.id.today);
        calendarView = findViewById(R.id.calendarView);
        final CharSequence[] date = {null};

        btn_cam.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.activity_list);
            }
        });

        btn_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.activity_upload);
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
//                String day;
//                day = year + "년" + (month+1) + "월" + dayOfMonth + "일";
//                today.setText(day);
                date[0] = year + "/" + (month+1) + "/" + dayOfMonth;
                today.setText(date[0]);
            }
        });
    }
}