package com.p3lj2.koveepetshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.ProductModel;
import com.p3lj2.koveepetshop.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

public class LogProdukAdapter extends RecyclerView.Adapter<LogProdukAdapter.ViewHolder> {
    private List<ProductModel> items = new ArrayList<>();

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

        public void viewBinder(ProductModel productModel) {
            textViews.get(0).setText(productModel.getProductName());
            String created = "Dibuat oleh " + productModel.getCreator().getName() + " pada " + Util.dateFormater("yyyy-MM-dd hh:mm:ss", "dd MMMM yyyy", productModel.getCreatedAt());
            String updated = "Diubah oleh " + productModel.getUpdater().getName() + " pada " + Util.dateFormater("yyyy-MM-dd hh:mm:ss", "dd MMMM yyyy", productModel.getUpdatedAt());
            String deleted = "Dihapus oleh " + productModel.getDeletor().getName() + " pada " + Util.dateFormater("yyyy-MM-dd hh:mm:ss", "dd MMMM yyyy", productModel.getDeletedAt());
            textViews.get(1).setText(created);
            textViews.get(2).setText(updated);
            textViews.get(3).setText(deleted);
        }
    }

    public void setItems(List<ProductModel> productModels) {
        this.items = productModels;
        notifyDataSetChanged();
    }
}
