package com.codingwithimran.fycommerce.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.codingwithimran.fycommerce.Modals.AddressModals;
import com.codingwithimran.fycommerce.R;

import java.util.ArrayList;

public class AddressAdapters extends RecyclerView.Adapter<AddressAdapters.AddressViewHolder>{
    Context context;
    ArrayList<AddressModals> addressList;
    SelectedAddress selectedAddress;
    private RadioButton selectedRadioButton;

    public AddressAdapters() {
    }

    public AddressAdapters(Context context, ArrayList<AddressModals> addressList, SelectedAddress selectedAddress) {
        this.context = context;
        this.addressList = addressList;
        this.selectedAddress = selectedAddress;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.address_item, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
            AddressModals modals = addressList.get(position);
            holder.address.setText(modals.getFullAddress());
            holder.radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(AddressModals addressModals : addressList){
                        addressModals.setSelected(false);
                    }
                    modals.setSelected(true);
                    if(selectedRadioButton != null){
                        selectedRadioButton.setChecked(false);
                    }
                    selectedRadioButton = (RadioButton) view;
                    selectedRadioButton.setChecked(true);
                    selectedAddress.setAddress(modals.getFullAddress());
                }
            });

    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder{
        TextView address;
        RadioButton radioButton;
        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            address = (itemView).findViewById(R.id.address_add);
            radioButton = (itemView).findViewById(R.id.select_address);

        }
    }
    public interface SelectedAddress{
        void setAddress(String address);
    }
}
