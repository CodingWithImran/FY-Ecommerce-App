package com.codingwithimran.fycommerce.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codingwithimran.fycommerce.Adapters.MyCartAdapter;
import com.codingwithimran.fycommerce.Modals.MyCartModal;
import com.codingwithimran.fycommerce.Modals.ShowAllProductModal;
import com.codingwithimran.fycommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ViewCartActivity extends AppCompatActivity {
    RecyclerView cart_rec;
    MyCartAdapter myCartAdapter;
    ArrayList<MyCartModal> cartList;
    FirebaseFirestore database;
    FirebaseAuth auth;
    int OverAllTotalAmount;
    TextView totalamount;
    Button checkout;
    HashMap<String, Object> orderMap = new HashMap<>();
    HashMap<String, Object> productMap = new HashMap<>();
    int i=1;
    int data;
    MyCartModal allpromodal;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        //  ToolBar in cart Activity
        Toolbar toolbar = findViewById(R.id.cart_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // connection with layout
        totalamount = findViewById(R.id.cart_totalPrice);
        checkout = findViewById(R.id.checkout_btn);
        cart_rec = findViewById(R.id.cart_rec);

        // boradcast receiver for total amount
        LocalBroadcastManager.getInstance(this)
                .registerReceiver( dataReceiver, new IntentFilter("MyTotalAmount"));

        // Instance of database
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Waite...");
        cartList = new ArrayList<>();
        myCartAdapter = new MyCartAdapter(this, cartList);
        cart_rec.setAdapter(myCartAdapter);
        cart_rec.setLayoutManager(new LinearLayoutManager(this));

        // enter data in order Map

        // show code from database
        progressDialog.show();
        database.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User" +
                        "s").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                allpromodal = document.toObject(MyCartModal.class);
                                String documentId = document.getId();
                                allpromodal.setProductId(documentId);
                                cartList.add(allpromodal);
//                                HashMap<String, Object> documentMap = (HashMap<String, Object>) document.getData();
//                                dataMap.put(Integer.toString(i++), documentMap);
                                myCartAdapter.notifyDataSetChanged();
                            }
                            for (int i = 0; i < cartList.size(); i++) {
                                productMap.put("productName" + i, allpromodal.getProductName());
                                productMap.put("productPrice" + i, allpromodal.getTotalPrice());
                                productMap.put("productQuantity" + i, allpromodal.getQuantity());
                                productMap.put("productID " + i, allpromodal.getProductId());
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        // Store cart Data in hashmap functions



        // Checkout payment
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewCartActivity.this, AddressActivity.class);
//                intent.putExtra("buyMapItems", dataMap);
//                intent.putExtra("totalamount", cartOrder);
                intent.putExtra("is_from_cart", true);
                intent.putExtra("orderMap", orderMap);
                intent.putExtra("productMap", productMap);
                startActivity(intent);
            }
        });


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
//      Register receiver broadcast
    private BroadcastReceiver dataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            data = intent.getIntExtra("cartAmount", 0);
            totalamount.setText("Total Price : " + data);
            // Do something with the data here
            orderMap.put("totalPrice", data);
            orderMap.put("customerId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
    };

    public void refreshActivtiy() {
        finish();
        startActivity(getIntent());
    }
}