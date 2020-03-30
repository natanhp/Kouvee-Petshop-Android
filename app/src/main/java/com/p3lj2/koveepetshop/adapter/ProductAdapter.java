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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private EventClickListener eventClickListener;

    @Getter
    private List<ProductResponseModel> productResponseModels = new ArrayList<>();

    public ProductAdapter(EventClickListener eventClickListener) {
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemBinder(productResponseModels.get(position));
    }

    @Override
    public int getItemCount() {
        return productResponseModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.img_product_image)
        ImageView imageView;

        @BindViews({R.id.tv_product_name, R.id.tv_product_measurement, R.id.tv_product_price, R.id.tv_product_quantity, R.id.tv_update})
        List<TextView> productAttribute;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void itemBinder(ProductResponseModel productResponseModel) {
            Glide.with(itemView)
                    .load(productResponseModel.getImageUrl())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageView);

            productAttribute.get(0).setText(productResponseModel.getProductModel().getProductName());
            productAttribute.get(1).setText(productResponseModel.getProductModel().getMeassurement());
            productAttribute.get(2).setText(String.valueOf(productResponseModel.getProductModel().getProductPrice()));
            productAttribute.get(3).setText(String.valueOf(productResponseModel.getProductModel().getProductQuantity()));
            productAttribute.get(4).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            eventClickListener.onEventClick(getAdapterPosition());
        }
    }

    public void setProductResponseModels(List<ProductResponseModel> productResponseModels) {
        this.productResponseModels = productResponseModels;
        notifyDataSetChanged();
    }

    public void delete(int position) {
        productResponseModels.remove(position);
        notifyItemRemoved(position);
    }
}
