package com.p3lj2.koveepetshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.CustomerModel;
import com.p3lj2.koveepetshop.util.EventClickListener;
import com.p3lj2.koveepetshop.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import lombok.Getter;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    private EventClickListener eventClickListener;

    @Getter
    private List<CustomerModel> customerModels = new ArrayList<>();

    public CustomerAdapter(EventClickListener eventClickListener) {
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.viewBinder(customerModels.get(position));
    }

    @Override
    public int getItemCount() {
        return customerModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindViews({R.id.tv_customer_name, R.id.tv_address, R.id.tv_birth_date, R.id.tv_phone_number, R.id.tv_update})
        List<TextView> customerViews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void viewBinder(CustomerModel customerModel) {
            customerViews.get(0).setText(customerModel.getName());
            customerViews.get(1).setText(customerModel.getAddress());
            customerViews.get(2).setText(Util.dateFormater(customerModel.getDateBirth()));
            customerViews.get(3).setText(customerModel.getPhoneNumber());
            customerViews.get(4).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            eventClickListener.onEventClick(getAdapterPosition(), null);
        }
    }

    public void setCustomerModels(List<CustomerModel> customerModels) {
        this.customerModels = customerModels;
        notifyDataSetChanged();
    }

    public void delete(int position) {
        this.customerModels.remove(position);
        notifyItemRemoved(position);
    }
}
