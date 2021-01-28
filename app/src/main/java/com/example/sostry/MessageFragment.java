package com.example.sostry;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MessageFragment extends Fragment {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private ResultReceiver resultReceiver;
    private TextView textLatLong;



    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.SEND_SMS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    RECORD_AUDIO};

    private Button stopbtn;
    private MediaRecorder mRecorder;

    private static final String LOG_TAG = "AudioRecording";
    private static String mFileName = null;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;

    View myin;
   private ImageView btn_sos;
   private String message = "I Need Help , I am here:";
   private String phonenumber1, phonenumber2, phonenumber3, phonenumber4;
   private String latitudee,longitudee;
   private String finalsms,linkaddress;
   private String LocationAddress = "Loading...";

   private FirebaseAuth fauth;
   private FirebaseFirestore fstore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myin = inflater.inflate(R.layout.fragment_message , container, false);

        btn_sos = myin.findViewById(R.id.imageView_soso);

        resultReceiver = new AddressResultReceiver(new Handler());

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/AudioRecording.3gp";
        stopbtn = myin.findViewById(R.id.button_btnStop);

        textLatLong = myin.findViewById(R.id.textView_textLatLong);

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fstore.collection("users").document(fauth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {
                    phonenumber1 = documentSnapshot.getString("Contact 1");
                    phonenumber2 = documentSnapshot.getString("Contact 2");
                    phonenumber3 = documentSnapshot.getString("Contact 3");
                    phonenumber4 = documentSnapshot.getString("Contact 4");

                }
            }
        });
        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopbtn.setVisibility(View.GONE);
                try {
                    mRecorder.stop();
                    mRecorder.release();
                    Toast.makeText(getContext(), "Recording Saved...", Toast.LENGTH_LONG).show();
                }catch (Exception e)
                {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_sos.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {





                   if (CheckPermissions()) {
                       stopbtn.setVisibility(View.VISIBLE);

                       mRecorder = new MediaRecorder();
                       mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                       mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                       mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                       mRecorder.setOutputFile(mFileName);
                       try {
                           mRecorder.prepare();
                       } catch (IOException e) {
                           Log.e(LOG_TAG, "prepare() failed");
                       }
                       mRecorder.start();
                       Toast.makeText(getContext(), "Recording Started", Toast.LENGTH_LONG).show();
                   } else {
                       RequestPermissions();
                   }


                   ActivityCompat.requestPermissions(
                           getActivity(),
                           PERMISSIONS_STORAGE,
                           REQUEST_EXTERNAL_STORAGE
                   );
//
                   if (ContextCompat.checkSelfPermission(
                           getContext(), Manifest.permission.ACCESS_FINE_LOCATION
                   ) != PackageManager.PERMISSION_GRANTED) {
                       ActivityCompat.requestPermissions(
                               getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION   //check here
                       );

                   } else {
                       getCurrentLocation();
                   }

                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {



                           try {
                               SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                               String format = simpleDateFormat.format(new Date());

                               final DocumentReference docRef = fstore.collection("SOSAlerts").document();
                               String LastLocation = LocationAddress;
                               String Lat = latitudee;
                               String Long = longitudee;
                               String Solved = "No";
                               String UserID = fauth.getCurrentUser().getUid().toString();

                               Map<String,Object> user = new HashMap<>();
                               user.put("DATE_TIME_STAMP",format);
                               user.put("LastLocation",LastLocation);
                               user.put("Lat",Lat);
                               user.put("Long",Long);
                               user.put("Solved",Solved);
                               user.put("UserID",UserID);
                               docRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                       if(task.isSuccessful()){
                                           Toast.makeText(getContext(), "SOS Generated", Toast.LENGTH_SHORT).show();
                                       }
                                       else{
                                           Toast.makeText(getContext(), "Data Is Not Inserted ! Something Went Wrong", Toast.LENGTH_SHORT).show();
                                       }

                                   }
                               });
//                               finalsms =  message + "" + longitudee +  "Latitude:" + latitudee +"Location:"+ LocationAddress;
                               linkaddress = "https://www.google.com/maps/place/?q="+latitudee+","+longitudee;
                               finalsms = "Need Help! Track Me"+"\n"+linkaddress;
                               Toast.makeText(getContext(), phonenumber1, Toast.LENGTH_LONG).show();
//                               Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                               Toast.makeText(getContext(),finalsms, Toast.LENGTH_SHORT).show();

                               SmsManager sms = SmsManager.getDefault(); // using android SmsManager
                               sms.sendTextMessage(phonenumber1, null, finalsms, null, null);
                               sms.sendTextMessage(phonenumber2, null, finalsms, null, null);
                               sms.sendTextMessage(phonenumber3, null, finalsms, null, null);
                               sms.sendTextMessage(phonenumber4, null, finalsms, null, null);// adding number and text
                           } catch (Exception e) {
                               Toast.makeText(getContext(), "Cannot send sms", Toast.LENGTH_SHORT).show();
                               e.printStackTrace();
                           }

                       }
                   }, 7000);
               }
           });

        return myin;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(getContext(), "Permissions Denied", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_AUDIO_PERMISSION_CODE) {
            if (grantResults.length > 0) {
                boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (permissionToRecord && permissionToStore) {
                    Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getContext(), RECORD_AUDIO);

        if(result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED)
        {
            stopbtn.setVisibility(View.VISIBLE);
            return true;
        }
        else {
            return false;
        }
    }
    private void RequestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    private void getCurrentLocation() {


        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates(locationRequest, new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(getContext()).removeLocationUpdates(this);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestLocationIndex = locationResult.getLocations().size() - 1;
                    double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                    double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                    textLatLong.setText(
                            String.format(
                                    "Lat: %s \nLong: %s",latitude,longitude +"\n" + LocationAddress
                            )
                    );
                    latitudee = Double.toString(latitude);
                    longitudee=Double.toString(longitude);



                    Location location = new Location("providerNA");
                    location.setLatitude(latitude);
                    location.setLongitude(longitude);
                    fetchAddressFromLatLong(location);

                } else {
//                    progressBar.setVisibility(View.GONE);
                }

            }


        }, Looper.getMainLooper());
    }

    private void fetchAddressFromLatLong(Location location) {
        Intent intent = new Intent(getContext(), FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        getActivity().startService(intent);
      //  startActivity(intent);
    }

    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCESS_RESULT) {

                LocationAddress = resultData.getString(Constants.RESULT_DATA_KEY);


            } else {
                Toast.makeText(getContext(), resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }

        }
    }
}
