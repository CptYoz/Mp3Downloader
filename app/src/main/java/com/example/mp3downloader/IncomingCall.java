package com.example.mp3downloader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class IncomingCall extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        try
        {
            if(intent != null && intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL"))
            {
System.out.println("asddddddddddddd");
                MainActivity.getInstace().pause();
            }
            else
            {
//get the phone state
                String newPhoneState = intent.hasExtra(TelephonyManager.EXTRA_STATE) ? intent.getStringExtra(TelephonyManager.EXTRA_STATE) : null;
                Bundle bundle = intent.getExtras();

                if(newPhoneState != null && newPhoneState.equals(TelephonyManager.EXTRA_STATE_RINGING))
                {
//read the incoming call number

                }
                else if(newPhoneState != null && newPhoneState.equals(TelephonyManager.EXTRA_STATE_IDLE))
                {
//Once the call ends, phone will become idle
                    Log.i("PHONE RECEIVER", "Telephone is now idle");
                   // MainActivity.getInstace().play();
                }
                else if(newPhoneState != null && newPhoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
                {
//Once you receive call, phone is busy
                    Log.e("PHONE RECEIVER", "Telephone is now busy");
                    MainActivity.getInstace().pause();
                }

            }

        }
        catch(Exception ee)
        {
            Log.i("Telephony receiver", ee.getMessage());
        }
    }
}
