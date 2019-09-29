package com.nugu.uniseoul;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.nugu.uniseoul.adapter.BusRouteRecyclerViewAdapter;
import com.nugu.uniseoul.data.BusData;
import com.nugu.uniseoul.data.Code;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BusRouteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BusRouteRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<BusData> mDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_route);

        recyclerView = (RecyclerView)findViewById(R.id.bus_route_recyclerview);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        new Thread(new Runnable() {
            StringBuffer buffer = new StringBuffer();
            String[] busRoute = null;
            @Override
            public void run() {
                String[] rtData = getIntent().getExtras().getString("rtData").split(",");
                String rtId = rtData[0];
                String rtNm = rtData[1];

                String queryUrl = "http://ws.bus.go.kr/api/rest/busRouteInfo/getStaionByRoute?ServiceKey="+getString(R.string.bus_api_key)+"&busRouteId="+rtId;
                // TODO Auto-generated method stub
                Log.d("url",queryUrl);
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
                                        buffer.append(xpp.getText()+" 방향"+"\n");//direction 요소의 TEXT 읽어와서 문자열버퍼에 추가
                                    }
                                }
                                else if (tag.equals("stationNm")) {
                                    xpp.next();
                                    if(!buffer.toString().contains(xpp.getText())) {
                                        buffer.append(xpp.getText() + "\n");//stationNm 요소의 TEXT 읽어와서 문자열버퍼에 추가
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

                busRoute = buffer.toString().split("\n");
                mDataset = new ArrayList<>();
                for(String route:busRoute){
                    BusData busData = new BusData();
                    if(route.contains("방향")){
                        busData.setString(route);
                        busData.setViewType(Code.ViewType.NAME);
                    }else{
                        busData.setString(route);
                        busData.setViewType(Code.ViewType.DIRECTION);
                    }
                    mDataset.add(busData);
                }

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mAdapter = new BusRouteRecyclerViewAdapter(BusRouteActivity.this, mDataset, rtNm);
                        recyclerView.setAdapter(mAdapter);
                    }
                });

            }
        }).start();


    }
}
