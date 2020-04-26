package com.brainmusic.custom;

import android.media.MediaPlayer;

/**
 * 自定义音乐播放器，记录了当前的状态
 */
public class MusicPlayer extends MediaPlayer {
    public int status = -1;
    public static final int START = 0;
    public static final int PAUSE = 1;

    @Override
    public void start() throws IllegalStateException {
        super.start();
        status = START;
    }

    @Override
    public void pause() throws IllegalStateException {
        super.pause();
        status = PAUSE;
    }
}
