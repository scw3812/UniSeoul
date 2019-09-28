package com.nugu.uniseoul;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.MarkerIcons;
import com.nugu.uniseoul.task.GeoTransTask;
import com.nugu.uniseoul.task.GeocoderTask;
import com.nugu.uniseoul.data.CourseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private String[] address;
    private CourseData courseData;
    private double lat = 0;
    private double lon = 0;
    private double xStation = 0;
    private double yStation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        courseData = (CourseData)intent.getSerializableExtra("course");
        address = new String[]{courseData.getCourseAddress()};

        //지도 생성
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
    }

    //지도 생성 메서드
    @UiThread
    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {
        String json = "";

        try {
            json = new GeocoderTask(getString(R.string.naver_client_id),getString(R.string.naver_client_secret)).execute(address).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONObject(json).getJSONArray("addresses");
            for(int i = 0; i< jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                lat = Double.parseDouble(obj.getString("y"));
                lon = Double.parseDouble(obj.getString("x"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LatLng latLng = new LatLng(lat, lon);
        CameraPosition cameraPosition = new CameraPosition(latLng, 13);
        naverMap.setCameraPosition(cameraPosition);

        InfoWindow infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(MapActivity.this) {
            @NonNull
            @Override
            public String getText(@NonNull InfoWindow infoWindow) {
                return courseData.getCourseTitle()+"\n"+address[0];
            }
        });

        // 지도를 클릭하면 정보 창을 닫음
        naverMap.setOnMapClickListener((coord, point) -> {
            infoWindow.close();
        });

        // 마커를 클릭하면:
        Overlay.OnClickListener listener = overlay -> {
            Marker marker = (Marker)overlay;
            if(marker.getIcon().equals(OverlayImage.fromResource(R.drawable.marker_green))){
                if (marker.getInfoWindow() == null) {
                    // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                    infoWindow.open(marker);
                } else {
                    // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                    infoWindow.close();
                }
            }else{
                if(marker.getIcon().equals(OverlayImage.fromResource(R.drawable.marker_black))){
                    Intent intent = new Intent(MapActivity.this, TransportActivity.class);
                    intent.putExtra("busData", marker.getTag().toString());
                    startActivityForResult(intent,1);
                }else{
                    Intent intent = new Intent(MapActivity.this, SubwayActivity.class);
                    intent.putExtra("subwayData", marker.getTag().toString());
                    startActivityForResult(intent, 1);
                }
            }

            return true;
        };

        Marker marker = new Marker();
        marker.setPosition(new LatLng(lat, lon));
        marker.setTag(address[0]);
        marker.setOnClickListener(listener);
        marker.setIcon(OverlayImage.fromResource(R.drawable.marker_green));
        marker.setMap(naverMap);

        new Thread(new Runnable() {

            @Override
            public void run() {

                // TODO Auto-generated method stub
                final String[] stations = getBusStationXmlData(lat,lon).split("\n");
                final String[] subway = getSubwayStationXmlData(lat,lon).split("\n");
                Log.d("hahha",getSubwayStationXmlData(lat,lon));

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        for(int i = 3; i<stations.length; i+=4){
                            double latStation = Double.parseDouble(stations[i-1]);
                            double lonStation = Double.parseDouble(stations[i-2]);
                            Marker markerStation = new Marker();
                            markerStation.setPosition(new LatLng(latStation,lonStation));
                            markerStation.setIcon(OverlayImage.fromResource(R.drawable.marker_black));
                            markerStation.setTag(stations[i]+","+stations[i-3]);
                            markerStation.setOnClickListener(listener);
                            markerStation.setMap(naverMap);
                        }

                        for(int i = 3; i<subway.length; i+=4){
                            String[] xyStation = new String[]{subway[i-1],subway[i]};

                            String json = "";

                            try {
                                json = new GeoTransTask(getString(R.string.kakao_application_key)).execute(xyStation).get();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            try {
                                JSONArray jsonArray = new JSONObject(json).getJSONArray("documents");
                                for(int j = 0; j< jsonArray.length(); j++){
                                    JSONObject obj = jsonArray.getJSONObject(j);
                                    xStation = Double.parseDouble(obj.getString("x"));
                                    yStation = Double.parseDouble(obj.getString("y"));
                                    Log.d("gps",yStation+","+xStation);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Marker markerStation = new Marker();
                            markerStation.setPosition(new LatLng(yStation, xStation));
                            markerStation.setIcon(OverlayImage.fromResource(R.drawable.marker_red));
                            markerStation.setTag(subway[i-3]+","+subway[i-2]);
                            markerStation.setOnClickListener(listener);
                            markerStation.setMap(naverMap);
                        }
                    }
                });
            }
        }).start();
    }

    String getBusStationXmlData(double lat, double lon) {
        StringBuffer buffer = new StringBuffer();

        String queryUrl = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByPos?ServiceKey="+getString(R.string.bus_api_key)+"&tmX=" + lon + "&tmY=" + lat + "&radius=600";
        Log.d("url", queryUrl);
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
                        else if (tag.equals("gpsX")) {
                            xpp.next();
                            buffer.append(xpp.getText() + "\n");//gpsX 요소의 TEXT 읽어와서 문자열버퍼에 추가
                        }
                        else if (tag.equals("gpsY")) {
                            xpp.next();
                            buffer.append(xpp.getText() + "\n");//gpsY 요소의 TEXT 읽어와서 문자열버퍼에 추가
                        }
                        else if (tag.equals("arsId")) {
                            xpp.next();
                            buffer.append(xpp.getText() + "\n");//arsId 요소의 TEXT 읽어와서 문자열버퍼에 추가
                        }
                        else if (tag.equals("stationNm")) {
                            xpp.next();
                            buffer.append(xpp.getText());//stationNm 요소의 TEXT 읽어와서 문자열버퍼에 추가
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName(); //태그 이름 얻어오기

                        if (tag.equals("itemList")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈

                        break;
                }

                eventType = xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }
        return buffer.toString();//StringBuffer 문자열 객체 반환
    }

    String getSubwayStationXmlData(double lat, double lon) {
        StringBuffer buffer = new StringBuffer();

        GeoPoint in_pt = new GeoPoint(lon, lat);
        GeoPoint tm_pt = GeoTrans.convert(GeoTrans.GEO, GeoTrans.TM, in_pt);

        String queryUrl = "http://swopenapi.seoul.go.kr/api/subway/"+getString(R.string.subway_api_key1)+"/xml/nearBy/0/5/"+tm_pt.getX()+"/"+tm_pt.getY();
        Log.d("url", queryUrl);
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

                        if (tag.equals("row")) ;// 첫번째 검색결과
                        else if (tag.equals("subwayXcnts")) {
                            xpp.next();
                            buffer.append(xpp.getText() + "\n");//statnNm 요소의 TEXT 읽어와서 문자열버퍼에 추가
                        }
                        else if (tag.equals("subwayYcnts")) {
                            xpp.next();
                            buffer.append(xpp.getText());//subwayNm 요소의 TEXT 읽어와서 문자열버퍼에 추가
                        }
                        else if (tag.equals("subwayNm")) {
                            xpp.next();
                            buffer.append(xpp.getText() + "\n");//subwayXcnts 요소의 TEXT 읽어와서 문자열버퍼에 추가
                        }
                        else if (tag.equals("statnNm")) {
                            xpp.next();
                            buffer.append(xpp.getText() + "\n");//subwayYcnts 요소의 TEXT 읽어와서 문자열버퍼에 추가
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName(); //태그 이름 얻어오기

                        if (tag.equals("row")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈

                        break;
                }

                eventType = xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch blocke.printStackTrace();
        }
        return buffer.toString();//StringBuffer 문자열 객체 반환
    }
}

