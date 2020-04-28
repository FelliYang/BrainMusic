package com.brainmusic.db;

import android.content.Context;

import org.litepal.crud.LitePalSupport;

public class Music extends LitePalSupport {
    int id;
    String name;
    String path; //歌曲路径
    int easyStatus; //轻松度
    int img;
    static int num = 0;

    public Music(String name, String path, int easyStatus) {
        this.name = name;
        this.path = path;
        this.easyStatus = easyStatus;
    }
    public Music(){
        name = "";
        path = "";
        easyStatus = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getEasyStatus() {
        return easyStatus;
    }

    public void setEasyStatus(int easyStatus) {
        this.easyStatus = easyStatus;
    }
}
