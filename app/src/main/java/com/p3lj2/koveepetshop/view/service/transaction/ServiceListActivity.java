package com.p3lj2.koveepetshop.view.service.transaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.adapter.ServiceListAdapter;
import com.p3lj2.koveepetshop.model.ServiceDetailComplete;
import com.p3lj2.koveepetshop.viewmodel.ServiceDetailViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceListActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private ServiceListAdapter serviceListAdapter;
    private ServiceDetailViewModel serviceDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        ButterKnife.bind(this);

        serviceDetailViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ServiceDetailViewModel.class);
        loadingStateHandler();
        initRecyclerView();
    }

    private void initRecyclerView() {
        serviceListAdapter = new ServiceListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(serviceListAdapter);
        getServiceData();
    }

    private void getServiceData() {
        serviceDetailViewModel.getAll().observe(this, new Observer<List<ServiceDetailComplete>>() {
            @Override
            public void onChanged(List<ServiceDetailComplete> serviceDetailCompletes) {
                serviceListAdapter.setServiceDetailCompletes(serviceDetailCompletes);
            }
        });
    }

    private void loadingStateHandler() {
        serviceDetailViewModel.getIsLoading().observe(this, this::progressBarHandler);
    }


    private void progressBarHandler(boolean status) {
        if (status) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
