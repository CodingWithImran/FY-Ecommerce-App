package com.codingwithimran.fycommerce.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codingwithimran.fycommerce.Activity.DetailActivity;
import com.codingwithimran.fycommerce.Modals.ProductModal;
import com.codingwithimran.fycommerce.R;

import java.io.Serializable;
import java.util.ArrayList;

public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.productViewHolder>{




    Context context;
    ArrayList<ProductModal> list ;

    public NewProductAdapter(Context context, ArrayList<ProductModal> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public productViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new productViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull productViewHolder holder, int position) {
        ProductModal productModal = list.get(position);
        Glide.with(context).load(productModal.getProduct_img()).into(holder.product_img);
        holder.product_name.setText(productModal.getName());
        holder.product_price.setText(String.valueOf(productModal.getPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("newProductDetails",  list.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class productViewHolder extends RecyclerView.ViewHolder{
        TextView product_name, product_price;
        ImageView product_img;
        public productViewHolder(@NonNull View itemView) {
            super(itemView);
            product_name = (itemView).findViewById(R.id.product_name);
            product_price = (itemView).findViewById(R.id.prodcut_price);
            product_img = (itemView).findViewById(R.id.product_img);
        }
    }
}


// For Dynamic Heights


