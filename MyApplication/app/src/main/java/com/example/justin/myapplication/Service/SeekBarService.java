package com.example.justin.myapplication.Service;

import android.media.MediaPlayer;
import android.widget.SeekBar;

public class SeekBarService implements SeekBar.OnSeekBarChangeListener{

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;

    public SeekBarService(MediaPlayer mediaPlayer,SeekBar seekBar) {
        this.mediaPlayer = mediaPlayer;
        this.seekBar=seekBar;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayer.seekTo(seekBar.getProgress());
    }

}
