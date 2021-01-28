package com.example.sostry;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfilenamePic extends AppCompatActivity {

    TextInputLayout name, con1, con2 , con3 , con4;
    TextView saved;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Profile");
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profilenamepic);


        saved = findViewById(R.id.saveddmainopen);
        con1 = findViewById(R.id.em1);
        con2 = findViewById(R.id.em2);
        con3 = findViewById(R.id.em3);
        con4 = findViewById(R.id.em4);

        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        final DocumentReference docRef = fstore.collection("users").document(userID);



        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!con1.getEditText().getText().toString().isEmpty() && !con2.getEditText().getText().toString().isEmpty() &&
                        !con3.getEditText().getText().toString().isEmpty() && !con4.getEditText().getText().toString().isEmpty() ){

                    String C1 = con1.getEditText().getText().toString().trim();
                    String C2 = con2.getEditText().getText().toString().trim();
                    String C3 = con3.getEditText().getText().toString().trim();
                    String C4 = con4.getEditText().getText().toString().trim();


//                    String C1 = Contact1.getText().toString().trim();
//                    String C2 = Contact2.getText().toString().trim();
//                    String C3 = Contact3.getText().toString().trim();
//                    String C4 = Contact4.getText().toString().trim();
                    if(!isValidMobile(C1)){
                        con1.setError("Enter Valid Phone Number");
                        return;

                    }
                    if(!isValidMobile(C2)){
                        con2.setError("Enter Valid Phone Number");
                        return;
                    }
                    if(!isValidMobile(C3)){
                        con3.setError("Enter Valid Phone Number");
                        return;
                    }
                    if(!isValidMobile(C4)){
                        con4.setError("Enter Valid Phone Number");
                        return;
                    }


                    Map<String,Object> user = new HashMap<>();
                    user.put("Contact 1",C1);
                    user.put("Contact 2",C2);
                    user.put("Contact 3",C3);
                    user.put("Contact 4",C4);





                    docRef.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
//                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                                finish();
                                showDialog();
                            }
                            else{
                                Toast.makeText(ProfilenamePic.this, "Data Is Not Inserted ! Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }
                else{
                    Toast.makeText(ProfilenamePic.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                    return;
                }

           //    Intent intent = new Intent(getApplicationContext() , MainActivity.class);
           //    startActivity(intent);
            }
        });
    }
    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
    public void showDialog(){
        LayoutInflater inflater = LayoutInflater.from(ProfilenamePic.this);
        View view =inflater.inflate(R.layout.alert_dialog,null);


        Button permissionbtn = view.findViewById(R.id.permissionsbtn);


        permissionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                runTimePermission();
//
            }
        });
        AlertDialog alertDialog = new AlertDialog.Builder(ProfilenamePic.this).setView(view).create();
        alertDialog.show();


    }
    public void runTimePermission(){

        Dexter.withContext(this).withPermissions(

                Manifest.permission.SEND_SMS,Manifest.permission.INTERNET,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO,Manifest.permission.CAMERA,Manifest.permission.CALL_PHONE
        ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if(multiplePermissionsReport.areAllPermissionsGranted()){
//
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).onSameThread().check();


    }

    @Override
    protected void onStart() {
        super.onStart();

    }


}
