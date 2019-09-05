package com.example.mp3downloader;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;

public class AddSongList extends Fragment implements PlayerInterface, AddSongListAdapt.songsAddedList {
    private String key;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private ArrayList<String> songNames = new ArrayList<>();
    private ArrayList<String> songPaths = new ArrayList<>();
    private ArrayList<String> addedNames;
    private ArrayList<String> addedPaths;
    private AddSongListAdapt adapt;
    private RecyclerView recyclerView;
    private ImageButton button;
    private Activity activity = getActivity();


    @Override
    public void AddedToList(ArrayList<String> addedN, ArrayList<String> addedP,String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        editor.remove(key);
        editor.remove(key+"cglnrozrmrtozr");
        String json = gson.toJson(addedN);
        editor.putString(key,json);
        editor.commit();

        json = gson.toJson(addedP);
        editor.putString(key+"cglnrozrmrtozr",json);
        editor.commit();

        getFragmentManager().beginTransaction().remove(this).commit();

        ListsFragment.TextClicked a = (ListsFragment.TextClicked)getActivity();
        a.sendText(key);

    }


    public void setKey(String key,ArrayList<String> s,ArrayList<String> a){
        this.key = key;
        addedNames = s;
        addedPaths = a;
    }

    @Override
    public void songStu(int pos) {
        ((DownMainInter)getActivity()).songStuu(pos);
    }

    @Override
    public void listUpdate(ArrayList<String> names, ArrayList<String> paths) {
        ((DownMainInter)getActivity()).listUpdatee(names,paths);
    }

    @Override
    public void notPlaying(String s) {

    }

    public void getPlayList(String rootPath) {
        Log.e("TAG","asdsad file");

        try {
            Log.e("TAG","try");

            File rootFolder = new File(rootPath);
            File[] files = rootFolder.listFiles();
            for (File file : files) {
                Log.e("TAG","for");
                if (file.getName().endsWith(".mp3")) {
                    if(!addedPaths.contains(file.getAbsolutePath())){
                        songNames.add(file.getName());
                        songPaths.add(file.getAbsolutePath());
                    }
                    Log.e("TAG","in file");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recycleThem() {
        for(String s: songNames)Log.e("songNames:",s);
        adapt = new AddSongListAdapt(songNames,songPaths,this,button, activity,key,this,addedNames,addedPaths);
        recyclerView.setAdapter(adapt);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_dialog_fragment,container,false);
        button = view.findViewById(R.id.addBut);
        recyclerView = view.findViewById(R.id.playListRecyc);
        getPlayList(Environment.getExternalStorageDirectory()+"/FreeMp3");
        recycleThem();


        return view;


    }
}
