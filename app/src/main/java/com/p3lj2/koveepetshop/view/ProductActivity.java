package com.p3lj2.koveepetshop.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.adapter.ProductAdapter;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.util.EventClickListener;
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.viewmodel.ProductViewModel;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.search_view)
    SearchView searchView;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    private ProductViewModel productViewModel;
    private ProductAdapter productAdapter;
    private EmployeeModel employee = new EmployeeModel();
    static final String EXTRA_PRODUCT = "com.p3lj2.koveepetshop.view.EXTRA_PRODUCT";
    private static final int UPDATE_REQUEST = 3;
    private BidiMap<Integer, Integer> supplierMap = new DualHashBidiMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.product);

        productViewModel = new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(this).getApplication()).create(ProductViewModel.class);

        loadingStateHandler();
        getEmployeeData();
        createProduct();
        setUpRecyclerView();
        deleteOnSwipe();
        searchViewHandler();
    }

    private void createProduct() {

        floatingActionButton.setOnClickListener(view -> startActivityForResult(new Intent(this, InsertProductActivity.class), UPDATE_REQUEST));
    }

    private void setUpRecyclerView() {
        productAdapter = new ProductAdapter(itemUpdateListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(productAdapter);

        getAllProducts();
    }

    private void getAllProducts() {
        productViewModel.getAll().observe(this, productResponseModels -> {
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
                Util.confirmationDialog(getString(R.string.product_deletion), getString(R.string.product_deletion_confirmation), ProductActivity.this)
                        .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                            productViewModel.delete(employee.getToken(),
                                    productAdapter.getProductResponseModels().get(viewHolder.getAdapterPosition()).getProductModel().getId(),
                                    employee.getId());

                            productAdapter.delete(viewHolder.getAdapterPosition());
                            Toast.makeText(ProductActivity.this, R.string.product_deletion_success, Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(getString(R.string.no), (dialogInterface, i) -> productAdapter.notifyItemChanged(viewHolder.getAdapterPosition()))
                        .show();
            }
        })
                .attachToRecyclerView(recyclerView);
    }

    private EventClickListener itemUpdateListener = new EventClickListener() {
        @Override
        public void onEventClick(int position, @Nullable Integer data) {
            if (data != null) {
                switch (data) {
                    case R.id.tv_update:
                        Intent intent = new Intent(ProductActivity.this, UpdateProductActivity.class);
                        intent.putExtra(EXTRA_PRODUCT, productAdapter.getProductResponseModels().get(position));
                        startActivityForResult(intent, UPDATE_REQUEST);
                        break;
                    default:
                        Toast.makeText(ProductActivity.this, R.string.something_wrong_msg, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ProductActivity.this, R.string.something_wrong_msg, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_REQUEST && resultCode == Activity.RESULT_OK) {
            productViewModel.getAll();
        }
    }

    private void searchViewHandler() {
        searchView.setEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                productViewModel.getByName(query).observe(ProductActivity.this, productResponseModels -> productAdapter.setProductResponseModels(productResponseModels));

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(() -> {
            productViewModel.getAll();
            return false;
        });
    }

    private void getEmployeeData() {
        productViewModel.getEmployee().observe(ProductActivity.this, employeeDataModel -> {
            if (employeeDataModel != null) {
                employee = employeeDataModel;
            }
        });
    }

    private void loadingStateHandler() {
        productViewModel.getIsLoading().observe(this, this::progressBarHandler);
        productViewModel.getEmployeeLoading().observe(this, this::progressBarHandler);
    }

    private void progressBarHandler(boolean status) {
        if (status) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
