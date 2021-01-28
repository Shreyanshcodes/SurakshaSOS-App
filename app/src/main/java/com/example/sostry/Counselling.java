package com.example.sostry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Counselling extends AppCompatActivity {


    FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;

    EditText Detail, Role, Mode, Time;
    Button counsellingBtn;
    String userID;
    String datetime = System.currentTimeMillis() + "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselling);

        Detail = findViewById(R.id.problemtxt);
        Role =findViewById(R.id.roletxt);
        Mode = findViewById(R.id.modetxt);
        Time = findViewById(R.id.timeslottxt);

        counsellingBtn = findViewById(R.id.counsellingBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();



        counsellingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final DocumentReference docRef = fstore.collection("CounsellingRequests").document(userID);

                String Details = Detail.getText().toString();
                String Roles = Role.getText().toString();
                String Modes = Mode.getText().toString().trim();
                String Times = Time.getText().toString().trim();

                Map<String, Object> user = new HashMap<>();
                user.put("CounsellingDetailsSharedByUser", Details);
                user.put("RoleOfAspirant", Roles);
                user.put("ModeRequested", Modes);
                user.put("TimePrefferedByUser", Times);
                user.put("CounsellingRequestGeneratedOn", datetime);


                docRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Counselling.this, "Successfully Requested for counselling", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(Counselling.this, "Something Went Wrong! Check Your Internet", Toast.LENGTH_LONG).show();
                        }

                    }
                });


            }
        });




    }
}
