package com.example.mp3downloader;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;


public class NotificationService {
    private Context parent;
    private NotificationManager nManager;
    private NotificationCompat.Builder nBuilder;
    private RemoteViews remoteView;
    private final String CHANNEL_ID = "channelchannelwhatthefischannell?";
    private Intent play,pause, next, back;
    private MediaSessionCompat mediaSessionCompat;
    private int not = 0;

    public NotificationService(){
    }
    public NotificationService(Context parent, String s, Activity activity,int a)  {

        not = a;
        this.parent = parent;
        Bitmap bitmap = BitmapFactory.decodeResource(parent.getResources(), R.mipmap.large_icon);

        mediaSessionCompat = new MediaSessionCompat(activity, "a");

        nManager = (NotificationManager) parent.getSystemService(Context.NOTIFICATION_SERVICE);

        play = new Intent(parent, Constants.class);
        play.putExtra("DOo", "play");
        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(parent, 0, play, 0);

        pause = new Intent(parent,Constants.class).putExtra("DOo","pause");
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(parent,4,pause,0);


        next = new Intent(parent, Constants.class);
        next.putExtra("DOo", "next");
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(parent, 1, next, 0);

        back = new Intent(parent, Constants.class);
        back.putExtra("DOo", "back");
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(parent, 2, back, 0);


        Intent intent = activity.getIntent();
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setAction(Intent.ACTION_MAIN);
        PendingIntent pendingIntent = PendingIntent.getActivity(parent, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent dismiss = new Intent(parent, NotDismissReciever.class);
        intent.putExtra("NotDismiss", 0);

        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(parent, 4, dismiss, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = parent.getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            mChannel.setShowBadge(false);

            nManager.createNotificationChannel(mChannel);
        }

        if(not == 0) {
            nBuilder = new NotificationCompat.Builder(parent, CHANNEL_ID)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setContentTitle(s)
                    .setLargeIcon(bitmap)
                    .setSmallIcon(R.mipmap.note_orange)
                    .addAction(R.drawable.ic_skip_previous_black_24dp, "back", prevPendingIntent) // #0
                    .addAction(R.drawable.ic_pause_black_24dp, "play", pausePendingIntent)  // #1
                    .addAction(R.drawable.ic_skip_next_black_24dp, "next", nextPendingIntent)     // #2
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(0, 1, 2)
                            .setMediaSession(mediaSessionCompat.getSessionToken()))
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setOnlyAlertOnce(true)
                    .setContentIntent(pendingIntent)
                    .setChannelId(CHANNEL_ID)
                    .setAutoCancel(false)
                    .setDeleteIntent(dismissPendingIntent)
                    .setVibrate(new long[]{0L})
                    .setOngoing(false);

            Log.e("NOT","defsadasd");
            nManager.notify(0, nBuilder.build());
            not = 1;

        }
        else if (not == 1){

            nBuilder = new NotificationCompat.Builder(parent, CHANNEL_ID)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                    .setContentTitle(s)
                    .setLargeIcon(bitmap)
                    .setSmallIcon(R.mipmap.note_orange)
                    .addAction(R.drawable.ic_skip_previous_black_24dp, "back", prevPendingIntent)
                    .addAction(R.drawable.ic_pause_black_24dp, "play", pausePendingIntent)
                    .addAction(R.drawable.ic_skip_next_black_24dp, "next", nextPendingIntent)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(0, 1, 2)
                            .setMediaSession(mediaSessionCompat.getSessionToken()))
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setOnlyAlertOnce(true)
                    .setContentIntent(pendingIntent)
                    .setDeleteIntent(dismissPendingIntent)
                    .setChannelId(CHANNEL_ID)
                    .setAutoCancel(false)
                    .setVibrate(new long[]{0L})
                    .setOngoing(false);
            Log.e("NOT","tamamdır");
            nManager.notify(0, nBuilder.build());
        }
        else{
            nBuilder = new NotificationCompat.Builder(parent, CHANNEL_ID)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setContentTitle(s)
                    .setLargeIcon(bitmap)
                    .setSmallIcon(R.mipmap.note_orange)
                    .addAction(R.drawable.ic_skip_previous_black_24dp, "back", prevPendingIntent)
                    .addAction(R.drawable.play_tusu, "play", playPendingIntent)
                    .addAction(R.drawable.ic_skip_next_black_24dp, "next", nextPendingIntent)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(0, 1, 2)
                            .setMediaSession(mediaSessionCompat.getSessionToken()))
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setOnlyAlertOnce(true)
                    .setContentIntent(pendingIntent)
                    .setDeleteIntent(dismissPendingIntent)
                    .setChannelId(CHANNEL_ID)
                    .setAutoCancel(false)
                    .setVibrate(new long[]{0L})
                    .setOngoing(false);
            Log.e("NOT","play");
            nManager.notify(0, nBuilder.build());
        }
    }




}
