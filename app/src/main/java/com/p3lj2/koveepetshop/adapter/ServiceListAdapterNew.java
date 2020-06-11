package com.p3lj2.koveepetshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.ServiceDetailModel;
import com.p3lj2.koveepetshop.util.EventClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import lombok.Getter;

public class ServiceListAdapterNew extends RecyclerView.Adapter<ServiceListAdapterNew.ViewHolder> {
    private EventClickListener eventClickListener;

    public ServiceListAdapterNew(EventClickListener eventClickListener) {
        this.eventClickListener = eventClickListener;
    }

    @Getter
    private List<ServiceDetailModel> serviceDetailModels = new ArrayList<>();

    @NonNull
    @Override
    public ServiceListAdapterNew.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.service_list_item_new, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceListAdapterNew.ViewHolder holder, int position) {
        holder.viewBinder(serviceDetailModels.get(position));
    }

    @Override
    public int getItemCount() {
        return serviceDetailModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindViews({R.id.tv_service_name, R.id.tv_service_price})
        List<TextView> textViews;

        @BindView(R.id.btn_add)
        ImageView btnAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void viewBinder(ServiceDetailModel serviceDetailModel) {
            textViews.get(0).setText(serviceDetailModel.getCompleteName());
            String strPrice = "Rp " + serviceDetailModel.getPrice();
            textViews.get(1).setText(strPrice);
            btnAdd.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            eventClickListener.onEventClick(getAdapterPosition(), null);
        }
    }

    public void setServiceDetailCompletes(List<ServiceDetailModel> serviceDetailModels) {
        this.serviceDetailModels = serviceDetailModels;
        notifyDataSetChanged();
    }
}
