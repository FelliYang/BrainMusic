package com.brainmusic.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;

import com.brainmusic.db.Music;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                String fileName = file.substring(0,file.length()-4);
                music.setName(fileName);
                music.setPath(directory+"/"+file);
                music.save();
            } else {
                addToDatabase(directory + "/" + file, manager);
            }
        }
    }

    /**
     * 使用轻松度查询数据库
     * @return
     */
    public List<Music> getMusicWithEasyStatus(){
        List<Music> res =  LitePal.order("easyStatus desc").limit(8).find(Music.class);
        List<Music> tmp = LitePal.where("easyStatus = ?","0").limit(2).find(Music.class);
        if(!tmp.isEmpty()){
            if(res.isEmpty()) {
                res.addAll(tmp);
            }else{
                for(Music m:tmp){ //判断是否重复
                    int flag = 0;
                    for(Music t:res){
                        if(m.getId() == t.getId()){
                            flag = 1;
                            break;
                        }
                    }
                    if(flag!=1){ //没用重复
                        res.add(m);
                    }
                }
            }
        }
        return res;
    }
}
