package com.p3lj2.koveepetshop.view.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.adapter.LogProdukAdapter;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.model.ProductModel;
import com.p3lj2.koveepetshop.viewmodel.LogViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductLogFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private LogViewModel logViewModel;
    private LogProdukAdapter logAdapter;
    private EmployeeModel employee;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        logViewModel = new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()).create(LogViewModel.class);
        loadingStateHandler();
        getEmployeeData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        logAdapter = new LogProdukAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(logAdapter);
    }

    private void getData() {
        logViewModel.getProduct(employee.getToken()).observe(getViewLifecycleOwner(), new Observer<List<ProductModel>>() {
            @Override
            public void onChanged(List<ProductModel> productModels) {
                if (productModels != null) {
                    logAdapter.setItems(productModels);
                }
            }
        });
    }

    private void getEmployeeData() {
        logViewModel.employeeModel().observe(getViewLifecycleOwner(), new Observer<EmployeeModel>() {
            @Override
            public void onChanged(EmployeeModel employeeModel) {
                if (employeeModel != null) {
                    employee = employeeModel;
                    getData();
                }
            }
        });
    }

    private void loadingStateHandler() {
        logViewModel.getIsLoading().observe(getViewLifecycleOwner(), this::progressBarHandler);
        logViewModel.getEmployeeIsLoading().observe(getViewLifecycleOwner(), this::progressBarHandler);
    }


    private void progressBarHandler(boolean status) {
        if (status) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

}
