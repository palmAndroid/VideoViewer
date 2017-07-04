package com.videoviewer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.videoviewer.CategoryListActivity;
import com.videoviewer.MainActivity;
import com.videoviewer.R;
import com.videoviewer.entity.CategoryInfo;
import com.videoviewer.entity.VideoCategoryInfo;

import java.util.List;

/**
 * Created by ankita.shukla on 6/23/2017.
 */

public class CategoryLoadAdapter extends RecyclerView.Adapter<CategoryLoadAdapter.ViewHolder> {
    private List<CategoryInfo> mVideoCategoryInfo;
    private int itemLayout;
    public static Context context;

    public CategoryLoadAdapter(Context contex, List<CategoryInfo> log, int itemLayout) {
        context = contex;
        this.mVideoCategoryInfo = log;
        this.itemLayout = itemLayout;
    }

    @Override
    public CategoryLoadAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new CategoryLoadAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CategoryLoadAdapter.ViewHolder holder, final int position) {
        final CategoryInfo item = mVideoCategoryInfo.get(position);
        holder.itemView.setTag(item);
        holder.title.setText(item.getTitle());

       holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryListActivity.class);
                intent.putExtra("CHANNEL_ID", item.getId());
                intent.putExtra("TITLE", item.getTitle());
                context.startActivity(intent);
            }
        });
    }

    private int convertDpToPx(int dp) {
        return Math.round(dp * (context.getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public int getItemCount() {
        return mVideoCategoryInfo.size();
    }

    public void add(CategoryInfo item) {
        mVideoCategoryInfo.add(item);
    }

    public void remove(CategoryInfo item) {
        int position = mVideoCategoryInfo.indexOf(item);
        mVideoCategoryInfo.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public CardView card;
        public ViewHolder(final View itemView) {
            super(itemView);
            card = (CardView)itemView.findViewById(R.id.card_view);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}