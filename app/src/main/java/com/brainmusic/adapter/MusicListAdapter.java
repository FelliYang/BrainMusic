package com.brainmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.brainmusic.MainActivity;
import com.brainmusic.R;
import com.brainmusic.db.Music;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
    public List<Music> mMusicList;
    private Context mContext;
    private int num = 1;
    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView musicName;
        public ImageView musicImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.musicName = itemView.findViewById(R.id.music_name);
            this.musicImg = itemView.findViewById(R.id.list_img);
        }
    }

    public MusicListAdapter(List<Music> musicList) {
        mMusicList = musicList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext = parent.getContext();
        }
        final View view = LayoutInflater.from(mContext).inflate(R.layout.musiclist_item_layout,parent,false);
        final ViewHolder viewHolder =  new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Music music = mMusicList.get(position);
        holder.musicName.setText(music.getName());
        //holder.musicImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.a2));
        String iconname="a" + num;
        int resID=mContext.getResources().getIdentifier(iconname, "drawable",  mContext.getPackageName());
        holder.musicImg.setImageResource(resID);
        holder.musicName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) mContext;
                activity.initMediaPlayer(music.getPath());
                activity.musicPlayer.start();
                activity.pauseOrPlay.setImageDrawable(activity.getResources().getDrawable(R.drawable.pause));
                activity.musicName.setText(music.getName());
            }
        });
        if(this.num <= 25){
            this.num ++;
        } else {
            this.num = 1;
        }
    }

    @Override
    public int getItemCount() {
        return mMusicList.size();
    }
}
