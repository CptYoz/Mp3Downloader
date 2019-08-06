package com.example.mp3downloader;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class AddSongListAdapt extends RecyclerView.Adapter<AddSongListAdapt.ViewHolder> {
    private ArrayList<String> names;
    private ArrayList<String> paths;
    private ArrayList<String> addedN;
    private ArrayList<String> addedP;
    private ImageButton button;
    private String key;
    private Activity activity;
    private songsAddedList addTolist;
    private PlayerInterface playerInterface;

    public interface songsAddedList{
        public void AddedToList(ArrayList<String> addedN,ArrayList<String> addedP,String key);
    }


    public AddSongListAdapt(ArrayList<String> names,  ArrayList<String> paths,PlayerInterface playerInterface,ImageButton button,Activity activity,String key, songsAddedList songsAddedList,ArrayList<String> addedN,  ArrayList<String> addedP)
    { this.playerInterface = playerInterface;
    this.activity = activity;
        this.names = names;
        this.button = button;
        this.key = key;
        this.paths = paths;
        addTolist = songsAddedList;
        this.addedN = addedN;
        this.addedP = addedP;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.selected_list_items,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        playerInterface.listUpdate(names,paths);
        final int count = addedN.size();
        String a = names.get(i);
        a = a.substring(0, a.length() - 4);
        viewHolder.textView.setText(a);
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    addedN.add(names.get(i));
                    addedP.add(paths.get(i));
                    Log.e("Added",names.get(i));
                    Log.e("Added",paths.get(i));
                }
                else{
                    addedN.remove(names.get(i));
                    addedP.remove(paths.get(i));
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTolist.AddedToList(addedN,addedP,key);
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        CheckBox checkBox;
        public ViewHolder(View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.listText);
            checkBox = itemView.findViewById(R.id.listCheck);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playerInterface.songStu(getAdapterPosition());
                }
            });

        }
    }
}
