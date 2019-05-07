package com.example.mp3downloader;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    public static BottomNavigationView navigationView;
    public static SearchFragment sFragment;
    public static DownloadsFragment dFragment;
    public static ListsFragment lFragment;
    public static FragmentManager manager;
    public static FragmentTransaction transaction;
    public static FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        container = (FrameLayout)findViewById(R.id.contains);

        sFragment = new SearchFragment();
        dFragment = new DownloadsFragment();
        lFragment = new ListsFragment();

        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.add(R.id.contains,lFragment,"List Fragment");
        transaction.commit();

        navigationView = (BottomNavigationView)findViewById(R.id.navigationView);
        navigationView.setSelectedItemId(R.id.list);
        navigationView.setOnNavigationItemSelectedListener(this);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case  R.id.search:
                getSupportFragmentManager().beginTransaction().replace(R.id.contains,sFragment).commit();
                return true;
            case R.id.download:
                getSupportFragmentManager().beginTransaction().replace(R.id.contains,dFragment).commit();
                return true;
            case R.id.list:
                getSupportFragmentManager().beginTransaction().replace(R.id.contains,lFragment).commit();
                return true;
        }
        return false;
    }
}
