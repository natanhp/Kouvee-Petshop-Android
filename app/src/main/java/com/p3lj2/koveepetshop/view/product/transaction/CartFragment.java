package com.p3lj2.koveepetshop.view.product.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.adapter.CartAdapter;
import com.p3lj2.koveepetshop.model.ProductModel;
import com.p3lj2.koveepetshop.util.EventClickListener;
import com.p3lj2.koveepetshop.viewmodel.ProductTransactionViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ProductTransactionViewModel productTransactionViewModel;
    private CartAdapter cartAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        productTransactionViewModel = new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()).create(ProductTransactionViewModel.class);
        initRecyclerView();
    }

    private void initRecyclerView() {
        cartAdapter = new CartAdapter(eventClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(cartAdapter);
        getCart();
    }

    private EventClickListener eventClickListener = new EventClickListener() {
        @Override
        public void onEventClick(int position, @Nullable Integer viewId) {
            Toast.makeText(getContext(), "Halo bro", Toast.LENGTH_SHORT).show();
        }
    };

    private void getCart() {
        productTransactionViewModel.getCart().observe(getViewLifecycleOwner(), new Observer<List<ProductModel>>() {
            @Override
            public void onChanged(List<ProductModel> productModels) {
                if (productModels != null) {
                    cartAdapter.setProductModels(productModels);
                }
            }
        });
    }
}
