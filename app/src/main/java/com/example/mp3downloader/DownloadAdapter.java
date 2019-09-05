package com.example.mp3downloader;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.MyViewHolder> implements ItemTouchHelperAdapter {


    ArrayList<String> name;
    ArrayList<String> path;
    private PlayerInterface playerInterface;
    private ArrayList<String> deletedSong = new ArrayList<>();
    private ArrayList<String> deletedPath = new ArrayList<>();
    private Activity activity;
    private int deletedPosition;
    private boolean deleted;
    private refresh fresh;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private Type type;

    public interface refresh {
        void refreshMe();
        void refreshMain();
    }


    public DownloadAdapter(ArrayList<String> name, ArrayList<String> path, PlayerInterface playerInterface, Activity activity, refresh fresh) {
        this.playerInterface = playerInterface;
        this.fresh = fresh;
        this.name = name;
        this.path = path;
        this.activity = activity;
        preferences = PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
        editor = preferences.edit();
        gson = new Gson();
        type = new TypeToken<List<String>>() {}.getType();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setSelected(true);
        String a = name.get(position);
        a = a.substring(0, a.length() - 4);
        holder.textView.setText(a);
        playerInterface.listUpdate(name, path);
        holder.songCount.setText(String.valueOf(1+position));
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView songCount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.listText);
            songCount = itemView.findViewById(R.id.songCount);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("size", String.valueOf(name.size()));
                    Log.e("tag", name.get(getAdapterPosition()));
                    Log.e("size", String.valueOf(path.size()));
                    Log.e("tag", path.get(getAdapterPosition()));

                    playerInterface.songStu(getAdapterPosition());
                }
            });


        }


    }

    private void changeOthers(String s, String ss) {
        Map<String, ?> allEntries = preferences.getAll();
        if (allEntries.size() > 1) {
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                Log.d("Entryset size", String.valueOf(allEntries.size()));
                if (!entry.getKey().equals("App Restrictions") && !entry.getKey().contains("cglnrozrmrtozr")) {
                    Log.e("key", entry.getKey());
                    String json = preferences.getString(entry.getKey(), "");
                    Log.e("Json", json);
                    ArrayList<String> a = gson.fromJson(json, type);
                    if (a == null) {
                        Log.d("A boş ?", "ssadasdsdasdasdd");
                        continue;
                    }
                    if (a.contains(s)) {
                        int pos = a.indexOf(s);
                        a.set(pos, ss+".mp3");
                        json = preferences.getString(entry.getKey() + "cglnrozrmrtozr", "");
                        ArrayList<String> b = gson.fromJson(json, type);
                        System.out.println(b);
                        String changePath = b.get(pos).replaceAll("\""+s+"\"",ss+".mp3");
                        System.out.println(changePath);
                        b.set(pos,b.get(pos).replaceAll(s,ss+".mp3"));
                        editor.remove(entry.getKey()).commit();
                        editor.remove(entry.getKey() + "cglnrozrmrtozr").commit();
                        System.out.println(b);

                        json = gson.toJson(a);
                        editor.putString(entry.getKey(),json).commit();
                        json= gson.toJson(b);
                        editor.putString(entry.getKey() + "cglnrozrmrtozr",json).commit();

                        fresh.refreshMain();
                    }
                    for (String aa : a) Log.d("Liste", aa);
                }
            }
        }

    }

    private void deletePlaylistSongs(String s){
        Map<String, ?> allEntries = preferences.getAll();
        if (allEntries.size() > 1) {
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                if (!entry.getKey().equals("App Restrictions") && !entry.getKey().contains("cglnrozrmrtozr")) {
                    Log.e("key", entry.getKey());
                    String json = preferences.getString(entry.getKey(), "");
                    Log.e("Json", json);
                    ArrayList<String> a = gson.fromJson(json, type);
                    if (a == null) {
                        Log.d("A boş ?", "ssadasdsdasdasdd");
                        continue;
                    }
                    if (a.contains(s)) {
                        int pos = a.indexOf(s);

                        a.remove(pos);
                        json = gson.toJson(a);
                        editor.putString(entry.getKey(),json).apply();

                         json = preferences.getString(entry.getKey()+"cglnrozrmrtozr","");
                         a = gson.fromJson(json,type);

                         a.remove(pos);
                         json = gson.toJson(a);
                         editor.putString(entry.getKey()+"cglnrozrmrtozr",json).apply();
                        fresh.refreshMain();
                    }
                }
            }
        }

    }


    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemDismiss(int position) {
    deleteItem(position);
    }

    @Override
    public void onRename(final int pos) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity,R.style.AlertDialogTheme);
        View view = activity.getLayoutInflater().inflate(R.layout.rename, null, false);
        final EditText editText = view.findViewById(R.id.rename);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                fresh.refreshMe();
            }
        });
        alertDialog.setNegativeButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName =editText.getText().toString();
                if (newName.matches("")){
                    Toast.makeText(activity.getBaseContext(),"Give a proper name",Toast.LENGTH_LONG).show();
                    fresh.refreshMe();

                }
                else {
                    String filePath = path.get(pos);
                    filePath = filePath.substring(0, path.get(pos).length() - name.get(pos).length());
                    File from = new File(path.get(pos));
                    Log.e("File Name", from.getName());
                    Log.e("File path", from.getAbsolutePath());
                    Log.e("File path", from.getPath());

                    File to = new File(filePath, newName + ".mp3");
                    if(to.exists()){
                        Toast.makeText(activity.getBaseContext(),"Name in use",Toast.LENGTH_LONG).show();
                        fresh.refreshMe();
                    }else {
                        Log.e("File to Name", to.getName());
                        Log.e("File path", to.getAbsolutePath());

                        boolean add = from.renameTo(to);
                        Log.e("add", String.valueOf(add));

                        Log.e("File after Name", from.getName());
                        Log.e("Path after Name", from.getAbsolutePath());

                        changeOthers(name.get(pos), newName);
                        Log.e("Bu değişti", name.get(pos));
                        fresh.refreshMe();
                    }}}
        });
        alertDialog.setView(view);
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    @Override
    public void notifyIndex() {

    }

    public void deleteItem(final int position) {
        deleted = true;
        deletedPosition = position;

        deletedSong.add(name.get(position));
        deletedPath.add(path.get(position));
        name.remove(position);
        path.remove(position);


        notifyItemRemoved(position);
        showUndoSnackbar();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (deletedSong.size() > 0) {
                    Log.e("Deleting: ", deletedSong.get(0));
                    playerInterface.notPlaying(deletedSong.get(0));
                    new File(deletedPath.get(0)).delete();
                    deletePlaylistSongs(deletedSong.get(0));
                    deletedSong.remove(0);
                    deletedPath.remove(0);

                }
            }
        }, 6000);

    }

    private void showUndoSnackbar() {
        View view = activity.findViewById(R.id.activity_main);
        Snackbar snackbar = Snackbar.make(view, "Song Deleted",
                Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name.add(deletedPosition, deletedSong.get(deletedSong.size() - 1));
                path.add(deletedPosition, deletedPath.get(deletedPath.size() - 1));

                deletedSong.remove(deletedSong.size() - 1);
                deletedPath.remove(deletedPath.size() - 1);
                notifyItemInserted(deletedPosition);
            }
        });

        snackbar.setActionTextColor(Color.WHITE);
        snackbar.show();
    }
}
