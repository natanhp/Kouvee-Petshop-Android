package com.p3lj2.koveepetshop.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.viewmodel.ProductViewModel;

import java.util.Objects;

import butterknife.ButterKnife;

public class ProductFragment extends Fragment {

    private ProductViewModel productViewModel;
    private OwnerActivity ownerActivity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        ownerActivity = (OwnerActivity) getActivity();
        if (ownerActivity != null) {
            Objects.requireNonNull(ownerActivity.getSupportActionBar()).setTitle("Product");
        }

        productViewModel = new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(getActivity()).getApplication()).create(ProductViewModel.class);

        createProduct();
    }

    private void createProduct() {

        if (ownerActivity != null) {
            ownerActivity.floatingActionButton.setOnClickListener(view -> startActivity(new Intent(getActivity(), InsertProductActivity.class)));
        }
    }
}