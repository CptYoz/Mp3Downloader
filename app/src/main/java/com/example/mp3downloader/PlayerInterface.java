package com.example.mp3downloader;

import java.util.ArrayList;

public interface PlayerInterface {
    public void songStu(int pos);
    public void listUpdate(ArrayList<String> names, ArrayList<String> paths);
    void notPlaying(String s);

}
