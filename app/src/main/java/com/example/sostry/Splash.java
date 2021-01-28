package com.example.sostry;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Thread.sleep;

public class Splash extends AppCompatActivity {
    ImageView starty , endy , logo,logo2;
    Animation topAnime , lower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashh);
        getSupportActionBar().hide();

        starty = findViewById(R.id.imageView1);
        endy = findViewById(R.id.nameee);
        logo=findViewById(R.id.nameeelogo);
        logo2 = findViewById(R.id.nameeelogo2);
        topAnime = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        lower = AnimationUtils.loadAnimation(this,R.anim.namelower);
        starty.setAnimation(topAnime);
         endy.setAnimation(lower);
         logo.setAnimation(lower);
         logo2.setAnimation(lower);
        Thread myThread = new Thread(new Runnable() {


            @Override
            public void run() {


                try {
                    sleep(1000);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                       //     starty.setVisibility(View.INVISIBLE);
                         //   endy.setVisibility(View.VISIBLE);
                        }

                    });
                    sleep(1000);

                     Intent intentt = new Intent(getApplicationContext() , IntroActivity.class);
                     startActivity(intentt);
                     finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        myThread.start();



    }
}
