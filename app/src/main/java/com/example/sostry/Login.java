package com.example.sostry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {
    Button go , request;
    TextInputLayout phone, opt;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;

    String verificationId;
    PhoneAuthProvider.ForceResendingToken token;
    Boolean verificationInProgress = false;
    String userOTP;
    String num = "";
    int optnum = 0;
    String CountryCode = "+91";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        go = findViewById(R.id.login_button);
        phone = findViewById(R.id.username);
        opt = findViewById(R.id.Password);
        request = findViewById(R.id.requestopt);
        fstore = FirebaseFirestore.getInstance();



        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if(!phone.getEditText().getText().toString().isEmpty() && phone.getEditText().getText().toString().length()==10) {
                        num = phone.getEditText().getText().toString();
                        String phonenum = CountryCode+ num;
                        Toast.makeText(Login.this, num +"", Toast.LENGTH_SHORT).show();
                        requestOTP(phonenum);


                    }else {
                        Toast.makeText(Login.this, "Please Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                    }



                }
                catch (Exception e)
                {
                    Toast.makeText(Login.this, "Please Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
                }

            }
        });


        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
          //      optnum = Integer.parseInt(opt.getEditText().getText().toString());
                userOTP = opt.getEditText().getText().toString();
                try {
                    if(!opt.getEditText().getText().toString().isEmpty() && userOTP.length()==6){

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,userOTP);
                        verifyAuth(credential);
                        go.setText("Please wait");
                    }


                } catch (Exception e)
                {
                    Toast.makeText(Login.this, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(fauth.getCurrentUser()!=null){
            checkUserProfile();
        }
    }

    private void requestOTP(String phonenum) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phonenum, 30L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);



                verificationId = s;
                token = forceResendingToken;
          //      nextbtn.setText("Verify OTP");
                verificationInProgress = true;

            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(Login.this, "Cannot Verify"+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void verifyAuth(PhoneAuthCredential credential) {
        fauth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    checkUserProfile();

                }
                else{
                    Toast.makeText(Login.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void checkUserProfile() {
        DocumentReference docRef = fstore.collection("users").document(fauth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    startActivity(new Intent(getApplicationContext(),MessageFragment.class));
                    finish();
                }
                else{
                    startActivity(new Intent(getApplicationContext(),AddDetails.class));
                    finish();
                }
            }
        });

    }
}
