package com.nugu.uniseoul;

import android.content.Intent;
import android.content.res.Configuration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

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
import com.nugu.uniseoul.adapter.ContentsFragmentAdapter;
import com.nugu.uniseoul.adapter.SlideFragmentAdapter;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private String[] navItems = {"관광지 정보", "음식점 정보", "숙박 정보"};
    private ListView listView;
    private LinearLayout drawer;
    private ViewPager slideViewPager;
    private TabLayout tabLayout;
    private ViewPager contentsViewPager;
    private ContentsFragmentAdapter contentsFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.main_drawer_list);
        drawer = (LinearLayout)findViewById(R.id.main_drawer);
        slideViewPager = (ViewPager)findViewById(R.id.main_top_viewpager);
        tabLayout = (TabLayout)findViewById(R.id.main_tablayout);
        contentsViewPager = (ViewPager)findViewById(R.id.main_viewpager);

        tabLayout.addTab(tabLayout.newTab().setText("역사/문화"));
        tabLayout.addTab(tabLayout.newTab().setText("공원"));
        tabLayout.addTab(tabLayout.newTab().setText("숲길"));

        slideViewPager.setAdapter(new SlideFragmentAdapter(getSupportFragmentManager()));

        contentsFragmentAdapter = new ContentsFragmentAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        contentsViewPager.setAdapter(contentsFragmentAdapter);
        contentsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(this);

        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_menu_text, navItems));
        listView.setOnItemClickListener(new DrawerItemClickListener());

        //툴바 커스텀
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);
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
        contentsViewPager.setCurrentItem(tab.getPosition());
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
                    break;
                case 1:
                    startActivity(new Intent(MainActivity.this,RestaurantActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(MainActivity.this,AccomoActivity.class));
                    break;
            }
            drawerLayout.closeDrawer(drawer);
        }
    }
}
