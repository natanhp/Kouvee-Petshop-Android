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
import com.p3lj2.koveepetshop.adapter.ServiceDetailAdapter;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.model.ServiceDetailComplete;
import com.p3lj2.koveepetshop.util.EventClickListener;
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.viewmodel.ServiceDetailViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceDetailActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.search_view)
    SearchView searchView;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    private ServiceDetailViewModel serviceDetailViewModel;
    private ServiceDetailAdapter serviceDetailAdapter;
    private EmployeeModel employee;
    static final String EXTRA_SERVICE_DETAIL = "com.p3lj2.koveepetshop.view.EXTRA_SERVICE_DETAIL";
    private static final int UPDATE_REQUEST = 8;
    private List<ServiceDetailComplete> serviceDetailCompletesBak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.service_detail);

        serviceDetailViewModel = new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(this).getApplication()).create(ServiceDetailViewModel.class);

        createServiceDetail();

        serviceDetailViewModel.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        serviceDetailViewModel.getEmployeeIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        setUpRecyclerView();
        deleteOnSwipe();
        searchViewHandler();
    }

    private void createServiceDetail() {
        floatingActionButton.setOnClickListener(view -> startActivityForResult(new Intent(this, InsertServiceDetailActivity.class), UPDATE_REQUEST));
    }

    private void setUpRecyclerView() {
        serviceDetailAdapter = new ServiceDetailAdapter(itemUpdateListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(serviceDetailAdapter);

        getAllSizes();
    }

    private void getAllSizes() {
        serviceDetailViewModel.getAll().observe(this, serviceDetailCompletes -> {
            serviceDetailCompletesBak = new ArrayList<>(serviceDetailCompletes);
            serviceDetailAdapter.setServiceDetailCompletes(serviceDetailCompletes);
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
                Util.confirmationDialog(getString(R.string.service_detail_deletion), getString(R.string.service_detail_delete_confirmation), ServiceDetailActivity.this)
                        .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                            getEmployee();
                            serviceDetailViewModel.delete(employee.getToken(),
                                    serviceDetailAdapter.getServiceDetailCompletes().get(viewHolder.getAdapterPosition()).getServiceDetailModel().getId());

                            serviceDetailAdapter.delete(viewHolder.getAdapterPosition());
                            Toast.makeText(ServiceDetailActivity.this, R.string.service_detail_deleted, Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(getString(R.string.no), (dialogInterface, i) -> serviceDetailAdapter.notifyItemChanged(viewHolder.getAdapterPosition()))
                        .show();
            }
        })
                .attachToRecyclerView(recyclerView);
    }

    private EventClickListener itemUpdateListener = position -> {
        Intent intent = new Intent(ServiceDetailActivity.this, UpdateServiceDetailActivity.class);
        intent.putExtra(EXTRA_SERVICE_DETAIL, serviceDetailAdapter.getServiceDetailCompletes().get(position));
        startActivityForResult(intent, UPDATE_REQUEST);
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_REQUEST && resultCode == Activity.RESULT_OK) {
            serviceDetailViewModel.getAll();
        }
    }

    private void searchViewHandler() {
        searchView.setEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                List<ServiceDetailComplete> serviceDetailCompletes = new ArrayList<>();

                for (ServiceDetailComplete serviceDetailComplete : serviceDetailCompletesBak) {
                    String serviceDetailName = serviceDetailComplete.getServiceCompleteName().toLowerCase();
                    if (serviceDetailName.contains(query.toLowerCase())) {
                        serviceDetailCompletes.add(serviceDetailComplete);
                    }
                    serviceDetailAdapter.setServiceDetailCompletes(serviceDetailCompletes);
                }

                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(() -> {
            getEmployee();
            serviceDetailViewModel.getAll();
            return false;
        });
    }

    private void getEmployee() {
        if (employee == null) {
            serviceDetailViewModel.getEmployee().observe(ServiceDetailActivity.this, employeeDataModel -> {
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
