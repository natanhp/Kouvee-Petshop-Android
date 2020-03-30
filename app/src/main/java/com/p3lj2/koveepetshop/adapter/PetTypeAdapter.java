package com.p3lj2.koveepetshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.PetTypeModel;
import com.p3lj2.koveepetshop.util.EventClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import lombok.Getter;

public class PetTypeAdapter extends RecyclerView.Adapter<PetTypeAdapter.ViewHolder> {
    private EventClickListener eventClickListener;

    @Getter
    private List<PetTypeModel> petTypeModels = new ArrayList<>();

    public PetTypeAdapter(EventClickListener eventClickListener) {
        this.eventClickListener = eventClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_type_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.viewBinder(petTypeModels.get(position));
    }

    @Override
    public int getItemCount() {
        return petTypeModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindViews({R.id.tv_pet_type, R.id.tv_update})
        List<TextView> petTypeViews;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void viewBinder(PetTypeModel petTypeModel) {
            petTypeViews.get(0).setText(petTypeModel.getType());
            petTypeViews.get(1).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            eventClickListener.onEventClick(getAdapterPosition());
        }
    }

    public void setPetTypeModels(List<PetTypeModel> petTypeModels) {
        this.petTypeModels = petTypeModels;
        notifyDataSetChanged();
    }

    public void delete(int position) {
        petTypeModels.remove(position);
        notifyItemRemoved(position);
    }
}
