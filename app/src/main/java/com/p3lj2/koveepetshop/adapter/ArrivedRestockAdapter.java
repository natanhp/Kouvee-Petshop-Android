package com.p3lj2.koveepetshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.ProductRestockDetail;
import com.p3lj2.koveepetshop.model.ProductRestockModel;
import com.p3lj2.koveepetshop.util.EventClickListener;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class ArrivedRestockAdapter extends BaseExpandableListAdapter {
    private Context context;

    @Getter
    private List<ProductRestockModel> productRestockModels = new ArrayList<>();
    private EventClickListener eventClickListener;

    public ArrivedRestockAdapter(Context context, EventClickListener eventClickListener) {
        this.context = context;
        this.eventClickListener = eventClickListener;
    }

    @Override
    public int getGroupCount() {
        return productRestockModels.size();
    }

    @Override
    public int getChildrenCount(int i) {
        List<ProductRestockDetail> productRestockDetails = productRestockModels.get(i).getProductRestockDetails();
        return productRestockDetails.size();
    }

    @Override
    public Object getGroup(int i) {
        return productRestockModels.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        List<ProductRestockDetail> productRestockDetails = productRestockModels.get(i).getProductRestockDetails();
        return productRestockDetails.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        ProductRestockModel productRestockModel = (ProductRestockModel) getGroup(i);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (layoutInflater != null) {
                view = layoutInflater.inflate(R.layout.group_restock_layout, viewGroup, false);
            }

            if (view != null) {
                TextView tvRestockCode = view.findViewById(R.id.tv_restock_code);
                TextView tvSupplierName = view.findViewById(R.id.tv_supplier_name);
                Button btnConfirm = view.findViewById(R.id.btn_confirm);

                tvRestockCode.setText(productRestockModel.getId());
                tvSupplierName.setText(productRestockModel.getSupplierName());

                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        eventClickListener.onEventClick(i, null);
                    }
                });
            }

        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ProductRestockDetail productRestockDetail = (ProductRestockDetail) getChild(i, i1);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (layoutInflater != null) {
                view = layoutInflater.inflate(R.layout.child_restock_layout, viewGroup, false);
            }
        }

        if (view != null) {
            TextView productName = view.findViewById(R.id.tv_product_name);
            TextView productQuantity = view.findViewById(R.id.tv_product_quantity);

            productName.setText(productRestockDetail.getProductName());
            String quantity = productRestockDetail.getItemQty() + " " + productRestockDetail.getMeasurement();
            productQuantity.setText(quantity);
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void setProductRestockModels(List<ProductRestockModel> productRestockModels) {
        this.productRestockModels = productRestockModels;
        notifyDataSetChanged();
    }
}
