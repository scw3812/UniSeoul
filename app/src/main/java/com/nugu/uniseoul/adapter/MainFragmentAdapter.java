package com.nugu.uniseoul.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.nugu.uniseoul.fragment.CourseFragment;
import com.nugu.uniseoul.fragment.MyPageFragment;
import com.nugu.uniseoul.fragment.VolFragment;

public class MainFragmentAdapter extends FragmentStatePagerAdapter {

    private int mPageCount;
    private String user_name;
    private String user_email;

    public MainFragmentAdapter(FragmentManager fm, int pageCount, String user_name, String user_email){
        super(fm);
        this.mPageCount = pageCount;
        this.user_name = user_name;
        this.user_email = user_email;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                CourseFragment courseFragment = new CourseFragment();
                return courseFragment;
            case 1 :
                VolFragment volFragment = new VolFragment();
                return volFragment;
            case 2 :
                MyPageFragment myPageFragment = new MyPageFragment();
                return myPageFragment;
            default :
                return null;
        }

    }

    @Override
    public int getCount() {
        return mPageCount;
    }

}
