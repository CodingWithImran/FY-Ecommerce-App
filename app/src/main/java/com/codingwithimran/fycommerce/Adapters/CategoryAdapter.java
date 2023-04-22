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
import com.codingwithimran.fycommerce.Activity.ShowAllActivity;
import com.codingwithimran.fycommerce.Modals.Category;
import com.codingwithimran.fycommerce.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    Context context;
    ArrayList<Category> list;

    public CategoryAdapter(Context context, ArrayList<Category> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_list, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category modal = list.get(position);
        holder.cat_name.setText(modal.getCategory_name());
        Glide.with(context).load(modal.getCategory_icon()).into(holder.cat_img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowAllActivity.class);
                intent.putExtra("cat_type", modal.getCategory_type());
                (context).startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class CategoryViewHolder extends RecyclerView.ViewHolder{
        ImageView cat_img;
        TextView cat_name;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cat_img = (itemView).findViewById(R.id.product_img);
            cat_name = (itemView).findViewById(R.id.product_name);
        }
    }
}
