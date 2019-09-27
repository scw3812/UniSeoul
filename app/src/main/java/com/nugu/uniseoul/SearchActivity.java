package com.nugu.uniseoul;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.nugu.uniseoul.adapter.CourseRecyclerViewAdapter;
import com.nugu.uniseoul.data.CourseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CourseRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            SearchView searchView = (SearchView) findViewById(R.id.search_text);
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setQueryHint("여행지 검색");
            searchView.setIconifiedByDefault(false);
            searchView.setSubmitButtonEnabled(true);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                @Override
                public boolean onQueryTextSubmit(String query) {
                    query = query.toLowerCase();
                    makeAdapater(query);
                    if(mAdapter != null){
                        mAdapter.notifyDataSetChanged();
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            if (searchManager != null) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            }
        }
    }

    public void makeAdapater(String search) {

        recyclerView = (RecyclerView) findViewById(R.id.search_recyclerview);

        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        List<CourseData> course = new ArrayList<>();

        String json1 = null;
        String json2 = null;
        try {
            //여행json파일1 데이터 가져오기
            InputStream is = getAssets().open("seoultrip1.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            json1 = new String(buffer, "UTF-8");
            //여행json파일2 데이터 가져오기
            is = getAssets().open("seoultrip2.json");
            size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
            json2 = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //코스 데이터에 텍스트랑 이미지, 콘텐츠 넣기
        try {
            JSONObject jsonObject1 = new JSONObject(json1);
            JSONArray arrayData1 = jsonObject1.getJSONArray("DATA");

            JSONObject jsonObject2 = new JSONObject(json2);
            JSONArray arrayData2 = jsonObject2.getJSONArray("DATA");

            for (int i = 0; i < arrayData1.length(); i++) {
                JSONObject obj = arrayData1.getJSONObject(i);
                if (!obj.getString("facility_nm").toLowerCase().contains(search)) {
                    continue;
                }
                CourseData courseData = new CourseData();
                courseData.setCourseTitle(obj.getString("facility_nm"));
                courseData.setCourseImage(obj.getString("images"));
                courseData.setCourseHomepage(obj.getString("homepage"));
                courseData.setCourseTel(obj.getString("tel"));
                courseData.setCourseAddress(obj.getString("address"));
                courseData.setCourseBarrierFree(new String[]{"N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N", "N"});
                courseData.setCourseTripBarrierFree(obj.getString("resources").split("\"")[3]);
                courseData.setCourseTheme(obj.getString("theme_cd"));
                courseData.setCourseTripBarrierFree(obj.getString("resources"));

                course.add(courseData);
            }
            for (int i = 0; i < arrayData2.length(); i++) {
                JSONObject obj = arrayData2.getJSONObject(i);
                if (!obj.getString("sisulname").toLowerCase().contains(search)) {
                    continue;
                }
                CourseData courseData = new CourseData();
                courseData.setCourseTitle(obj.getString("sisulname"));
                courseData.setCourseImage(obj.getString("images"));
                courseData.setCourseHomepage(obj.getString("homepage"));
                courseData.setCourseTel(obj.getString("tel"));
                courseData.setCourseAddress(obj.getString("addr"));
                courseData.setCourseBarrierFree(new String[]{obj.getString("st1"), obj.getString("st2"),
                        obj.getString("st3"), obj.getString("st4"), obj.getString("st5"),
                        obj.getString("st6"), obj.getString("st7"), obj.getString("st8"),
                        obj.getString("st9"), obj.getString("st10"), obj.getString("st11"),
                        obj.getString("st12")});
                courseData.setCourseTheme(obj.getString("theme_cd"));

                course.add(courseData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("confirm", Integer.toString(course.size()));

        if (course.size() == 0) {
            Toast.makeText(SearchActivity.this, "검색결과가 없습니다.", Toast.LENGTH_LONG).show();
        } else {
            mAdapter = new CourseRecyclerViewAdapter(course, SearchActivity.this);
        }
        recyclerView.setAdapter(mAdapter);
    }
}
