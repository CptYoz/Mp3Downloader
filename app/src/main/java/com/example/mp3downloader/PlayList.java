package com.example.mp3downloader;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlayList extends Fragment implements PlayerInterface,StoredListAdapt.refresh {

    private RecyclerView recyclerView;
    private ImageButton backButton;
    private TextView textView;
    private String key;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> paths = new ArrayList<>();
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private StoredListAdapt adapt;
    private closePlayList closeCallBack;
    private ImageButton addListBut;
    private addListFrag addListFragg;


    @Override
    public void refreshMe() {
        getActivity().getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();   }




    public interface closePlayList{
        public void closeIt();
    }

    public interface addListFrag{
        public void addListSong(String key,ArrayList<String> s, ArrayList<String> c);
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

    public void setKey(String s){
        key = s;
        Log.e("Key set edildi: ", key);
    }

    private void getPlayList() {
        String json = preferences.getString(key,"");
        if (!json.isEmpty()){
            Type type = new TypeToken<List<String>>() {}.getType();
            names = gson.fromJson(json,type);
            for(String s : names)Log.e("NAmesssss:",s);
            json = preferences.getString(key+"cglnrozrmrtozr","");
            paths = gson.fromJson(json,type);
            for(String s : paths)Log.e("Pathssss:",s);

        }

    }

    private void recycleThem(){
        adapt = new StoredListAdapt(names,paths,this,getActivity(),key,preferences,this);
        recyclerView.setAdapter(adapt);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(adapt);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list,container,false);
        recyclerView = view.findViewById(R.id.playListRecyc);
        backButton = view.findViewById(R.id.barBackButton);
        textView = view.findViewById(R.id.barListName);
        addListBut = view.findViewById(R.id.addListSong);
        closeCallBack = (closePlayList) getActivity();
        addListFragg = (addListFrag)getActivity();
        textView.setText(key);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        gson = new Gson();
        getPlayList();
        final Fragment fragment = this;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(fragment).commit();
                closeCallBack.closeIt();

            }
        });
        recycleThem();
        addListBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            getFragmentManager().beginTransaction().remove(fragment).commit();
            addListFragg.addListSong(key,names,paths);
            }
        });
        return view;
    }

}
