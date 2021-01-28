package com.example.sostry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VisualReport extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    int abccc = 0;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    public int check,permission1;
    private long backPressedTime;

    ImageView selectedImage;
    Button cameraBtn,galleryBtn,up;
    String currentPhotoPath,urlforpicture,role;
    StorageReference storageReference;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String userID;
    TextView details;
    RadioButton radioButton;
    RadioGroup rg;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_report);

        selectedImage = findViewById(R.id.displayImageView);
        cameraBtn = findViewById(R.id.cameraBtn);
        galleryBtn = findViewById(R.id.ReportUploadBtn);
        details = findViewById(R.id.image_description);
        rg = findViewById(R.id.radiogrp);

        fauth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
//      DocumentReference docRef1 = fstore.collection("users").document(fauth.getCurrentUser().getUid());

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = fauth.getCurrentUser().getUid();
        urlforpicture=storageReference.getPath();

        rg.setOnCheckedChangeListener(this);

        final DocumentReference docRef = fstore.collection("VisualEvidenceReports").document();



        cameraBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Toast.makeText(VisualReport.this, "Your Role"+role, Toast.LENGTH_SHORT).show();


                askCameraPermissions();
                                       }
                                       });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File f = new File(currentPhotoPath);
                urlforpicture = f.getName();

                Uri contentUri = Uri.fromFile(f);
                uploadImageToFirebase(f.getName(),contentUri);
                String Currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

                String details1 = details.getText().toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                String format = simpleDateFormat.format(new Date());
                String ReportID = format+Currentuser;
                String AuthenticationOfReport = "PendingForAuthentication";




                Map<String,Object> user = new HashMap<>();
                user.put("ReportDetail",details1);
                user.put("ImageReference",urlforpicture);
                user.put("UserID",Currentuser);
                user.put("ReportID",ReportID);
                user.put("SubmittedOn",format);
                user.put("AuthenticationOfReport",AuthenticationOfReport);
                user.put("Report Perspective:",role);


                docRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(VisualReport.this, "Please wait for image to Upload", Toast.LENGTH_SHORT).show();
                         //   finish();
                                                }
                        else{
                            Toast.makeText(VisualReport.this, "Something is wrong", Toast.LENGTH_LONG).show();
                            }

                    }
                });



            }
        });
    }





    private void askCameraPermissions(){
        // if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        permission1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA )
                == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);

        }
        else {

            int permission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                        this,
                        PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE
                );
            }
            else {
                dispatchTakePictureIntent();
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
                 }
            else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
                 }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                galleryBtn.setVisibility(View.VISIBLE);
                File f = new File(currentPhotoPath);
                selectedImage.setImageURI(Uri.fromFile(f));
                Log.d("tag", "Absolute Url of Image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);




            }

        }

        if (requestCode == GALLERY_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
                selectedImage.setImageURI(contentUri);
                uploadImageToFirebase(imageFileName, contentUri);


            }

        }


    }

    private void uploadImageToFirebase(String name, Uri contentUri) {
        abccc = 1;
        final StorageReference image = storageReference.child("pictures/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
                    }
                });

                Toast.makeText(VisualReport.this, "Success", Toast.LENGTH_SHORT).show();
                abccc = 2;
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VisualReport.this, "Upload Failed,Please Check Your Internet Connectivity!", Toast.LENGTH_SHORT).show();
            }
        });

    }



    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {


            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                //        "com.example.registeringuserteam6t",
                        "com.example.sostry",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onBackPressed() {


        if(abccc==0) {
            if(backPressedTime + 2000 > System.currentTimeMillis())
            {
                finish();
            }
            Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
        }

        if(abccc == 1)
        {
            Toast.makeText(getApplicationContext(), "Please wait for the picture to upload", Toast.LENGTH_SHORT).show();
        }
        if(abccc == 2)
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
