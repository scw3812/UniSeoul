package com.nugu.uniseoul.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,10,0);
        switch (position) {
            case 0:
                holder.imageView.setImageResource(R.drawable.grass_festival);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://korean.visitkorea.or.kr/detail/fes_detail.do?cotid=612732b5-43e4-4410-9cbd-1cc28c5782e9"));
                        context.startActivity(intent);
                    }
                });

                TextView textView1 = new TextView(context);
                textView1.setText("   #억새   ");
                textView1.setBackgroundResource(R.drawable.hashtag_box);
                textView1.setTypeface(null, Typeface.ITALIC);
                textView1.setLayoutParams(params);
                holder.linearLayout.addView(textView1);

                TextView textView4 = new TextView(context);
                textView4.setText("   #하늘공원   ");
                textView4.setBackgroundResource(R.drawable.hashtag_box);
                textView4.setTypeface(null, Typeface.ITALIC);
                textView4.setLayoutParams(params);
                holder.linearLayout.addView(textView4);

                TextView textView8 = new TextView(context);
                textView8.setText("   #올림픽공원   ");
                textView8.setBackgroundResource(R.drawable.hashtag_box);
                textView8.setTypeface(null, Typeface.ITALIC);
                holder.linearLayout.addView(textView8);
                break;
            case 1:
                holder.imageView.setImageResource(R.drawable.goong_moonlight);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://korean.visitkorea.or.kr/detail/fes_detail.do?cotid=eaf1594a-6f7c-4152-87f0-a4350d5df4f7"));
                        context.startActivity(intent);
                    }
                });

                TextView textView2 = new TextView(context);
                textView2.setText("   #창덕궁   ");
                textView2.setBackgroundResource(R.drawable.hashtag_box);
                textView2.setTypeface(null, Typeface.ITALIC);
                textView2.setLayoutParams(params);
                holder.linearLayout.addView(textView2);

                TextView textView5 = new TextView(context);
                textView5.setText("   #야경   ");
                textView5.setBackgroundResource(R.drawable.hashtag_box);
                textView5.setTypeface(null, Typeface.ITALIC);
                textView5.setLayoutParams(params);
                holder.linearLayout.addView(textView5);

                TextView textView7 = new TextView(context);
                textView7.setText("   #달빛기행   ");
                textView7.setBackgroundResource(R.drawable.hashtag_box);
                textView7.setTypeface(null, Typeface.ITALIC);
                holder.linearLayout.addView(textView7);
                break;
            case 2:
                holder.imageView.setImageResource(R.drawable.dobong_festival);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dobongsanf.com/"));
                        context.startActivity(intent);
                    }
                });

                TextView textView3 = new TextView(context);
                textView3.setText("   #도봉산   ");
                textView3.setBackgroundResource(R.drawable.hashtag_box);
                textView3.setTypeface(null, Typeface.ITALIC);
                textView3.setLayoutParams(params);
                holder.linearLayout.addView(textView3);

                TextView textView6 = new TextView(context);
                textView6.setText("   #가을축제   ");
                textView6.setBackgroundResource(R.drawable.hashtag_box);
                textView6.setTypeface(null, Typeface.ITALIC);
                holder.linearLayout.addView(textView6);
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
