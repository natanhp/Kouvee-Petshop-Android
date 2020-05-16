package com.p3lj2.koveepetshop.view.cashier.payment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.adapter.ProductTransactionDetailAdapter;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.model.ProductTransactionDetailModel;
import com.p3lj2.koveepetshop.util.EventClickListener;
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.viewmodel.ProductTransactionViewModel;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductPaymentFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.tv_total)
    TextView tvTotal;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private ProductTransactionDetailAdapter productTransactionDetailAdapter;
    private ProductTransactionViewModel productTransactionViewModel;
    private int total;
    private Boolean isSuccess;
    private EmployeeModel employee;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_product_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            String title = ProductPaymentFragmentArgs.fromBundle(getArguments()).getProductTransactionId();
            Objects.requireNonNull(((PaymentActivity) requireActivity()).getSupportActionBar()).setTitle(title);
        }

        productTransactionViewModel = new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()).create(ProductTransactionViewModel.class);
        loadingStateHandler();
        getEmployeeData();
        initRecyclerView();
        deleteOnSwipe();
    }

    private void initRecyclerView() {
        productTransactionDetailAdapter = new ProductTransactionDetailAdapter(eventClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(productTransactionDetailAdapter);
        getProductTransactionDetail();
    }

    private EventClickListener eventClickListener = new EventClickListener() {
        @Override
        public void onEventClick(int position, @Nullable Integer viewId) {
            updateCart(position);
        }
    };

    private void updateCart(int position) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.product_quantity_input, requireView().findViewById(android.R.id.content), false);
        EditText inputQty = view.findViewById(R.id.edt_product_quantity);
        Util.confirmationDialog(getString(R.string.edit_product), "", getContext())
                .setView(view)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    if (inputQty.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getContext(), R.string.all_column_must_be_filled, Toast.LENGTH_SHORT).show();
                    } else {
                        do {
                            if (employee != null) {
                                List<ProductTransactionDetailModel> productTransactionDetailModels = productTransactionDetailAdapter.getProductTransactionDetailModels();
                                ProductTransactionDetailModel productTransactionDetail = productTransactionDetailModels.get(position);
                                productTransactionDetail.setUpdatedBy(employee.getId());
                                productTransactionDetail.setItemQty(Integer.parseInt(inputQty.getText().toString().trim()));
                                productTransactionViewModel.updateDetailById(employee.getToken(), productTransactionDetail);
                                productTransactionViewModel.setProductTransactionDetails(productTransactionDetailModels);
                                productTransactionDetailAdapter.notifyItemChanged(position);
                                isSuccessHandler();
                            }
                        } while (employee == null);
                    }
                })
                .setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.cancel())
                .show();
    }

    private void deleteOnSwipe() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Util.confirmationDialog(getString(R.string.product_deletion), getString(R.string.product_deletion_confirmation), getContext())
                        .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                            ProductTransactionDetailModel productTransactionDetailModel = productTransactionDetailAdapter.getProductTransactionDetailModels().get(viewHolder.getAdapterPosition());
                            productTransactionViewModel.deleteDetailById(employee.getToken(), productTransactionDetailModel.getId(), employee.getId());
                            productTransactionDetailAdapter.removeProductTransactionModel(viewHolder.getAdapterPosition());
                            productTransactionViewModel.setProductTransactionDetails(productTransactionDetailAdapter.getProductTransactionDetailModels());
                        })
                        .setNegativeButton(getString(R.string.no), (dialogInterface, i) -> productTransactionDetailAdapter.notifyItemChanged(viewHolder.getAdapterPosition()))
                        .show();
            }
        })
                .attachToRecyclerView(recyclerView);
    }


    private void getProductTransactionDetail() {
        productTransactionViewModel.getProductTransactionDetails().observe(getViewLifecycleOwner(), new Observer<List<ProductTransactionDetailModel>>() {
            @Override
            public void onChanged(List<ProductTransactionDetailModel> productTransactionDetailModels) {
                total = 0;
                for (ProductTransactionDetailModel productTransactionDetailModel : productTransactionDetailModels) {
                    total += productTransactionDetailModel.getProductModel().getProductPrice() * productTransactionDetailModel.getItemQty();
                }

                String totalString = "Rp " + total;
                tvTotal.setText(totalString);
                productTransactionDetailAdapter.setProductTransactionDetailModels(productTransactionDetailModels);
            }
        });
    }

    private void loadingStateHandler() {
        productTransactionViewModel.getIsLoadingEmployee().observe(getViewLifecycleOwner(), this::progressBarHandler);
        productTransactionViewModel.getIsLoading().observe(getViewLifecycleOwner(), this::progressBarHandler);
    }

    private void progressBarHandler(boolean status) {
        if (status) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void isSuccessHandler() {
        productTransactionViewModel.getIsSuccess().observe(getViewLifecycleOwner(), new Observer<Object[]>() {
            @Override
            public void onChanged(Object[] objects) {
                isSuccess = (Boolean) objects[0];
                Toast.makeText(getContext(), (String) objects[1], Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getEmployeeData() {
        productTransactionViewModel.getEmployee().observe(getViewLifecycleOwner(), new Observer<EmployeeModel>() {
            @Override
            public void onChanged(EmployeeModel employeeModel) {
                if (employeeModel != null) {
                    employee = employeeModel;
                }
            }
        });
    }
}
