package com.example.mp3downloader;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;



public class MainActivity extends AppCompatActivity implements DownloadsFragment.refreshListF,SearchFragment.aa, BottomNavigationView.OnNavigationItemSelectedListener,DownMainInter,ListDialogFragment.sendList,ListsFragment.TextClicked,PlayList.closePlayList,PlayList.addListFrag {
    public  BottomNavigationView navigationView;
    public  SearchFragment sFragment;
    public  DownloadsFragment dFragment;
    public static ListsFragment lFragment;
    public static FragmentManager manager;
    public static FragmentTransaction transaction;
    private final int REQ_STORAGE = 1;
    public  FrameLayout container;
    private WebView a;
    private TelephonyManager telephonyManager;
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
    private boolean looping = false;
    private Notification nBuilder;
    private static MainActivity ins;
    private int notOnGoing = 0;
    private String playingSong;
    private Activity activity;
    private Context context;
    private HeadsetButtonReceiver hr;
    private ComponentName mReceiverComponent;
    private AudioManager mAudioManager;
    private int acılıs = 0;


    @Override
    public void onBackPressed() {
        
        Intent i = new Intent();
        i.setAction((Intent.ACTION_MAIN));
        i.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(i);
    }

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
        shuffledNames.remove(s);
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
                playingSong = a;
                if(notOnGoing == 0){
                    try{
                        NotificationService notificationService = new NotificationService(this,a,this,0);
                        notOnGoing =1;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    try{
                        NotificationService notificationService = new NotificationService(this,a,this,1);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }


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
                playingSong = a;
                songName.setText(a);
                if(notOnGoing == 0){
                    try{
                        NotificationService notificationService = new NotificationService(this,a,this,0);
                        notOnGoing =1;

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    try{
                        NotificationService notificationService = new NotificationService(this,a,this,1);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }

        } catch (IOException e) {
            Toast.makeText(getBaseContext(),"Song Deleted",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        int seconds = (player.getDuration() / 1000) % 60 ;
        int minutes = (player.getDuration() / (1000*60)) % 60;
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
        int seconds = (player.getCurrentPosition() / 1000) % 60 ;
        int minutes = (player.getCurrentPosition() / (1000*60)) % 60;
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

    public static MainActivity  getInstace(){
        return ins;
    }

    private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String incomingNumber) {

            if (state == TelephonyManager.CALL_STATE_RINGING) {
                Log.i("LOG_TAG","State : RING RING");
                pause();
            }
            if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                Log.i("LOG_TAG","State : OFFHOOK");
                pause();
            }
            if (state == TelephonyManager.CALL_STATE_IDLE) {
                Log.i("LOG_TAG","State : IDLE");
                if(acılıs == 0)acılıs=1;
                else play();
            }
        }
    };



    @Override
    protected void onDestroy() {
        unregisterReceiver(sFragment.onDownloadComplete);
        mAudioManager.unregisterMediaButtonEventReceiver(mReceiverComponent);
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
        ins = this;
        activity = this;
        context = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},REQ_STORAGE);

        telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);

        mAudioManager =  (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mReceiverComponent = new ComponentName(this,HeadsetButtonReceiver.class);
        mAudioManager.registerMediaButtonEventReceiver(mReceiverComponent);



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
        seekBar = findViewById(R.id.barSeek);

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

        songName = findViewById(R.id.barSongname);
        songName.setSelected(true);
        songName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gdt.onTouchEvent(event);
            }
        });
        playBut = findViewById(R.id.playBut);



        playBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clicked == 0){
                    player.pause();
                    playBut.setImageResource(R.mipmap.play_deneme_1);
                    isPaused = true;
                    clicked = 1;
                    NotificationService notificationService = new NotificationService(context,playingSong,activity,23);
                }
                else {
                    player.start();
                    updateBar();
                    playBut.setImageResource(R.mipmap.pause_orange);
                    isPaused=false;
                    clicked=0;
                    NotificationService notificationService = new NotificationService(context,playingSong,activity,1);

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

        navigationView = findViewById(R.id.navigationView);
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

    public void nextSong(){

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
    }

    public void prevSong(){
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

    public void pause(){
        player.pause();
        playBut.setImageResource(R.mipmap.play_deneme_1);
        isPaused = true;
        clicked = 1;
        Log.e("PHONE RECEIVER", "Telephone is now pause");

        NotificationService notificationService = new NotificationService(this,playingSong,this,23);
    }

    public void play(){
        player.start();
        updateBar();
        playBut.setImageResource(R.mipmap.pause_orange);
        isPaused=false;
        clicked=0;
        Log.e("PHONE RECEIVER", "Telephone is now busy");

        NotificationService notificationService = new NotificationService(this,playingSong,this,1);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_HEADSETHOOK){
            if(isPaused)play();
            else pause();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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

    public void killMe(){
        System.exit(0   );
    }

    public void headsetBut(){
        if(clicked == 0){
            player.pause();
            playBut.setImageResource(R.mipmap.play_deneme_1);
            isPaused = true;
            clicked = 1;
            NotificationService notificationService = new NotificationService(context,playingSong,activity,23);
        }
        else {
            player.start();
            updateBar();
            playBut.setImageResource(R.mipmap.pause_orange);
            isPaused=false;
            clicked=0;
            NotificationService notificationService = new NotificationService(context,playingSong,activity,1);

        }
    }


}
