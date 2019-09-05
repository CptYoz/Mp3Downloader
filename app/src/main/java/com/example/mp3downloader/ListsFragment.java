package com.example.mp3downloader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class ListsFragment extends Fragment implements AddList,ListsRecycAdapt.keysToPlay {
    private String listName;
    private ImageButton addList;
    private Fragment fragment;
    private FrameLayout containerr;
    public static FragmentManager manager;
    public static FragmentTransaction transaction;
    private ArrayList<String> songNames ;
    private ArrayList<String> songPaths ;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ArrayList<String> storedList = new ArrayList<>();
    private ListsRecycAdapt adapt;
    private RecyclerView recyclerView;
    private Activity activity;
    private Gson gson;
    private AlertDialog dialog;


    private TextClicked mCallback;

    public interface TextClicked{
        void sendText(String text);
    }

    @Override
    public void keys(String s) {
        mCallback.sendText(s);
        containerr.setVisibility(View.VISIBLE);

    }

    @Override
    public void detachAtach(String a) {
        getActivity().getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    public void removeMe(){
        getChildFragmentManager().beginTransaction().remove(this).commit();
    }

    public void containerVisible(){
        containerr.setVisibility(View.VISIBLE);
    }
    public void containerInisible(){
        containerr.setVisibility(View.INVISIBLE);
    }

    public void setListName(ArrayList<String> a, ArrayList<String>b) {
        songNames = a;
        songPaths = b;
        Log.e("Addlist","Burda");
        for(String s:a)Log.e("ListFragment_addedNames", s);
        if(a.isEmpty()){
            editor.putString(listName,"");
            editor.commit();
            editor.putString(listName+"cglnrozrmrtozr","");
            editor.commit();
        }else {
            String json = gson.toJson(a);
            editor.putString(listName,json);
            editor.commit();

            String jj = gson.toJson(songPaths);
            editor.putString(listName+"cglnrozrmrtozr",jj);
            editor.commit();
        }


        getActivity().getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();
        checkShared();
    }

    @Override
    public void addList(ArrayList<String> addedN, ArrayList<String> addedP) {
        songNames = addedN;
        songPaths = addedP;
        Log.e("Addlist","Burda");

        String json = gson.toJson(addedN);
        editor.putString(listName,json);
        editor.commit();

        String jj = gson.toJson(addedP);
        editor.putString(listName+"cglnrozrmrtozr",jj);
        editor.commit();


        checkShared();
    }

    private void checkShared() {
        storedList.clear();
        Map<String,?> keys = preferences.getAll();
        for(Map.Entry<String,?> entry: keys.entrySet()){
            if(!entry.getKey().endsWith("cglnrozrmrtozr")&&!entry.getKey().equals("App Restrictions")){
                storedList.add(entry.getKey());
                Log.e("Bulacam oglum seni",entry.getKey());
            }
        }
    }

    public void detachAtach(){
        getActivity().getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.lists_fragment,container,false);
        addList = view.findViewById(R.id.addList);
        mCallback = (TextClicked)getActivity();
        fragment = new ListDialogFragment();
        containerr = view.findViewById(R.id.listContainer);
        recyclerView = view.findViewById(R.id.listsRecyc);
        activity = getActivity();



        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = preferences.edit();
        gson = new Gson();

        checkShared();
        if(storedList.size()>0){
            recycleThem();
        }

        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view1 = getLayoutInflater().inflate(R.layout.list_name,null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialogTheme);
                final EditText listNamee =view1.findViewById(R.id.listName);
                builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listNamee.getText().toString().matches("")){
                            Toast.makeText(getContext(),"Give a proper name",Toast.LENGTH_LONG).show();
                        }
                        else if (preferences.contains(listNamee.getText().toString())){
                            Toast.makeText(getContext(),"Name in use. Try an other!",Toast.LENGTH_LONG).show();
                        }
                        else {
                            listName = listNamee.getText().toString();
                            containerr.setVisibility(View.VISIBLE);
                            manager = getChildFragmentManager();
                            transaction = manager.beginTransaction();
                            transaction.replace(containerr.getId(),fragment).commit();
                        }
                            }
                        });
                builder.setView(view1);
                dialog = builder.create();
                dialog.show();

            }
        });


        return view;
    }

    private void recycleThem() {
        adapt = new ListsRecycAdapt(storedList, activity,this,preferences,editor);
        recyclerView.setAdapter(adapt);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(adapt);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

    }
}
