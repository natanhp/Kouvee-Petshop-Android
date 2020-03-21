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
import com.p3lj2.koveepetshop.adapter.CustomerAdapter;
import com.p3lj2.koveepetshop.model.EmployeeDataModel;
import com.p3lj2.koveepetshop.util.EventClickListener;
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.viewmodel.CustomerViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomersActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.search_view)
    SearchView searchView;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    private CustomerViewModel customerViewModel;
    private CustomerAdapter customerAdapter;
    private EmployeeDataModel employee = new EmployeeDataModel();
    static final String EXTRA_CUSTOMER = "com.p3lj2.koveepetshop.view.EXTRA_CUSTOMER";
    private static final int UPDATE_REQUEST = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.customer);

        customerViewModel = new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(this).getApplication()).create(CustomerViewModel.class);

        createCustomer();

        setUpRecyclerView();
        deleteOnSwipe();
//        searchViewHandler();
    }

    private void initProgressBar() {
        customerViewModel.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        customerViewModel.getEmployeeIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });
    }

    private void handleProgressBar(boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void createCustomer() {
        floatingActionButton.setOnClickListener(view -> startActivityForResult(new Intent(this, InsertCustomerActivity.class), UPDATE_REQUEST));
    }

    private void setUpRecyclerView() {
        customerAdapter = new CustomerAdapter(itemUpdateListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(customerAdapter);

        getAllSizes();
    }

    private void getAllSizes() {
        customerViewModel.getEmployee().observe(this, employeeDataModel -> {
            employee = employeeDataModel;

            customerViewModel.getAll(employee.getToken()).observe(CustomersActivity.this, customerModels -> customerAdapter.setCustomerModels(customerModels));
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
                Util.confirmationDialog(getString(R.string.customer_deletion), getString(R.string.customer_delete_confirmation), CustomersActivity.this)
                        .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                            getEmployee();
                            customerViewModel.delete(employee.getToken(),
                                    customerAdapter.getCustomerModels().get(viewHolder.getAdapterPosition()).getId(),
                                    employee.getId());

                            customerAdapter.delete(viewHolder.getAdapterPosition());
                            Toast.makeText(CustomersActivity.this, R.string.customer_deleted, Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(getString(R.string.no), (dialogInterface, i) -> customerAdapter.notifyItemChanged(viewHolder.getAdapterPosition()))
                        .show();
            }
        })
                .attachToRecyclerView(recyclerView);
    }

    private EventClickListener itemUpdateListener = position -> {
        Intent intent = new Intent(this, UpdateCustomerActivity.class);
        intent.putExtra(EXTRA_CUSTOMER, customerAdapter.getCustomerModels().get(position));
        startActivityForResult(intent, UPDATE_REQUEST);
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_REQUEST && resultCode == Activity.RESULT_OK) {
            customerViewModel.getEmployee().observe(this, employeeDataModel -> customerViewModel.getAll(employeeDataModel.getToken()));
        }
    }

    private void searchViewHandler() {
        searchView.setEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                customerViewModel.search(employee.getToken(), query).observe(this, customerModels -> customerAdapter.setCustomerModels(customerModels));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(() -> {
            getEmployee();
            customerViewModel.getAll(employee.getToken());
            return false;
        });
    }

    private void getEmployee() {
        if (employee == null) {
            customerViewModel.getEmployee().observe(this, employeeDataModel -> {
                if (employeeDataModel != null) {
                    employee = employeeDataModel;
                }
            });
        }
    }
}
