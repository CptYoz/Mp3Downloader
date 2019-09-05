package com.example.mp3downloader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListsRecycAdapt extends RecyclerView.Adapter<ListsRecycAdapt.ViewHolder> implements ItemTouchHelperAdapter {
    public ArrayList<String> keys;
    Activity activity;
    public keysToPlay keysToPlay;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private ArrayList<String> deletedNames;
    private ArrayList<String> deletedPaths;
    int mRecentlyDeletedItemPosition;
    String deletedKey;

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(keys, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(keys, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);


    }
    @Override
    public void notifyIndex() {
        notifyDataSetChanged();
    }
    @Override
    public void onItemDismiss(int position) {
        Log.e("Dismniss","dsiadasdasd");
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
                notifyItemChanged(pos);
            }
        });
        alertDialog.setNegativeButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName =editText.getText().toString();
                if (newName.matches("")){
                    Toast.makeText(activity.getBaseContext(),"Give a proper name",Toast.LENGTH_LONG).show();
                    notifyItemChanged(pos);

                }
                else if (preferences.contains(newName)){
                    Toast.makeText(activity.getBaseContext(),"Name in use. Try an other!",Toast.LENGTH_LONG).show();
                    notifyItemChanged(pos);

                }
                else{
                    String json = preferences.getString(keys.get(pos), "");
                    String jj = preferences.getString(keys.get(pos) + "cglnrozrmrtozr", "");

                    editor.remove(keys.get(pos)).remove(keys.get(pos) + "cglnrozrmrtozr").apply();

                    keys.set(pos, newName);
                    editor.putString(keys.get(pos), json).putString(keys.get(pos) + "cglnrozrmrtozr", jj).apply();

                    notifyItemChanged(pos);

                }
            }
        });
        alertDialog.setView(view);
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }


    public interface keysToPlay {
        void keys(String s);
        void detachAtach(String a);
    }

    public ListsRecycAdapt(ArrayList<String> keys, Activity activity, keysToPlay keysToPlay,SharedPreferences preferences,SharedPreferences.Editor editor) {
        this.keysToPlay = keysToPlay;
        this.keys = keys;
        this.activity = activity;
        this.preferences = preferences;
        this.editor = editor;
        gson = new Gson();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_view_l, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.textView.setText(keys.get(i));
        viewHolder.songCount.setText(String.valueOf(i+1));
    }

    @Override
    public int getItemCount() {
        return keys.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView,songCount;

        public ViewHolder(View itemView) {
            super(itemView);
            songCount = itemView.findViewById(R.id.songCount);
            textView = itemView.findViewById(R.id.listText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    keysToPlay.keys(keys.get(getAdapterPosition()));
                }
            });

        }
    }

    public void deleteItem(int position) {
        deletedKey = keys.get(position);
        mRecentlyDeletedItemPosition = position;
        String Json = preferences.getString(keys.get(position),"");
        Type type = new TypeToken<List<String>>() {}.getType();
        deletedNames = gson.fromJson(Json,type);

        Json = preferences.getString(keys.get(position)+"cglnrozrmrtozr","");
        deletedPaths = gson.fromJson(Json,type);

        editor.remove(keys.get(position));
        editor.commit();
        editor.remove(keys.get(position)+"cglnrozrmrtozr");
        editor.commit();

        keysToPlay.detachAtach("a");

        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = activity.findViewById(R.id.activity_main);
        Snackbar snackbar = Snackbar.make(view, "Playlist Deleted",
                Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = gson.toJson(deletedNames);
                editor.putString(deletedKey,json);
                editor.commit();

                json = gson.toJson(deletedPaths);
                editor.putString(deletedKey+"cglnrozrmrtozr",json);
                editor.commit();
                keysToPlay.detachAtach("a");
                notifyItemInserted(mRecentlyDeletedItemPosition);
            }
        });
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.show();
    }
}
