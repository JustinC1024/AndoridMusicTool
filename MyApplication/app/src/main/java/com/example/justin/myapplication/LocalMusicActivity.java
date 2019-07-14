package com.example.justin.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
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
import com.example.justin.myapplication.util.MusicUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocalMusicActivity extends AppCompatActivity {

    private List<Music> musicList;
    private RecyclerView recyclerView;
    private MediaPlayer mediaPlayer;
    private MusicAdapter musicAdapter;
    private MusicUtil musicUtil;
    private LinearLayoutManager linearLayoutManager;

    private CircleImageView circleImageView;
    private SeekBar seekBar;
    private TextView textView1;
    private TextView textView2;
    private ImageButton imageButton1;
    private ImageButton imageButton2;
    private ImageButton imageButton3;
    private TextView textView3;
    private TextView textView4;
    private ImageButton imageButton4;
    private CircleImageViewNo circleImageViewNo;

    private BigPlayerFragment bigPlayerFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private String lastPath;
    private int musicNow;
    private boolean musicReady=false;

    private String artist;
    private String title;
    private String duration;
    private Music music;

    //接收子进程传达的信息
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            seekBar.setProgress(msg.what);
            if (textView4!=null){
                textView4.setText(timeStyle(msg.what));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);
        /*
         * 界面
         * */
        mediaPlayer=new MediaPlayer();
        musicUtil=new MusicUtil(this);
        linearLayoutManager=new LinearLayoutManager(this);
        musicList=musicUtil.initMusicList();
        musicAdapter=new MusicAdapter(musicList);
        recyclerView=findViewById(R.id.musicRecyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(musicAdapter);
        bigPlayerFragment=new BigPlayerFragment();
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.bigPlayer,bigPlayerFragment);
        fragmentTransaction.hide(bigPlayerFragment);
        fragmentTransaction.commit();
        /*
         * 控件初始化
         * */
        imageButton1=findViewById(R.id.miniPlay);
        imageButton2=findViewById(R.id.miniPrevious);
        imageButton3=findViewById(R.id.miniNext);
        seekBar=findViewById(R.id.miniSeekBar);
        textView1=findViewById(R.id.miniTitle);
        textView2=findViewById(R.id.miniArtist);
        circleImageView=findViewById(R.id.miniImage);
        /*
         * 点击歌曲
         * */
        musicAdapter.setOnItemClickListener(new MusicAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                musicReady=true;
                musicNow=position;
                Music m=musicList.get(position);
                File f=new File(m.getUrl());
                if (f.exists()) {
                    /*
                     * 播放事件
                     * */
                    if (clickMusic(f)){
                        imageButton1.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause,null));
                    }else {
                        imageButton1.setImageDrawable(getResources().getDrawable(R.drawable.ic_play,null));
                    }
                    textView1.setText(m.getTitle());
                    textView2.setText(m.getArtist());
                    initSeekBar(m);

                    music=m;
                    artist=m.getArtist();
                    title=m.getTitle();
                    duration=timeStyle(m.getDuration());
                    getImg();
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
                    Toast.makeText(LocalMusicActivity.this,"请选择歌曲",Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*
         * 点击上一首
         * */
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicReady){
                    if (previousMusic()){
                        imageButton1.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause,null));
                        Toast.makeText(LocalMusicActivity.this,"上一首",Toast.LENGTH_SHORT).show();
                    }else {
                        mediaPlayer.reset();
                        imageButton1.setImageDrawable(getResources().getDrawable(R.drawable.ic_play,null));
                        Toast.makeText(LocalMusicActivity.this,"未知错误",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LocalMusicActivity.this,"请选择歌曲",Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*
         * 点击下一首
         * */
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicReady){
                    if (nextMusic()){
                        imageButton1.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause,null));
                        Toast.makeText(LocalMusicActivity.this,"下一首",Toast.LENGTH_SHORT).show();
                    }else {
                        mediaPlayer.reset();
                        imageButton1.setImageDrawable(getResources().getDrawable(R.drawable.ic_play,null));
                        Toast.makeText(LocalMusicActivity.this,"未知错误",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(LocalMusicActivity.this,"请选择歌曲",Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*
        * 打开播放页
        * */
        circleImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                /*
                * fragment显现
                * */
                fragmentManager=getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.show(bigPlayerFragment);
                fragmentTransaction.commit();
                /*
                * 改变控件
                * */
                imageButton1=findViewById(R.id.bigPlay);
                imageButton2=findViewById(R.id.bigPrevious);
                imageButton3=findViewById(R.id.bigNext);
                textView1=findViewById(R.id.bigArtist);
                textView2=findViewById(R.id.bigTitle);
                textView3=findViewById(R.id.musicTotalTime);
                textView4=findViewById(R.id.musicNowTime);
                seekBar=findViewById(R.id.bigSeekBar);
                imageButton4=findViewById(R.id.bigBack);
                circleImageViewNo=findViewById(R.id.bigArtistImg);
                textView1.setText(artist);
                textView2.setText(title);
                textView3.setText(duration);
                getImg();
                if (mediaPlayer.isPlaying()){
                    imageButton1.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle,null));
                }else {
                    imageButton1.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle,null));
                }

                imageButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (musicReady){
                            if (playMusic()) {
                                imageButton1.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle,null));
                            }else {
                                imageButton1.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle,null));
                            }
                        }else {
                            Toast.makeText(LocalMusicActivity.this,"请选择歌曲",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                imageButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (musicReady){
                            if (previousMusic()){
                                imageButton1.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle,null));
                                Toast.makeText(LocalMusicActivity.this,"上一首",Toast.LENGTH_SHORT).show();
                                textView3.setText(duration);
                            }else {
                                mediaPlayer.reset();
                                imageButton1.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle,null));
                                Toast.makeText(LocalMusicActivity.this,"未知错误",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(LocalMusicActivity.this,"请选择歌曲",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                imageButton3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (musicReady){
                            if (nextMusic()){
                                imageButton1.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle,null));
                                Toast.makeText(LocalMusicActivity.this,"下一首",Toast.LENGTH_SHORT).show();
                                textView3.setText(duration);
                            }else {
                                mediaPlayer.reset();
                                imageButton1.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_circle,null));
                                Toast.makeText(LocalMusicActivity.this,"未知错误",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(LocalMusicActivity.this,"请选择歌曲",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                initSeekBar(music);
                imageButton4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragmentManager=getSupportFragmentManager();
                        fragmentTransaction=fragmentManager.beginTransaction();
                        fragmentTransaction.hide(bigPlayerFragment);
                        fragmentTransaction.commit();
                        imageButton1=findViewById(R.id.miniPlay);
                        imageButton2=findViewById(R.id.miniPrevious);
                        imageButton3=findViewById(R.id.miniNext);
                        seekBar=findViewById(R.id.miniSeekBar);
                        textView1=findViewById(R.id.miniTitle);
                        textView2=findViewById(R.id.miniArtist);
                        circleImageViewNo=null;
                        textView1.setText(artist);
                        textView2.setText(title);
                        if (mediaPlayer.isPlaying()){
                            imageButton1.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause,null));
                        }else {
                            imageButton1.setImageDrawable(getResources().getDrawable(R.drawable.ic_play,null));
                        }
                    }
                });

            }
        });

        returnMusic();
    }
    /*
     * MediaPlayer初始化
     * */
    private void initMediaPlayer(File file){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(file.getPath());
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
    private boolean clickMusic(File file){
        boolean flag=false;
        if (file.getPath().equals(lastPath)){
            flag=playMusic();
        }else {
            initMediaPlayer(file);
            flag=true;
        }
        lastPath=file.getPath();
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
     * 上一首
     * */
    private boolean previousMusic(){
        boolean flag=false;
        if (musicNow==0){
            musicNow=musicList.size();
        }
        Music m=musicList.get(musicNow-1);
        File f=new File(m.getUrl());
        if (f.exists()){
            initMediaPlayer(f);
            flag=true;
        }
        musicNow=musicNow-1;
        artist=m.getArtist();
        title=m.getTitle();
        duration=timeStyle(m.getDuration());
        textView1.setText(m.getTitle());
        textView2.setText(m.getArtist());
        initSeekBar(m);
        getImg();
        return flag;
    }
    /*
     * 下一首
     * */
    private boolean nextMusic(){
        boolean flag=false;
        if (musicNow==musicList.size()-1){
            musicNow=-1;
        }
        Music m=musicList.get(musicNow+1);
        File f=new File(m.getUrl());
        if (f.exists()){
            initMediaPlayer(f);
            flag=true;
        }
        musicNow=musicNow+1;
        artist=m.getArtist();
        title=m.getTitle();
        duration=timeStyle(m.getDuration());
        textView1.setText(m.getTitle());
        textView2.setText(m.getArtist());
        initSeekBar(m);
        getImg();
        return flag;
    }
    /*
     * 音乐进度条
     * */
    private void initSeekBar(Music m){
        new Thread(new SeekBarThread()).start();
        seekBar.setMax((int)m.getDuration());
        System.out.println("时长"+m.getDuration());
        Thread thread=new Thread(new SeekBarThread());
        thread.start();
        /*
         * SeekBar监听事件
         * */
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    /*
     * 创建子线程
     * */
    class SeekBarThread implements Runnable{
        @Override
        public void run() {
            while (mediaPlayer!=null){
                handler.sendEmptyMessage(mediaPlayer.getCurrentPosition());
                try {
                    Thread.sleep(80);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(mediaPlayer.getCurrentPosition());
//                System.out.println("当前时长"+mediaPlayer.getCurrentPosition());
            }
        }
    }
    /*
     * 播放结束回调
     * */
    private void returnMusic(){
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextMusic();
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                System.out.println("报错"+what);
                mp.start();
                return true;
            }
        });
    }
    /*
    * 时间格式化
    * */
    private String timeStyle(long mtime){
        Date date=new Date(mtime);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("mm:ss");
        return simpleDateFormat.format(date);
    }
    /*
    * 获取图片
    * */
    private void getImg(){
        String url="http://192.168.43.13:9000/pic";
        OkHttpClient client=new OkHttpClient();
        FormBody.Builder formBuilder=new FormBody.Builder();
        formBuilder.add("picName",artist);
        Request request=new Request.Builder().url(url).post(formBuilder.build()).build();
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
                                Toast.makeText(LocalMusicActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res=response.body().string();
                String picNum = null;
                try {
                    JSONObject jsonObject=new JSONObject(res);
                    picNum=jsonObject.getString("img");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                URL pic=new URL("http://singerimg.kugou.com/uploadpic/softhead/240/20"+picNum);
                System.out.println("图片:"+pic);
                HttpURLConnection conn=(HttpURLConnection) pic.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream inputStream=conn.getInputStream();
                final Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                if (circleImageView!=null){
                    circleImageView.post(new Runnable() {
                        @Override
                        public void run() {
                            circleImageView.setImageBitmap(bitmap);
                        }
                    });
                }
                if (circleImageViewNo!=null){
                    circleImageViewNo.post(new Runnable() {
                        @Override
                        public void run() {
                            circleImageViewNo.setImageBitmap(bitmap);
                        }
                    });
                }
                inputStream.close();
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
