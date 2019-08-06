package com.example.mp3downloader;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.PrecomputedText;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ConsoleMessage;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Handler;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.support.constraint.Constraints.TAG;


public class SearchFragment extends Fragment implements MyAdapter.passData {

    public  WebView webView, playerW;
    private boolean isloading = false;
    public static String searched;
    private static ArrayList<String> songNames = new ArrayList<>();
    private Button searchBut;
    private ImageButton close;
    private String songName;
    private String songImage;
    private static String size ;
    private EditText searchInput;
    private  RecyclerView recyclerView;
    private  MyAdapter adapt;
    private  Context mContext;
    private int position;
    private File directory;
    private long downloadID;
    private NotificationManager notificationManager;
    private final String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
    private final String[] welcomeList = { "Billie Eilish","Ariana Grande","Shawn Mendes","Ed Sheeran","Taylor Swift","Imagine Dragons","Marshmello","Camila Cabello","Khalid","Halsey","Selena Gomez","Adele","Bruno Mars"
    ,"Rihanna","Beyoncé","Lady Gaga","Katy Perry","Jonas Brothers","Post Malone","Twenty One Pilots","The Beatles","Demi Lovato","Dua Lipa","Justin Bieber","Shakira","Drake","Nick Jonas","Anne-Marie","Justin Timberlake",
    "Sia","Grace VanderWaal","Adam Levine","XXXTentacion","Elton John","P!nk","Harry Styles","Bebe Rexha","Nicki Minaj","Bangtan Boys","Ava Max","Zayn Malik","Zendaya","Ellie Goulding","Sam Smith",
    "Jennifer Lopez","Christina Perri","The Weeknd","BLACKPINK","5 Seconds of Summer","Chris Brown","Joe Jonas","Kelly Clarkson","Brendon Urie","Louis Tomlinson","Madonna","Niall Horan","Blake Shelton",
    "Why Don't We","Troye Sivan","Liam Payne","TXT","Lil Uzi Vert","Calvin Harris","Luke Bryan","Alec Benjamin","NF","Cher","Jason Derulo","David Guetta","Kelly Rowland","The Vamps","Enrique Iglesias","Selena",
    "Jessica Simpson","Kylie Minogue","Aaliyah","Ashanti","Tyler Joseph","Jazmine Sullivan","Paul Simon","James Blunt","Monica","Keyshia Cole","Neil Young","Shreya Ghoshal","Chely Wright","Lil Pump","Borns","Rittz"};

    @Override
    public void str(String s) {
        songName = s;
    }

    @Override
    public void pos(int i) {
        position = i;
    }

    @Override
    public void stopPlay(){
        ((aa)getActivity()).stopIt();
    }

    public interface aa{
        public void stopIt();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)  {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        Toast.makeText(getContext(),"Remember to Search as Artist Name - Song Name...",Toast.LENGTH_LONG).show();
        Random random = new Random();
        searched = "\"" + welcomeList[random.nextInt(welcomeList.length)] + "\"";

        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean connected;
        if (networkInfo != null) {
            connected = true;
        } else {
    connected = false;        }


        if (connected== false){
            Toast.makeText(getContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }

        if (Environment.getExternalStorageState() == null) {
            directory = new File(Environment.getDataDirectory()
                    + "/FreeMp3/");

            if (!directory.exists()) {
                directory.mkdir();
            }
        } else if (Environment.getExternalStorageState() != null) {
            directory = new File(Environment.getExternalStorageDirectory()
                    + "/FreeMp3/");

            if (!directory.exists()) {
                directory.mkdir();
            }
        }

        mContext = getContext();


        webView = (view).findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(final WebView view, String url) {
                if(songNames.isEmpty()){
                    Log.e("Searched: ",searched);
                view.loadUrl("javascript:(function(){document.getElementById('query').value=" + searched + ";})();");
                view.loadUrl("javascript:(function(){document.getElementById('button').click();})()");
                Log.e("pageFinished", "page Finished");
                getLength(view);}
            }});

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setTitle(songName);
                request.setDestinationInExternalPublicDir(directory.getName(), songName+".mp3");
                DownloadManager dm = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
                downloadID = dm.enqueue(request);
            }
        });

        searchBut = view.findViewById(R.id.searchBut);
        close = view.findViewById(R.id.close);

        playerW = (WebView) view.findViewById(R.id.playerW);
        playerW.getSettings().setDomStorageEnabled(true);
        playerW.getSettings().setJavaScriptEnabled(true);

        searchInput = view.findViewById(R.id.editText);
        webView.loadUrl("https://www.mp3juices.cc/");
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handle = false;
                if(actionId == EditorInfo.IME_ACTION_SEND){
                    if (!searchInput.getText().toString().equals("")) {
                        songNames.clear();
                        searched = "\"" + searchInput.getText().toString() + "\"";
                        songNames.clear();
                        webView.loadUrl("https://www.mp3juices.cc/");
                        hideKeyboardFrom(getContext(), v);
                    } else Toast.makeText(getContext(), "Nothing to search!", Toast.LENGTH_LONG).show();
                    handle = true;
                }
                return handle;
            }
        });
        searchBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchInput.getText().toString().equals("")) {
                    songNames.clear();
                    searched = "\"" + searchInput.getText().toString() + "\"";
                    songNames.clear();
                    webView.loadUrl("https://www.mp3juices.cc/");
                    hideKeyboardFrom(getContext(), v);
                } else Toast.makeText(getContext(), "Nothing to search!", Toast.LENGTH_LONG).show();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("javascript:document.querySelector(\"[class='player " + position + "']\").click()");
                Log.e("close:","closee");
                playerW.loadUrl("javascript:document.open();document.close();");
                playerW.setVisibility(View.INVISIBLE);
                close.setVisibility(View.INVISIBLE);
            }
        });

        recyclerView = view.findViewById(R.id.recyc);
        adapt = new MyAdapter(songNames,getContext(),webView,this,playerW,close);

        return view;
    }


    public void RecylceThem() {
        recyclerView.setAdapter(adapt);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private  void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private  void getLength(final WebView view){
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.evaluateJavascript("document.querySelectorAll('#results .name').length;;", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.e("size",value);
                        size = value;
                        for(int i=0;i<Integer.valueOf(value);i++){
                            assignNames(view,i);
                        }

                    }
                });
            }
        },1000);

    }

    private  void assignNames(final WebView view, final int i){
        String s = "document.querySelectorAll(\"#results .name\")["+i+"].textContent";

        view.evaluateJavascript(s, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                value = value.replace("\"","");
                Log.e("song",value);
                songNames.add(value);
                if(songNames.size()==Integer.valueOf(size)){
                    RecylceThem();
                }
            }
        });

    }

    public BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadID == id) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);

                    // Configure the notification channel.
                    notificationChannel.setDescription("Channel description");
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.setVibrationPattern(new long[]{0, 500, 0, 0});
                    notificationChannel.enableVibration(true);
                    notificationManager.createNotificationChannel(notificationChannel);
                }


                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext(), NOTIFICATION_CHANNEL_ID);

                notificationBuilder.setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.ic_add_black_24dp)
                        .setContentTitle("Download Complete")
                        .setContentText(songName);
                notificationManager.notify(/*notification id*/1, notificationBuilder.build());
            }
        }
    };


}






