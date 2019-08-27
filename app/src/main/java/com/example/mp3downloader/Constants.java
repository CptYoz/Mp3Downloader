package com.example.mp3downloader;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Constants extends BroadcastReceiver {
    private mainPlayPause playPause;
    @Override
    public void onReceive(Context context, Intent intent) {

        String action= intent.getStringExtra("DOo");
        Log.i("Nottt", action);



       if (action.equals("back")) {
            MainActivity.getInstace().prevSong();
        } else if (action.equals("play")) {
            MainActivity.getInstace().pause();
        }else if (action.equals("pause")){
           MainActivity.getInstace().play();
       }
        else MainActivity.getInstace().nextSong();

    }
}
