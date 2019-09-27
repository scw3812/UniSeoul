package com.nugu.uniseoul.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nugu.uniseoul.MainActivity;
import com.nugu.uniseoul.R;
import com.nugu.uniseoul.SearchActivity;
import com.nugu.uniseoul.adapter.ContentsFragmentAdapter;
import com.nugu.uniseoul.adapter.SlideFragmentAdapter;


public class CourseFragment extends Fragment {

    private Toolbar toolbar;
    private ViewPager slideViewPager;
    private TabLayout courseTabLayout;
    private ViewPager contentsViewPager;
    private ContentsFragmentAdapter contentsFragmentAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_course, container, false);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        slideViewPager = (ViewPager) rootView.findViewById(R.id.main_course_top_viewpager);
        courseTabLayout = (TabLayout) rootView.findViewById(R.id.main_course_tablayout);
        contentsViewPager = (ViewPager) rootView.findViewById(R.id.main_course_viewpager);

        courseTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#0C3218"));

        slideViewPager.setAdapter(new SlideFragmentAdapter(getChildFragmentManager()));
        slideViewPager.bringToFront();

        contentsFragmentAdapter = new ContentsFragmentAdapter(getChildFragmentManager(), courseTabLayout.getTabCount());
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

        toolbar.inflateMenu(R.menu.menu);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("");
        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
