package com.example.justin.myapplication.Service;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.ImageButton;

import com.example.justin.myapplication.R;
import com.example.justin.myapplication.bean.Music;
import com.example.justin.myapplication.util.MusicUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MusicService {

    private Context context;

    public MusicService(Context context) {
        this.context = context;
    }

    private MediaPlayer mediaPlayer=new MediaPlayer();
    private String lastPath;

    /*
    * 播放
    * */
    public boolean playMusic(File file){
        boolean flag=false;
        if (file.getPath().equals(lastPath)){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }else {
                mediaPlayer.start();
                flag=true;
            }
        }else {
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(file.getPath());
                //异步加载
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });
                flag=true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        lastPath=file.getPath();
        return flag;
    }
}
