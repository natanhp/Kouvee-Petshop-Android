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
import com.p3lj2.koveepetshop.adapter.SupplierAdapter;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.util.EventClickListener;
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.viewmodel.SupplierViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SupplierActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.search_view)
    SearchView searchView;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    private SupplierViewModel supplierViewModel;
    private SupplierAdapter supplierAdapter;
    private EmployeeModel employee;
    static final String EXTRA_SUPPLIER = "com.p3lj2.koveepetshop.view.EXTRA_SUPPLIER";
    private static final int UPDATE_REQUEST = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.supplier);

        supplierViewModel = new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(this).getApplication()).create(SupplierViewModel.class);

        createSupplier();

        supplierViewModel.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        supplierViewModel.getEmployeeIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        setUpRecyclerView();
        deleteOnSwipe();
        searchViewHandler();
    }

    private void createSupplier() {
        floatingActionButton.setOnClickListener(view -> startActivity(new Intent(this, InsertSupplierActivity.class)));
    }

    private void setUpRecyclerView() {
        supplierAdapter = new SupplierAdapter(itemUpdateListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(supplierAdapter);

        getAllSuppliers();
    }

    private void getAllSuppliers() {
        supplierViewModel.getEmployee().observe(this, employeeDataModel -> {
            employee = employeeDataModel;

            supplierViewModel.getAll(employee.getToken()).observe(SupplierActivity.this, supplierModels -> supplierAdapter.setSupplierModels(supplierModels));
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
                Util.confirmationDialog(getString(R.string.pet_size_deletion), getString(R.string.pet_size_delete_confirmation), SupplierActivity.this)
                        .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                            getEmployee();
                            supplierViewModel.delete(employee.getToken(),
                                    supplierAdapter.getSupplierModels().get(viewHolder.getAdapterPosition()).getIdSupplier(),
                                    employee.getId());

                            supplierAdapter.delete(viewHolder.getAdapterPosition());
                            Toast.makeText(SupplierActivity.this, R.string.supplier_deleted, Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(getString(R.string.no), (dialogInterface, i) -> supplierAdapter.notifyItemChanged(viewHolder.getAdapterPosition()))
                        .show();
            }
        })
                .attachToRecyclerView(recyclerView);
    }

    private EventClickListener itemUpdateListener = new EventClickListener() {
        @Override
        public void onEventClick(int position, @Nullable Integer viewId) {
            Intent intent = new Intent(SupplierActivity.this, UpdateSupplierActivity.class);
            intent.putExtra(EXTRA_SUPPLIER, supplierAdapter.getSupplierModels().get(position));
            SupplierActivity.this.startActivityForResult(intent, UPDATE_REQUEST);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_REQUEST && resultCode == Activity.RESULT_OK) {
            supplierViewModel.getEmployee().observe(this, employeeDataModel -> supplierViewModel.getAll(employeeDataModel.getToken()));
        }
    }

    private void searchViewHandler() {
        searchView.setEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                supplierViewModel.search(employee.getToken(), query).observe(SupplierActivity.this, supplierModels -> supplierAdapter.setSupplierModels(supplierModels));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(() -> {
            getEmployee();
            supplierViewModel.getAll(employee.getToken());
            return false;
        });
    }

    private void getEmployee() {
        if (employee == null) {
            supplierViewModel.getEmployee().observe(this, employeeDataModel -> {
                if (employeeDataModel != null) {
                    employee = employeeDataModel;
                }
            });
        }
    }

    private void handleProgressBar(boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
