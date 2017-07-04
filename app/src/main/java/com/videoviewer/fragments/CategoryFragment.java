package com.videoviewer.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.videoviewer.R;
import com.videoviewer.adapter.CategoryLoadAdapter;
import com.videoviewer.adapter.VideoCategoryRecyclerAdapter;
import com.videoviewer.config.Config;
import com.videoviewer.constants.Constants;
import com.videoviewer.entity.CategoryInfo;
import com.videoviewer.entity.VideoCategoryInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ankita.shukla on 6/22/2017.
 */

public class CategoryFragment extends Fragment{
    private List<CategoryInfo> categoryList;
    CategoryLoadAdapter categoryAdapter;
    public CategoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v =  inflater.inflate(R.layout.category_layout, container, false);
        categoryList = new ArrayList<>();

        RecyclerView recycler = (RecyclerView)v.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoryAdapter = new CategoryLoadAdapter(getActivity(), categoryList, R.layout.cat_row_video);
        recycler.setAdapter(categoryAdapter);
        new CategoryLoadData().execute("");
        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    class CategoryLoadData extends AsyncTask<String, String, String> {
        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();
            try {
                /**
                 * https://www.googleapis.com/youtube/v3/videoCategories?part=snippet&regionCode=IN&key=AIzaSyD4symUHxxPQAYgcaqXmC5kGjt5Gdc65rY
                 */
                URL url = new URL(Constants.API_LINK + "videoCategories?part=snippet&regionCode=IN&key=" + Config.YOUTUBE_API_KEY);
               urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                try {
                    JSONObject json = new JSONObject(result.toString());
                    JSONArray items = json.getJSONArray("items");
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject data = items.getJSONObject(i);
                        String id = data.getString("id");
                        JSONObject snippet = data.getJSONObject("snippet");

                        String title = snippet.getString("title");
                        String channelId = snippet.getString("channelId");
                        CategoryInfo info = new CategoryInfo();
                        info.setId(id);
                        info.setChannel_id(channelId);
                        info.setTitle(title);
                        categoryList.add(info);
                    }
                } catch (JSONException e) {
                    Log.e("JSON_EXCEPTION","exception:"+e);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            categoryAdapter.notifyDataSetChanged();
        }
    }
}
