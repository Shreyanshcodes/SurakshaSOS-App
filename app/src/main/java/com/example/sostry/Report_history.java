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

public class Report_history extends Fragment {

    Button VisualReportHistory,AudioReportHistory;
    View myinf;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myinf =  inflater.inflate(R.layout.report_history_fragment , container, false);

        final Context context = getContext();

        VisualReportHistory = myinf.findViewById(R.id.btnVisualReportHistory);
        AudioReportHistory  = myinf.findViewById(R.id.btnAudioReportHistory);

        VisualReportHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, UserReports.class));
//
            }
        });

        AudioReportHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, UserReportsAudio.class));


            }
        });




        return  myinf;
    }



}
