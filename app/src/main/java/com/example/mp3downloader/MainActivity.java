package com.example.mp3downloader;

import android.Manifest;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements DownloadsFragment.refreshListF,SearchFragment.aa,BottomNavigationView.OnNavigationItemSelectedListener,DownMainInter,ListDialogFragment.sendList,ListsFragment.TextClicked,PlayList.closePlayList,PlayList.addListFrag {
    public  BottomNavigationView navigationView;
    public  SearchFragment sFragment;
    public  DownloadsFragment dFragment;
    public static ListsFragment lFragment;
    public static FragmentManager manager;
    public static FragmentTransaction transaction;
    private final int REQ_STORAGE = 1;
    public  FrameLayout container;
    private WebView a;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> paths = new ArrayList<>();
    private int position = 0;
    private MediaPlayer player;
    private SeekBar seekBar;
    private TextView songName,songStartTime,songEndTime;
    private ImageButton loopBut,shuffleBut,playBut;
    private int clicked= 0;
    private boolean isPaused = true;
    private Runnable runnable;
    private Handler handler = new Handler();
    private boolean shuffle = false;
    private ArrayList<String> shuffledNames;
    private ArrayList<String> shuffledPaths;
    private Random r ;
    private int randPos;
    private final String CHANNEL_ID = "channelchannelwhatthefischannell?";
    private Intent play,next,back;
    private boolean looping = false;
    private Notification nBuilder;





    @Override
    public void closeIt() {
        lFragment.containerInisible();
    }

    @Override
    public void addListSong(String key,ArrayList<String> s,ArrayList<String> a) {
        AddSongList addSongList = new AddSongList();
        addSongList.setKey(key,s,a);
        getSupportFragmentManager().beginTransaction().add(R.id.listContainer,addSongList).commit();
    }

    @Override
    public void sendText(String text) {
        PlayList playList = new PlayList();
        playList.setKey(text);
        getSupportFragmentManager().beginTransaction().add(R.id.listContainer,playList).commit();
    }

    @Override
    public void sendAList(ArrayList<String> a, ArrayList<String> b) {
        lFragment.setListName(a,b);
    }

    @Override
    public void songStuu(int pos) {
        position = pos;
        shuffledPaths = new ArrayList<>(paths);
        shuffledNames = new ArrayList<>(names);
        randPos = pos;
        startPlaying();
    }

    @Override
    public void stopIt() {
        if(clicked == 0){
            player.pause();
            playBut.setImageResource(R.mipmap.play_deneme_1);

            isPaused = true;
            clicked = 1;
        }
    }

    @Override
    public void notPlaying(String s) {
        int pos = names.indexOf(s);
        if (shuffledNames.contains(s))shuffledNames.remove(shuffledNames.indexOf(s));
        Log.e("Evet","Dogru");
        Log.e("Evet",songName.getText().toString()+".mp3");
        String s1= songName.getText().toString()+".mp3";
        if (s1.equals(s)){
            if (shuffle){
                player.reset();
                if (shuffledNames.size()==0){
                    shuffledPaths = new ArrayList<>(paths);
                    shuffledNames = new ArrayList<>(names);
                    randPos = r.nextInt(shuffledNames.size());
                    startPlaying();

                }
                else {
                    randPos = r.nextInt(shuffledNames.size());
                    startPlaying();
                }

            }else {
                player.reset();
                position++;
                if(position>names.size()-1)position=0;
                startPlaying();

            }
        }
    }



    @Override
    public void listUpdatee(ArrayList<String> names, ArrayList<String> paths) {
        this.names = names;
        this.paths = paths;
        shuffledPaths = new ArrayList<>(paths);
        shuffledNames = new ArrayList<>(names);
    }

    private void startPlaying(){
        if(names.size()>0){
        clicked=0;
        System.out.println(shuffledNames);
        System.out.println(names);

        if(player.isPlaying()||isPaused){
            isPaused=false;
            player.reset();
        }
        seekBar.setProgress(0);
        songStartTime.setText("00:00");
        try {
            if (shuffle){
                position = names.indexOf(shuffledNames.get(randPos));
                player.setDataSource(shuffledPaths.get(randPos));
                player.prepare();
                String a = shuffledNames.get(randPos);
                a = a.substring(0,a.length()-4);
                NotificationService notificationService = new NotificationService(this,a,this);
                songName.setText(a);
                shuffledNames.remove(randPos);
                shuffledPaths.remove(randPos);
                Log.e("Size: ",String.valueOf(shuffledNames.size()));
                Log.e("Names Size: ",String.valueOf(names.size()));

            }else {
                try {
                    player.setDataSource(paths.get(position));
                    player.prepare();
                }catch (Exception e){
                    Toast.makeText(getBaseContext(),"Song Deleted",Toast.LENGTH_LONG).show();
                }

                String a = names.get(position);
                a = a.substring(0,a.length()-4);
                songName.setText(a);
                NotificationService notificationService = new NotificationService(this,a,this);

            }

        } catch (IOException e) {
            Toast.makeText(getBaseContext(),"Song Deleted",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        int seconds = (int) (player.getDuration() / 1000) % 60 ;
        int minutes = (int) ((player.getDuration() / (1000*60)) % 60);
        if(seconds<10)songEndTime.setText(minutes+":0"+seconds);
        else songEndTime.setText(minutes+":"+seconds);
        Log.e("Song Duration: ",String.valueOf(player.getDuration()));

    }else {
            seekBar.setProgress(0);
            songStartTime.setText("00:00");
            songName.setText("Song");
            songEndTime.setText("00:00");
        }
    }

    private void updateBar() {
        seekBar.setProgress(player.getCurrentPosition());
        int seconds = (int) (player.getCurrentPosition() / 1000) % 60 ;
        int minutes = (int) ((player.getCurrentPosition() / (1000*60)) % 60);
        if(seconds<10)songStartTime.setText(minutes+":0"+seconds);
        else songStartTime.setText(minutes+":"+seconds);

        if(player.isPlaying()){
            runnable = new Runnable() {
                @Override
                public void run() {
                    updateBar();
                }
            };
            handler.postDelayed(runnable,500);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for(String s : permissions)Log.e("per",s);
        for(int i : grantResults)Log.e("per",""+i);
        if (grantResults.length == 0
                || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Log.e("per","Burdayım");
                System.exit(0);
        }
        return;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},REQ_STORAGE);

        final GestureDetector gdt = new GestureDetector(getBaseContext(),new GestureListener());
        container = findViewById(R.id.contains);
        sFragment = new SearchFragment();
        dFragment = new DownloadsFragment();
        lFragment = new ListsFragment();
        registerReceiver(sFragment.onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        r = new Random();

        songStartTime = findViewById(R.id.songStartTime);
        songEndTime = findViewById(R.id.songEndTime);
        shuffleBut = findViewById(R.id.shuffleBut);
        loopBut = findViewById(R.id.loopBut);

        player = new MediaPlayer();
        seekBar = (SeekBar)findViewById(R.id.barSeek);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser==true)player.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        songName = (TextView)findViewById(R.id.barSongname);
        songName.setSelected(true);
        songName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gdt.onTouchEvent(event);
            }
        });
        playBut = (ImageButton) findViewById(R.id.playBut);

        playBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clicked == 0){
                    player.pause();
                    playBut.setImageResource(R.mipmap.play_deneme_1);
                    isPaused = true;
                    clicked = 1;
                }
                else {
                    player.start();
                    updateBar();
                    playBut.setImageResource(R.mipmap.pause_orange);
                    isPaused=false;
                    clicked=0;
                }
            }
        });

        loopBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(looping == false){
                   looping = true;
                   loopBut.setImageResource(R.mipmap.loop_4_orange);
               }else {
                   looping = false;
                   loopBut.setImageResource(R.mipmap.loop_4);
               }

            }
        });

        shuffleBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shuffle){
                    shuffleBut.setImageResource(R.mipmap.shuffle_4);
                    shuffle = false;
                }else {
                    shuffle = true;
                    if (!names.isEmpty()){
                        shuffledPaths = new ArrayList<>(paths);
                        shuffledNames = new ArrayList<>(names);
                        shuffledNames.remove(position);
                        shuffledPaths.remove(position);
                    }
                    shuffleBut.setImageResource(R.mipmap.shuffle_4_orange);
                }

            }
        });

        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(player.getDuration());
                    player.start();
                    playBut.setImageResource(R.mipmap.pause_orange);
                    updateBar();


            }
        });

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(looping){
                    player.start();
                }
                else {
                    if (shuffle){
                        player.reset();
                        if (shuffledNames.size()==0){
                            shuffledPaths = new ArrayList<>(paths);
                            shuffledNames = new ArrayList<>(names);
                            randPos = r.nextInt(shuffledNames.size());
                            startPlaying();

                        }
                        else {
                            randPos = r.nextInt(shuffledNames.size());
                            startPlaying();
                        }

                    }else {
                        player.reset();
                        position++;
                        if(position>names.size()-1)position=0;
                        Log.e("path",paths.get(position));
                        Log.e("names",names.get(position));
                        startPlaying();
                        Log.e("tag","bittim");
                    }
                }

                playBut.setImageResource(R.mipmap.pause_orange);
            }
        });



        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.contains,lFragment,"List Fragment");
        transaction.commit();

        navigationView = (BottomNavigationView)findViewById(R.id.navigationView);
        navigationView.setSelectedItemId(R.id.list);
        navigationView.setOnNavigationItemSelectedListener(this);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case  R.id.search:

                if (dFragment.isVisible())getSupportFragmentManager().beginTransaction().hide(dFragment).commit();

                if (lFragment.isVisible())getSupportFragmentManager().beginTransaction().hide(lFragment).commit();

                if (sFragment.isAdded())getSupportFragmentManager().beginTransaction().show(sFragment).commit();
                else {
                    getSupportFragmentManager().beginTransaction().add(R.id.contains,sFragment).commit();
                    getSupportFragmentManager().beginTransaction().show(sFragment).commit();
                }
                return true;
            case R.id.download:
                getSupportFragmentManager().beginTransaction().hide(sFragment).commit();

                getSupportFragmentManager().beginTransaction().hide(lFragment).commit();

                if (dFragment.isAdded()){
                    getSupportFragmentManager().beginTransaction().detach(dFragment).commit();
                    getSupportFragmentManager().beginTransaction().attach(dFragment).commit();
                    getSupportFragmentManager().beginTransaction().show(dFragment).commit();
                }
                else getSupportFragmentManager().beginTransaction().add(R.id.contains,dFragment).commit();

                return true;
            case R.id.list:
                if (sFragment.isVisible())getSupportFragmentManager().beginTransaction().hide(sFragment).commit();

                if (dFragment.isVisible())getSupportFragmentManager().beginTransaction().hide(dFragment).commit();

                if (lFragment.isAdded())getSupportFragmentManager().beginTransaction().show(lFragment).commit();
                else getSupportFragmentManager().beginTransaction().add(R.id.contains,lFragment).commit();
                return true;
        }
        return false;
    }

    @Override
    public void listFrefresh() {
        lFragment.detachAtach();
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private final int SWIPE_MIN_DISTANCE = 100;
        private final int SWIPE_THRESHOLD_VELOCITY = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) >     SWIPE_THRESHOLD_VELOCITY) {
                // Right to left, your code here
                if(shuffle){
                    if(shuffledNames.isEmpty()){
                        shuffledPaths = new ArrayList<>(paths);
                        shuffledNames = new ArrayList<>(names);
                    }
                    randPos = r.nextInt(shuffledNames.size());
                    startPlaying();
                }else {
                    position++;
                    if(position>names.size()-1)position=0;
                    startPlaying();
                }
                return true;

            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE &&     Math.abs(velocityX) >  SWIPE_THRESHOLD_VELOCITY) {
                // Left to right, your code here
                Log.e("Left To Right","left-right");
                if(seekBar.getProgress()>3000){
                    player.seekTo(0);
                }
                else{
                    if(shuffle){
                        if(shuffledNames.isEmpty()){
                            shuffledPaths = new ArrayList<>(paths);
                            shuffledNames = new ArrayList<>(names);
                        }
                        randPos = r.nextInt(shuffledNames.size());
                        startPlaying();
                    }else {
                        position--;
                        if (position == -1)position=0;
                        startPlaying();
                    }

                }


                return true;
            }
            return false;
        }
    }

}
