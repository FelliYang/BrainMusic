package com.brainmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brainmusic.adapter.MusicListAdapter;
import com.brainmusic.custom.MusicPlayer;
import com.brainmusic.db.Music;
import com.brainmusic.util.DBUtil;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    public MusicPlayer musicPlayer;
    private DBUtil dbUtil;
    Button play,pause, reset;
    RecyclerView musicList;
    MusicListAdapter adapter;
    public ImageView pauseOrPlay; //播放、暂停按钮
    public TextView musicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayout();
        initDataBase(); //初始化数据库
        showMusicList();

//        checkDataBase();//验证数据库是否有效

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(musicPlayer !=null){
            musicPlayer.release();
        }
    }

    void initLayout(){
        //初始化组件
        musicList = findViewById(R.id.musicList);
        play = findViewById(R.id.start);
        pause = findViewById(R.id.pause);
        reset = findViewById(R.id.reset);
        pauseOrPlay = findViewById(R.id.pause_or_paly);
        musicName = findViewById(R.id.music_name);
        //初始化音乐播放器
        musicPlayer = new MusicPlayer();
        //初始化数据库工具类
        dbUtil = new DBUtil(this);

        //设置监听函数
        play.setOnClickListener(this);
        reset.setOnClickListener(this);
        pause.setOnClickListener(this);
        pauseOrPlay.setOnClickListener(this);

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
                if(!musicPlayer.isPlaying())
                    musicPlayer.start();
                break;
            case R.id.pause:
                if(musicPlayer.isPlaying())
                    musicPlayer.pause();
                break;
            case R.id.reset:
                if(musicPlayer.isPlaying()){
                    musicPlayer.reset(); //重置
//                    initMediaPlayer();
                }
                break;
            case R.id.pause_or_paly:
                if(musicPlayer.status==MusicPlayer.START){
                    musicPlayer.pause();
                    pauseOrPlay.setImageDrawable(getResources().getDrawable(R.drawable.play));
                }else if(musicPlayer.status==MusicPlayer.PAUSE){
                    musicPlayer.start();
                    pauseOrPlay.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                }
                break;
            default:
                break;


        }
    }

    /**
     * 使用指定路径的mp3文件初始化mediaPlayer
     * @param musicFilePath
     */
    public void initMediaPlayer(String musicFilePath){
        try{
            musicPlayer.reset();
            AssetManager assetManager = getAssets();
            AssetFileDescriptor afd = assetManager.openFd(musicFilePath);
            musicPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(), afd.getLength()); //指定播放音频的路径
            musicPlayer.prepare(); //进入到准备状态
            musicPlayer.setLooping(true); //循环播放
        }catch(IOException e){
            Toast.makeText(this, "音乐文件无法播放", Toast.LENGTH_SHORT).show();
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

