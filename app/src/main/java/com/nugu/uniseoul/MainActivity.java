package com.nugu.uniseoul;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.nugu.uniseoul.adapter.MainFragmentAdapter;
import com.nugu.uniseoul.viewpager.SwipeViewPager;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private TabLayout tabLayout;
    private SwipeViewPager viewPager;
    private MainFragmentAdapter mainFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String user_name = (String) intent.getStringExtra("user_name");
        String user_email = (String) intent.getStringExtra("user_email");

        tabLayout = (TabLayout)findViewById(R.id.main_tablayout);
        viewPager = (SwipeViewPager)findViewById(R.id.main_viewpager);

        tabLayout.setSelectedTabIndicator(null);

        tabLayout.addOnTabSelectedListener(this);

        mainFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), user_name, user_email);
        viewPager.setAdapter(mainFragmentAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setPagingDisabled();
        tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
