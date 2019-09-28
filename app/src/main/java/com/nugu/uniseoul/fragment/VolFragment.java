package com.nugu.uniseoul.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nugu.uniseoul.R;
import com.nugu.uniseoul.adapter.VolRecyclerViewAdapter;
import com.nugu.uniseoul.data.VolData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VolFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<VolData> reviews;
    String[] reviewData = null;
    FirebaseAuth mAuth;
    String user_name;
    String user_email;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_vol,container,false);


        //recyclerView
        recyclerView = (RecyclerView) viewGroup.findViewById(R.id.vol_recyclerview);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        //
        Intent intent = getActivity().getIntent();

        Bundle bundle = intent.getExtras();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        user_name = user.getDisplayName();
        user_email = user.getEmail();


        Thread mThread = new Thread() {
            public void run() {
                parseVol("http://15.164.80.191:3000/vol_list/",user_name, user_email);
                //Thread.interrupted();
            }
        };

        mThread.start();
        try{
            mThread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        return viewGroup;
    }

    protected void parseVol(String addr,String user_name, String user_email){

        String receiveMsg;

        String url = addr;
        Log.d("url",url);

       InputStream is = null;
        try {
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String str;
            StringBuffer buffer = new StringBuffer();
            while ((str = rd.readLine()) != null) {
                buffer.append(str);
            }

            try {

                JSONObject jsonObj;
                jsonObj = new JSONObject(buffer.toString());
                Log.d("buffer",buffer.toString());
                JSONArray arrayDocs = jsonObj.getJSONArray("docs");

                List<VolData> volDatas = new ArrayList<>();

                for (int i = 0, j = arrayDocs.length(); i < j; i++) {
                    JSONObject obj = arrayDocs.getJSONObject(i);

                    Log.d("vol_data", obj.toString());
                    //분류
                    VolData volData = new VolData();

                    volData.setV_num(obj.getString("v_num"));
                    volData.setTitle(obj.getString("v_title"));
                    volData.setContent(obj.getString("v_content"));

                    volData.setS_date(obj.getString("s_date"));
                    volData.setE_date(obj.getString("e_date"));

                    volData.setMax_helper(obj.getString("max_helper"));
                    volData.setCurrent_helper(obj.getString("current_helper"));

                    volData.setMax_uni(obj.getString("max_uni"));
                    volData.setCurrent_uni(obj.getString("current_uni"));

                    volData.setR_date(obj.getString("r_date"));
                    volData.setR_time(obj.getString("r_time"));

                    volData.setPlace(obj.getString("v_place"));


                    volDatas.add(volData);
                }

                mAdapter = new VolRecyclerViewAdapter(volDatas,user_name,user_email, getActivity());
                recyclerView.setAdapter(mAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        catch (java.io.FileNotFoundException e1) {

            List<VolData> volDatas = new ArrayList<>();
            VolData volData = new VolData();

            volData.setTitle("진행중인 서비스가 없습니다.");
            volData.setContent("\n No service in progress  \n" );
            volDatas.add(volData);

            mAdapter = new VolRecyclerViewAdapter(volDatas,user_name,user_email, getActivity());
            recyclerView.setAdapter(mAdapter);




            e1.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
