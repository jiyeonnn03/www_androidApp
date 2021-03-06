package com.example.www;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.Manifest.permission.ACCESS_MEDIA_LOCATION;

public class MainActivity extends AppCompatActivity {
    Button btn_viewList, btn_view;
    MaterialCalendarView materialCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        intentFilter.addDataScheme("file");

        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if (intent != null) {
                    try {
                        if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
                            Toast.makeText(MainActivity.this, "?????????????????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
                        }
                       //mount ?????? ??????????????? ??? ??????
                        else if (action.equals(Intent.ACTION_MEDIA_UNMOUNTED) || action.equals(Intent.ACTION_MEDIA_REMOVED)) {
                            AlertAuthoritySetting();
                        }
                    } catch (NullPointerException e) {

                    }
                }
            }
        };
        registerReceiver(mReceiver, intentFilter);

        final String[] result_date = new String[5];//?????? ????????? ?????? ??????

        //?????? ??????
        Calendar startTimeCalendar = Calendar.getInstance();
        int currentYear = startTimeCalendar.get(Calendar.YEAR);
        int currentMonth = startTimeCalendar.get(Calendar.MONTH);
        int currentDate = startTimeCalendar.get(Calendar.DATE);

        btn_viewList = (Button) findViewById(R.id.btn_viewList); //???????????? ??????

        //?????? ?????? ??????
        materialCalendarView = findViewById(R.id.calendarView);
        materialCalendarView.addDecorators(
                new SundayRed(),
                new SaturdayBlue(),
                new TodayGreen()
        );

        //?????? ?????? ?????? ?????? ??????
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMaximumDate(CalendarDay.from(currentYear, currentMonth, currentDate))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        materialCalendarView.setShowOtherDates(MaterialCalendarView.SHOW_OUT_OF_RANGE);


        materialCalendarView.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                SimpleDateFormat calendar_view_format = new SimpleDateFormat("yyyy??? MM???");
                String monthAndYear = calendar_view_format.format(day.getDate());
                return monthAndYear;
            }
        });

        //???????????? ?????? ?????? ?????? ???, ????????? ???????????? ??????
        btn_viewList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });

        //?????? ?????? ??? ???????????? ????????? ?????? ??????
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                btn_view = (Button) findViewById(R.id.btn_view);

                result_date[0] = date.getYear() + "??? " + (date.getMonth() + 1) + "??? " + date.getDay() + "???";
                result_date[1] = date.getYear() + "";
                result_date[2] = (date.getMonth() + 1) + "";
                if (result_date[2].length() < 2)
                    result_date[2] = "0" + result_date[2];

                result_date[3] = date.getDay() + "";
                if (result_date[3].length() < 2)
                    result_date[3] = "0" + result_date[3];

                btn_view.setText(result_date[0] + "??? ?????? ??????");
                btn_view.setVisibility(View.VISIBLE);
                btn_view.setEnabled(true);

                //???????????? ?????? ?????? ???, ?????? ????????? ?????? ???????????? ??????
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

    public void AlertAuthoritySetting() { // ?????? ??????
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("SD ?????? ??????");
        builder.setMessage("?????? ???????????? ????????? ????????? ??? ????????????. ?????? ????????? ????????? ??????????????????");
        builder.setCancelable(false);
        builder.setPositiveButton("???", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) { // SD ?????? ???????????? ????????????
                try {
                    Intent intent = new Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS);
                    intent.putExtra("sd", "ok");
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    Intent intent = new Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS);
                    startActivity(intent);
                }
            }
        });
        builder.setNegativeButton("?????????", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) { // ?????? ?????? ????????? ??????
                finish();
            }
        });
        builder.create().show();
    }
}