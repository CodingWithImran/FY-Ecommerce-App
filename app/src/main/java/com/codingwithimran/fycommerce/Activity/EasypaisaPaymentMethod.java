package com.codingwithimran.fycommerce.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codingwithimran.fycommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EasypaisaPaymentMethod extends AppCompatActivity {
    EditText holderName, transactionId, price;
    Button submitscrshot, submitPayment;
    ImageView scrshot;

    HashMap<String, String> map;
    HashMap<String, String> productMap;
    FirebaseFirestore db;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easypaisa_payment_method);
        holderName = findViewById(R.id.et_name);
        transactionId = findViewById(R.id.et_transactionId);
        price = findViewById(R.id.et_price);
        submitscrshot = findViewById(R.id.et_select_scrshot);
        scrshot = findViewById(R.id.et_scrshotImage);
        submitPayment = findViewById(R.id.et_submit_payment);
        // Instances for Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        // Get data from Payment Activity
        Intent intent = getIntent();
        boolean isFromCart = intent.getBooleanExtra("easyisfromcart", false);
        if (isFromCart) {
            // data is from the cart activity
            map = (HashMap<String, String>) getIntent().getSerializableExtra("easymap");
            productMap = (HashMap<String, String>) getIntent().getSerializableExtra("easyproductmap");
        } else {
            map = (HashMap<String, String>) getIntent().getSerializableExtra("easymap");
        }
        Log.d("map", "onCreate: " + map );


        // on submit payment
        submitPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentOnCash();
            }
        });

    }
    public void paymentOnCash(){
        boolean isFromCart = getIntent().getBooleanExtra("is_from_cart", false);
        String saveCurrentTime, saveCurrentDate;
        String name = holderName.getText().toString();
        String trsction_id = transactionId.getText().toString();
        String pricepay = price.getText().toString();

        if (isFromCart) {
            // data is from the cart activity
            Calendar calfordate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MM, dd, yyyy");
            saveCurrentDate = currentDate.format(calfordate.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calfordate.getTime());

            map.put("saveCurrentDate", saveCurrentDate);
            map.put("saveCurrentTime", saveCurrentTime);
            map.put("paymentStatus", "Paid");
            map.put("AccountHolderName", name);
            map.put("TransactionId", trsction_id);
            map.put("Pay Price", pricepay);

            map.put("trackingStatus", "processing");
            map.put("OrderNumber", UUID.randomUUID().toString());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference ordersRef = db.collection("Cart orders");

// Create a new order document with the orderMap data
            DocumentReference orderDocRef = ordersRef.document();
            orderDocRef.set(map)
                    .addOnSuccessListener(aVoid -> {
                        // Order document created successfully, add details products
                        CollectionReference detailsProductsRef = orderDocRef.collection("detailsProducts");
                        detailsProductsRef.add(productMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
//                                deleteCartItems();
//                                db.collection("AddToCart").document(auth.getCurrentUser().getUid())
//                                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if(task.isSuccessful()){
//                                                    Toast.makeText(PaymentActivity.this, "Orders successful submit", Toast.LENGTH_SHORT).show();
//                                                    startActivity(new Intent(PaymentActivity.this, MainActivity.class));
//                                                }
//                                            }
//                                        });
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Error creating order document
                    });
        } else {
            map.put("customerId",FirebaseAuth.getInstance().getCurrentUser().getUid());
            map.put("paymentStatus", "Paid");
            map.put("TrackingStatus", "processing");
            map.put("AccountHolderName", name);
            map.put("TransactionId", trsction_id);
            map.put("Pay Price", pricepay);
            map.put("OrderNumber", UUID.randomUUID().toString());
            db.collection("Direct Purchase").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(EasypaisaPaymentMethod.this, "Orders Successfully Deposit", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(PaymentActivity.this, MainActivity.class));
                    }
                }
            });
        }
    }
}