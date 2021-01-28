package com.example.sostry;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    NavigationView navigationView;
    private long backPressedTime;
    FirebaseAuth fauth11;
    FirebaseFirestore fstore11;
    TextView nameside;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.BLACK);
        setSupportActionBar(toolbar);

        fauth11 = FirebaseAuth.getInstance();
        fstore11 = FirebaseFirestore.getInstance();

        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headView = navigationView.getHeaderView(0);
        ImageView profile = headView.findViewById(R.id.proflepic);
        nameside = headView.findViewById(R.id.nameehead);

        DocumentReference docRef1 = fstore11.collection("users").document(fauth11.getCurrentUser().getUid());
        docRef1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()){


                           String fullname = documentSnapshot.getString("firstName")+ " " + documentSnapshot.getString("lastName");
                          nameside.setText(fullname);

                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "working", Toast.LENGTH_SHORT).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open , R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        drawer.openDrawer(GravityCompat.START);

        if(savedInstanceState == null) {
            drawer.openDrawer(GravityCompat.START);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MessageFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_message);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.nav_message:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new MessageFragment()).commit();
                break;
            }
            case R.id.nav_chat:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new ChatFragment()).commit();
                break;
            }
            case R.id.nav_file:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new SellerReseller()).commit();
                break;
            }
            case R.id.nav_emcontact:
            {
                Intent intent1 = new Intent(MainActivity.this, RecyclerTry.class);
                startActivity(intent1);
                break;
            }
            case R.id.nav_share:
            {
             //   URL url = "ncrb.gov.in/"
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://ncrb.gov.in/"));
                startActivity(intent);
                break;
            }
            case R.id.nav_send: {
                Intent intent = new Intent(this , MapsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.settignss:{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new settingsone()).commit();
                break;
            }


            case R.id.nav_report_history:{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Report_history()).commit();
                break;
            }

            case R.id.nav_counselling:{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CounsellingRequest()).commit();
                break;
            }
        }



        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
       if(drawer.isDrawerOpen(GravityCompat.START))
       {
           drawer.closeDrawer(GravityCompat.START);
       } else
       {
        //   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container , new MessageFragment()).commit();
        //   navigationView.setCheckedItem(R.id.nav_message);

           if(backPressedTime + 2000 > System.currentTimeMillis())
           {
             //  super.onBackPressed();
               finish();
           }
           else {
               Toast.makeText(getApplicationContext(),"Press Again To Exit" , Toast.LENGTH_SHORT).show();
           }
           backPressedTime = System.currentTimeMillis();
       }



       }


    }



