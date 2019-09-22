package com.nugu.uniseoul;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.nugu.uniseoul.adapter.ContentsFragmentAdapter;
import com.nugu.uniseoul.adapter.SlideFragmentAdapter;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private Toolbar toolbar;
    private ViewPager slideViewPager;
    private TabLayout courseTabLayout;
    private ViewPager contentsViewPager;
    private ContentsFragmentAdapter contentsFragmentAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        slideViewPager = (ViewPager)findViewById(R.id.main_top_viewpager);
        courseTabLayout = (TabLayout)findViewById(R.id.main_course_tablayout);
        contentsViewPager = (ViewPager)findViewById(R.id.main_viewpager);
        tabLayout = (TabLayout)findViewById(R.id.main_tablayout);

        courseTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#000000"));
        tabLayout.setSelectedTabIndicator(null);

        slideViewPager.setAdapter(new SlideFragmentAdapter(getSupportFragmentManager()));

        tabLayout.addOnTabSelectedListener(this);

        contentsFragmentAdapter = new ContentsFragmentAdapter(getSupportFragmentManager(), courseTabLayout.getTabCount());
        contentsViewPager.setAdapter(contentsFragmentAdapter);
        contentsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(courseTabLayout));
        courseTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        });

        //툴바 커스텀
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
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
}
