package com.nugu.uniseoul.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nugu.uniseoul.MainActivity;
import com.nugu.uniseoul.R;
import com.nugu.uniseoul.adapter.CourseRecyclerViewAdapter;
import com.nugu.uniseoul.data.CourseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private CourseRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_course_category, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.main_recyclerview);

        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);

        List<CourseData> course = new ArrayList<>();

        String json1 = null;
        String json2 = null;
        try{
            //여행json파일1 데이터 가져오기
            InputStream is = getActivity().getAssets().open("seoultrip1.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            json1 = new String(buffer,"UTF-8");
            //여행json파일2 데이터 가져오기
            is = getActivity().getAssets().open("seoultrip2.json");
            size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
            json2 = new String(buffer,"UTF-8");
        }catch (IOException e){
            e.printStackTrace();
        }

        //코스 데이터에 텍스트랑 이미지, 콘텐츠 넣기
        try {
            JSONObject jsonObject1 = new JSONObject(json1);
            JSONArray arrayData1 = jsonObject1.getJSONArray("DATA");

            JSONObject jsonObject2 = new JSONObject(json2);
            JSONArray arrayData2 = jsonObject2.getJSONArray("DATA");

            for(int i = 0; i < arrayData1.length(); i++){
                JSONObject obj = arrayData1.getJSONObject(i);
                if(!obj.getString("theme_cd").equals("01")){
                    continue;
                }
                CourseData courseData = new CourseData();

                courseData.setCourseTitle(obj.getString("facility_nm"));
                courseData.setCourseImage(obj.getString("images"));
                courseData.setCourseHomepage(obj.getString("homepage"));
                courseData.setCourseTel(obj.getString("tel"));
                courseData.setCourseAddress(obj.getString("address"));
                courseData.setCourseBarrierFree(new String[]{"N","N","N","N","N","N","N","N","N","N","N","N"});
                courseData.setCourseTripBarrierFree(obj.getString("resources"));
                courseData.setCourseTheme(obj.getString("theme_cd"));
                courseData.setCourseTripBarrierFree(obj.getString("resources"));

                course.add(courseData);
            }
            for(int i = 0; i < arrayData2.length(); i++){
                JSONObject obj = arrayData2.getJSONObject(i);
                if(!obj.getString("theme_cd").equals("01")){
                    continue;
                }
                CourseData courseData = new CourseData();
                courseData.setCourseTitle(obj.getString("sisulname"));
                courseData.setCourseImage(obj.getString("images"));
                courseData.setCourseHomepage(obj.getString("homepage"));
                courseData.setCourseTel(obj.getString("tel"));
                courseData.setCourseAddress(obj.getString("addr"));
                courseData.setCourseBarrierFree(new String[]{obj.getString("st1"),obj.getString("st2"),
                        obj.getString("st3"),obj.getString("st4"),obj.getString("st5"),
                        obj.getString("st6"),obj.getString("st7"),obj.getString("st8"),
                        obj.getString("st9"),obj.getString("st10"),obj.getString("st11"),
                        obj.getString("st12")});
                courseData.setCourseTheme(obj.getString("theme_cd"));

                course.add(courseData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mAdapter = new CourseRecyclerViewAdapter(course, getActivity());
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
