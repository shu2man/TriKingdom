package com.example.yellow.trikingdom;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

/**
 * Created by Yellow on 2017-11-19.
 */
/*
public class MyMusicService extends Service {
    private MediaPlayer mMediaPlayer;

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mMediaPlayer.stop();
    }
    @Override
    @Deprecated
    public void onStart(Intent intent,int startId){
        super.onStart(intent,startId);
        if (mMediaPlayer==null){
            mMediaPlayer=MediaPlayer.create(this,R.raw.linghai_pipayu);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
        }
    }
}*/
