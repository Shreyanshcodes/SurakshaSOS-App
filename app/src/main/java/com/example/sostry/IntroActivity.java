package com.example.sostry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tableIndicator;
    TextView nextBtn , skipp;
    Button getstart ;
    int position = 0;
    Animation BTNAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_intro);
        getSupportActionBar().hide();

        if(restorePrefData())
        {
            Intent mainActivity =new Intent(getApplicationContext() , LoginregisterActivity.class);  //Login
            startActivity(mainActivity);
            finish();
        }


        tableIndicator = findViewById(R.id.tab_idicator);
        nextBtn = findViewById(R.id.buttonnext);
        getstart = findViewById(R.id.btn_get_started);
        skipp = findViewById(R.id.skippp);
        BTNAnim = AnimationUtils.loadAnimation( getApplicationContext() ,R.anim.btn_animation);

        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("SOS Button","Get support in any kind of danger or in need of help",R.drawable.sosfinal));
        mList.add(new ScreenItem("Report Crime","Upload suspicious images of crime incidents",R.drawable.newpoli));
        mList.add(new ScreenItem("Safe Reseller","Be safe from police custody for selling second hand articles in case of stolen property",R.drawable.shorkeeper));


        screenPager = findViewById(R.id.walkthroughtintro);
        introViewPagerAdapter = new IntroViewPagerAdapter(this , mList);
        screenPager.setAdapter(introViewPagerAdapter);


        tableIndicator.setupWithViewPager(screenPager);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                position = screenPager.getCurrentItem();
                if(position < mList.size() - 1)
                {
                    position++;
                    screenPager.setCurrentItem(position);
                }

                if(position >= mList.size() - 1)
                {
                     Loadscreen();
                }

            }
        });

        skipp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mainActivity =new Intent(getApplicationContext() , LoginregisterActivity.class);
                startActivity(mainActivity);
                finish();

            }
        });

        tableIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == mList.size() - 1)
                {
                    Loadscreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        getstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(getApplicationContext(),LoginregisterActivity.class);

                startActivity(mainActivity);
                savedPrefs();
                finish();
            }
        });
    }
    private boolean restorePrefData()
    {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPrefs" , MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore = preferences.getBoolean("isintroOpen", false);
        return isIntroActivityOpenedBefore;
    }

    private void savedPrefs() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isintroOpen" , true);
        editor.commit();

    }

    private void Loadscreen()
    {
                    nextBtn.setVisibility(View.INVISIBLE);
                    getstart.setVisibility(View.VISIBLE);
                     tableIndicator.setVisibility(View.INVISIBLE);
                     getstart.setAnimation(BTNAnim);
                     savedPrefs();

    }
}
