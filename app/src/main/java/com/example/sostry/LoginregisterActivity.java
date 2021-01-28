package com.example.sostry;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class LoginregisterActivity extends AppCompatActivity {


    private static final String TAG = "LoginregisterActivity";
    int AUTHUI_REQUEST_CODE = 1011;
    Button regis;
    FirebaseFirestore fstore;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginregister);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            startActivity(new Intent(this, MainActivity.class));

            this.finish();


        }

    }



    public void handleLoginRegister(View view) {

        try {
            {


                List<AuthUI.IdpConfig> providers = Arrays.asList(



                        new AuthUI.IdpConfig.PhoneBuilder().build()

                );


                Intent intent = AuthUI.getInstance()

                        .createSignInIntentBuilder()

                        .setAvailableProviders(providers)

                        //        .setTosAndPrivacyPolicyUrls("https://example.com", "https://example.com")

                        .setLogo(R.drawable.final_logo)

                        //       .setAlwaysShowSignInMethodScreen(true)

                        //        .setIsSmartLockEnabled(false)

                        .build();


                startActivityForResult(intent, AUTHUI_REQUEST_CODE);
            }
        } catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }



    @Override

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTHUI_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                // We have signed in the user or we have a new user

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                Log.d(TAG, "onActivityResult: " + user.toString());

                //Checking for User (New/Old)

                if (user.getMetadata().getCreationTimestamp() == user.getMetadata().getLastSignInTimestamp()) {

                    //This is a New User

                } else {

                    //This is a returning user

                }


                Intent intent = new Intent(this, AddDetails.class);

                startActivity(intent);

                this.finish();

            } else {

                // Signing in failed

                IdpResponse response = IdpResponse.fromResultIntent(data);

                if (response == null) {

                    Log.d(TAG, "onActivityResult: the user has cancelled the sign in request");

                } else {

                    Log.e(TAG, "onActivityResult: ", response.getError());

                }

            }

        }

    }

}

