package com.example.user.musicmp3test;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
 {

    private ConstraintLayout constraintLayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    static FragmentPagerAdapter fragmentPagerAdapter;
    private AnimationDrawable animDrawable;


    static TextView music_txtCurrentTime,music_txtAllTime,txtCurrentPlayMusic;
    static ImageButton music_btnStop,music_btnPlay,music_btnPause;
    SeekBar music_seekBar;
    static LinearLayout music_LinearLayout;
    static  MediaPlayer mediaPlayer=new MediaPlayer();

    int playbackPosition=0;
    static boolean btnIsPause=false;
    boolean btnSeekBarChange=false;
    static String muPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MP3");
        getSupportActionBar().hide();
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MODE_PRIVATE);

        constraintLayout= findViewById(R.id.constraintLayout);
        viewPager= findViewById(R.id.viewPager);
        tabLayout= findViewById(R.id.tabLayout);
        music_txtCurrentTime= findViewById(R.id.music_txtCurrentTime);
        music_txtAllTime=findViewById(R.id.music_txtAllTime);
        txtCurrentPlayMusic=findViewById(R.id.txtCurrentPlayMusic);
        music_btnStop = findViewById(R.id.music_btnStop);
        music_btnPlay = findViewById(R.id.music_btnPlay);
        music_btnPause = findViewById(R.id.music_btnPause);

        music_seekBar= findViewById(R.id.music_seekBar);
        music_LinearLayout= findViewById(R.id.music_LinearLayout);

        
        Log.d("TAG","onCreate()");

        fragmentPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i==1){
                    Log.d("뮤직","리플레시");
                    fragmentPagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        backgroundColor(); //백그라운드 칼라

        Log.d("뮤직","1");


        music_LinearLayout.setVisibility(View.GONE);



        //리스트 누르면 자동 재생
        music_btnPlay.setOnClickListener(this);
        music_btnPause.setOnClickListener(this);
        music_btnStop.setOnClickListener(this);

        music_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    btnSeekBarChange=true;
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

    private void backgroundColor() {

        Log.d("뮤직","2");

        animDrawable = (AnimationDrawable) (constraintLayout.getBackground() != null && constraintLayout.getBackground() instanceof AnimationDrawable ?
                constraintLayout.getBackground() : null);
        if (animDrawable != null) {
            animDrawable.setEnterFadeDuration(5000);
            animDrawable.setExitFadeDuration(5000);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (animDrawable != null && !animDrawable.isRunning()) {
            animDrawable.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animDrawable != null && !animDrawable.isRunning()) {
            animDrawable.stop();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.music_btnPlay :
                Log.d("뮤직","11노래시작");
                music_btnPlay.setVisibility(View.INVISIBLE);
                music_btnPause.setVisibility(View.VISIBLE);
                if (MymusicListAdapter.isSubMusicStart==true){

                    muPath=MymusicListAdapter.submusicPath;
                }else {
                    muPath=MusicActivty.musicpath;
                }
                MymusicListAdapter.isSubMusicStart=false;
                musicStat(btnIsPause,muPath);

                Thread thread = new Thread() {
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("mm:ss");
                    @Override
                    public void run() {
                        if (mediaPlayer==null){

                            return;
                        }


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                music_seekBar.setMax(mediaPlayer.getDuration());

                            }
                        });
                        while (mediaPlayer.isPlaying()){

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    music_seekBar.setProgress(mediaPlayer.getCurrentPosition());
                                    music_txtCurrentTime.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                                    music_txtAllTime.setText(simpleDateFormat.format(mediaPlayer.getDuration()));

                                }
                            });
                            SystemClock.sleep(200);
                        }

                    }
                };
                thread.start();

                break;
            case R.id.music_btnPause:
                btnIsPause=true;
                playbackPosition=mediaPlayer.getCurrentPosition();

                mediaPlayer.pause();
                music_btnPlay.setVisibility(View.VISIBLE);
                music_btnPause.setVisibility(View.INVISIBLE);


                break;

            case R.id.music_btnStop :
                playbackPosition=0;
                Log.d("뮤직",String.valueOf(btnSeekBarChange)+" / "+String.valueOf(btnIsPause)+"/"+String.valueOf(playbackPosition));

                    if (mediaPlayer.isPlaying()|| btnSeekBarChange==true || btnIsPause==true) {

                        mediaPlayer.stop();
                        music_txtCurrentTime.setText("00:00");
                        music_txtAllTime.setText("00:00");
                        music_seekBar.setProgress(0);
                        music_btnPause.setVisibility(View.INVISIBLE);
                        music_btnPlay.setVisibility(View.VISIBLE);
                        music_LinearLayout.setVisibility(View.GONE);


                    }
                    btnIsPause=false;

                break;

        }
    }
    public  void musicStat(boolean btnIsPause,String musicPath) {
        Log.d("뮤직",musicPath);
        try {

            if (btnIsPause==false){

                mediaPlayer.reset();
                mediaPlayer.setDataSource(musicPath);
                mediaPlayer.prepare();
                mediaPlayer.start();

            }else if(btnIsPause==true){
                mediaPlayer.seekTo(playbackPosition);
                mediaPlayer.start();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


 }
