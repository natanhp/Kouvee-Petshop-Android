package com.p3lj2.koveepetshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.PetComplete;
import com.p3lj2.koveepetshop.util.EventClickListener;
import com.p3lj2.koveepetshop.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import lombok.Getter;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {
    private EventClickListener eventClickListener;

    public PetAdapter(EventClickListener eventClickListener) {
        this.eventClickListener = eventClickListener;
    }

    @Getter
    private List<PetComplete> petCompletes = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.viewBinder(petCompletes.get(position));
    }

    @Override
    public int getItemCount() {
        return petCompletes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindViews({R.id.tv_pet_name, R.id.tv_pet_type, R.id.tv_pet_size, R.id.tv_birth_date, R.id.tv_customer_name, R.id.tv_update})
        List<TextView> petViews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void viewBinder(PetComplete petComplete) {
            petViews.get(0).setText(petComplete.getPet().getName());
            petViews.get(1).setText(petComplete.getType());
            petViews.get(2).setText(petComplete.getSize());
            petViews.get(3).setText(Util.dateFormater(petComplete.getPet().getDateBirth()));
            petViews.get(4).setText(petComplete.getCustomer());
            petViews.get(5).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            eventClickListener.onEventClick(getAdapterPosition());
        }
    }

    public void setPetCompletes(List<PetComplete> petCompletes) {
        this.petCompletes = petCompletes;
        notifyDataSetChanged();
    }

    public void delete(int position) {
        petCompletes.remove(position);
        notifyItemRemoved(position);
    }
}
