package com.p3lj2.koveepetshop.view.cashier.payment;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.adapter.ProductTransactionDetailAdapter;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.model.ProductTransactionDetailModel;
import com.p3lj2.koveepetshop.model.ProductTransactionModel;
import com.p3lj2.koveepetshop.util.EventClickListener;
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.viewmodel.ProductTransactionViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductPaymentFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindViews({R.id.tv_total, R.id.tv_change})
    List<TextView> textViews;

    @BindViews({R.id.edt_discount, R.id.edt_payment})
    List<EditText> editTexts;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private ProductTransactionDetailAdapter productTransactionDetailAdapter;
    private ProductTransactionViewModel productTransactionViewModel;
    private static double total;
    private static double change;
    private static double discount = 0;
    private static Boolean isSuccess;
    private EmployeeModel employee;
    private ProductTransactionModel productTransactionModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_product_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            productTransactionModel = ProductPaymentFragmentArgs.fromBundle(getArguments()).getProductTransaction();
            Objects.requireNonNull(((PaymentActivity) requireActivity()).getSupportActionBar()).setTitle(productTransactionModel.getId());
        }

        productTransactionViewModel = new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()).create(ProductTransactionViewModel.class);
        isSuccessHandler();
        loadingStateHandler();
        getEmployeeData();
        initRecyclerView();
        deleteOnSwipe();
        hanclerPayment();
    }

    private void initRecyclerView() {
        productTransactionDetailAdapter = new ProductTransactionDetailAdapter(eventClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(productTransactionDetailAdapter);
        getProductTransactionDetail();
    }

    private EventClickListener eventClickListener = (position, viewId) -> updateCart(position);

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
                textViews.get(0).setText(totalString);
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

    private void hanclerPayment() {
        if (productTransactionModel.getCustomer().getName().equalsIgnoreCase("-")) {
            editTexts.get(0).setEnabled(false);
            discount = 0;
        }

        editTexts.get(0).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String paymentStr = editTexts.get(1).getText().toString().trim();
                String discountStr = editable.toString().trim();
                if (!paymentStr.isEmpty() && !discountStr.isEmpty()) {
                    double payment = Double.parseDouble(paymentStr);
                    discount = Double.parseDouble(discountStr);
                    double tmpTotal = total;
                    change = payment + discount - tmpTotal;

                    String totalStr = "Rp " + change;
                    productTransactionViewModel.setStrTotal(totalStr);
                }
            }
        });

        editTexts.get(1).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String paymentStr = editable.toString().trim();
                String discountStr = editTexts.get(0).getText().toString().trim();
                if (!paymentStr.isEmpty()) {
                    double payment = Double.parseDouble(paymentStr);
                    if (!discountStr.isEmpty()){
                        discount = Double.parseDouble(discountStr);
                    }
                    double tmpTotal = total;
                    change = payment + discount - tmpTotal;
                    String totalStr = "Rp " + change;
                    productTransactionViewModel.setStrTotal(totalStr);
                }
            }
        });

        productTransactionViewModel.getStrTotal().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                EditText editText = editTexts.get(1);
                textViews.get(1).setText(s);
                editText.setError(null);
                if (change < 0)  {
                    editText.setError(getString(R.string.payment_not_enough));
                }
            }
        });
    }

    @OnClick(R.id.btn_process)
    void onClickProcess(View view) {
        if (change >= 0) {
            productTransactionViewModel.confirm(employee.getToken(), productTransactionModel);
            boolean checkSuccess = isSuccess != null ? isSuccess : false;
            if (checkSuccess) {
                Util.createProductTransactionInvoice(requireContext(), productTransactionModel, employee, total, discount);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NavDirections navDirections = ProductPaymentFragmentDirections.actionNavigationProductPaymentToNavigationProductTransactionList();
                        Navigation.findNavController(requireView()).navigate(navDirections);
                    }
                }, 3500);
            }
        }
    }
}
