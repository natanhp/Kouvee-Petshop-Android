package com.p3lj2.koveepetshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.ProductTransactionModel;
import com.p3lj2.koveepetshop.util.EventClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import lombok.Getter;

public class ProductTransactionListAdapter extends RecyclerView.Adapter<ProductTransactionListAdapter.ViewHolder> {
    @Getter
    private List<ProductTransactionModel> productTransactionModels = new ArrayList<>();

    private EventClickListener eventClickListener;

    public ProductTransactionListAdapter(EventClickListener eventClickListener) {
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_transaction_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.viewBinder(productTransactionModels.get(position));
    }

    @Override
    public int getItemCount() {
        return productTransactionModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindViews({R.id.tv_transaction_code, R.id.tv_customer_name})
        List<TextView> textViews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        public void viewBinder(ProductTransactionModel productTransactionModel) {
            textViews.get(0).setText(productTransactionModel.getId());
            textViews.get(1).setText(productTransactionModel.getCustomerName());
        }

        @Override
        public void onClick(View view) {
            eventClickListener.onEventClick(getAdapterPosition(), null);
        }
    }

    public void setProductTransactionModels(List<ProductTransactionModel> productTransactionModels) {
        this.productTransactionModels = productTransactionModels;
        notifyDataSetChanged();
    }

    public void deleteProductTransactionModel(int position) {
        productTransactionModels.remove(position);
        notifyItemRemoved(position);
    }
}
