package com.example.sostry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
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

public class AddDetails extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;
    private static final int PICK_IMAGE_REQUEST = 1;


    EditText firstname, lastname, email, aadharnumber, dob;
    Button registerBtn;
    String userID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_details);
        getSupportActionBar().hide();


        firstname = findViewById(R.id.first_name);
        lastname = findViewById(R.id.last_name);
        email = findViewById(R.id.email_id);
        aadharnumber = findViewById(R.id.aadhar_number);
        dob = findViewById(R.id.date_birth);
        registerBtn = findViewById(R.id.registerBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        final DocumentReference docRef = fstore.collection("users").document(userID);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String first = firstname.getText().toString();
                String last = lastname.getText().toString();
                String emailid = email.getText().toString().trim();
                String aadhar = aadharnumber.getText().toString().trim();
                String datebirth = dob.getText().toString();

                VerhoeffAlgorithm verhoff = new VerhoeffAlgorithm();
                verhoff.validateAadharNumber(aadhar);

                if(TextUtils.isEmpty(first)){
                    firstname.setError("Cannot Be Empty");
                    return;

                }
                if(TextUtils.isEmpty(last)){
                    lastname.setError("Cannot Be Empty");
                    return;
                }
                if(!email.getText().toString().matches(emailPattern)){
                    email.setError("Email is not valid");
                    return;
                }
                if(!verhoff.validateAadharNumber(aadhar)){
                    aadharnumber.setError("Please Provide a valid Aadhaar!");
                    return;
                }

                    Map<String, Object> user = new HashMap<>();
                    user.put("firstName", first);
                    user.put("lastName", last);
                    user.put("EmailID", emailid);
                    user.put("AadharNumber", aadhar);
                    user.put("DateOfBirth", datebirth);


                    docRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(getApplicationContext(), ProfilenamePic.class));
                                finish();
                            } else {
                                Toast.makeText(AddDetails.this, "Data Is Not Inserted ! Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });



            }
        });
    }




}
