package com.nugu.uniseoul.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nugu.uniseoul.R;

public class MyPageFragment extends Fragment {

    String user_name;
    String user_email;

    public MyPageFragment(String user_name, String user_email){
        this.user_name = user_name;
        this.user_email = user_email;
        System.out.println("frag" +user_name + user_email);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_my_page,container,false);

        return viewGroup;
    }
}
