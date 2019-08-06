package com.example.mp3downloader;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Constants extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"recieved", Toast.LENGTH_SHORT).show();

        String action= intent.getStringExtra("DOo");
        Log.i("Nottt", action);


        if (action.equals("back")) {
            Log.i("Nottt", "back");

        } else if (action.equals("play")) {
            Log.i("Nottt", "play");
        }
        else Log.i("Nottt", "Next");

    }
}
