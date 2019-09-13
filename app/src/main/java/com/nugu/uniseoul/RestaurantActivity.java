package com.nugu.uniseoul;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.nugu.uniseoul.adapter.CourseRecyclerViewAdapter;
import com.nugu.uniseoul.adapter.SlideFragmentAdapter;
import com.nugu.uniseoul.data.CourseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private String[] navItems = {"관광지 정보", "음식점 정보", "숙박 정보"};
    private ListView listView;
    private LinearLayout drawer;
    private ViewPager slideViewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        recyclerView = (RecyclerView) findViewById(R.id.restaurant_recyclerview);
        drawerLayout = (DrawerLayout) findViewById(R.id.restaurant_drawerlayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.restaurant_drawer_list);
        drawer = (LinearLayout)findViewById(R.id.restaurant_drawer);
        slideViewPager = (ViewPager)findViewById(R.id.restaurant_viewpager);
        tabLayout = (TabLayout)findViewById(R.id.restaurant_tablayout);

        tabLayout.addTab(tabLayout.newTab().setText("역사/문화"));
        tabLayout.addTab(tabLayout.newTab().setText("공원"));
        tabLayout.addTab(tabLayout.newTab().setText("숲길"));

        tabLayout.addOnTabSelectedListener(this);
        slideViewPager.setAdapter(new SlideFragmentAdapter(getSupportFragmentManager()));

        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_menu_text, navItems));
        listView.setOnItemClickListener(new DrawerItemClickListener());

        //툴바 커스텀
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<CourseData> course = new ArrayList<>();

        String json = null;
        try{
            //식당json파일 데이터 가져오기
            InputStream is = getAssets().open("seoulrestaurant.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            json = new String(buffer,"UTF-8");
        }catch (IOException e){
            e.printStackTrace();
        }

        //코스 데이터에 텍스트랑 이미지, 콘텐츠 넣기
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray arrayData = jsonObject.getJSONArray("DATA");

            for(int i = 0; i < arrayData.length(); i++){
                JSONObject obj = arrayData.getJSONObject(i);
                CourseData courseData = new CourseData();
                courseData.setCourseTitle(obj.getString("sisulname"));
                courseData.setCourseContents(obj.getString("tel"));
                courseData.setCourseAddress(obj.getString("addr"));
                courseData.setCourseBarrierFree(new String[]{obj.getString("st1"),obj.getString("st2"),
                        obj.getString("st3"),obj.getString("st4"),obj.getString("st5"),
                        obj.getString("st6"),obj.getString("st7"),obj.getString("st8"),
                        obj.getString("st9"),obj.getString("st10"),obj.getString("st11"),
                        obj.getString("st12")});

                course.add(courseData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter = new CourseRecyclerViewAdapter(course, RestaurantActivity.this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    //툴바 메뉴 클릭 이벤트
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.toolbar_menu1:
                Toast.makeText(this, "1111",Toast.LENGTH_SHORT).show();
                break;
            case R.id.toolbar_menu2:
                Toast.makeText(this, "2222",Toast.LENGTH_SHORT).show();
                break;
            case R.id.toolbar_menu3:
                Toast.makeText(this, "3333",Toast.LENGTH_SHORT).show();
                break;
        }
        if(drawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    //네비메뉴 클릭 이벤트
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
            switch (position) {
                case 0:
                    startActivity(new Intent(RestaurantActivity.this,MainActivity.class));
                    break;
                case 1:
                    break;
                case 2:
                    startActivity(new Intent(RestaurantActivity.this,AccomoActivity.class));
                    break;
            }
            drawerLayout.closeDrawer(drawer);
        }
    }
}
