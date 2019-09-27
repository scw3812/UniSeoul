package com.nugu.uniseoul.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.nugu.uniseoul.viewpager.LinePagerIndicatorDecoration;
import com.nugu.uniseoul.R;
import com.nugu.uniseoul.adapter.SlideRecyclerViewAdapter;


public class SlideFragment extends Fragment {

    private RecyclerView recyclerView;
    private PagerSnapHelper pagerSnapHelper;
    private LinearLayoutManager layoutManager;
    private Handler handler;
    private Runnable runnable;
    private ImageView arrowLeft, arrowRight;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_slide, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.slide_recyclerview);
        arrowLeft = (ImageView)view.findViewById(R.id.slide_arrow_left);
        arrowRight = (ImageView)view.findViewById(R.id.slide_arrow_right);

        arrowLeft.bringToFront();
        arrowRight.bringToFront();

        arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutManager.findFirstVisibleItemPosition() != 0){
                    recyclerView.scrollToPosition(layoutManager.findFirstVisibleItemPosition()-1);
                }
            }
        });
        arrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutManager.findFirstVisibleItemPosition() != 2){
                    recyclerView.scrollToPosition(layoutManager.findFirstVisibleItemPosition()+1);
                }
            }
        });

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        pagerSnapHelper = new PagerSnapHelper();
        if(handler == null){
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    if(layoutManager.findFirstVisibleItemPosition() != 2){
                        recyclerView.smoothScrollToPosition(layoutManager.findFirstVisibleItemPosition() + 1);
                    }else{
                        recyclerView.smoothScrollToPosition(0);
                    }
                    handler.postDelayed(this, 3000);
                }
            };
        }
        handler.postDelayed(runnable,3000);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new SlideRecyclerViewAdapter(getContext()));
        recyclerView.addItemDecoration(new LinePagerIndicatorDecoration());
        pagerSnapHelper.attachToRecyclerView(recyclerView);
        return view;
    }
}
