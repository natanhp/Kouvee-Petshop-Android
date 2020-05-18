package com.p3lj2.koveepetshop.view.cashier.payment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.adapter.ProductTransactionListAdapter;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.model.ProductTransactionDetailModel;
import com.p3lj2.koveepetshop.model.ProductTransactionModel;
import com.p3lj2.koveepetshop.util.EventClickListener;
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.viewmodel.ProductTransactionViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductTransactionListFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private ProductTransactionViewModel productTransactionViewModel;
    private ProductTransactionListAdapter productTransactionListAdapter;
    private EmployeeModel employee;
    private Boolean isSuccess;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_transaction_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        productTransactionViewModel = new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()).create(ProductTransactionViewModel.class);
        loadingStateHandler();
        initRecyclerView();
        getEmployeeData();
        deleteOnSwipe();
    }

    private void initRecyclerView() {
        productTransactionListAdapter = new ProductTransactionListAdapter(eventClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(productTransactionListAdapter);
    }

    private EventClickListener eventClickListener = new EventClickListener() {
        @Override
        public void onEventClick(int position, @Nullable Integer viewId) {
            ProductTransactionModel productTransactionModel = productTransactionListAdapter.getProductTransactionModels().get(position);
            List<ProductTransactionDetailModel> productTransactionDetailModels = productTransactionModel.getProductTransactionkDetails();
            productTransactionViewModel.setProductTransactionDetails(productTransactionDetailModels);
            NavDirections directions = ProductTransactionListFragmentDirections.actionNavigationProductTransactionListToNavigationProductPayment(productTransactionModel);
            Navigation.findNavController(requireView()).navigate(directions);
        }
    };

    private void getProductTransactionList() {
        productTransactionViewModel.getAll(employee.getToken()).observe(getViewLifecycleOwner(), new Observer<List<ProductTransactionModel>>() {
            @Override
            public void onChanged(List<ProductTransactionModel> productTransactionModels) {
                if (productTransactionModels != null) {
                    productTransactionListAdapter.setProductTransactionModels(productTransactionModels);
                }
            }
        });

        isSuccessHandler();
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

    private void deleteOnSwipe() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Util.confirmationDialog(getString(R.string.transaction_deletion), getString(R.string.transaction_deletetion_confirmation), getContext())
                        .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                            productTransactionViewModel.deleteTransactionById(employee.getToken(), productTransactionListAdapter.getProductTransactionModels().get(viewHolder.getAdapterPosition()).getId(),employee.getId());
                            productTransactionListAdapter.deleteProductTransactionModel(viewHolder.getAdapterPosition());
                            isSuccessHandler();
                        })
                        .setNegativeButton(getString(R.string.no), (dialogInterface, i) -> productTransactionListAdapter.notifyItemChanged(viewHolder.getAdapterPosition()))
                        .show();
            }
        })
                .attachToRecyclerView(recyclerView);
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

    private void getEmployeeData() {
        productTransactionViewModel.getEmployee().observe(getViewLifecycleOwner(), new Observer<EmployeeModel>() {
            @Override
            public void onChanged(EmployeeModel employeeModel) {
                if (employeeModel != null) {
                    employee = employeeModel;
                    getProductTransactionList();
                }
            }
        });
    }
}
