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

public class UserReportsAudio extends AppCompatActivity {
    public RecyclerView recyclerView;
    public FirebaseFirestore firebaseFirestore;
    public FirebaseAuth fauth;
    public FirestoreRecyclerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reports_audio);

        recyclerView = findViewById(R.id.firestorelistaudio);
        firebaseFirestore = FirebaseFirestore.getInstance();
        fauth = FirebaseAuth.getInstance();
        String UID = fauth.getCurrentUser().getUid();


        Query query = firebaseFirestore.collection("AudioEvidenceReports").whereEqualTo("UserID",UID);

        FirestoreRecyclerOptions<ReportsAudioModal> options = new FirestoreRecyclerOptions.Builder<ReportsAudioModal>().setQuery(query,ReportsAudioModal.class).build();

        adapter = new FirestoreRecyclerAdapter<ReportsAudioModal, ReportsAudioViewHolder>(options) {
            @NonNull
            @Override
            public ReportsAudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item_audio,parent,false);
                return new ReportsAudioViewHolder(view);

            }

            @Override
            public void onBindViewHolder(@NonNull ReportsAudioViewHolder reportsAudioViewHolder, int i, @NonNull ReportsAudioModal reportsAudioModal) {
                reportsAudioViewHolder.list_status_audio.setText(reportsAudioModal.getAuthenticationOfReport());
                reportsAudioViewHolder.list_submittedon_audio.setText(reportsAudioModal.getReportDetail());
                reportsAudioViewHolder.list_rid_audio.setText(reportsAudioModal.getReportID());

            }


        };
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);







    }



    public class ReportsAudioViewHolder extends RecyclerView.ViewHolder{

        public TextView list_rid_audio,list_status_audio,list_submittedon_audio;

        public ReportsAudioViewHolder(@NonNull View itemView) {
            super(itemView);

            list_rid_audio = itemView.findViewById(R.id.list_rid_audio);
            list_status_audio =itemView.findViewById(R.id.list_status_audio);
            list_submittedon_audio = itemView.findViewById(R.id.list_so_audio);
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