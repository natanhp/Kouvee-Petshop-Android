package com.p3lj2.koveepetshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.PetSizeModel;
import com.p3lj2.koveepetshop.util.EventClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import lombok.Getter;

public class PetSizeAdapter extends RecyclerView.Adapter<PetSizeAdapter.ViewHolder> {
    private EventClickListener eventClickListener;

    @Getter
    private List<PetSizeModel> petSizeModels = new ArrayList<>();

    public PetSizeAdapter(EventClickListener eventClickListener) {
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_size_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.viewBinder(petSizeModels.get(position));
    }

    @Override
    public int getItemCount() {
        return petSizeModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindViews({R.id.tv_pet_size, R.id.tv_update})
        List<TextView> petSizeViews;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void viewBinder(PetSizeModel petSizeModel) {
            petSizeViews.get(0).setText(petSizeModel.getSize());
            petSizeViews.get(1).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            eventClickListener.onEventClick(getAdapterPosition());
        }
    }

    public void setPetSizeModels(List<PetSizeModel> petSizeModels) {
        this.petSizeModels = petSizeModels;
        notifyDataSetChanged();
    }

    public void delete(int position) {
        petSizeModels.remove(position);
        notifyItemRemoved(position);
    }
}
