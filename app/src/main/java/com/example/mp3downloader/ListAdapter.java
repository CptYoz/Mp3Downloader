package com.example.mp3downloader;

import android.app.Activity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private ArrayList<String> name;
    private ArrayList<String> path;
    private PlayerInterface playerInterface;
    private ArrayList<String> addedName = new ArrayList<>();
    private ArrayList<String> addedPath = new ArrayList<>();
    private Activity activity;
    private ImageButton addBut;
    private backTo backTo;
    private SparseBooleanArray itemStateArray= new SparseBooleanArray();


    public interface backTo{
        void sendBList(ArrayList<String> a, ArrayList<String> b);
    }

    public ListAdapter(ArrayList<String> name, ArrayList<String> path, PlayerInterface playerInterface, Activity activity, ImageButton addBut,backTo backTo){
        this.name = name;
        this.path = path;
        this.playerInterface = playerInterface;
        this.activity = activity;
        this.addBut = addBut;
        this.backTo = backTo;
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
        playerInterface.listUpdate(name,path);
        String a = name.get(i);
        a = a.substring(0,a.length()-4);
        viewHolder.textView.setText(a);

        if (!itemStateArray.get(i,true)) viewHolder.checkBox.setChecked(true);
        else viewHolder.checkBox.setChecked(false);

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    itemStateArray.put(i,true);
                    addedName.add(name.get(i));
                    addedPath.add(path.get(i));
                    Log.e("Added",name.get(i));
                    Log.e("Added",path.get(i));
                }
                else{
                    itemStateArray.put(i,false);

                    addedName.remove(name.get(i));
                    addedPath.remove(path.get(i));
                }
            }
        });

        addBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backTo.sendBList(addedName,addedPath);

            }
        });
    }


    @Override
    public int getItemCount() {
        return name.size();    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        CheckBox checkBox;
        public ViewHolder(View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.listText);
            checkBox=itemView.findViewById(R.id.listCheck);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playerInterface.songStu(getAdapterPosition());
                }
            });

        }
    }

}
