package com.example.sostry;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.drm.DrmStore;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
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

public class ProfileFragment extends Fragment {

    View resell;
    Button front , back;
    int abc;
    Button buy;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA };
    public int check,permission1;

    ImageView selectedImage , selectedimage1;
    String currentPhotoPath,urlforpicture , currentphotopath1 , urlforpicture1;
    StorageReference storageReference;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String userID;
    TextView details;
    String productdisstr;
    TextInputLayout prodis ,  sellername , sellernumber;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        resell =  inflater.inflate(R.layout.fragment_profile , container, false);

        front = resell.findViewById(R.id.frontimgclick);
        back = resell.findViewById(R.id.backimgclick);
        buy = resell.findViewById(R.id.finallybuy);
        prodis = resell.findViewById(R.id.productdis);

        sellername = resell.findViewById(R.id.sellernameas);
        sellernumber = resell.findViewById(R.id.sellerphoneas);
        selectedImage = resell.findViewById(R.id.fontimageview);
        selectedimage1 = resell.findViewById(R.id.backimageview);

        fauth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
//      DocumentReference docRef1 = fstore.collection("users").document(fauth.getCurrentUser().getUid());

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = fauth.getCurrentUser().getUid();
        urlforpicture=storageReference.getPath();
        urlforpicture1=storageReference.getPath();

        final DocumentReference docRef = fstore.collection("ReSellerAndBuyer").document();

        front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                abc = 1;
                askCameraPermissions();

                //open camera here
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abc = 2;
                askCameraPermissions();

                // open camera here
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File f = new File(currentPhotoPath);
                File f1 = new File(currentphotopath1);
                urlforpicture1 =  f1.getName();
                urlforpicture = f.getName();
//                selectedImage.setImageURI(Uri.fromFile(f));
//                Log.d("tag", "Absolute Url of Image is " + Uri.fromFile(f));


//                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri1 = Uri.fromFile(f1);
                Uri contentUri = Uri.fromFile(f);
                uploadImageToFirebase(f.getName(),contentUri);
                uploadImageToFirebase(f1.getName(),contentUri1);
//                mediaScanIntent.setData(contentUri);
//                this.sendBroadcast(mediaScanIntent);
                String Currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

                String details1 = prodis.getEditText().getText().toString();
                String name = sellername.getEditText().getText().toString();
                String phone = sellernumber.getEditText().toString();



                Map<String,Object> user = new HashMap<>();
                user.put("ReportDescription",details1);
                user.put("sellername",name);
                user.put("sellernum",phone);
                user.put("ImageReference",urlforpicture);
                user.put("Imagettwo",urlforpicture1);
                user.put("UserID",Currentuser);


                docRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getContext(), "Please Wait for file to upload", Toast.LENGTH_SHORT).show();
                          //  finish();
                        }
                        else{
                            Toast.makeText(getContext(), "Something is wrong", Toast.LENGTH_LONG).show();
                        }

                    }
                });



                // send name , phonenumber , 2 images , description to firebase


            }
        });

        return resell;
    }

    private void askCameraPermissions(){
        // if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        permission1 = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA )
                == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);

        }
        else {

            int permission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                        getActivity(),
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
                Toast.makeText(getContext(), "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
            //    galleryBtn.setVisibility(View.VISIBLE);


                if(abc == 1) {
                    File f = new File(currentPhotoPath);
                    selectedImage.setImageURI(Uri.fromFile(f));
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(f);
                    mediaScanIntent.setData(contentUri);
                    getActivity().sendBroadcast(mediaScanIntent);
                }
                File f1=null;
                if(abc ==2)
                {    f1 = new File(currentphotopath1);
                    selectedimage1.setImageURI(Uri.fromFile(f1));
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(f1);
                    mediaScanIntent.setData(contentUri);
                    getActivity().sendBroadcast(mediaScanIntent);
                }




                //uploadImageToFirebase(f.getName(),contentUri);


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

                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Upload Failed.", Toast.LENGTH_SHORT).show();
            }
        });

    }



    private String getFileExt(Uri contentUri) {
        ContentResolver c = getActivity().getContentResolver();
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
        if(abc == 1) {
            currentPhotoPath = image.getAbsolutePath();
        }
        if(abc == 2)
        {
         currentphotopath1 = image.getAbsolutePath();
        }
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {


            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        //        "com.example.registeringuserteam6t",
                        "com.example.sostry",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
}
