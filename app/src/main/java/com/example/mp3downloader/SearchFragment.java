package com.example.mp3downloader;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.zip.Inflater;

public class SearchFragment extends Fragment {
    TextView t;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        try {
            Document doc = Jsoup.connect("https://www.mp3juices.cc/").get();
            ((TextView)view.findViewById(R.id.textView3)).setText(doc.title()+ " qwdweq");

            Elements elements = doc.getElementsByTag("div");
            for(Element element:elements) {
                ((TextView)view.findViewById(R.id.textView3)).append("içerdeyim");

                ((TextView) view.findViewById(R.id.textView3)).append(" " + element);
            }



    }catch (IOException e){
            e.printStackTrace();
        }


return view;
    }}