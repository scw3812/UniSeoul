package com.nugu.uniseoul;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class BusRouteActivity extends AppCompatActivity {

    private TextView busRouteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_route);

        busRouteTextView = (TextView) findViewById(R.id.bus_route_textview);


        new Thread(new Runnable() {
            StringBuffer buffer = new StringBuffer();
            @Override
            public void run() {
                String rtId = getIntent().getExtras().getString("rtId");

                String queryUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute?ServiceKey=공공데이터API키&busRouteId="+rtId;
                // TODO Auto-generated method stub
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
                                else if (tag.equals("direction")) {
                                    xpp.next();
                                    if(!buffer.toString().contains(xpp.getText())){
                                        buffer.append(xpp.getText()+"\n");//direction 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                    }
                                }
                                else if (tag.equals("stationNm")) {
                                    xpp.next();
                                    if(!buffer.toString().contains(xpp.getText())) {
                                        buffer.append(xpp.getText() + " - ");//stationNm 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                    }
                                }
                                break;

                            case XmlPullParser.TEXT:
                                break;

                            case XmlPullParser.END_TAG:
                                break;
                        }

                        eventType = xpp.next();
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch blocke.printStackTrace();
                }
                buffer.deleteCharAt(buffer.length()-2);

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        busRouteTextView.setText(buffer.toString());
                    }
                });

            }
        }).start();


    }
}
