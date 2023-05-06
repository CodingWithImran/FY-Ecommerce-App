package com.codingwithimran.fycommerce.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codingwithimran.fycommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EasypaisaPaymentMethod extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 45;
    private Uri mImageUri;
    EditText holderName, transactionId, price;
    Button submitscrshot, submitPayment;
    ImageView scrshot;

    HashMap<String, String> map;
    HashMap<String, String> productMap;
    boolean isFromCart;
    FirebaseFirestore db;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    public EasypaisaPaymentMethod() {
    }

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

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Waite ... ");

        // Get data from Payment Activity
        Intent intent = getIntent();
        isFromCart = intent.getBooleanExtra("easyisfromcart", false);
        if (isFromCart) {
            // data is from the cart activity
            map = (HashMap<String, String>) getIntent().getSerializableExtra("orderMap");
            productMap = (HashMap<String, String>) getIntent().getSerializableExtra("productMap");
        } else {
            map = (HashMap<String, String>) getIntent().getSerializableExtra("buyMapItems");
        }
        Log.d("map", "onCreate: " + map );

        // on submit scrshot
        submitscrshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        // on submit payment
        submitPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentOnCash();
            }
        });

    }

   // choose a image from gallery
   private void openFileChooser() {
       Intent intent = new Intent();
       intent.setType("image/*");
       intent.setAction(Intent.ACTION_GET_CONTENT);
       startActivityForResult(intent, PICK_IMAGE_REQUEST);
   }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            scrshot.setVisibility(View.VISIBLE);
            Glide.with(this).load(mImageUri).into(scrshot);
        }
    }
    public void paymentOnCash(){
        isFromCart = getIntent().getBooleanExtra("easyisfromcart", false);
               // Get data from fields
                String saveCurrentTime, saveCurrentDate;
                String name = holderName.getText().toString();
                String trsction_id = transactionId.getText().toString();
                String pricepay = price.getText().toString();
        // current time for orders
        Calendar calfordate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM, dd, yyyy");
        saveCurrentDate = currentDate.format(calfordate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calfordate.getTime());

        if (isFromCart) {
            // data is from the cart activity

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



            if (mImageUri != null) {
                progressDialog.show();
                StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploads/" + System.currentTimeMillis() + ".jpg");
                fileRef.putFile(mImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    map.put("screenshot", imageUrl);
                                    DocumentReference orderDocRef = ordersRef.document();
                                    orderDocRef.set(map)
                                            .addOnSuccessListener(aVoid -> {
                                                // Order document created successfully, add details products
                                                CollectionReference detailsProductsRef = orderDocRef.collection("detailsProducts");
                                                detailsProductsRef.add(productMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(EasypaisaPaymentMethod.this, "Orders Successfully Deposit", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(EasypaisaPaymentMethod.this, MainActivity.class));
                                                    }
                                                });
                                            })
                                            .addOnFailureListener(e -> {
                                                // Error creating order document
                                            });
                                }
                            });
                        }
                    }
                });
            }

// Create a new order document with the orderMap data

        } else {
            map.put("saveCurrentDate", saveCurrentDate);
            map.put("saveCurrentTime", saveCurrentTime);
            map.put("customerId",FirebaseAuth.getInstance().getCurrentUser().getUid());
            map.put("paymentStatus", "Paid");
            map.put("TrackingStatus", "processing");
            map.put("AccountHolderName", name);
            map.put("TransactionId", trsction_id);
            map.put("Pay Price", pricepay);
            map.put("OrderNumber", UUID.randomUUID().toString());
            if (mImageUri != null) {
                progressDialog.show();
                StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploads/" + System.currentTimeMillis() + ".jpg");
                fileRef.putFile(mImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    map.put("screenshot", imageUrl);
                                    db.collection("Direct Purchase").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            progressDialog.dismiss();
                                            if(task.isSuccessful()){
                                                Toast.makeText(EasypaisaPaymentMethod.this, "Orders Successfully Deposit", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(EasypaisaPaymentMethod.this, MainActivity.class));
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }


        }
    }
}