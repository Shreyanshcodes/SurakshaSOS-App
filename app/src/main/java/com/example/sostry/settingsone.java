package com.example.sostry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class settingsone extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private Context context;
    private Button save , logoutt;
    private String userID;

    private FirebaseAuth fauth;
    private FirebaseFirestore fstore;

    private TextInputLayout firstname , lastname, adharrr , con1, con2 , con3 , con4  , emailll;
    private TextView phonewala;

    private View myInflator;
 //   private ImageView changeimg,loadimg;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myInflator = inflater.inflate(R.layout.settingsss , container, false);
   //     changeimg = myInflator.findViewById(R.id.newprofileimg);
   //     loadimg = myInflator.findViewById(R.id.loadimg);
        context = getContext();
        save = myInflator.findViewById(R.id.savesettings);
        logoutt = myInflator.findViewById(R.id.logout11);
        phonewala = myInflator.findViewById(R.id.phonenum11);

        //like this
        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        firstname = myInflator.findViewById(R.id.nameset);
        lastname = myInflator.findViewById(R.id.lastname11);
        emailll = myInflator.findViewById(R.id.email11);
        adharrr = myInflator.findViewById(R.id.Adhar11);
        con1 = myInflator.findViewById(R.id.em1set);
        con2 = myInflator.findViewById(R.id.em2set);
        con3 = myInflator.findViewById(R.id.em3set);
        con4 = myInflator.findViewById(R.id.em4set);

        userID = fauth.getCurrentUser().getUid();

        final DocumentReference docRef = fstore.collection("users").document(fauth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()){

                    firstname.getEditText().setText(documentSnapshot.getString("firstName"));
                    lastname.getEditText().setText(documentSnapshot.getString("lastName"));
                    emailll.getEditText().setText(documentSnapshot.getString("EmailID"));
                    adharrr.getEditText().setText(documentSnapshot.getString("AadharNumber"));
                    phonewala.setText(fauth.getCurrentUser().getPhoneNumber());
                    con1.getEditText().setText(documentSnapshot.getString("Contact 1"));
                    con2.getEditText().setText(documentSnapshot.getString("Contact 2"));
                    con3.getEditText().setText(documentSnapshot.getString("Contact 3"));
                    con4.getEditText().setText(documentSnapshot.getString("Contact 4"));

                }
            }
        });





        logoutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                startActivity(new Intent(getContext(), Splash.class));
                getActivity().finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String C1 = con1.getEditText().getText().toString();
                String C2 = con2.getEditText().getText().toString();
                String C3 = con3.getEditText().getText().toString();
                String C4 = con4.getEditText().getText().toString();
                String fname = firstname.getEditText().getText().toString();
                String lname = lastname.getEditText().getText().toString();
                String ename = emailll.getEditText().getText().toString().trim();
                String aname = adharrr.getEditText().getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
//                String first = firstname.getText().toString();
//                String last = lastname.getText().toString();
//                String emailid = email.getText().toString().trim();
//                String aadhar = aadharnumber.getText().toString();
//                String datebirth = dob.getText().toString();

                VerhoeffAlgorithm verhoff = new VerhoeffAlgorithm();
                verhoff.validateAadharNumber(aname);

                if(TextUtils.isEmpty(fname)){
                    firstname.setError("Cannot Be Empty");
                    return;

                }
                if(TextUtils.isEmpty(lname)){
                    lastname.setError("Cannot Be Empty");
                    return;
                }
                if(!ename.matches(emailPattern)){
                    emailll.setError("Email is not valid");
                    return;
                }
                if(!verhoff.validateAadharNumber(aname)){
                    adharrr.setError("Please Provide a valid Aadhaar!");
                    return;
                }
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


                final Map<String,Object> user = new HashMap<>();
                user.put("Contact 1",C1);
                user.put("Contact 2",C2);
                user.put("Contact 3",C3);
                user.put("Contact 4",C4);
                user.put("firstName",fname);
                user.put("lastName", lname);
                user.put("EmailID", ename);
                user.put("AadharNumber", aname);

                docRef.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getContext(), "Data Is Not Inserted ! Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

    //   changeimg.setOnClickListener(new View.OnClickListener() {
    //       @Override
    //       public void onClick(View view) {
    //           openFileChooser();
    //       }
    //   });


        return myInflator;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null ) {
            mImageUri = data.getData();

   //         Picasso.with(context).load(mImageUri).into(loadimg);
        }
        }
    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }





}
