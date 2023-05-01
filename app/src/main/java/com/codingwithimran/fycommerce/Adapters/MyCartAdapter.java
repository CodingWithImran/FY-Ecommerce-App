package com.codingwithimran.fycommerce.Adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codingwithimran.fycommerce.Activity.ViewCartActivity;
import com.codingwithimran.fycommerce.Modals.MyCartModal;
import com.codingwithimran.fycommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.myCartViewHolder> {
    Context context;
    ArrayList<MyCartModal> cartlist;
    FirebaseFirestore db;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    int totalAmount = 0;
    public MyCartAdapter(Context context, ArrayList<MyCartModal> cartlist) {
        this.context = context;
        this.cartlist = cartlist;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("please Waite ...");
    }
    @NonNull
    @Override
    public myCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_cart_item, parent, false);
        return new myCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myCartViewHolder holder, int position) {
        MyCartModal modal = cartlist.get(position);
        holder.current_date.setText(modal.getCurrentDate());
        holder.current_time.setText(modal.getCurrentTime());
        holder.product_name.setText(modal.getProductName());
        holder.product_price.setText(modal.getProductPrice());
        holder.total_price.setText(String.valueOf(modal.getTotalPrice()));
        holder.total_quantity.setText(String.valueOf(modal.getQuantity()));

//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                db.collection("AddToCart").document(auth.getCurrentUser().getUid())
//                        .collection("Users").document(modal.getProductId())
//                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if(task.isSuccessful()){
//                                    cartlist.remove(modal);
//                                    notifyDataSetChanged();
//                                    Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//                return false;
//            }
//        });

        holder.deleteCartview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                        .collection("Users").document(modal.getProductId())
                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    cartlist.remove(modal);
                                    notifyDataSetChanged();
                                    ((ViewCartActivity)context).refreshActivtiy();
                                    Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        totalAmount = totalAmount + cartlist.get(position).getTotalPrice();
        Log.d("amount", String.valueOf(totalAmount));
        Intent intent = new Intent("MyTotalAmount");
        intent.putExtra("cartAmount", totalAmount);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return cartlist.size();
    }

    public class myCartViewHolder extends RecyclerView.ViewHolder{
        TextView current_date, current_time, product_name, product_price, total_price, total_quantity;
        ImageView deleteCartview;
        public myCartViewHolder(@NonNull View itemView) {
            super(itemView);
            current_date = (itemView).findViewById(R.id.current_date);
            current_time = (itemView).findViewById(R.id.current_time);
            product_name = (itemView).findViewById(R.id.product_name);
            product_price = (itemView).findViewById(R.id.product_price);
            total_price = (itemView).findViewById(R.id.total_price);
            total_quantity = (itemView).findViewById(R.id.total_quantity);
            deleteCartview = (itemView).findViewById(R.id.deletecartitem);

            // set long click listener on ViewHolder
        }
    }
}
