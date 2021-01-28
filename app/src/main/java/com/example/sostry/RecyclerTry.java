package com.example.sostry;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class RecyclerTry extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<ModelClass> modelClassesList;
    String ph;
    private static final int REQUEST_CALL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclertry);

        

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        modelClassesList = new ArrayList<>();

        modelClassesList.add(new ModelClass(R.drawable.ic_radio_but , "POLICE","Crime committed\n Thief chor kill murder help stolen fight cops 100","100" ));
        modelClassesList.add(new ModelClass(R.drawable.ic_radio_but, "FIRE","Fire emergency \n aag flame agg burn smoke hot " ,"101"));
        modelClassesList.add(new ModelClass(R.drawable.ic_radio_but, "AMBULANCE","Need doctor support \n injured help health medical docter blood khun red headache checkup dardth ","102" ));
        modelClassesList.add(new ModelClass(R.drawable.ic_radio_but, "Disaster Management Services","Floods, earthquakes \n tsunami sunami rockfall bhukamb bard ","108" ));
        modelClassesList.add(new ModelClass(R.drawable.ic_radio_but, "Women Helpline","Women related issues \n force rape blood husband child son daughter periods","1091" ));
        modelClassesList.add(new ModelClass(R.drawable.ic_radio_but, "Women Helpline for Domestic Abuse ","Misbehaviour \n fight husband kill shout mara thappad argument ","181" ));
        modelClassesList.add(new ModelClass(R.drawable.ic_radio_but, "Senior Citizen Helpline","Help for the aged \n old die getup pee support son stick health","1091" ));
        modelClassesList.add(new ModelClass(R.drawable.ic_radio_but, "Relief Commissioner For Natural Calamities","Calamity help \n flood tsunami earthquake ","1070" ));
        modelClassesList.add(new ModelClass(R.drawable.ic_radio_but, "Railway Accident Emergency Service","Railway incidents \n patri railway train blood suicide leg cut hit and run","1072" ));
        modelClassesList.add(new ModelClass(R.drawable.ic_radio_but, "Road Accident Emergency Service","Roads accidents \n blood road car gadi truck auto riksha accident thoka bus jeep pedestrians","1073" ));
        modelClassesList.add(new ModelClass(R.drawable.ic_radio_but, "Children In Difficult Situation","Child abuse \n child kidnapping beggar small batcha labour work no payment childlabour misbehaviour beatern mara hit no payment","1098" ));
        modelClassesList.add(new ModelClass(R.drawable.ic_radio_but, "NATIONAL EMERGENCY NUMBER","For emergencies \n accident emergency help support" , "112"));

        buildRecyclerView();

    }

    public void buildRecyclerView()
    {
        mRecyclerView = findViewById(R.id.recyclerviewcoding);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new Adapter(modelClassesList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new Adapter.OnItemClickListener() {

            @Override
            public void onItemClick(int positions) {

                //changeItem(positions, "Clicked");
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });
    }

    public void changeItem(int position, String text) {
        modelClassesList.get(position).changeText111(text);
        mAdapter.notifyItemChanged(position);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void removeItem(int position) {
     //   modelClassesList.remove(position);

      // if(ContextCompat.checkSelfPermission(recyclertry.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
      // {
      //     ActivityCompat.requestPermissions(recyclertry.this , new String[] {Manifest.permission.CALL_PHONE} , REQUEST_CALL);
      // }else
      // {
            ph =  modelClassesList.get(position).getnum();
            makePhoneCall();
      //      String dial = "+91"+ ph;
      //      startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
      //      Toast.makeText(this, ph, Toast.LENGTH_SHORT).show();
      //  }


   //     mAdapter.notifyItemRemoved(position);
    }

    private void makePhoneCall() {

        if (ph.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(RecyclerTry.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(RecyclerTry.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + ph;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        } else {
            Toast.makeText(RecyclerTry.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu , menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}
