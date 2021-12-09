package com.example.www;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyListAdapter extends BaseAdapter {

    private final List<DataList> mListData;
    public MyListAdapter(List listdata){
        //리스트 데이터 받기
        mListData = listdata;
    }
    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    // 각 아이템 화면에 뿌려질 때마다 호출
    public View getView(int position, View convertView, ViewGroup parent) {

    //layout inflate를 통해 레이아웃 View를 불러옴
        convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_list,parent,false);

        // 사진, 날짜, 최고기온, 최저기온, 체감날씨, 메모
        ImageView iv_picture2 = (ImageView)convertView.findViewById(R.id.iv_picture2);
        TextView tv_result_date = (TextView)convertView.findViewById(R.id.tv_result_date);
        TextView tv_result_temperMax = (TextView)convertView.findViewById(R.id.tv_result_temperMax);
        TextView tv_result_temperMin = (TextView)convertView.findViewById(R.id.tv_result_temperMin);
        TextView tv_result_user_weather = (TextView)convertView.findViewById(R.id.tv_result_user_weather);
        TextView tv_result_memo = (TextView)convertView.findViewById(R.id.tv_result_memo);

        // 현재 position 의 데이터
        DataList dataList = mListData.get(position);
        iv_picture2.setImageURI(dataList.get_picture());
        tv_result_date.setText(dataList.get_date());
        tv_result_temperMax.setText(dataList.get_temperMax());
        tv_result_temperMin.setText(dataList.get_temperMin());
        tv_result_user_weather.setText(dataList.get_uweather());
        tv_result_memo.setText(dataList.get_memo());

        return convertView;
    }
}