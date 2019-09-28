package com.nugu.uniseoul.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nugu.uniseoul.CourseActivity;
import com.nugu.uniseoul.R;
import com.nugu.uniseoul.data.CourseData;

import java.util.List;

public class CourseRecyclerViewAdapter extends RecyclerView.Adapter<CourseRecyclerViewAdapter.MyViewHolder>{
    private List<CourseData> mDataset;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView course_textview;
        private ImageView course_imageview;
        private ImageView course_accessible;

        public MyViewHolder(View v) {
            super(v);

            course_textview = (TextView)v.findViewById(R.id.course_textview);
            course_imageview = (ImageView) v.findViewById(R.id.course_imageview);
            course_accessible = (ImageView)v.findViewById(R.id.course_accessible);

            course_accessible.bringToFront();
        }
    }

    public CourseRecyclerViewAdapter(List<CourseData> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    @Override
    public CourseRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_course, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //뷰에 코스데이터 값 넣기
        final CourseData course = mDataset.get(position);
        holder.course_textview.setText(course.getCourseTitle());
        Glide.with(context).load(course.getCourseImage()).placeholder(R.drawable.uniseoul_placeholder).into(holder.course_imageview);
        if(course.getCourseTripBarrierFree() == null){
            holder.course_accessible.setVisibility(View.INVISIBLE);
        }
        if(course.getCourseTripBarrierFree() != null){
            holder.course_accessible.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CourseActivity.class);
                intent.putExtra("course",course);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }
}
