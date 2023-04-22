package com.codingwithimran.fycommerce.Adapters;

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
import com.codingwithimran.fycommerce.Activity.ShowAllActivity;
import com.codingwithimran.fycommerce.Modals.ShowAllProductModal;
import com.codingwithimran.fycommerce.R;

import java.util.ArrayList;

public class ShowAllProductAdapter extends RecyclerView.Adapter<ShowAllProductAdapter.showAllviewHolder> {

    Context context;
    ArrayList<ShowAllProductModal> list;

    public ShowAllProductAdapter(Context context, ArrayList<ShowAllProductModal> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public showAllviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_all_items, parent, false);
        return new showAllviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull showAllviewHolder holder, int position) {
            ShowAllProductModal modal = list.get(position);
        Glide.with(context).load(modal.getProduct_img()).into(holder.product_img);
        holder.product_name.setText(modal.getName());
        holder.product_price.setText(String.valueOf(modal.getPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("showAllDetail",  list.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class showAllviewHolder  extends RecyclerView.ViewHolder {
        ImageView   product_img;
        TextView product_name, product_price;
       public showAllviewHolder(@NonNull View itemView) {
           super(itemView);
           product_img = (itemView).findViewById(R.id.sa_product_img);
           product_price = (itemView).findViewById(R.id.sa_product_price);
           product_name = (itemView).findViewById(R.id.sa_product_name);
       }
   }
}
