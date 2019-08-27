package com.example.mp3downloader;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class ListDialogFragment extends Fragment implements PlayerInterface, ListAdapter.backTo {
    private static final String TAG="ListDialogFragment";
    private RecyclerView recyclerView;
    private ArrayList<String> songNames = new ArrayList<>();
    private ArrayList<String> songPaths = new ArrayList<>();
    private ListAdapter adapt;
    private Activity activity = getActivity();
    private ImageButton addBut;
    private ArrayList<String> addedNames;
    private ArrayList<String> addedPaths;
    private AddList addList;
    private Fragment fragmentA;
    private sendList sendList;

    public interface sendList{
        public void sendAList(ArrayList<String> a, ArrayList<String>b);
    }

    public void getList(ArrayList<String> a, ArrayList<String>b){
        addedPaths = b;
        addedNames = a;

    }

    @Override
    public void sendBList(ArrayList<String> a, ArrayList<String> b) {
        sendList = (sendList)getActivity();
        sendList.sendAList(a,b);
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
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
                    songNames.add(file.getName());
                    songPaths.add(file.getAbsolutePath());
                    Log.e("TAG","in file");

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void RecylceThem() {
        adapt = new ListAdapter(songNames,songPaths,this,activity,addBut,this);
        recyclerView.setAdapter(adapt);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_dialog_fragment,container,false);
        recyclerView = (RecyclerView)view.findViewById(R.id.playListRecyc);
        addBut = view.findViewById(R.id.addBut);
        final Fragment fragment = this;

        songNames.clear();
        songPaths.clear();
        getPlayList(Environment.getExternalStorageDirectory()+"/FreeMp3");
        if(songNames.size()!=0){
            RecylceThem();
        }
        else {
            Toast.makeText(getContext(),"Noting to add!",Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }

        return view;
    }
}
