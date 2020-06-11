package com.p3lj2.koveepetshop.view.service.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.adapter.ServiceListAdapter;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.model.ServiceDetailModel;
import com.p3lj2.koveepetshop.model.ServiceTransactionDetailModel;
import com.p3lj2.koveepetshop.model.ServiceTransactionModel;
import com.p3lj2.koveepetshop.viewmodel.ServiceDetailViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceCartFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.tv_total)
    TextView tvTotal;

    private ServiceListAdapter serviceListAdapter;
    private ServiceDetailViewModel serviceDetailViewModel;
    private static double total = 0;
    private EmployeeModel employee;
    private int petId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_service_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        serviceDetailViewModel = new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()).create(ServiceDetailViewModel.class);
        loadingStateHandler();
        getEmployeeData();
        getPetid();
        initRecyclerView();
    }

    private void initRecyclerView() {
        serviceListAdapter = new ServiceListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(serviceListAdapter);
        getServiceData();
    }

    private void getEmployeeData() {
        serviceDetailViewModel.getEmployee().observe(getViewLifecycleOwner(), new Observer<EmployeeModel>() {
            @Override
            public void onChanged(EmployeeModel employeeModel) {
                if (employeeModel != null) {
                    employee = employeeModel;
                }
            }
        });
    }


    private void getServiceData() {
        serviceDetailViewModel.getCart().observe(getViewLifecycleOwner(), new Observer<List<ServiceDetailModel>>() {
            @Override
            public void onChanged(List<ServiceDetailModel> serviceDetailModels) {
                serviceListAdapter.setServiceDetailCompletes(serviceDetailModels);
                total = 0;

                for (ServiceDetailModel serviceDetailModel : serviceDetailModels) {
                    total += serviceDetailModel.getPrice();
                }

                String strTotal = "Rp " + total;
                serviceDetailViewModel.setStrTotal(strTotal);

                serviceDetailViewModel.getStrTotal().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s != null) {
                            tvTotal.setText(s);
                        }
                    }
                });
            }
        });
    }

    private void loadingStateHandler() {
        serviceDetailViewModel.getIsLoading().observe(getViewLifecycleOwner(), this::progressBarHandler);
        serviceDetailViewModel.getEmployeeIsLoading().observe(getViewLifecycleOwner(), this::progressBarHandler);
    }


    private void progressBarHandler(boolean status) {
        if (status) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void getPetid() {
        serviceDetailViewModel.getSelectedPet().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer != null) {
                    petId = integer;
                }
            }
        });
    }

    @OnClick(R.id.btn_process)
    void onClickProcess(View view) {
        List<ServiceTransactionDetailModel> serviceTransactionDetailModels = new ArrayList<>();

        for (ServiceDetailModel serviceDetailModel : serviceListAdapter.getServiceDetailModels()) {
            ServiceTransactionDetailModel serviceTransactionDetailModel = new ServiceTransactionDetailModel();
            serviceTransactionDetailModel.setCreatedBy(employee.getId());
            serviceTransactionDetailModel.setServiceDetailId(serviceDetailModel.getId());

            serviceTransactionDetailModels.add(serviceTransactionDetailModel);
        }

        ServiceTransactionModel serviceTransactionModel = new ServiceTransactionModel();
        serviceTransactionModel.setCreatedBy(employee.getId());
        serviceTransactionModel.setPetId(petId);
        serviceTransactionModel.setServiceTransactionDetails(serviceTransactionDetailModels);
        serviceTransactionModel.setTotal(total);

        serviceDetailViewModel.insertTransaction(employee.getToken(), serviceTransactionModel);

        serviceDetailViewModel.getIsSuccess().observe(getViewLifecycleOwner(), new Observer<Object[]>() {
            @Override
            public void onChanged(Object[] objects) {
                if (objects != null) {
                    boolean status = (boolean) objects[0];
                    if (status) {
                        serviceDetailViewModel.resetAll();
                        serviceListAdapter.setServiceDetailCompletes(new ArrayList<>());
                        tvTotal.setText("Rp 0");
                    }
                    Toast.makeText(getContext(), (String) objects[1], Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
