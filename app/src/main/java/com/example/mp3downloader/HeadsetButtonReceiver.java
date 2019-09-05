package com.example.mp3downloader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

public class HeadsetButtonReceiver extends BroadcastReceiver {
    public HeadsetButtonReceiver ()
    {
        super ();
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        Log.e("Recieved","Recieved");
        if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            return;
        }
        KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        Log.e("Key Event",String.valueOf(event));
        if (event == null) {
            return;
        }
        int action = event.getAction();
        if (action == KeyEvent.ACTION_UP) {
            MainActivity.getInstace().headsetBut();
        }
    }

}
