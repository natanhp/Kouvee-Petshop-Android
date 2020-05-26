package com.p3lj2.koveepetshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.ServiceDetailComplete;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import lombok.Getter;

public class ServiceListOnlyAdapter extends RecyclerView.Adapter<ServiceListOnlyAdapter.ViewHolder> {
    @Getter
    private List<ServiceDetailComplete> serviceDetailCompletes = new ArrayList<>();

    @NonNull
    @Override
    public ServiceListOnlyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.service_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceListOnlyAdapter.ViewHolder holder, int position) {
        holder.viewBinder(serviceDetailCompletes.get(position));
    }

    @Override
    public int getItemCount() {
        return serviceDetailCompletes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindViews({R.id.tv_service_name, R.id.tv_service_price})
        List<TextView> textViews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void viewBinder(ServiceDetailComplete serviceDetailComplete) {
            textViews.get(0).setText(serviceDetailComplete.getServiceCompleteName());
            String strPrice = "Rp " + serviceDetailComplete.getServiceDetailModel().getPrice();
            textViews.get(1).setText(strPrice);
        }
    }

    public void setServiceDetailCompletes(List<ServiceDetailComplete> serviceDetailCompletes) {
        this.serviceDetailCompletes = serviceDetailCompletes;
        notifyDataSetChanged();
    }
}
