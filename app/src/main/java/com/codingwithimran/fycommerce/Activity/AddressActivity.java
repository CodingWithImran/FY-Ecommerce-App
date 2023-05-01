package com.codingwithimran.fycommerce.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.codingwithimran.fycommerce.Adapters.AddressAdapters;
import com.codingwithimran.fycommerce.Adapters.NewProductAdapter;
import com.codingwithimran.fycommerce.Modals.AddressModals;
import com.codingwithimran.fycommerce.Modals.PopularProductModal;
import com.codingwithimran.fycommerce.Modals.ProductModal;
import com.codingwithimran.fycommerce.Modals.ShowAllProductModal;
import com.codingwithimran.fycommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class AddressActivity extends AppCompatActivity implements AddressAdapters.SelectedAddress{
    Toolbar toolbar;
    ProductModal productModal = null;
    PopularProductModal popularProductModal = null;
    ShowAllProductModal showAllProductModal = null;
    Button addressbtn, paymentBtn;
    RecyclerView rec_address;
    AddressAdapters addressAdapters;
    ArrayList<AddressModals> addressList;
    FirebaseFirestore database;
    FirebaseAuth auth;
    String maddress;
    HashMap<String, String> map;
    HashMap<String, String> productMap;
    Object obj;
    ProgressDialog progressDialog;
    int totalamount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        // Code For Toolbar
        toolbar = findViewById(R.id.address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Layout connections
        addressbtn = findViewById(R.id.add_address_btn);
        rec_address = findViewById(R.id.address_recycler);
        paymentBtn = findViewById(R.id.payment_btn);

        Intent intent = getIntent();
        boolean isFromCart = intent.getBooleanExtra("is_from_cart", false);
        if (isFromCart) {
            // data is from the cart activity
            map = (HashMap<String, String>) getIntent().getSerializableExtra("orderMap");
            productMap = (HashMap<String, String>) getIntent().getSerializableExtra("productMap");
        } else {
            // data is from the direct purchase activity
//            obj = getIntent().getSerializableExtra("items");
            map = (HashMap<String, String>) getIntent().getSerializableExtra("buyMapItems");
//            totalamount = getIntent().getIntExtra("totalamount", 0);
        }




        // Instance Of Firebase
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        // Dialog Options show
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Waite...");
        progressDialog.show();
        // ArrayList and setLayout Manager
        addressList = new ArrayList<>();
        rec_address.setLayoutManager(new LinearLayoutManager(this));
        // Set Adapters
        addressAdapters = new AddressAdapters(getApplicationContext(), addressList, this);
        rec_address.setAdapter(addressAdapters);
        // Get Datafrom firebase for showing address
        database.collection("currentUser").document(auth.getCurrentUser().getUid())
                        .collection("Address").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            for(DocumentSnapshot doc : task.getResult().getDocuments()){
                                AddressModals modals = doc.toObject(AddressModals.class);
                                addressList.add(modals);
                                map.put("customerName", modals.getCustomerName());
                                map.put("customerNumber", modals.getCustomerNumber());
                                map.put("customerCity", modals.getCustomerCity());
                                map.put("customerFullAddress", modals.getFullAddress());
                                addressAdapters.notifyDataSetChanged();
                            }
                        }
                    }
                });



        // move on addAddress Activity
        addressbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddressActivity.this, AddAddressActivity.class));
            }
        });

        // Payment Button for payment
        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressActivity.this, PaymentActivity.class);
//                double amount = 0.0;
//                if(obj instanceof ProductModal){
//                    ProductModal  productModal = (ProductModal) obj;
//                    intent.putExtra("product", productModal);
//                    amount = productModal.getPrice();
//                }
//                if(obj instanceof PopularProductModal){
//                    PopularProductModal popularProductModal = (PopularProductModal) obj;
//                    intent.putExtra("product", productModal);
//                    amount = popularProductModal.getPrice();
//                }
//                if(obj instanceof ShowAllProductModal){
//                    ShowAllProductModal showAllProductModal = (ShowAllProductModal) obj;
//                    intent.putExtra("product", productModal);
//                    amount = showAllProductModal.getPrice();
//                }
//                if (productModal != null || popularProductModal != null || showAllProductModal != null) {
//                        // do something with productModal
//                        intent.putExtra("amount", amount);
//                    }
//                else{
//                    intent.putExtra("amount", totalamount);
//                }
                intent.putExtra("buyMapItems", map);
                intent.putExtra("productMap", productMap);
                if (isFromCart) {
                    intent.putExtra("is_from_cart", true);
                    intent.putExtra("orderMap", map);
                    intent.putExtra("productMap", productMap);
                } else {
                    intent.putExtra("buyMapItems", map);
//                    totalamount = getIntent().getIntExtra("totalamount", 0);
                }

//                Toast.makeText(AddressActivity.this, "" + map, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }

    @Override
    public void setAddress(String address) {
        maddress = address;
    }
}