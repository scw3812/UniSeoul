package com.nugu.uniseoul.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        View view = LayoutInflater.from(context).inflate(R.layout.slide_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideRecyclerViewAdapter.MyViewHolder holder, int position) {
        switch (position) {
            case 0:
                holder.imageView.setImageResource(R.drawable.background_seoul);
                break;
            case 1:
                holder.imageView.setImageResource(R.drawable.goong);
                holder.textView.setText("경복궁 야간개장");
                break;
            case 2:
                holder.imageView.setImageResource(R.drawable.tower);
                holder.textView.setText("남산타워");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView textView;

        public MyViewHolder(View itemView){
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.slide_image);
            textView = (TextView)itemView.findViewById(R.id.slide_title);

        }
    }
}
