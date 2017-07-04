package com.videoviewer.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.videoviewer.R;
import com.videoviewer.adapter.VideoCategoryRecyclerAdapter;
import com.videoviewer.config.Config;
import com.videoviewer.constants.Constants;
import com.videoviewer.entity.VideoCategoryInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ankita.shukla on 6/22/2017.
 */

public class TrandingFragment extends Fragment {

    private VideoCategoryRecyclerAdapter videoRecyclerAdapter;
    private List<VideoCategoryInfo> videoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tranding_layout, container, false);
        videoList = new ArrayList<>();
        RecyclerView recycler = (RecyclerView) v.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        videoRecyclerAdapter = new VideoCategoryRecyclerAdapter(getActivity(), videoList, R.layout.list_row_video);
        recycler.setAdapter(videoRecyclerAdapter);
        new LoadVideoData().execute("");
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    class LoadVideoData extends AsyncTask<String, String, String> {
        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(Constants.API_LINK + "videos?part=snippet&chart=mostPopular&regionCode=IN&key=" + Config.YOUTUBE_API_KEY+ "&maxResults=20");
                https://www.googleapis.com/youtube/v3/videos?chart=mostPopular&key=AIzaSyD4symUHxxPQAYgcaqXmC5kGjt5Gdc65rY&part=snippet&maxResults=4
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
                        String defaultURL;
                        String highUrl;
                        if (snippet.isNull("thumbnails")) {
                            defaultURL = null;
                            highUrl = null;
                        } else {
                            JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                            JSONObject defaultThumbnail = thumbnails.getJSONObject("medium");
                            defaultURL = defaultThumbnail.getString("url");
                            JSONObject defaultThumbnailLarge = thumbnails.getJSONObject("high");
                            highUrl = defaultThumbnailLarge.getString("url");
                        }
                        String title = snippet.getString("title");
                        String description = snippet.getString("description");
                        VideoCategoryInfo info = new VideoCategoryInfo();
                        info.setId(id);
                        info.setUrlLarge(highUrl);
                        info.setTitle(title);
                        if (defaultURL != null)
                            info.setImage(defaultURL);
                        info.setDescription(description);
                        videoList.add(info);
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
            videoRecyclerAdapter.notifyDataSetChanged();
        }
    }
}
