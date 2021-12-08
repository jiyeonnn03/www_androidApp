package com.example.www;

import android.net.Uri;
import android.widget.ImageView;

public class DataList {
    int[] dailyImage={

    };

    DataList(Uri _picture, String _date, String _temperMax, String _temperMin, String _uweather, String _memo){
        date = _date;
        picture = _picture;
        temperMax = _temperMax;
        temperMin = _temperMin;
        uweather = _uweather;
        memo = _memo;
//        dailyIndex = _dailyIndex;
    }

    // 데이터 은닉화.
    private Uri picture;
    private String date; //날짜
    private String temperMax; //최고기온
    private String temperMin; //최저기온
    private String uweather; //체감날씨
    private String memo; //메모
//    private int dailyIndex;

//    public int getImage() {
//        return dailyImage[dailyIndex];
//    }
    public String get_date() {
    return date;
}
    public void set_date(String _date) {
        this.date = _date;
    }

    public Uri get_picture() {
        return picture;
    }
    public void set_picture(Uri _picture) {
        this.picture = _picture;
    }

    public String get_temperMax() {
        return temperMax;
    }
    public void set_temperMax(String _temperMax) {
        this.temperMax = _temperMax;
    }

    public String get_temperMin() {
        return temperMin;
    }
    public void set_temperMin(String _temperMin) {
        this.temperMin = _temperMin;
    }

    public String get_uweather() {
        return uweather;
    }
    public void set_uweather(String _uweather) {
        this.uweather = _uweather;
    }

    public String get_memo() {
        return memo;
    }
    public void set_memo(String _memo) {
        this.memo = _memo;
    }
}
