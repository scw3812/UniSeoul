package com.nugu.uniseoul;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class TransportActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private String arsId;
    private String stationNm;
    private List<String> data;
    private List<String> rtIds;
    private List<String> rtNms;
    private LinearLayout.LayoutParams params;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_transport);

        gridLayout = (GridLayout) findViewById(R.id.transport_layout);
        textView = (TextView)findViewById(R.id.transport_textview);
        rtIds = new ArrayList<>();
        rtNms = new ArrayList<>();
        String busData[] = getIntent().getStringExtra("busData").split(",");
        stationNm = busData[0];
        arsId = busData[1];

        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 10, 10);  // 왼쪽, 위, 오른쪽, 아래 순서입니다.

        textView.setText(stationNm+" 정류장");

        new Thread(new Runnable() {

            @Override
            public void run() {

                // TODO Auto-generated method stub
                data = getRouteXmlData(arsId);
                for (int j = 0; j < data.size(); j++) {
                    if (j != 0 && j % 2 == 1) {
                        if (!rtNms.contains(data.get(j))) {
                            rtNms.add(data.get(j));
                        }
                    } else {
                        if (!rtIds.contains(data.get(j))) {
                            rtIds.add(data.get(j));
                        }
                    }
                }

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if(rtNms.size() == 0){
                            TextView textView = new TextView(TransportActivity.this);
                            textView.setText("경유하는 저상버스가 없습니다.");
                            gridLayout.addView(textView);
                        }
                        for (int i = 0; i < rtNms.size(); i++) {
                            Button button = new Button(TransportActivity.this);
                            button.setText(rtNms.get(i));
                            button.setBackgroundResource(R.drawable.bus_route_textbox);
                            button.setTextColor(Color.parseColor("#ffffff"));
                            button.setLayoutParams(params);
                            final String rtId = rtIds.get(i);
                            final String rtNm = rtNms.get(i);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(TransportActivity.this, BusRouteActivity.class);
                                    intent.putExtra("rtData", rtId+","+rtNm);
                                    startActivity(intent);
                                }
                            });
                            gridLayout.addView(button);
                        }
                    }
                });

            }
        }).start();
    }

    List<String> getRouteXmlData(String arsId) {
        List<String> routeList = new ArrayList<>();

        String queryUrl = "http://ws.bus.go.kr/api/rest/stationinfo/getLowStationByUid?ServiceKey="+getString(R.string.bus_api_key)+"&arsId=" + arsId;

        try {
            URL url = new URL(queryUrl);//문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();//태그 이름 얻어오기

                        if (tag.equals("itemList")) ;// 첫번째 검색결과
                        else if (tag.equals("rtNm")) {
                            xpp.next();
                            routeList.add(xpp.getText());
                        } else if (tag.equals("busRouteId")) {
                            xpp.next();
                            routeList.add(xpp.getText());
                        }
                        break;

                    default:
                        break;
                }

                eventType = xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }

        return routeList;
    }
}





