package com.codingwithimran.fycommerce.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.codingwithimran.fycommerce.Modals.PopularProductModal;
import com.codingwithimran.fycommerce.Modals.ProductModal;
import com.codingwithimran.fycommerce.Modals.ShowAllProductModal;
import com.codingwithimran.fycommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {
    Toolbar toolbar;
    private static final int RAZORPAY_PAYMENT_REQUEST_CODE = 1234;

    TextView subtotal, totalAmount, discount, shipping;
    Button paybtn, btncashondelivery;
    ProductModal productModal = null;
    PopularProductModal popularProductModal = null;
    ShowAllProductModal showAllProductModal = null;
    FirebaseFirestore database;
    FirebaseAuth auth;
    HashMap<String, String> map;
    HashMap<String, String> productMap;
    boolean isFromCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        subtotal = findViewById(R.id.sub_total);
        discount = findViewById(R.id.textView17);
        shipping = findViewById(R.id.textView18);
        totalAmount = findViewById(R.id.total_amt);
        paybtn = findViewById(R.id.pay_btn);
        btncashondelivery = findViewById(R.id.payondelivery);
        // code for toolbar
        toolbar = findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // instance of firebaseFirestore
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        Intent intent = getIntent();
        boolean isFromCart = intent.getBooleanExtra("is_from_cart", false);
        if (isFromCart) {
            // data is from the cart activity
            map = (HashMap<String, String>) getIntent().getSerializableExtra("orderMap");
            productMap = (HashMap<String, String>) getIntent().getSerializableExtra("productMap");
            subtotal.setText(String.valueOf(map.get("totalPrice")));
            totalAmount.setText(String.valueOf(map.get("totalPrice")));
        } else {
            map = (HashMap<String, String>) getIntent().getSerializableExtra("buyMapItems");
            subtotal.setText(String.valueOf(map.get("totalPrice")));
            totalAmount.setText(String.valueOf(map.get("totalPrice")));
        }

/*

*/
        // Pay money and add products
        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentActivity.this, EasypaisaPaymentMethod.class);
                //startActivity(new Intent(MainActivity.this, PaymentActivity.class));
                i.putExtra("price", totalAmount.getText().toString());
                    i.putExtra("easymap", map);
                    i.putExtra("easyproductmap", productMap);
                    i.putExtra("easyisfromcart", isFromCart);
                startActivityForResult(i, 0);
                //startActivity(i);
            }
        });
        btncashondelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentOnCash();
            }
        });

    }

    private void paymentMethod() {
        Checkout checkout = new Checkout();
        final Activity activity = PaymentActivity.this;
        try {
            JSONObject options = new JSONObject();
            //Set Company Name
            options.put("name", "My E-Commerce App");
            //Ref no
            options.put("description", "Reference No. #123456");
            //Image to be display
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_9A33XWu170gUtm");
            // Currency type
            options.put("currency", "USD");
            double total = Double.parseDouble(totalAmount.getText().toString());
            options.put("amount", total);
            JSONObject preFill = new JSONObject();
            //email
            preFill.put("email", "developer.kharag@gmail.com");
            //contact
            preFill.put("contact", "7489347378");
            options.put("prefill", preFill);
            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in starting checkout Processing", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onPaymentSuccess(String s) {


    }
    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "payment is uns", Toast.LENGTH_SHORT).show();
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RAZORPAY_PAYMENT_REQUEST_CODE) {
//            if (resultCode == Activity.RESULT_OK) {
//                String razorpayPaymentID = data.getStringExtra("razorpay_payment_id");
//                String razorpayOrderID = data.getStringExtra("razorpay_order_id");
//                String razorpaySignature = data.getStringExtra("razorpay_signature");
//                map.put("razorpayPaymentID", razorpayPaymentID);
//                map.put("razorpayOrderID", razorpayOrderID);
//                map.put("razorpaySignature", razorpaySignature);
//                database.collection("Order").document(auth.getCurrentUser().getUid()).collection("User Order")
//                        .add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentReference> task) {
//                                if(task.isSuccessful()){
//                                    Toast.makeText(PaymentActivity.this, "Orders Successfully Deposit", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//                // Store the order details in Firebase here
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                // Handle payment cancellation
//                Toast.makeText(this, "Result in cancellations", Toast.LENGTH_SHORT).show();
//            } else {
//                // Handle payment failure
//                Toast.makeText(this, "Result in failure", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    public void paymentOnCash(){
        isFromCart = getIntent().getBooleanExtra("is_from_cart", false);
        String saveCurrentTime, saveCurrentDate;
        if (isFromCart) {
            // data is from the cart activity
            Calendar calfordate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MM, dd, yyyy");
            saveCurrentDate = currentDate.format(calfordate.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calfordate.getTime());

            map.put("saveCurrentDate", saveCurrentDate);
            map.put("saveCurrentTime", saveCurrentTime);
            map.put("paymentStatus", "UnPaid");
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
                                deleteCartItems();
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
            map.put("paymentStatus", "UnPaid");
            map.put("TrackingStatus", "processing");
            map.put("OrderNumber", UUID.randomUUID().toString());
            database.collection("Direct Purchase").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(PaymentActivity.this, "Orders Successfully Deposit", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PaymentActivity.this, MainActivity.class));
                    }
                }
            });
        }
    }


    // Overide method for jazzcash

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that it is the SecondActivity with an OK result
        if (requestCode == 0 && resultCode == RESULT_OK) {
            // Get String data from Intent
            String ResponseCode = data.getStringExtra("pp_ResponseCode");
            System.out.println("DateFn: ResponseCode:" + ResponseCode);
            if(ResponseCode.equals("000")) {
                Toast.makeText(getApplicationContext(), "Payment Success", Toast.LENGTH_SHORT).show();
                onJazzCashSuccess();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Payment Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void onJazzCashSuccess(){
        isFromCart = getIntent().getBooleanExtra("is_from_cart", false);
        String saveCurrentTime, saveCurrentDate;

        Toast.makeText(this, ""+isFromCart, Toast.LENGTH_SHORT).show();
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


                                db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(PaymentActivity.this, "Orders successful submit", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Error creating order document
                    });

        } else {
            map.put("customerId",FirebaseAuth.getInstance().getCurrentUser().getUid());
            map.put("paymentStatus", "paid");
            map.put("TrackingStatus", "processing");
            map.put("OrderNumber", UUID.randomUUID().toString());
            database.collection("Direct Purchase").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(PaymentActivity.this, "Orders Successfully Deposit", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PaymentActivity.this, MainActivity.class));
                        startActivity(new Intent(PaymentActivity.this, MainActivity.class));
                    }
                }
            });
        }

    }

    public void deleteCartItems(){
//        database = FirebaseFirestore.getInstance();
        database.collection("AddToCart").document(auth.getCurrentUser().getUid()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(PaymentActivity.this, MainActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error deleting user's cart document", e);
                    }
                });
    }
}