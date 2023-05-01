package com.codingwithimran.fycommerce.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.Toast;

import com.codingwithimran.fycommerce.Adapters.TrackingOrderAdapter;
import com.codingwithimran.fycommerce.Modals.TrackingOrderModal;
import com.codingwithimran.fycommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TrackingOrderActivity extends AppCompatActivity {
    RecyclerView rec_trackingorder;
    ArrayList<TrackingOrderModal> list;
    TrackingOrderAdapter trackingOrderAdapter;
    FirebaseAuth auth;
    FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_order);

        rec_trackingorder = findViewById(R.id.rec_trackingorder);


        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();

        list = new ArrayList<>();
        trackingOrderAdapter = new TrackingOrderAdapter(this, list);
        rec_trackingorder.setAdapter(trackingOrderAdapter);

        rec_trackingorder.setLayoutManager(new LinearLayoutManager(this));

        database.collection("Direct Purchase").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                    TrackingOrderModal modal = snapshot.toObject(TrackingOrderModal.class);


                    if (modal.getCustomerId().equals(uid)) {
                        // Run further process
                        list.add(modal);
                        trackingOrderAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        database.collection("Cart orders").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                    TrackingOrderModal modal = snapshot.toObject(TrackingOrderModal.class);
                    if (modal.getCustomerId().equals(uid)) {
                        // Run further process
                        list.add(modal);
                        trackingOrderAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}