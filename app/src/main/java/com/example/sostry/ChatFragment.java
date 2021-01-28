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

public class ChatFragment extends Fragment {

    Button VisualReport11,AudioReport11;
    View myinf;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         myinf =  inflater.inflate(R.layout.fragment_chat , container, false);

         final Context context = getContext();

       VisualReport11 = myinf.findViewById(R.id.btnVisualReport11);
       AudioReport11  = myinf.findViewById(R.id.btnAudioReport11);

        VisualReport11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,VisualReport.class));

            }
        });

        AudioReport11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,AudioReport.class));


            }
        });




       return  myinf;
    }



}
