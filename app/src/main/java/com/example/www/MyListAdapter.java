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
        // 리스트를 받는다.
        mListData = listdata;
    }
    @Override
    //아이템 개수
    public int getCount() {
        //리스트의 사이즈로 아이템 개수를 설정함.
        return mListData.size();
    }

    @Override
    // 포지션 번째의 아이템
    public Object getItem(int position) {
        //리스트의 포지션번째의 가져오기.
        return mListData.get(position);
    }

    @Override
    // 포지션 번째의 아이디 (리스트 뷰에서는 인덱스가 된다)
    public long getItemId(int position) {
        return position;
    }

    @Override
    // 포지션 번째 아이템뷰 만들기. 가장 중요한 함수
    // 각 아이템이 화면에 보일때마다 호출되는 함수.
    public View getView(int position, View convertView, ViewGroup parent) {

    //레이아웃 인플레이트를 통해서 우리가 만든 레이아웃 View 를 불러온다.
        convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_list,parent,false);

        // 사진, 최고기온, 최저기온, 체감날씨, 메모
        ImageView iv_picture2 = (ImageView)convertView.findViewById(R.id.iv_picture2);
        TextView tv_result_date = (TextView)convertView.findViewById(R.id.tv_result_date);
        TextView tv_result_temperMax = (TextView)convertView.findViewById(R.id.tv_result_temperMax);
        TextView tv_result_temperMin = (TextView)convertView.findViewById(R.id.tv_result_temperMin);
        TextView tv_result_user_weather = (TextView)convertView.findViewById(R.id.tv_result_user_weather);
        TextView tv_result_memo = (TextView)convertView.findViewById(R.id.tv_result_memo);

        // 현재 position 의 데이터
        DataList dataList = mListData.get(position);
//        iv_picture2.setImageResource(dataList.getImage());

        iv_picture2.setImageURI(dataList.get_picture());
        tv_result_date.setText(dataList.get_date());
        tv_result_temperMax.setText(dataList.get_temperMax());
        tv_result_temperMin.setText(dataList.get_temperMin());
        tv_result_user_weather.setText(dataList.get_uweather());
        tv_result_memo.setText(dataList.get_memo());

        return convertView;
    }
}