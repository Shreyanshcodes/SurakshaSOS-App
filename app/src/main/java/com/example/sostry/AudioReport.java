package com.example.sostry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AudioReport extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    Button selectFile,upload;
    TextView notification,descriptionforaudio;
    Uri audiouri;

    FirebaseStorage storage;
    FirebaseFirestore fstore;
    private long backPressedTime;
    FirebaseAuth fauth;
    int abcccc=0;
    RadioGroup rg;


    String userID,url,role;
    String filename = System.currentTimeMillis() + "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_report);

        storage = FirebaseStorage.getInstance();
        selectFile = findViewById(R.id.selectFile);
        upload = findViewById(R.id.uploadAudioReport);
        notification = findViewById(R.id.notification);
        descriptionforaudio = findViewById(R.id.audioDescription);
        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = fauth.getCurrentUser().getUid();
        rg = findViewById(R.id.radiogrp);
        rg.setOnCheckedChangeListener(this);


        final DocumentReference docRef = fstore.collection("AudioEvidenceReports").document();


        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AudioReport.this, "Your Role"+role, Toast.LENGTH_SHORT).show();

                if(ContextCompat.checkSelfPermission(AudioReport.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    selectAudioFile();
                }
                else{
                    ActivityCompat.requestPermissions(AudioReport.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
                }

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(audiouri!=null){

                    String Currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String details1 = descriptionforaudio.getText().toString();

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                    String format = simpleDateFormat.format(new Date());
                    String ReportID = format+Currentuser;
                    String AuthenticationOfReport = "PendingForAuthentication";

                    Map<String,Object> user = new HashMap<>();
                    user.put("ReportDetail",details1);
                    user.put("UserID",Currentuser);
                    user.put("FileReference",filename);
                    user.put("ReportID",ReportID);
                    user.put("SubmittedOn",format);
                    user.put("AuthenticationOfReport",AuthenticationOfReport);
                    user.put("ReportPerspective:",role);

                    docRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(AudioReport.this, "Please wait for audio to upload", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else{
                                Toast.makeText(AudioReport.this, "Something is wrong", Toast.LENGTH_LONG).show();
                            }

                        }
                    });



                    if(audiouri!=null)
                        uploadFile(audiouri);
                    else
                        Toast.makeText(AudioReport.this, "PLz select", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadFile(Uri audiouri) {

        abcccc=1;




        final StorageReference storageReference = storage.getReference();

        storageReference.child("Audio Files Uploaded By Users").child(filename).putFile(audiouri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                url = taskSnapshot.getMetadata().toString();
                abcccc =2;
                Toast.makeText(AudioReport.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot){

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode ==9 &&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            selectAudioFile();
        }
        else {
            Toast.makeText(this, "Please Provide Permissions", Toast.LENGTH_SHORT).show();
        }

    }

    private void selectAudioFile() {

        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 86 && resultCode == RESULT_OK && data!=null)
        {
            upload.setVisibility(View.VISIBLE);
            audiouri = data.getData();
            notification.setText("A file is selected:"+data.getData().getLastPathSegment());
        }
        else {
            Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {


        if(abcccc==0) {
            if(backPressedTime + 2000 > System.currentTimeMillis())
            {
                finish();
            }
            Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
        }

        if(abcccc == 1)
        {
            Toast.makeText(getApplicationContext(), "Please wait for the audio to be upload", Toast.LENGTH_SHORT).show();
        }
        if(abcccc == 2)
        {   if(backPressedTime + 2000 > System.currentTimeMillis())
        {
            Toast.makeText(getApplicationContext(), "Reported Successfully", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
        }

        }

        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.radio_one:
                role = "Victim";
                break;

            case R.id.radio_two:
                role="Facilitator";
                break;

        }
    }
}
