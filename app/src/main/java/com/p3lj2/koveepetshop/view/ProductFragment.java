package com.p3lj2.koveepetshop.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.adapter.ProductAdapter;
import com.p3lj2.koveepetshop.model.EmployeeDataModel;
import com.p3lj2.koveepetshop.viewmodel.ProductViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private ProductViewModel productViewModel;
    private OwnerActivity ownerActivity;
    private ProductAdapter productAdapter;
    private EmployeeDataModel employee = new EmployeeDataModel();

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
            Objects.requireNonNull(ownerActivity.getSupportActionBar()).setTitle(getString(R.string.product));
        }

        productViewModel = new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(getActivity()).getApplication()).create(ProductViewModel.class);

        createProduct();

        productViewModel.getIsLoading().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean != null) {
                if (aBoolean) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        setUpRecyclerView();
        deleteOnSwipe();
    }

    private void createProduct() {

        if (ownerActivity != null) {
            ownerActivity.floatingActionButton.setOnClickListener(view -> startActivity(new Intent(getActivity(), InsertProductActivity.class)));
        }
    }

    private void setUpRecyclerView() {
        productAdapter = new ProductAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(ownerActivity.getApplicationContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(productAdapter);

        getAllProducts();
    }

    private void getAllProducts() {
        productViewModel.getAll().observe(getViewLifecycleOwner(), productResponseModels -> {
            if (!productResponseModels.isEmpty()) {
                productAdapter.setProductResponseModels(productResponseModels);
            }
        });
    }

    private void deleteOnSwipe() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                confirmationDialog(getString(R.string.product_deletion), getString(R.string.product_deletion_confirmation))
                        .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                            productViewModel.getEmployee().observe(getViewLifecycleOwner(), employeeDataModel -> {
                                if (employeeDataModel != null) {
                                    employee = employeeDataModel;
                                }
                            });

                            productViewModel.delete(employee.getToken(),
                                    productAdapter.getProductResponseModels().get(viewHolder.getAdapterPosition()).getProductModel().getId(),
                                    employee.getId());

                            productAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                            Toast.makeText(ownerActivity.getApplicationContext(), R.string.product_deletion_success, Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(getString(R.string.no), (dialogInterface, i) -> productAdapter.notifyItemChanged(viewHolder.getAdapterPosition()))
                        .show();
            }
        })
                .attachToRecyclerView(recyclerView);
    }

    private AlertDialog.Builder confirmationDialog(String title, String message) {
        return new AlertDialog.Builder(ownerActivity)
                .setTitle(title)
                .setMessage(message);
    }
}