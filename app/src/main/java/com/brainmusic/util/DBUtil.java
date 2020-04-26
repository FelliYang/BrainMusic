package com.brainmusic.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;

import com.brainmusic.db.Music;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

/**
 * 数据库工具类
 */
public class DBUtil {
    private Context mContext;

    public DBUtil(Context _context) {
        mContext = _context;
    }

    /**
     * 把标准模型库的模型添加到数据库中
     */
    public void initMusicLibrary(){
        SharedPreferences preferences = mContext.getSharedPreferences("status",MODE_PRIVATE);
        boolean initMusicLibrary = preferences.getBoolean("InitMusicLibrary",false);
        //只初始化一次
        if(initMusicLibrary==false){
            try {
                //添加标准模型
                AssetManager manager = mContext.getAssets();
                addToDatabase("Music", manager);

            } catch (IOException e) {
                e.printStackTrace();
            }

            //没有初始化
            SharedPreferences.Editor editor = mContext.getSharedPreferences("status",MODE_PRIVATE)
                    .edit();
            editor.putBoolean("InitMusicLibrary",true);
            editor.apply();
        }

    }

    /**
     *把指定目录下的所有mp3文件添加到数据库中
     * @param directory
     * @param manager
     * @throws Exception
     */
    private void addToDatabase(String directory, AssetManager manager)throws IOException {
        String [] files = manager.list(directory);

        for (String file : files) {
            if (file.endsWith(".mp3")) {
                //添加音乐到数据库
                Music music = new Music();
                music.setName(file);
                music.setPath(directory+"/"+file);
                music.save();
            } else {
                addToDatabase(directory + "/" + file, manager);
            }
        }
    }
}
