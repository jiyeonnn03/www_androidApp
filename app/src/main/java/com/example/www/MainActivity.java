package com.example.www;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

public class MainActivity extends AppCompatActivity {
    Button btn_add, btn_view;
    MaterialCalendarView materialCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] result_date = new String[5];//현재 클릭한 날짜 정보
//        final CalendarDay date = null;
        materialCalendarView = findViewById(R.id.calendarView);
        materialCalendarView.addDecorators(
                new SundayRed(),
                new SaturdayBlue()
        );

        //날짜 클릭 시 버튼의 텍스트 변경
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {

            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                btn_add = (Button) findViewById(R.id.btn_add);
                btn_view = (Button) findViewById(R.id.btn_view);

                result_date[0] = date.getYear() + "년 " + (date.getMonth()+1) + "월 " + date.getDay() + "일";
                result_date[1] = date.getYear() + "";
                result_date[2] = (date.getMonth()+1) + "";
                if (result_date[2].length() < 2)
                    result_date[2] = "0" + result_date[2];

                result_date[3] = date.getDay() + "";
                if (result_date[3].length() < 2)
                    result_date[3] = "0" + result_date[3];

                btn_view.setText(result_date[0] + "의 기록 보기");
                btn_view.setVisibility(View.VISIBLE);
                btn_view.setEnabled(true);


                //전체보기 버튼 클릭 시, 리스트 화면으로 전환
                btn_add.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ListActivity.class);
                        intent.putExtra("year", result_date[1]);
                        intent.putExtra("month", result_date[2]);
                        intent.putExtra("day", result_date[3]);
                        startActivity(intent);
                    }
                });

                //기록보기 버튼 클릭 시, 해당 날짜의 기록 화면으로 전환
                btn_view.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, UploadActivity.class);
                        intent.putExtra("year", result_date[1]);
                        intent.putExtra("month", result_date[2]);
                        intent.putExtra("day", result_date[3]);
                        startActivity(intent);

                    }
                });
            }
        });
    }
}