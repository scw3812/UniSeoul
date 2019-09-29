package com.nugu.uniseoul.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nugu.uniseoul.R;
import com.nugu.uniseoul.data.BusData;
import com.nugu.uniseoul.data.Code;

import java.util.List;

public class BusRouteRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<BusData> mDataset;
    private String rtNm;

    public BusRouteRecyclerViewAdapter(Context context, List<BusData> mDataset, String rtNm) {
        this.context = context;
        this.mDataset = mDataset;
        this.rtNm = rtNm;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType == Code.ViewType.NAME){
            view = inflater.inflate(R.layout.row_bus_name,parent,false);
            return new NameViewHolder(view);
        }else{
            view = inflater.inflate(R.layout.row_bus_route,parent,false);
            return new RouteViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof NameViewHolder){
            ((NameViewHolder)holder).nameTextView.setText(rtNm);
            ((NameViewHolder)holder).directionTextView.setText(mDataset.get(position).getString());
        }else{
            ((RouteViewHolder)holder).textView.setText(mDataset.get(position).getString());
        }
    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();
    }

    public class RouteViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = (TextView)itemView.findViewById(R.id.bus_route_recyclerview_textview);
        }
    }

    public class NameViewHolder extends RecyclerView.ViewHolder{

        private TextView nameTextView;
        private TextView directionTextView;

        public NameViewHolder(@NonNull View itemView){
            super(itemView);

            nameTextView = (TextView)itemView.findViewById(R.id.bus_route_recyclerview_name);
            directionTextView = (TextView)itemView.findViewById(R.id.bus_route_recyclerview_direction);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position).getViewType();
    }
}
