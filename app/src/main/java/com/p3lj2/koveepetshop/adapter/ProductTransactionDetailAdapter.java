package com.p3lj2.koveepetshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.ProductTransactionDetailModel;
import com.p3lj2.koveepetshop.util.EventClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import lombok.Getter;

public class ProductTransactionDetailAdapter extends RecyclerView.Adapter<ProductTransactionDetailAdapter.ViewHolder> {
    private EventClickListener eventClickListener;

    @Getter
    private List<ProductTransactionDetailModel> productTransactionDetailModels = new ArrayList<>();

    public ProductTransactionDetailAdapter(EventClickListener eventClickListener) {
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public ProductTransactionDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductTransactionDetailAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductTransactionDetailAdapter.ViewHolder holder, int position) {
        holder.viewBinder(productTransactionDetailModels.get(position));
    }

    @Override
    public int getItemCount() {
        return productTransactionDetailModels.size();
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

        public void viewBinder(ProductTransactionDetailModel productTransactionDetailModels) {
            String productQuantity = productTransactionDetailModels.getItemQty() + " " + productTransactionDetailModels.getProductModel().getMeassurement();
            String productPrice = "Rp " + productTransactionDetailModels.getProductModel().getProductPrice() * productTransactionDetailModels.getItemQty();
            textViews.get(0).setText(productTransactionDetailModels.getProductModel().getProductName());
            textViews.get(1).setText(productQuantity);
            textViews.get(2).setText(productPrice);
            btnEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            eventClickListener.onEventClick(getAdapterPosition(), null);
        }
    }

    public void setProductTransactionDetailModels(List<ProductTransactionDetailModel> productTransactionDetailModels) {
        this.productTransactionDetailModels = productTransactionDetailModels;
        notifyDataSetChanged();
    }

    public void removeProductTransactionModel(int position) {
        this.productTransactionDetailModels.remove(position);
        notifyItemRemoved(position);
    }
}
