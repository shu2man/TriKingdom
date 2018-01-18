package com.example.yellow.trikingdom;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

/**
 * Created by Yellow on 2017-11-19.
 */

public class MyMusicService extends Service {
    public mybind mb=new mybind();
    public static MediaPlayer players ;
    @Override
    public void onCreate(){
        super.onCreate();
        try {
            players =MediaPlayer.create(this,R.raw.linghai_pipayu);
            players.setLooping(true);
            players.start();
        }
        catch (Exception e) {}
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mb;
    }
    public class mybind extends Binder {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code)
            {
                case 101:if(players.isPlaying()) players.pause();
                else players.start();break;
            }
            return super.onTransact(code,data,reply,flags);
        }
    }
    @Override
    public void onDestroy(){
        players.stop();
        super.onDestroy();
    }
}
