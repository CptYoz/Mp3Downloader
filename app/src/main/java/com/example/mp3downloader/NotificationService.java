package com.example.mp3downloader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.browse.MediaBrowser;
import android.media.session.MediaSession;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.net.ResponseCache;

public class NotificationService {
    private Context parent;
    private NotificationManager nManager;
    private NotificationCompat.Builder nBuilder;
    private RemoteViews remoteView;
    private final String CHANNEL_ID = "channelchannelwhatthefischannell?";
    private Intent play, next, back;
    private MediaSessionCompat mediaSessionCompat;


    public NotificationService(Context parent, String s, Activity activity) {
        this.parent = parent;

        Bitmap bitmap = BitmapFactory.decodeResource(parent.getResources(), R.mipmap.large_icon);

        mediaSessionCompat = new MediaSessionCompat(activity, "a");

        nManager = (NotificationManager) parent.getSystemService(Context.NOTIFICATION_SERVICE);

        play = new Intent(parent,Constants.class);
        play.putExtra("DOo", "play");
        PendingIntent pausePendingIntent = PendingIntent.getActivity(parent, 0, play, 0);

        next = new Intent(parent, Constants.class);
        play.putExtra("DOo", "next");
        PendingIntent nextPendingIntent = PendingIntent.getActivity(parent, 0, next, 0);

        back = new Intent(parent,Constants.class);
        back.putExtra("DOo", "back");
        PendingIntent prevPendingIntent = PendingIntent.getActivity(parent, 0, back, 0);


        Intent intent = activity.getIntent();
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setAction(Intent.ACTION_MAIN);
        PendingIntent pendingIntent = PendingIntent.getActivity(parent, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = parent.getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.enableVibration(false);

            assert nManager != null;
            nManager.createNotificationChannel(mChannel);
        }

        nBuilder = new NotificationCompat.Builder(parent, CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE)
                .setContentTitle(s)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.mipmap.note_orange)
                .addAction(R.drawable.ic_skip_previous_black_24dp, "back", prevPendingIntent) // #0
                .addAction(R.drawable.ic_pause_black_24dp, "play", pausePendingIntent)  // #1
                .addAction(R.drawable.ic_skip_next_black_24dp, "next", nextPendingIntent)     // #2
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                        .setMediaSession(mediaSessionCompat.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setChannelId(CHANNEL_ID)
                .setAutoCancel(true)
                .setVibrate(new long[]{0L})
                .setOngoing(false);

        nManager.notify(0, nBuilder.build());
    }

    public void update(String s){
        nBuilder = new NotificationCompat.Builder(parent, CHANNEL_ID)
                .setContentTitle(s)
                .setOngoing(false);
        NotificationManagerCompat.from(parent).notify(0,nBuilder.build());
    }
    public void notificationCancel() {
        nManager.cancel(0);
    }

}
