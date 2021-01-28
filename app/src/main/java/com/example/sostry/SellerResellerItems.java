package com.example.sostry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class SellerResellerItems extends AppCompatActivity {


    RelativeLayout layout1,layout2,layout3,layout4,layout5,layout6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_reseller_items);


        layout1 =findViewById(R.id.mp_btn);
        layout2 =findViewById(R.id.w_btn);
        layout3 =findViewById(R.id.j_btn);
        layout4 =findViewById(R.id.e_btn);
        layout5 =findViewById(R.id.c_btn);
        layout6 =findViewById(R.id.o_btn);



        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ResellerBuyer.class));
            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ResellerBuyer.class));
            }
        });
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ResellerBuyer.class));
            }
        });
        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ResellerBuyer.class));
            }
        });
        layout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ResellerBuyer.class));
            }
        });
        layout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ResellerBuyer.class));
            }
        });

    }
}
