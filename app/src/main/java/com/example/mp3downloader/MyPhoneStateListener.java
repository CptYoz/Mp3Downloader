package com.example.mp3downloader;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

class MyPhoneStateListener extends PhoneStateListener {
    public void onCallStateChanged(int state, String incomingNumber) {

        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                Log.e("tag","ringing");
                // called when someone is ringing to this phone


                break;
        }
        }}
