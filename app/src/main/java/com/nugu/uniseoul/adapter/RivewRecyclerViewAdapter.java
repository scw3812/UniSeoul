package com.nugu.uniseoul.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nugu.uniseoul.CourseActivity;
import com.nugu.uniseoul.R;
import com.nugu.uniseoul.data.CourseData;
import com.nugu.uniseoul.data.ReviewData;

import java.util.HashMap;
import java.util.List;

public class RivewRecyclerViewAdapter extends RecyclerView.Adapter<RivewRecyclerViewAdapter.MyViewHolder>{

    private List<ReviewData>  mDataset;
    private Context context;

    //줄의 요소
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title_textview;
        private TextView content_textview;


        public MyViewHolder(View v) {
            super(v);
            title_textview = (TextView)v.findViewById(R.id.rowTitle);
            content_textview = (TextView) v.findViewById(R.id.rowContent);
        }
    }

    //constructor
    public RivewRecyclerViewAdapter(List<ReviewData> myDataset, Context context) {
        mDataset = myDataset; //{"_id":"5d6f240ab8f831638ea91d60","id":"서울시립미술관","title":"test","content":"test","__v":0}
        this.context = context;
    }

    //1row
    @Override
    public RivewRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_review, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ReviewData review = mDataset.get(position);


        holder.title_textview.setText(review.getTitle());
        holder.content_textview.setText(review.getContent());

    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }
}
