package com.example.mp3downloader;


import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class DownloadsFragment extends Fragment implements PlayerInterface,DownloadAdapter.refresh {


    private  DownloadAdapter adapt;
    private RecyclerView recyclerView;
    private ArrayList<String> songNames = new ArrayList<>();
    private ArrayList<String> songPaths = new ArrayList<>();

    public interface refreshListF{
        void listFrefresh();
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
        ((DownMainInter)getActivity()).notPlaying(s);

    }

    public void getPlayList(String rootPath) {
        Log.e("TAG","asdsad file");

        try {
            Log.e("TAG","try");

            File rootFolder = new File(rootPath);
            File[] files = rootFolder.listFiles(); //here you will get NPE if directory doesn't contains  any file,handle it like this.
            Arrays.sort( files, new Comparator()
            {
                public int compare(Object o1, Object o2) {

                    if (((File)o1).lastModified() > ((File)o2).lastModified()) {
                        return -1;
                    } else if (((File)o1).lastModified() < ((File)o2).lastModified()) {
                        return +1;
                    } else {
                        return 0;
                    }
                }

            });
            for (File file : files) {
                if (file.getName().endsWith(".mp3")) {
                     Log.e("file name", file.getName());

                        songNames.add(file.getName());
                        songPaths.add(file.getAbsolutePath());


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.downloads_fragment,container,false);
        recyclerView = view.findViewById(R.id.rView);
        songNames.clear();
        songPaths.clear();
        getPlayList(Environment.getExternalStorageDirectory()+"/FreeMp3");
        if(songNames!=null){
            RecylceThem();
        }

        return view;
    }

    public void RecylceThem() {
        adapt = new DownloadAdapter(songNames,songPaths,this,getActivity(),this);
        recyclerView.setAdapter(adapt);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(adapt);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void refreshMe() {
        getActivity().getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();   }

    @Override
    public void refreshMain() {
        ((refreshListF)getActivity()).listFrefresh();
    }
}
