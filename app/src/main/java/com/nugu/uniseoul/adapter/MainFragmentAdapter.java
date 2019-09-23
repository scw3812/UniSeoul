package com.nugu.uniseoul.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.nugu.uniseoul.MapActivity;
import com.nugu.uniseoul.fragment.CourseFragment;
import com.nugu.uniseoul.fragment.HistoryFragment;
import com.nugu.uniseoul.fragment.MyPageFragment;
import com.nugu.uniseoul.fragment.ParkFragment;
import com.nugu.uniseoul.fragment.TrailFragment;
import com.nugu.uniseoul.fragment.VolFragment;

public class MainFragmentAdapter extends FragmentStatePagerAdapter {

    private int mPageCount;

    public MainFragmentAdapter(FragmentManager fm, int pageCount){
        super(fm);
        this.mPageCount = pageCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                CourseFragment courseFragment = new CourseFragment();
                return courseFragment;
            case 1 :
                MyPageFragment myPageFragment = new MyPageFragment();
                return myPageFragment;
            case 2 :
                VolFragment volFragment = new VolFragment();
                return volFragment;
            default :
                return null;
        }

    }

    @Override
    public int getCount() {
        return mPageCount;
    }

}
