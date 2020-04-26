package com.brainmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView musicName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.musicName = itemView.findViewById(R.id.music_name);
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
    }

    @Override
    public int getItemCount() {
        return mMusicList.size();
    }
}
