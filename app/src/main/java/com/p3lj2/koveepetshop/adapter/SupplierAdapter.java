package com.p3lj2.koveepetshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.SupplierModel;
import com.p3lj2.koveepetshop.util.EventClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import lombok.Getter;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.ViewHolder> {
    private EventClickListener eventClickListener;

    @Getter
    private List<SupplierModel> supplierModels = new ArrayList<>();

    public SupplierAdapter(EventClickListener eventClickListener) {
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.supplier_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.viewBinder(supplierModels.get(position));
    }

    @Override
    public int getItemCount() {
        return supplierModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindViews({R.id.tv_supplier_name, R.id.tv_phone_number, R.id.tv_address, R.id.tv_update})
        List<TextView> supplierViews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void viewBinder(SupplierModel supplierModel) {
            supplierViews.get(0).setText(supplierModel.getName());
            supplierViews.get(1).setText(supplierModel.getPhoneNumber());
            supplierViews.get(2).setText(supplierModel.getAddress());
            supplierViews.get(3).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            eventClickListener.onEventClick(getAdapterPosition(), null);
        }
    }

    public void setSupplierModels(List<SupplierModel> supplierModels) {
        this.supplierModels = supplierModels;
        notifyDataSetChanged();
    }

    public void delete(int position) {
        supplierModels.remove(position);
        notifyItemRemoved(position);
    }
}
