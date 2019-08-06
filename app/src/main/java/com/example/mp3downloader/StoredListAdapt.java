package com.example.mp3downloader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StoredListAdapt extends RecyclerView.Adapter<StoredListAdapt.ViewHolder> implements ItemTouchHelperAdapter {
    ArrayList<String> names;
    ArrayList<String> path;
    PlayerInterface playerInterface;
    private Context mContext;
    private String[] recentlyDeleted = new String[2];
    private Activity mActivity;
    private int mRecentlyDeletedItemPosition;
    private String key;
    private SharedPreferences preferences;
    private Gson gson;
    private SharedPreferences.Editor editor;
    private refresh fresh;

    private Type type;


    public interface refresh {
        void refreshMe();
    }

    public StoredListAdapt(ArrayList<String> names, ArrayList<String> path, PlayerInterface playerInterface, Activity activity, String key, SharedPreferences preferences, refresh fresh) {
        this.names = names;
        this.fresh = fresh;
        this.path = path;
        mActivity = activity;
        this.playerInterface = playerInterface;
        this.preferences = preferences;
        this.key = key;
        editor = preferences.edit();
        gson = new Gson();
        type = new TypeToken<List<String>>() {
        }.getType();

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_view, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StoredListAdapt.ViewHolder viewHoler, int i) {
        playerInterface.listUpdate(names, path);
        String a = names.get(i);
        a = a.substring(0, a.length() - 4);
        viewHoler.textView.setText(a);
        viewHoler.songCount.setText(String.valueOf(1 + i));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView, songCount;

        public ViewHolder(View itemView) {
            super(itemView);
            songCount = itemView.findViewById(R.id.songCount);
            textView = itemView.findViewById(R.id.listText);
            mContext = textView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playerInterface.songStu(getAdapterPosition());
                }
            });
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(names, i, i + 1);
                Collections.swap(path, i, i + 1);

            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(names, i, i - 1);
                Collections.swap(path, i, i - 1);
            }
        }

        notifyItemMoved(fromPosition, toPosition);
        String json = gson.toJson(names);
        editor.putString(key, json);
        editor.commit();
        json = gson.toJson(path);
        editor.putString(key + "cglnrozrmrtozr", json);
        editor.commit();
    }

    @Override
    public void onItemDismiss(int position) {
        deleteItem(position);
    }

    @Override
    public void onRename(final int pos) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity, R.style.AlertDialogTheme);
        View view = mActivity.getLayoutInflater().inflate(R.layout.rename, null, false);
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
                String newName = editText.getText().toString();
                String filePath = path.get(pos);
                if (newName.matches("")) {
                    Toast.makeText(mActivity.getBaseContext(), "Give a proper name", Toast.LENGTH_LONG).show();
                    fresh.refreshMe();
                } else {
                    filePath = filePath.substring(0, path.get(pos).length() - names.get(pos).length());
                    File from = new File(path.get(pos));
                    Log.e("File Name", from.getName());
                    Log.e("File path", from.getAbsolutePath());
                    Log.e("File path", from.getPath());

                    File to = new File(filePath, newName + ".mp3");
                    if (to.exists()) {
                        Toast.makeText(mActivity.getBaseContext(), "Name in use", Toast.LENGTH_LONG).show();
                        fresh.refreshMe();
                    } else {
                        Log.e("File to Name", to.getName());
                        Log.e("File path", to.getAbsolutePath());

                        boolean add = from.renameTo(to);
                        Log.e("add", String.valueOf(add));

                        Log.e("File after Name", from.getName());
                        Log.e("Path after Name", from.getAbsolutePath());

                        changeOthers(names.get(pos), newName, pos);
                        Log.e("Bu değişti", names.get(pos));
                    }
                }

            }
        });
        alertDialog.setView(view);
        AlertDialog dialog = alertDialog.create();
        dialog.show();


    }

    @Override
    public void notifyIndex() {
        notifyDataSetChanged();
    }

    private void changeOthers(String s, String newName, int poss) {
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
                        int position = a.indexOf(s);
                        a.set(position, newName + ".mp3");
                        json = preferences.getString(entry.getKey() + "cglnrozrmrtozr", "");
                        ArrayList<String> b = gson.fromJson(json, type);
                        System.out.println(b);
                        String changePath = b.get(position).replaceAll("\"" + s + "\"", newName + ".mp3");
                        System.out.println(changePath);
                        b.set(position, b.get(position).replaceAll(s, newName + ".mp3"));
                        editor.remove(entry.getKey()).commit();
                        editor.remove(entry.getKey() + "cglnrozrmrtozr").commit();
                        System.out.println(b);

                        json = gson.toJson(a);
                        editor.putString(entry.getKey(), json).commit();
                        json = gson.toJson(b);
                        editor.putString(entry.getKey() + "cglnrozrmrtozr", json).commit();

                        fresh.refreshMe();
                    }
                    for (String aa : a) Log.d("Liste", aa);
                }
            }
        }
    }

    public void deleteItem(int position) {
        mRecentlyDeletedItemPosition = position;
        recentlyDeleted[0] = names.get(position);
        recentlyDeleted[1] = path.get(position);
        names.remove(position);
        path.remove(position);

        editor.remove(key);

        String json = gson.toJson(names);
        editor.putString(key, json);
        editor.commit();
        json = gson.toJson(path);
        editor.putString(key + "cglnrozrmrtozr", json);
        editor.commit();

        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = mActivity.findViewById(R.id.activity_main);
        Snackbar snackbar = Snackbar.make(view, "Song Deleted",
                Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                names.add(mRecentlyDeletedItemPosition, recentlyDeleted[0]);
                path.add(mRecentlyDeletedItemPosition, recentlyDeleted[1]);

                editor.remove(key);

                String json = gson.toJson(names);
                editor.putString(key, json);
                editor.commit();
                json = gson.toJson(path);
                editor.putString(key + "cglnrozrmrtozr", json);
                editor.commit();

                notifyItemInserted(mRecentlyDeletedItemPosition);
            }
        });
        snackbar.setActionTextColor(Color.WHITE);
        snackbar.show();
    }

}
