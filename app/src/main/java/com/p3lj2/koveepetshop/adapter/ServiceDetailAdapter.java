package com.p3lj2.koveepetshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.ServiceDetailComplete;
import com.p3lj2.koveepetshop.util.EventClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import lombok.Getter;

public class ServiceDetailAdapter extends RecyclerView.Adapter<ServiceDetailAdapter.ViewHolder> {
    private EventClickListener eventClickListener;

    @Getter
    private List<ServiceDetailComplete> serviceDetailCompletes = new ArrayList<>();

    public ServiceDetailAdapter(EventClickListener eventClickListener) {
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.service_detail_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.viewBinder(serviceDetailCompletes.get(position));
    }

    @Override
    public int getItemCount() {
        return serviceDetailCompletes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindViews({R.id.tv_service_name, R.id.tv_service_price, R.id.tv_update})
        List<TextView> serviceDetailViews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void viewBinder(ServiceDetailComplete serviceDetailComplete) {
            serviceDetailViews.get(0).setText(serviceDetailComplete.getServiceCompleteName());
            serviceDetailViews.get(1).setText(String.valueOf(serviceDetailComplete.getServiceDetailModel().getPrice()));
            serviceDetailViews.get(2).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            eventClickListener.onEventClick(getAdapterPosition());
        }
    }

    public void setServiceDetailCompletes(List<ServiceDetailComplete> serviceDetailCompletes) {
        this.serviceDetailCompletes = serviceDetailCompletes;
        notifyDataSetChanged();
    }

    public void delete(int position) {
        serviceDetailCompletes.remove(position);
        notifyItemRemoved(position);
    }
}
