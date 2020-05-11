package com.p3lj2.koveepetshop.view.restock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.adapter.RestockAdapter;
import com.p3lj2.koveepetshop.model.ProductModel;
import com.p3lj2.koveepetshop.model.ProductResponseModel;
import com.p3lj2.koveepetshop.util.EventClickListener;
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.viewmodel.ProductRestockViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductStockFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private ProductRestockViewModel productRestockViewModel;
    private RestockAdapter restockAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_product_stock, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        productRestockViewModel = new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()).create(ProductRestockViewModel.class);

        loadingStateHandler();
        initRecyclerView();
    }

    private void initRecyclerView() {
        restockAdapter = new RestockAdapter(eventClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(restockAdapter);
        getProductData();

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                disableClickViewHandler();
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void disableClickViewHandler() {
//        Don't change the anonymous function to lambda expression
        productRestockViewModel.getViewPositions().observe(getViewLifecycleOwner(), new Observer<HashMap<Integer, Integer>>() {
            @Override
            public void onChanged(HashMap<Integer, Integer> positions) {
                for (int position : positions.values()) {
                    if (recyclerView.findViewHolderForAdapterPosition(position) != null) {
                        Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(position)).itemView.findViewById(R.id.btn_restock).setEnabled(false);
                    }
                }
            }
        });
    }

    private void getProductData() {
        productRestockViewModel.getAllProduct().observe(getViewLifecycleOwner(), productResponseModels -> {
            List<ProductModel> productModels = new ArrayList<>();
            if (productResponseModels != null) {
                for (ProductResponseModel productResponseModel : productResponseModels) {
                    productModels.add(productResponseModel.getProductModel());
                }
                restockAdapter.setProductModels(productModels);
            }
        });
    }

    private EventClickListener eventClickListener = (position, viewId) -> restockDialog(position);

    private void restockDialog(int position) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.product_quantity_input, requireView().findViewById(android.R.id.content), false);
        EditText inputRestock = view.findViewById(R.id.edt_product_quantity);
        Util.confirmationDialog(getResources().getString(R.string.product_restock), "", getContext())
                .setView(view)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    if (inputRestock.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getContext(), R.string.all_column_must_be_filled, Toast.LENGTH_SHORT).show();
                    } else {
                        ProductModel productModel = new ProductModel();
                        productModel.setId(restockAdapter.getProductModels().get(position).getId());
                        productModel.setProductName(restockAdapter.getProductModels().get(position).getProductName());
                        productModel.setProductQuantity(Integer.parseInt(inputRestock.getText().toString().trim()));
                        productModel.setMeassurement(restockAdapter.getProductModels().get(position).getMeassurement());
                        productRestockViewModel.setBookedProductModels(productModel);
                        productRestockViewModel.setViewPositions(restockAdapter.getProductModels().get(position).getId(), position);
                    }
                })
                .setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.cancel())
                .show();
    }

    private void loadingStateHandler() {
        productRestockViewModel.getProductIsLoading().observe(getViewLifecycleOwner(), this::progressBarHandler);
    }


    private void progressBarHandler(boolean status) {
        if (status) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
