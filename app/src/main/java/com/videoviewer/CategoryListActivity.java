package com.videoviewer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.videoviewer.adapter.CategoryLoadAdapter;
import com.videoviewer.entity.CategoryInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ankita.shukla on 6/23/2017.
 */

public class CategoryListActivity extends AppCompatActivity {

    private List<CategoryInfo> categoryList;
    CategoryLoadAdapter categoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cat_list_layout);
        categoryList = new ArrayList<>();

        RecyclerView recycler = (RecyclerView)v.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter = new CategoryLoadAdapter(this, categoryList, R.layout.cat_row_video);
        recycler.setAdapter(categoryAdapter);

    }
}
