package com.example.www;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class ListActivity extends AppCompatActivity {

    Button goToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        goToMain = findViewById(R.id.goToMain);

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