package com.p3lj2.koveepetshop.view.restock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.adapter.ArrivedRestockAdapter;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.model.ProductRestockModel;
import com.p3lj2.koveepetshop.util.EventClickListener;
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.viewmodel.ProductRestockViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductArrivedFragment extends Fragment {
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.expandable_list_view)
    ExpandableListView expandableListView;

    private ProductRestockViewModel productRestockViewModel;
    private EmployeeModel employee;
    private ArrivedRestockAdapter arrivedRestockAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_product_arrived, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        productRestockViewModel = new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()).create(ProductRestockViewModel.class);
        initExpandableListAdapter();
        loadingStateHandler();
        getEmployeeData();
    }

    private void initExpandableListAdapter() {
        arrivedRestockAdapter = new ArrivedRestockAdapter(getContext(), eventClickListener);
        expandableListView.setAdapter(arrivedRestockAdapter);
    }

    private void getEmployeeData() {
        productRestockViewModel.getAllEmployees().observe(getViewLifecycleOwner(), new Observer<EmployeeModel>() {
            @Override
            public void onChanged(EmployeeModel employeeModel) {
                if (employeeModel != null) {
                    employee = employeeModel;
                }

                getData(employee.getToken());
            }
        });
    }

    private void getData(String bearerToken) {
        productRestockViewModel.getAll(bearerToken).observe(getViewLifecycleOwner(), new Observer<List<ProductRestockModel>>() {
            @Override
            public void onChanged(List<ProductRestockModel> productRestockModels) {
                if (productRestockModels != null) {
                    arrivedRestockAdapter.setProductRestockModels(productRestockModels);
                }
            }
        });
    }

    private EventClickListener eventClickListener = new EventClickListener() {
        @Override
        public void onEventClick(int position, @Nullable Integer viewId) {
            confirmRestock(arrivedRestockAdapter.getProductRestockModels().get(position).getId());
        }
    };

    private void confirmRestock(String productId) {
        Util.confirmationDialog(getString(R.string.booking_confirmation), getString(R.string.confirm_booking_confirmation), getContext())
                .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                    productRestockViewModel.confirm(employee.getToken(), productId, employee.getId());
                    productRestockViewModel.getAll(employee.getToken());
                    arrivedRestockAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), R.string.confirmation_success, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(getString(R.string.no), (dialogInterface, i) -> arrivedRestockAdapter.notifyDataSetChanged())
                .show();
    }

    private void loadingStateHandler() {
        productRestockViewModel.getEmployeeIsLoading().observe(getViewLifecycleOwner(), this::progressBarHandler);
        productRestockViewModel.getIsLoading().observe(getViewLifecycleOwner(), this::progressBarHandler);
    }

    private void progressBarHandler(boolean status) {
        if (status) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
