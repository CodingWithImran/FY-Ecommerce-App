package com.codingwithimran.fycommerce.Fragments;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codingwithimran.fycommerce.Activity.ShowAllActivity;
import com.codingwithimran.fycommerce.Adapters.CategoryAdapter;
import com.codingwithimran.fycommerce.Adapters.NewProductAdapter;
import com.codingwithimran.fycommerce.Adapters.PopularProductAdapters;
import com.codingwithimran.fycommerce.Modals.Category;
import com.codingwithimran.fycommerce.Modals.PopularProductModal;
import com.codingwithimran.fycommerce.Modals.ProductModal;
import com.codingwithimran.fycommerce.R;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


//    ImageSlider imgslider;

    RecyclerView recyclerView, product_recyclerView, rec_allproducts;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> list;
    FirebaseFirestore database;

    ArrayList<ProductModal> productlist;
    ArrayList<PopularProductModal> allProduct_list;
    NewProductAdapter productAdapter;
    PopularProductAdapters allproduct;
    ProgressDialog progressDialog;

    LinearLayout home_layout;
    TextView showall, newshowAll, cat_showall;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        imgslider = (view).findViewById(R.id.img_slider);
        recyclerView = (view).findViewById(R.id.rec_category);
        product_recyclerView = (view).findViewById(R.id.new_product_rec);
        rec_allproducts = (view).findViewById(R.id.popular_rec);
        home_layout = (view).findViewById(R.id.home_layout);
        showall = (view).findViewById(R.id.popular_see_all);
        newshowAll = (view).findViewById(R.id.newProducts_see_all);


        showall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShowAllActivity.class);
                startActivity(intent);
            }
        });

        newshowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ShowAllActivity.class);
                startActivity(intent);
            }
        });






        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Please wait....");
        progressDialog.show();
       home_layout.setVisibility(View.INVISIBLE);

//        List<SlideModel> imgslide = new ArrayList<>();
//        imgslide.add(new SlideModel( R.drawable.banner1, "Discount on shoes", ScaleTypes.CENTER_CROP));
//        imgslide.add(new SlideModel( R.drawable.banner2, "Discount On Perfumes", ScaleTypes.CENTER_CROP));
//        imgslide.add(new SlideModel( R.drawable.banner3, "Discount On Perfumes", ScaleTypes.CENTER_CROP));
//        imgslider.setImageList(imgslide);

        list = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), list);
        recyclerView.setAdapter(categoryAdapter);

        database = FirebaseFirestore.getInstance();
        database.collection("categories").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Category categoryModal = document.toObject(Category.class);
                                list.add(categoryModal);
                                home_layout.setVisibility(View.VISIBLE);
                                categoryAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        recyclerView.setLayoutManager(gridLayoutManager);

        productlist = new ArrayList<>();
        productAdapter = new NewProductAdapter(getContext(), productlist);
        product_recyclerView.setAdapter(productAdapter);
        LinearLayoutManager productgridlayout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        product_recyclerView.setLayoutManager(productgridlayout);

        database = FirebaseFirestore.getInstance();
        database.collection("NewProducts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ProductModal modal = document.toObject(ProductModal.class);
                                productlist.add(modal);
                                productAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        allProduct_list = new ArrayList<>();
        allproduct = new PopularProductAdapters(getContext(), allProduct_list);
        rec_allproducts.setAdapter(allproduct);
        GridLayoutManager allproduct_layoutmanager = new GridLayoutManager(getContext(), 3);
        rec_allproducts.setLayoutManager(allproduct_layoutmanager);

        database.collection("AllProducts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PopularProductModal allpromodal = document.toObject(PopularProductModal.class);
                                allProduct_list.add(allpromodal);
                                allproduct.notifyDataSetChanged();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });



        return view;
    }
}