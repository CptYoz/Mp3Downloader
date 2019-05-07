package com.example.mp3downloader;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.zip.Inflater;

public class WelcomeAnim extends AppCompatActivity {
    public static Thread t;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.move);
        ConstraintLayout c = (ConstraintLayout)findViewById(R.id.c);
        c.setAnimation(animation);

        t = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2500);
                    Intent i = new Intent(WelcomeAnim.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        };
        t.start();
    }



}
