package com.nugu.uniseoul.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nugu.uniseoul.R;

public class SlideRecyclerViewAdapter extends RecyclerView.Adapter<SlideRecyclerViewAdapter.MyViewHolder> {

    Context context;

    public SlideRecyclerViewAdapter(Context context) {
        super();
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_slide, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideRecyclerViewAdapter.MyViewHolder holder, int position) {
        switch (position) {
            case 0:
                holder.imageView.setImageResource(R.drawable.background_seoul);
                TextView textView1 = new TextView(context);
                textView1.setText("#서울");
                textView1.setBackgroundResource(R.drawable.hashtag_box);
                textView1.setTypeface(null, Typeface.ITALIC);
                holder.linearLayout.addView(textView1);
                break;
            case 1:
                holder.imageView.setImageResource(R.drawable.goong);
                TextView textView2 = new TextView(context);
                textView2.setText("#경복궁");
                textView2.setBackgroundResource(R.drawable.hashtag_box);
                textView2.setTypeface(null, Typeface.ITALIC);
                holder.linearLayout.addView(textView2);
                break;
            case 2:
                holder.imageView.setImageResource(R.drawable.tower);
                TextView textView3 = new TextView(context);
                textView3.setText("#남산타워");
                textView3.setBackgroundResource(R.drawable.hashtag_box);
                textView3.setTypeface(null, Typeface.ITALIC);
                holder.linearLayout.addView(textView3);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private LinearLayout linearLayout;

        public MyViewHolder(View itemView){
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.slide_image);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.slide_hashtag_layout);
        }
    }
}
