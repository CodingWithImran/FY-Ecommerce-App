package com.codingwithimran.fycommerce.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.codingwithimran.fycommerce.Adapters.ShowAllProductAdapter;
import com.codingwithimran.fycommerce.Modals.PopularProductModal;
import com.codingwithimran.fycommerce.Modals.ShowAllProductModal;
import com.codingwithimran.fycommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShowAllActivity extends AppCompatActivity {
    RecyclerView rec_allshow;
    ShowAllProductAdapter showAllProductAdapter;
    ArrayList<ShowAllProductModal> list;
    FirebaseFirestore database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);
        // code for toolbar
        Toolbar toolbar = findViewById(R.id.showAll_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // connect with Layout
        rec_allshow = findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.show();
        // Get data from parent Activity
        String type = getIntent().getStringExtra("cat_type");
        // set layout, adapters
        showAllProductAdapter = new ShowAllProductAdapter(this, list);
        rec_allshow.setAdapter(showAllProductAdapter);
        database = FirebaseFirestore.getInstance();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        rec_allshow.setLayoutManager(layoutManager);
        // code for adapters
        if(type==null){
            database.collection("AllProducts").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    ShowAllProductModal allpromodal = document.toObject(ShowAllProductModal.class);
                                    list.add(allpromodal);
                                    showAllProductAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
        if(type != null && type.equalsIgnoreCase(type)){
            database.collection("AllProducts").whereEqualTo("name", type)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    ShowAllProductModal allpromodal = document.toObject(ShowAllProductModal.class);
                                    list.add(allpromodal);
                                    showAllProductAdapter.notifyDataSetChanged();
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}