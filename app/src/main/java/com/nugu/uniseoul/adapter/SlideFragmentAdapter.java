package com.nugu.uniseoul.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.nugu.uniseoul.fragment.SlideFragment;

public class SlideFragmentAdapter extends FragmentStatePagerAdapter {

    public SlideFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
            case 1:
            case 2:
                return new SlideFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}