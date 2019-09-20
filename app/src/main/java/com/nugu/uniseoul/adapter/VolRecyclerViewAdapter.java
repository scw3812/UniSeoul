package com.nugu.uniseoul.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nugu.uniseoul.CourseActivity;
import com.nugu.uniseoul.R;
import com.nugu.uniseoul.VolActivity;
import com.nugu.uniseoul.VolListActivity;
import com.nugu.uniseoul.data.ReviewData;
import com.nugu.uniseoul.data.VolData;

import java.util.List;

public class VolRecyclerViewAdapter extends RecyclerView.Adapter<VolRecyclerViewAdapter.MyViewHolder>{

    private List<VolData>  mDataset;
    private Context context;
    private View.OnClickListener mOnClickListener;

    public String user_name;
    public String user_email;

    //줄의 요소
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title_textview;
        private TextView content_textview;


        public MyViewHolder(View v) {
            super(v);
            title_textview = (TextView)v.findViewById(R.id.rowTitleVol);
            content_textview = (TextView) v.findViewById(R.id.rowContentVol);
        }
    }

    //constructor

/*
    public VolRecyclerViewAdapter(List<VolData> myDataset, Context context) {
        mDataset = myDataset; //{"_id":"5d6f240ab8f831638ea91d60","id":"서울시립미술관","title":"test","content":"test","__v":0}
        this.context = context;
    }
*/
    public VolRecyclerViewAdapter(List<VolData> myDataset,String user_name, String user_email, Context context) {
        mDataset = myDataset; //{"_id":"5d6f240ab8f831638ea91d60","id":"서울시립미술관","title":"test","content":"test","__v":0}
        this.context = context;
        this.user_name = user_name;
        this.user_email = user_email;
        System.out.println("user id : " + user_name + "user_email "+user_email);
    }


    //1row
    @Override
    public VolRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_vol, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        VolData volData = mDataset.get(position);


        holder.title_textview.setText(volData.getTitle());
        holder.content_textview.setText(volData.getContent());


        holder.itemView.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), VolListActivity.class);

                intent.putExtra("volData",volData);
                intent.putExtra("user_id",user_name);
                intent.putExtra("user_email",user_email);
                v.getContext().startActivity(intent);

            }
        });
/*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), VolListActivity.class);
                intent.putExtra("volData",volData);
                intent.putExtra("user_id",user_name);
                intent.putExtra("user_email",user_email);
                v.getContext().startActivity(intent);
            }
        });
*/
    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }
}
