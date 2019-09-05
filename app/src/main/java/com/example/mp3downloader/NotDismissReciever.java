package com.example.mp3downloader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotDismissReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Kiling","kilinggg");
        int a = intent.getIntExtra("NotDismiss",0);

        MainActivity.getInstace().killMe();
    }
}
