package com.p3lj2.koveepetshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.ProductModel;
import com.p3lj2.koveepetshop.util.EventClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import lombok.Getter;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private EventClickListener eventClickListener;

    @Getter
    private List<ProductModel> productModels = new ArrayList<>();

    public CartAdapter(EventClickListener eventClickListener) {
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.viewBinder(productModels.get(position));
    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindViews({R.id.tv_product_name, R.id.tv_product_quantity, R.id.tv_sub_total})
        List<TextView> textViews;

        @BindView(R.id.btn_edit)
        ImageButton btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void viewBinder(ProductModel productModel) {
            String productQuantity = productModel.getProductQuantity() + " " + productModel.getMeassurement();
            String productPrice = "Rp " + productModel.getProductPrice() * productModel.getProductQuantity();
            textViews.get(0).setText(productModel.getProductName());
            textViews.get(1).setText(productQuantity);
            textViews.get(2).setText(productPrice);
            btnEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            eventClickListener.onEventClick(getAdapterPosition(), null);
        }
    }

    public void setProductModels(List<ProductModel> productModels) {
        this.productModels = productModels;
        notifyDataSetChanged();
    }
}
