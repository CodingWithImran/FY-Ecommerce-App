package com.codingwithimran.fycommerce.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.codingwithimran.fycommerce.Modals.PopularProductModal;
import com.codingwithimran.fycommerce.Modals.ProductModal;
import com.codingwithimran.fycommerce.Modals.ShowAllProductModal;
import com.codingwithimran.fycommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {
    ProductModal productModal = null;
    PopularProductModal popularProductModal = null;
    ShowAllProductModal showAllProductModal = null;
    ImageView productimg, addquantity, removequantity;
    TextView description, name, price, quantity;
    Button addtocart, buyNowbtn;
    FirebaseFirestore database;
    FirebaseAuth auth;
    VideoView productVideo;
    VideoView videoView;
    int totalQuantity = 1;
    int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // Code For ToolBar
        Toolbar toolbar = findViewById(R.id.detail_activity_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get data from Layout
        productimg = findViewById(R.id.productImage);
        description = findViewById(R.id.productDescription);
        name = findViewById(R.id.name_product);
        price = findViewById(R.id.price_product);
        buyNowbtn = findViewById(R.id.btn_buyNow);
        addtocart = findViewById(R.id.addToCartBtn);
        addquantity = findViewById(R.id.plus);
        removequantity = findViewById(R.id.minus);
        quantity = findViewById(R.id.Quantity);
        productVideo = findViewById(R.id.productVideo);
        videoView = findViewById(R.id.productVideo);

        // Instance for Firebase
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

       // Get Data From New, Popular and all product by put Extra
        final Object obj = getIntent().getSerializableExtra("newProductDetails");
        final Object showobj = getIntent().getSerializableExtra("showAllDetail");
        final Object allproduct_obj = getIntent().getSerializableExtra("popularProductDetail");

       // Check Instance for Product Modal
        if(obj instanceof ProductModal){
            productModal = (ProductModal) obj;
        } else if (allproduct_obj instanceof PopularProductModal) {
            popularProductModal = (PopularProductModal) allproduct_obj;
        }else if(showobj instanceof ShowAllProductModal){
            showAllProductModal = (ShowAllProductModal) showobj;
        }

        // Get Data from Modal if not null
        if(productModal != null){
            Glide.with(this).load(productModal.getProduct_img()).into(productimg);
            description.setText(productModal.getDescription());
            name.setText(productModal.getName());
            price.setText(String.valueOf(productModal.getPrice()));

            totalPrice = totalQuantity * productModal.getPrice();

            // set video or image in product Modal

            String imagePath = productModal.getProduct_img();
            String videoPath = productModal.getProduct_video();

            if(imagePath != null){
                Glide.with(this).load(imagePath).into(productimg);
                productimg.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);
//                Toast.makeText(this, "image path is not null", Toast.LENGTH_SHORT).show();
            }
            else if(videoPath != null){
                try {
                    videoView.setVideoURI(Uri.parse(videoPath));
                    videoView.setVisibility(View.VISIBLE);
                    productimg.setVisibility(View.GONE);
                    videoView.start();

                }catch (Exception e){
                    Toast.makeText(this, "Video not displayed due to some problems", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "video path is not null", Toast.LENGTH_SHORT).show();
            }
        }
        if (popularProductModal !=null) {
            Glide.with(this).load(popularProductModal.getProduct_img()).into(productimg);
            description.setText(popularProductModal.getDescription());
            name.setText(popularProductModal.getName());
            price.setText(String.valueOf(popularProductModal.getPrice()));

            totalPrice = totalQuantity * popularProductModal.getPrice();
            //  set video and image in popularProduct Modal

            String imagePath = popularProductModal.getProduct_img();
            String videoPath = popularProductModal.getProduct_video();

            if(imagePath != null){
                Glide.with(this).load(imagePath).into(productimg);
                productimg.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);
//                Toast.makeText(this, "image path is not null", Toast.LENGTH_SHORT).show();
            }
            else if(videoPath != null){
                try {
                    videoView.setVideoURI(Uri.parse(videoPath));
                    videoView.setVisibility(View.VISIBLE);
                    productimg.setVisibility(View.GONE);
                    videoView.start();

                }catch (Exception e){
                    Toast.makeText(this, "Video not displayed due to some problems", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "video path is not null", Toast.LENGTH_SHORT).show();
            }


        }   if (showAllProductModal !=null) {
            description.setText(showAllProductModal.getDescription());
            name.setText(showAllProductModal.getName());
            price.setText(String.valueOf(showAllProductModal.getPrice()));
            totalPrice = totalQuantity * showAllProductModal.getPrice();

            String imagePath = showAllProductModal.getProduct_img();
            String videoPath = showAllProductModal.getProduct_video();

            if(imagePath != null){
                Glide.with(this).load(imagePath).into(productimg);
                productimg.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);
//                Toast.makeText(this, "image path is not null", Toast.LENGTH_SHORT).show();
            }
            else if(videoPath != null){
                try {
                    videoView.setVideoURI(Uri.parse(videoPath));
                    videoView.setVisibility(View.VISIBLE);
                    productimg.setVisibility(View.GONE);
                    videoView.start();

                }catch (Exception e){
                    Toast.makeText(this, "Video not displayed due to some problems", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "video path is not null", Toast.LENGTH_SHORT).show();
            }



        }
        // End For Data from Modal if not null


        addquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalQuantity < 10){
                    totalQuantity = totalQuantity + 1;
                    quantity.setText(String.valueOf(totalQuantity));
                    if(productModal != null){
                        totalPrice = totalQuantity * productModal.getPrice();
                    }
                    if(popularProductModal != null){
                        totalPrice = totalQuantity * popularProductModal.getPrice();
                    }
                    if(showAllProductModal != null){
                        totalPrice = totalQuantity * showAllProductModal.getPrice();
                    }
                }
            }
        });

        removequantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalQuantity > 1){
                    totalQuantity = totalQuantity - 1;
                    quantity.setText(String.valueOf(totalQuantity));
                }
            }
        });

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(auth.getCurrentUser() != null){
                    addToCartfunct();
                }else{
                    Toast.makeText(DetailActivity.this, "First please creat a account", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DetailActivity.this, RegistrationActivity.class));
                }
            }
        });

        // Code for Buy Now / Checkout
        buyNowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(auth.getCurrentUser() != null){
                    buyNowFunction();
                }else{
                    Toast.makeText(DetailActivity.this, "First please creat a account", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DetailActivity.this, RegistrationActivity.class));
                }
            }
        });
   
   
   
    }
    private void buyNowFunction() {
       // Declared Hashmap for the store of data

        String saveCurrentTime, saveCurrentDate;

        Calendar calfordate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM, dd, yyyy");
        saveCurrentDate = currentDate.format(calfordate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calfordate.getTime());

        final HashMap<String, Object> buyMap = new HashMap<>();
        if(productModal != null){
            buyMap.put("productNumber", productModal.getProductNumber());
        }
        if(showAllProductModal != null){
            buyMap.put("productNumber", showAllProductModal.getProductNumber());

        }
        if(popularProductModal != null){
            buyMap.put("productNumber", popularProductModal.getProductNumber());
        }
        buyMap.put("ProductName", name.getText().toString());
        buyMap.put("ProductPrice", price.getText().toString());
        buyMap.put("currentTime", saveCurrentTime);
        buyMap.put("currentDate", saveCurrentDate);
        buyMap.put("Quantity", totalQuantity);
        buyMap.put("totalPrice", totalPrice);
       // End of Hashmap functions

        Intent intent = new Intent(DetailActivity.this, AddressActivity.class);
//        if(productModal != null){
////            intent.putExtra("items", productModal);
////            intent.putExtra("totalamount", totalPrice);
//        }
//        if(popularProductModal != null){
////            intent.putExtra("items", popularProductModal);
////            intent.putExtra("totalamount", totalPrice);
//        }
//        if(showAllProductModal != null){
////            intent.putExtra("items", showAllProductModal);
////            intent.putExtra("totalamount", totalPrice);
//        }
        intent.putExtra("buyMapItems", buyMap);
        startActivity(intent);
    }
    private void addToCartfunct() {
        String saveCurrentTime, saveCurrentDate;

        Calendar calfordate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM, dd, yyyy");
        saveCurrentDate = currentDate.format(calfordate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calfordate.getTime());

        final HashMap<String, Object> cartMap = new HashMap<>();
        if(productModal != null){
            cartMap.put("productNumber", productModal.getProductNumber());
        }
        if(showAllProductModal != null){
            cartMap.put("productNumber", showAllProductModal.getProductNumber());
        }
        if(popularProductModal != null){
            cartMap.put("productNumber", popularProductModal.getProductNumber());
        }
        cartMap.put("ProductName", name.getText().toString());
        cartMap.put("ProductPrice", price.getText().toString());
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("currentDate", saveCurrentDate);
        cartMap.put("Quantity", totalQuantity);
        cartMap.put("totalPrice", totalPrice);


        database.collection("AddToCart").document(auth.getCurrentUser().getUid()).collection("Users")
                .add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                        finish();
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

}