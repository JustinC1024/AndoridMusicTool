package com.example.justin.myapplication.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.justin.myapplication.bean.Music;

import java.util.ArrayList;
import java.util.List;

public class MusicUtil {

    private Context context;

    public MusicUtil(Context context) {
        this.context = context;
    }

    /*
     * 读取本地音乐
     * */
    public List<Music> initMusicList(){
        List<Music> ml=new ArrayList<Music>();
        ContentResolver contentResolver=context.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor.moveToFirst()){
            for(int i = 0; i <30;i++) {
                Music m = new Music();
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));

                if (isMusic != 0 && duration / (500 * 60) >= 1) {
                    m.setId(id);
                    m.setName(name);
                    m.setUrl(url);
                    String[] nameArray=m.getName().split(" - ");
                    m.setArtist(nameArray[0]);
                    String[] nameArray1=nameArray[1].split("\\.");
                    m.setTitle(nameArray1[0]);
                    m.setDuration(duration);
                    m.setIsMusic(isMusic);
                    ml.add(m);
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
        return ml;
    }

}
