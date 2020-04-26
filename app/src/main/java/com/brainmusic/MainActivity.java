package com.brainmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.brainmusic.adapter.MusicListAdapter;
import com.brainmusic.db.Music;
import com.brainmusic.util.DBUtil;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private MediaPlayer mediaPlayer;
    private DBUtil dbUtil;
    Button play,pause, reset;
    RecyclerView musicList;
    MusicListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayout();
        initMediaPlayer(); //初始化MediaPlayer
        initDataBase(); //初始化数据库
        showMusicList();

//        checkDataBase();//验证数据库是否有效

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    void initLayout(){
        //初始化组件
        musicList = findViewById(R.id.musicList);
        play = findViewById(R.id.start);
        pause = findViewById(R.id.pause);
        reset = findViewById(R.id.reset);
        //初始化音乐播放器
        mediaPlayer = new MediaPlayer();
        //初始化数据库工具类
        dbUtil = new DBUtil(this);

        //设置监听函数
        play.setOnClickListener(this);
        reset.setOnClickListener(this);
        pause.setOnClickListener(this);

        //初始化musicList
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        musicList.setLayoutManager(layoutManager);
        adapter = new MusicListAdapter(new ArrayList<Music>());
        musicList.setAdapter(adapter);

    }

    /**
     * 初始化数据库
     */
    void initDataBase(){
        //创建数据库
        LitePal.getDatabase();
        dbUtil.initMusicLibrary();


    }

    private void showMusicList() {
        adapter.mMusicList.clear();
        List<Music> list = LitePal.findAll(Music.class);
        adapter.mMusicList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start:
                if(!mediaPlayer.isPlaying())
                    mediaPlayer.start();
                break;
            case R.id.pause:
                if(mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                break;
            case R.id.reset:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.reset(); //重置
                    initMediaPlayer();
                }
                break;
            default:
                break;


        }
    }
    private void initMediaPlayer(){
        try{
            AssetManager assetManager = getAssets();
            AssetFileDescriptor afd = assetManager.openFd("Music/Collective - Missing.mp3");
            mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(), afd.getLength()); //指定播放音频的路径
            mediaPlayer.prepare(); //进入到准备状态
        }catch(IOException e){
            Log.e(TAG, "initMediaPlayer: there is some error");
            e.printStackTrace();
        }
    }

    private void checkDataBase() {
        List<Music> a = LitePal.findAll(Music.class);
        String musicList = "";
        int musicNum = 1;
        for(Music t : a){
            musicList += "Music "+musicNum +":" + t.getPath() ;
            musicNum ++;
        }
        Log.i(TAG, "onCreate: "+musicList);
    }

}

