package com.codingwithimran.fycommerce.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codingwithimran.fycommerce.Modals.TrackingOrderModal;
import com.codingwithimran.fycommerce.R;

import java.util.ArrayList;

public class TrackingOrderAdapter extends RecyclerView.Adapter<TrackingOrderAdapter.viewHolder>{
    Context context;
    ArrayList<TrackingOrderModal> list;

    public TrackingOrderAdapter(Context context, ArrayList<TrackingOrderModal> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.layout_tracking, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        TrackingOrderModal modal = list.get(position);
        holder.orderDate.setText(modal.getCurrentDate());
        holder.orderTime.setText(modal.getCurrentTime());
        holder.orderStatus.setText(modal.getTrackingStatus());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView orderDate, orderTime, orderStatus;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            orderDate = (itemView).findViewById(R.id.order_date);
            orderTime = (itemView).findViewById(R.id.order_time);
            orderStatus = (itemView).findViewById(R.id.order_status);
        }
    }
}
