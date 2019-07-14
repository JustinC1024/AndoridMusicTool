package com.example.justin.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.justin.myapplication.bean.Music;
import com.example.justin.myapplication.layout.CircleImageView;
import com.example.justin.myapplication.layout.CircleImageViewNo;
import com.example.justin.myapplication.util.MusicAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OnlineActivity extends AppCompatActivity {

    private List<Music> musicList=new ArrayList<Music>();
    private RecyclerView recyclerView;
    private MediaPlayer mediaPlayer;
    private MusicAdapter musicAdapter;
    private LinearLayoutManager linearLayoutManager;

    private ConstraintLayout constraintLayout;
    private ImageButton imageButton1;

    private String lastPath;
    private boolean musicReady=false;
    private Map<String,String> musicMap=new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        /*
         * 界面
         * */
        mediaPlayer=new MediaPlayer();
        linearLayoutManager=new LinearLayoutManager(this);
        String res=getIntent().getStringExtra("res");
        if (res!=null){
            initMusicList(res);
        }
        musicAdapter=new MusicAdapter(musicList);
        recyclerView=findViewById(R.id.musicRecyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(musicAdapter);
        /*
         * 控件初始化
         * */
        imageButton1=findViewById(R.id.onlinePlay);
        constraintLayout=findViewById(R.id.onlinePlayer);
        /*
         * 点击歌曲
         * */
        musicAdapter.setOnItemClickListener(new MusicAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                musicReady=true;
                Music m=musicList.get(position);
                if (m.getUrl()!=null) {
                    System.out.println("音乐路径："+m.getUrl());
                    /*
                     * 播放事件
                     * */
                    if (clickMusic(m.getUrl())){
                        imageButton1.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause,null));
                        getImg(m);
                    }else {
                        imageButton1.setImageDrawable(getResources().getDrawable(R.drawable.ic_play,null));
                    }
                }else {
                    Toast.makeText(OnlineActivity.this,"暂无资源",Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*
         * 点击播放
         * */
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicReady){
                    if (playMusic()) {
                        imageButton1.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause,null));
                    }else {
                        imageButton1.setImageDrawable(getResources().getDrawable(R.drawable.ic_play,null));
                    }
                }else {
                    Toast.makeText(OnlineActivity.this,"请选择歌曲",Toast.LENGTH_SHORT).show();
                }
            }
        });
        returnMusic();
    }
    /*
     * MediaPlayer初始化
     * */
    private void initMediaPlayer(String musicPath){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(musicPath);
            //异步加载
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    System.out.println("开始播放");
                    mediaPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
     * 播放
     * */
    private boolean clickMusic(String musicPath){
        boolean flag=false;
        if (musicPath.equals(lastPath)){
            flag=playMusic();
        }else {
            initMediaPlayer(musicPath);
            flag=true;
        }
        lastPath=musicPath;
        return flag;
    }
    /*
     * 播放按钮
     * */
    private boolean playMusic(){
        boolean flag=false;
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }else {
            mediaPlayer.start();
            flag=true;
        }
        return flag;
    }
    /*
     * 网络音乐初始化
     * */
    private void initMusicList(String res){
        try {
            JSONArray jsonArray = new JSONArray(res);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Music m = new Music();
                String a = jsonObject.getString("artist");
                m.setArtist(a);
                m.setTitle(jsonObject.getString("title"));
                if (!jsonObject.getString("url").equals("")){
                    m.setUrl("https://webfs.yun.kugou.com/" + jsonObject.getString("url") + ".mp3");
                }
                musicList.add(m);
                String img = "https://p3fx.kgimg.com/stdmusic/" + jsonObject.getString("img") + ".jpg";
                musicMap.put(a, img);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*
    * 获取图片
    * */
    private void getImg(final Music music){

        String url="http://192.168.43.13:9000/new";
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(url).build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(OnlineActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                URL pic;
                try {
                    pic = new URL(musicMap.get(music.getArtist()));
                    HttpURLConnection conn=(HttpURLConnection) pic.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream inputStream=conn.getInputStream();
                    final Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                    constraintLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            constraintLayout.setBackgroundDrawable(new BitmapDrawable(bitmap));
                        }
                    });
                    inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /*
     * 播放结束回调
     * */
    private void returnMusic(){
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                constraintLayout.setBackgroundResource(0);
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                System.out.println("报错"+what);
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }
}
