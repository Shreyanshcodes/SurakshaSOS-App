package com.example.sostry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CounsellingRequest extends Fragment {

    Button counselling;
    View myinf;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myinf =  inflater.inflate(R.layout.activity_counsellingfragment , container, false);


        final Context context = getContext();

        counselling = myinf.findViewById(R.id.btncounselling);


        counselling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, Counselling.class));

            }
        });
        return  myinf;
    }



}