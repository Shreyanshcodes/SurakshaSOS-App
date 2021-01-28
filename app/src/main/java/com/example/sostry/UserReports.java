package com.example.sostry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class UserReports extends AppCompatActivity {
    public RecyclerView recyclerView;
    public FirebaseFirestore firebaseFirestore;
    public FirebaseAuth fauth;
    public FirestoreRecyclerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reports);

        recyclerView = findViewById(R.id.firestorelist);
        firebaseFirestore = FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();
        String UID = fauth.getCurrentUser().getUid().toString();


        Query query = firebaseFirestore.collection("VisualEvidenceReports").whereEqualTo("UserID",UID);



        FirestoreRecyclerOptions<ReportsModal> options = new FirestoreRecyclerOptions.Builder<ReportsModal>().setQuery(query,ReportsModal.class).build();

        adapter = new FirestoreRecyclerAdapter<ReportsModal, ReportsViewHolder>(options) {
            @NonNull
            @Override
            public ReportsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item,parent,false);
                return new ReportsViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ReportsViewHolder reportsViewHolder, int i, @NonNull ReportsModal reportsModal) {
                reportsViewHolder.list_status.setText(reportsModal.getAuthenticationOfReport());
                reportsViewHolder.list_submittedon.setText(reportsModal.getReportDetail());
                reportsViewHolder.list_rid.setText(reportsModal.getReportID());
            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }

    public class ReportsViewHolder extends  RecyclerView.ViewHolder {

        public TextView list_rid,list_status,list_submittedon;


        public ReportsViewHolder(@NonNull View itemView) {
            super(itemView);

            list_rid=itemView.findViewById(R.id.list_rid);
            list_status=itemView.findViewById(R.id.list_status);
            list_submittedon = itemView.findViewById(R.id.list_so);


        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
