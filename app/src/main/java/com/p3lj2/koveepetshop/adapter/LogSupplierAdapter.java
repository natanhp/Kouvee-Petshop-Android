package com.p3lj2.koveepetshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.SupplierModel;
import com.p3lj2.koveepetshop.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

public class LogSupplierAdapter extends RecyclerView.Adapter<LogSupplierAdapter.ViewHolder> {
    private List<SupplierModel> items = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.viewBinder(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindViews({R.id.tv_name, R.id.tv_created, R.id.tv_updated, R.id.tv_deleted})
        List<TextView> textViews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void viewBinder(SupplierModel supplierModel) {
            textViews.get(0).setText(supplierModel.getName());
            String created = "Dibuat oleh " + supplierModel.getCreator().getName() + " pada " + Util.dateFormater("yyyy-MM-dd hh:mm:ss", "dd MMMM yyyy", supplierModel.getCreatedAt());
            String updated = "Diubah oleh " + supplierModel.getUpdater().getName() + " pada " + Util.dateFormater("yyyy-MM-dd hh:mm:ss", "dd MMMM yyyy", supplierModel.getUpdatedAt());
            String deleted = "Dihapus oleh " + supplierModel.getDeletor().getName() + " pada " + Util.dateFormater("yyyy-MM-dd hh:mm:ss", "dd MMMM yyyy", supplierModel.getDeletedAt());
            textViews.get(1).setText(created);
            textViews.get(2).setText(updated);
            textViews.get(3).setText(deleted);
        }
    }

    public void setItems(List<SupplierModel> supplierModels) {
        this.items = supplierModels;
        notifyDataSetChanged();
    }
}
