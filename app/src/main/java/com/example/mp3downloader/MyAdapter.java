package com.example.mp3downloader;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    public interface passData {
        void str(String s);
        void pos(int i);
        void stopPlay();
    }

    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();
    private Context mContext;
    private WebView view;
    private passData passData;
    private WebView webView, playerW;
    private VideoView videoView;
    private ImageButton close;
    private Map<Integer, Integer> map = new HashMap<Integer, Integer>();
    public boolean isClickable = true;
    private int downloadingSong;

    private final int[] countIf = {0};
    private void postDelay(final int i) {
        downloadingSong = i;

        final String a = "document.querySelector(\"#download_" + i + ">.options>.url\").href";
        final String b = "document.querySelector(\"#download_" + i + ">.options>.url\").click()";
        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.evaluateJavascript(a, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        if(countIf[0]>200){
                            Toast.makeText(mContext,"Can not download "+ names.get(i - 1)+" rightnow. Try it from an other resource.",Toast.LENGTH_LONG).show();
                            isClickable = true;
                            countIf[0]=0;
                        }else {

                        if (value.length() == 27 || value == null) {

                            countIf[0]++;
                            Log.e("Count",String.valueOf(countIf[0]));
                            Log.e("if:", "if");
                            postDelay(i);
                        } else {
                            Log.e("Tag", value);
                            Log.e("Tag", names.get(i - 1));
                            passData.str(names.get(i - 1));
                            value = value.replace("\"", "");
                            Log.e("download link:", value);
                            webView.loadUrl(value);
                            isClickable = true;
                            countIf[0]=0;
                        }
                    }}
                });
            }
        }, 100);
    }


    private void playDelay(final int i) {
        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.evaluateJavascript("document.querySelector(\"#player>[src]\").getAttribute(\"src\")", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        value = value.replace("\"", "");
                        playerW.setVisibility(View.VISIBLE);
                        close.setVisibility(View.VISIBLE);
                        passData.stopPlay();
                        playerW.loadUrl(value);
                        passData.pos(i);
                    }
                });
            }
        }, 150);
    }

    public MyAdapter(ArrayList<String> names, Context mContext, WebView webView, passData passData, WebView playerW, ImageButton close) {
        this.names = names;
        this.images = images;
        this.webView = webView;
        this.mContext = mContext;
        this.passData = passData;
        this.playerW = playerW;
        this.close = close;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_items, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final int position = i + 1;
        viewHolder.textView.setSelected(true);
        viewHolder.textView.setText(names.get(i));

        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClickable) {
                    webView.loadUrl("javascript:document.querySelector(\"[class='download " + position + "']\").click()");
                    postDelay(position);
                    isClickable = false;
                } else {
                    Toast.makeText(mContext, "Please wait until "+names.get(downloadingSong-1)+" is ready.", Toast.LENGTH_LONG).show();
                }
            }
        });
        viewHolder.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("javascript:document.querySelector(\"[class='player " + position + "']\").click()");
                playDelay(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        LinearLayout constraintLayout;
        ImageButton button;
        ImageButton button2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.downName);
            imageView = itemView.findViewById(R.id.imageView);
            constraintLayout = itemView.findViewById(R.id.linearL);
            button = itemView.findViewById(R.id.downloadButton);
            button2 = itemView.findViewById(R.id.playButton);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Log.e("Tag", "" + pos);
                }
            });
        }

    }




}
