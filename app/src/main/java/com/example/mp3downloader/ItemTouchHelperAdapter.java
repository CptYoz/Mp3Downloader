package com.example.mp3downloader;

public interface ItemTouchHelperAdapter {


    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

    void onRename(int pos);

    void notifyIndex();

}
