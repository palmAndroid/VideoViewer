package com.videoviewer.adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.videoviewer.MainActivity;
import com.videoviewer.R;
import com.videoviewer.VideoViewActivity;
import com.videoviewer.entity.VideoCategoryInfo;
import java.util.List;

/**
 * Created by ankita.shukla on 6/23/2017.
 */

public class VideoCategoryRecyclerAdapter extends RecyclerView.Adapter<VideoCategoryRecyclerAdapter.ViewHolder> {
    private List<VideoCategoryInfo> mVideoCategoryInfo;
    private int itemLayout;
    public static Context context;

    public VideoCategoryRecyclerAdapter(Context contex, List<VideoCategoryInfo> log, int itemLayout) {
        context = contex;
        this.mVideoCategoryInfo = log;
        this.itemLayout = itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final VideoCategoryInfo item = mVideoCategoryInfo.get(position);
        final MainActivity mainActivity = (MainActivity) context;
        holder.itemView.setTag(item);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        Picasso.with(context).load(item.getImage()).resize(convertDpToPx(145), convertDpToPx(81)).into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(mainActivity, VideoViewActivity.class);
                    intent.putExtra("VIDEO_ID", item.getId());
                    intent.putExtra("VIDEO_TITLE", item.getTitle());
                    intent.putExtra("VIDEO_DESCRIPTION", item.getDescription());
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

    public void add(VideoCategoryInfo item) {
        mVideoCategoryInfo.add(item);
    }

    public void remove(VideoCategoryInfo item) {
        int position = mVideoCategoryInfo.indexOf(item);
        mVideoCategoryInfo.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public ImageView image;

        public ViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            image = (ImageView) itemView.findViewById(R.id.image);

        }
    }
}