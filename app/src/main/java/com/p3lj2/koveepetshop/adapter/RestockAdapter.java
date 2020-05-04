package com.p3lj2.koveepetshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import lombok.Setter;

public class RestockAdapter extends RecyclerView.Adapter<RestockAdapter.ViewHolder> {
    private EventClickListener eventClickListener;

    @Getter
    private List<ProductModel> productModels = new ArrayList<>();

    @Setter
    private boolean isUpdateable = false;

    public RestockAdapter(EventClickListener eventClickListener) {
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.restock_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.viewBinder(productModels.get(position));
    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindViews({R.id.tv_product_name, R.id.tv_product_quantity, R.id.tv_label_product_quantity})
        List<TextView> textViews;

        @BindView(R.id.btn_restock)
        Button btnRestock;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void viewBinder(ProductModel productModel) {
            if (isUpdateable) {
                textViews.get(2).setText(R.string.booked_quantity);
                btnRestock.setText(R.string.update);
            }
            String productQuantity = productModel.getProductQuantity() + " " + productModel.getMeassurement();
            textViews.get(0).setText(productModel.getProductName());
            textViews.get(1).setText(productQuantity);

            btnRestock.setOnClickListener(this);
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

    public void delete(int position) {
        productModels.remove(position);
        notifyItemRemoved(position);
    }
}
