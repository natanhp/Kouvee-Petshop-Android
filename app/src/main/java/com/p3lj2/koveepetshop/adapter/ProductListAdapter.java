package com.p3lj2.koveepetshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.ProductResponseModel;
import com.p3lj2.koveepetshop.util.EventClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import lombok.Getter;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private EventClickListener eventClickListener;

    @Getter
    private List<ProductResponseModel> responseModels = new ArrayList<>();

    public ProductListAdapter(EventClickListener eventClickListener) {
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.viewBinder(responseModels.get(position));
    }

    @Override
    public int getItemCount() {
        return responseModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.img_product_image)
        ImageView imgProduct;

        @BindViews({R.id.tv_product_name, R.id.tv_product_quantity, R.id.tv_product_price})
        List<TextView> textViews;

        @BindView(R.id.btn_add)
        ImageView btnAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void viewBinder(ProductResponseModel productResponseModel) {
            Glide.with(itemView)
                    .load(productResponseModel.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imgProduct);

            String productQuantity = productResponseModel.getProductModel().getProductQuantity() + " " + productResponseModel.getProductModel().getMeassurement();
            String productPrice = "Rp " + productResponseModel.getProductModel().getProductPrice();
            textViews.get(0).setText(productResponseModel.getProductModel().getProductName());
            textViews.get(1).setText(productQuantity);
            textViews.get(2).setText(productPrice);
            btnAdd.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            eventClickListener.onEventClick(getAdapterPosition(), null);
        }
    }

    public void setProductResponseModels(List<ProductResponseModel> productResponseModels) {
        responseModels = productResponseModels;
        notifyDataSetChanged();
    }
}
