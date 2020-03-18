package com.p3lj2.koveepetshop.view;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.p3lj2.koveepetshop.adapter.ServiceAdapter;
import com.p3lj2.koveepetshop.model.EmployeeDataModel;
import com.p3lj2.koveepetshop.util.EventClickListener;
import com.p3lj2.koveepetshop.viewmodel.ServiceViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.search_view)
    SearchView searchView;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    private ServiceViewModel serviceViewModel;
    private ServiceAdapter serviceAdapter;
    private EmployeeDataModel employee;
    static final String EXTRA_SERVICE = "com.p3lj2.koveepetshop.view.EXTRA_SERVICE";
    private static final int UPDATE_REQUEST = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.service);

        serviceViewModel = new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(this).getApplication()).create(ServiceViewModel.class);

        createService();

        serviceViewModel.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        serviceViewModel.getEmployeeIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        setUpRecyclerView();
        deleteOnSwipe();
//        searchViewHandler();
    }

    private void createService() {
        floatingActionButton.setOnClickListener(view -> startActivity(new Intent(this, InsertServiceActivity.class)));
    }

    private void setUpRecyclerView() {
        serviceAdapter = new ServiceAdapter(itemUpdateListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(serviceAdapter);

        getAllTypes();
    }

    private void getAllTypes() {
        serviceViewModel.getEmployee().observe(this, employeeDataModel -> {
            employee = employeeDataModel;

            serviceViewModel.getAll(employee.getToken()).observe(ServiceActivity.this, serviceModels -> serviceAdapter.setServiceModels(serviceModels));
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
                confirmationDialog(getString(R.string.service_deletion), getString(R.string.service_delete_confirmation))
                        .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                            getEmployee();
                            serviceViewModel.delete(employee.getToken(),
                                    serviceAdapter.getServiceModels().get(viewHolder.getAdapterPosition()).getId(),
                                    employee.getId());

                            serviceAdapter.delete(viewHolder.getAdapterPosition());
                            Toast.makeText(ServiceActivity.this, R.string.service_deleted, Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(getString(R.string.no), (dialogInterface, i) -> serviceAdapter.notifyItemChanged(viewHolder.getAdapterPosition()))
                        .show();
            }
        })
                .attachToRecyclerView(recyclerView);
    }

    private AlertDialog.Builder confirmationDialog(String title, String message) {
        return new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
    }

    private EventClickListener itemUpdateListener = position -> {
        Intent intent = new Intent(this, UpdateServiceActivity.class);
        intent.putExtra(EXTRA_SERVICE, serviceAdapter.getServiceModels().get(position));
        startActivityForResult(intent, UPDATE_REQUEST);
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_REQUEST && resultCode == Activity.RESULT_OK) {
            serviceViewModel.getEmployee().observe(this, employeeDataModel -> serviceViewModel.getAll(employeeDataModel.getToken()));
        }
    }

    private void searchViewHandler() {
        searchView.setEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                serviceViewModel.search(employee.getToken(), query).observe(ServiceActivity.this, serviceModels -> serviceAdapter.setServiceModels(serviceModels));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(() -> {
            getEmployee();
            serviceViewModel.getAll(employee.getToken());
            return false;
        });
    }

    private void getEmployee() {
        if (employee == null) {
            serviceViewModel.getEmployee().observe(this, employeeDataModel -> {
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
