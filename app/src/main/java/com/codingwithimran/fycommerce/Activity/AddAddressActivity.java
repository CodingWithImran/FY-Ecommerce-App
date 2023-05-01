package com.codingwithimran.fycommerce.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.codingwithimran.fycommerce.Adapters.AddressAdapters;
import com.codingwithimran.fycommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {
    EditText name, address, city,phone_number;
    Button btn_add_address;
    Toolbar toolbar;
    FirebaseAuth auth;
    FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        toolbar = findViewById(R.id.add_address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.ad_name);
        address = findViewById(R.id.ad_address);
        city = findViewById(R.id.ad_city);
        phone_number = findViewById(R.id.ad_phone);
        btn_add_address = findViewById(R.id.ad_add_address);

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        // Add Address by clicking add address Button
        btn_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u_name = name.getText().toString();
                String u_address = address.getText().toString();
                String u_city = city.getText().toString();
                String u_number = phone_number.getText().toString();

                String final_address = "";

                if(!u_name.isEmpty()){
                    final_address += "name " + u_name;
                }
                if(!u_address.isEmpty()){
                    final_address += " address " + u_address;
                }
                if(!u_city.isEmpty()){
                    final_address +=" city " + u_city;
                }
                if(!u_number.isEmpty()){
                    final_address += " number " + u_number;
                }

                if(!u_name.isEmpty() && !u_address.isEmpty() && !u_city.isEmpty() &&  !u_number.isEmpty()){
                    Map<String, String> map = new HashMap<>();
                    map.put("fullAddress", final_address);
                    map.put("CustomerName", u_name);
                    map.put("customerAddress", u_address);
                    map.put("customerCity", u_city);
                    map.put("customerNumber", u_number);

                    database.collection("currentUser").document(auth.getCurrentUser().getUid())
                            .collection("Address").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(AddAddressActivity.this, "Address has been added successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(AddAddressActivity.this, DetailActivity.class);
                                            startActivity(intent);
                                        }
                                }
                            });

                }else{
                    Toast.makeText(AddAddressActivity.this, "Kindly filled all the field", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}