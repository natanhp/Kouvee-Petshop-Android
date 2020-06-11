package com.p3lj2.koveepetshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

public class LogEmployeeAdapter extends RecyclerView.Adapter<LogEmployeeAdapter.ViewHolder> {
    private List<EmployeeModel> items = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.viewBinder(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindViews({R.id.tv_name, R.id.tv_created, R.id.tv_updated, R.id.tv_deleted})
        List<TextView> textViews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void viewBinder(EmployeeModel employeeModel) {
            textViews.get(0).setText(employeeModel.getName());
            String created = "Dibuat oleh " + employeeModel.getCreator().getName() + " pada " + Util.dateFormater("yyyy-MM-dd hh:mm:ss", "dd MMMM yyyy", employeeModel.getCreatedAt());
            String updated = "Diubah oleh " + employeeModel.getUpdater().getName() + " pada " + Util.dateFormater("yyyy-MM-dd hh:mm:ss", "dd MMMM yyyy", employeeModel.getUpdatedAt());
            String deleted = "Dihapus oleh " + employeeModel.getDeletor().getName() + " pada " + Util.dateFormater("yyyy-MM-dd hh:mm:ss", "dd MMMM yyyy", employeeModel.getDeletedAt());
            textViews.get(1).setText(created);
            textViews.get(2).setText(updated);
            textViews.get(3).setText(deleted);
        }
    }

    public void setItems(List<EmployeeModel> employeeModels) {
        this.items = employeeModels;
        notifyDataSetChanged();
    }
}
