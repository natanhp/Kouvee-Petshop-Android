package com.p3lj2.koveepetshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.ServiceModel;
import com.p3lj2.koveepetshop.util.EventClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import lombok.Getter;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    private EventClickListener eventClickListener;

    @Getter
    private List<ServiceModel> serviceModels = new ArrayList<>();

    public ServiceAdapter(EventClickListener eventClickListener) {
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.viewBinder(serviceModels.get(position));
    }

    @Override
    public int getItemCount() {
        return serviceModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindViews({R.id.tv_service, R.id.tv_update})
        List<TextView> serviceViews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void viewBinder(ServiceModel serviceModel) {
            if (serviceModel != null) {
                serviceViews.get(0).setText(serviceModel.getServiceName());
                serviceViews.get(1).setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            eventClickListener.onEventClick(getAdapterPosition(), null);
        }
    }

    public void setServiceModels(List<ServiceModel> serviceModels) {
        this.serviceModels = serviceModels;
        notifyDataSetChanged();
    }

    public void delete(int position) {
        serviceModels.remove(position);
        notifyItemRemoved(position);
    }
}
