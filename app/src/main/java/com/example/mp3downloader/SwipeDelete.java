package com.example.mp3downloader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

public class SwipeDelete extends ItemTouchHelper.SimpleCallback {

    private StoredListAdapt adapt;
    private Drawable icon;
    private ListsRecycAdapt listsRecycAdapt;
    private ColorDrawable background;
    private Context context;
    private DownloadAdapter downloadAdapter;


    public SwipeDelete(DownloadAdapter downloadAdapter,Context context) {
        super(0,ItemTouchHelper.LEFT );
        this.downloadAdapter = downloadAdapter;
        this.context = context;
    }






    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
    return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        int position = viewHolder.getAdapterPosition();
                downloadAdapter.deleteItem(position);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        icon = ContextCompat.getDrawable(context,R.drawable.ic_delete_sweep_black_24dp);
        background = new ColorDrawable(Color.WHITE);

        View itemView = viewHolder.itemView;
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        /*if (dX > 0) { // Swiping to the right
            int iconLeft = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            int iconRight = itemView.getLeft() + iconMargin;
            icon.setBounds(iconRight, iconTop, iconLeft, iconBottom);
            background.setBounds(itemView.getLeft(), itemView.getTop(),itemView.getLeft() + ((int) dX), itemView.getBottom());
            background.setBounds(itemView.getRight() + ((int) dX),itemView.getTop(), itemView.getRight(), itemView.getBottom());

        } */if (dX < 0 && dY==0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            background.setBounds(0 ,itemView.getTop(), itemView.getRight() + ((int)dX) , itemView.getBottom());

        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }



        background.draw(c);
        icon.draw(c);
    }

}
