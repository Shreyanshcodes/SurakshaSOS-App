package com.example.sostry;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SellerReseller extends Fragment {

    RelativeLayout layout1,layout2,layout3,layout4,layout5,layout6;
    View resell;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        resell =  inflater.inflate(R.layout.activity_seller_reseller_items , container, false);

        layout1 =resell.findViewById(R.id.mp_btn);
        layout2 =resell.findViewById(R.id.w_btn);
        layout3 =resell.findViewById(R.id.j_btn);
        layout4 =resell.findViewById(R.id.e_btn);
        layout5 =resell.findViewById(R.id.c_btn);
        layout6 =resell.findViewById(R.id.o_btn);

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ResellerBuyer.class));
            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ResellerBuyer.class));
            }
        });
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ResellerBuyer.class));
            }
        });
        layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ResellerBuyer.class));
            }
        });
        layout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ResellerBuyer.class));
            }
        });
        layout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ResellerBuyer.class));
            }
        });




        return resell;
    }
}
