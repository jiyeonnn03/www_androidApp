package com.example.www;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ListActivity extends AppCompatActivity {

    Button btn_goToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

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