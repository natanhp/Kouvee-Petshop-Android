package com.p3lj2.koveepetshop.view.product.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.adapter.ProductListAdapter;
import com.p3lj2.koveepetshop.model.ProductModel;
import com.p3lj2.koveepetshop.model.ProductResponseModel;
import com.p3lj2.koveepetshop.util.EventClickListener;
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.viewmodel.ProductTransactionViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductListFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private ProductTransactionViewModel productTransactionViewModel;
    private ProductListAdapter productListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_product_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        productTransactionViewModel = new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()).create(ProductTransactionViewModel.class);
        loadingStateHandler();
        initRecyclerView();
    }

    private void initRecyclerView() {
        productListAdapter = new ProductListAdapter(eventClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(productListAdapter);
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
        productTransactionViewModel.getViewPositions().observe(getViewLifecycleOwner(), new Observer<HashMap<Integer, Integer>>() {
            @Override
            public void onChanged(HashMap<Integer, Integer> positions) {
                for (int position : positions.values()) {
                    if (recyclerView.findViewHolderForAdapterPosition(position) != null) {
                        ImageButton btnAdd =Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(position)).itemView.findViewById(R.id.btn_add);
                        btnAdd.setEnabled(false);
                        Glide.with(Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(position)).itemView)
                                .load(R.drawable.ic_add_shopping_cart_gray_24dp)
                                .into(btnAdd);
                    }
                }
            }
        });
    }

    private EventClickListener eventClickListener = (position, viewId) -> buyDialog(position);

    private void buyDialog(int position) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.product_quantity_input, requireView().findViewById(android.R.id.content), false);
        EditText inputQty = view.findViewById(R.id.edt_product_quantity);
        Util.confirmationDialog(getString(R.string.buy_product), "", getContext())
                .setView(view)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    if (inputQty.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getContext(), R.string.all_column_must_be_filled, Toast.LENGTH_SHORT).show();
                    } else {
                        ProductModel productModel = new ProductModel();
                        productModel.setId(productListAdapter.getResponseModels().get(position).getProductModel().getId());
                        productModel.setProductName(productListAdapter.getResponseModels().get(position).getProductModel().getProductName());
                        productModel.setProductQuantity(Integer.parseInt(inputQty.getText().toString().trim()));
                        productModel.setMeassurement(productListAdapter.getResponseModels().get(position).getProductModel().getMeassurement());
                        productModel.setProductPrice(productListAdapter.getResponseModels().get(position).getProductModel().getProductPrice());
                        productTransactionViewModel.setCart(productModel);
                        productTransactionViewModel.setViewPositions(productListAdapter.getResponseModels().get(position).getProductModel().getId(), position);
                    }
                })
                .setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.cancel())
                .show();
    }

    private void getProductData() {
        productTransactionViewModel.getAllProducts().observe(getViewLifecycleOwner(), new Observer<List<ProductResponseModel>>() {
            @Override
            public void onChanged(List<ProductResponseModel> productResponseModels) {
                productListAdapter.setProductResponseModels(productResponseModels);
            }
        });
    }

    private void loadingStateHandler() {
        productTransactionViewModel.getProductIsLoading().observe(getViewLifecycleOwner(), this::progressBarHandler);
    }


    private void progressBarHandler(boolean status) {
        if (status) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
