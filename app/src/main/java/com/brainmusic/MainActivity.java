package com.brainmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private MediaPlayer mediaPlayer;
    Button play,pause, reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayout();
        initMediaPlayer(); //初始化MediaPlayer

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
        play = findViewById(R.id.start);
        pause = findViewById(R.id.pause);
        reset = findViewById(R.id.reset);
        //初始化音乐播放器
        mediaPlayer = new MediaPlayer();

        //设置监听函数
        play.setOnClickListener(this);
        reset.setOnClickListener(this);
        pause.setOnClickListener(this);
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
            AssetFileDescriptor afd = assetManager.openFd("Collective - Missing.mp3");
            mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(), afd.getLength()); //指定播放音频的路径
            mediaPlayer.prepare(); //进入到准备状态
        }catch(IOException e){
            Log.e(TAG, "initMediaPlayer: there is some error");
            e.printStackTrace();
        }
    }
}

