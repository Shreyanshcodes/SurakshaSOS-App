package com.example.sostry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CounsellingFragment extends AppCompatActivity {

    Button Counselling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsellingfragment);

            Counselling = findViewById(R.id.btncounselling);

            Counselling.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), com.example.sostry.Counselling.class));
                }
            });


    }
}
