package com.jagtar.fishing;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class Sounds {

    private SoundPool soundPool;
    private int hookCollision;
    private int shoot;
    private int bgMusic;
    private  int hookStart;


    public Sounds(Context context){

        //Creating sound pool
        soundPool = new SoundPool(7, AudioManager.STREAM_MUSIC, 0);

        //adding sounds to sound pool
        bgMusic = soundPool.load(context,R.raw.bgmusic,1);
        hookCollision = soundPool.load(context,R.raw.pop,1);
        shoot = soundPool.load(context,R.raw.pistol,1);
        hookStart = soundPool.load(context,R.raw.begin,1);




    }


    public void getHookCollision() {
        soundPool.play(hookCollision,2.0f,2.0f,1,0,1.0f);
    }

    public void getShoot() {
        soundPool.play(shoot,1.0f,1.0f,1,0,1.0f);
    }

    public void getbgMusic() {
        soundPool.play(bgMusic,0.3f,0.3f,1,-1,1.0f);
    }

    public void getHookStart() {
        soundPool.play(hookStart,1.0f,1.0f,1,0,1.0f);
    }
}
