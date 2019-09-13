package com.nugu.uniseoul.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.nugu.uniseoul.fragment.HistoryFragment;
import com.nugu.uniseoul.fragment.ParkFragment;
import com.nugu.uniseoul.fragment.TrailFragment;

public class ContentsFragmentAdapter extends FragmentStatePagerAdapter {

    private int mPageCount;

    public ContentsFragmentAdapter(FragmentManager fm, int pageCount){
        super(fm);
        this.mPageCount = pageCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                HistoryFragment historyFragment = new HistoryFragment();
                return historyFragment;
            case 1 :
                ParkFragment parkFragment = new ParkFragment();
                return parkFragment;
            case 2 :
                TrailFragment trailFragment = new TrailFragment();
                return trailFragment;
            default :
                return null;
        }

    }

    @Override
    public int getCount() {
        return mPageCount;
    }
}
