package com.p3lj2.koveepetshop.view.product.transaction;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.p3lj2.koveepetshop.adapter.CartAdapter;
import com.p3lj2.koveepetshop.model.CustomerModel;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.model.ProductModel;
import com.p3lj2.koveepetshop.model.ProductTransactionDetailModel;
import com.p3lj2.koveepetshop.model.ProductTransactionModel;
import com.p3lj2.koveepetshop.util.EventClickListener;
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.viewmodel.ProductTransactionViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.tv_total)
    TextView tvTotal;

    @BindView(R.id.spinner_customer_name)
    Spinner spinnerCustomer;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private ProductTransactionViewModel productTransactionViewModel;
    private CartAdapter cartAdapter;
    private EmployeeModel employee;
    private List<CustomerModel> customerModels = new ArrayList<>();
    private HashMap<Integer, Integer> customerMap = new HashMap<>();
    private double totalCounter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        tvTotal.setText("Rp 0");
        productTransactionViewModel = new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()).create(ProductTransactionViewModel.class);
        loadingStateHandler();
        getEmployeeData();
        initRecyclerView();
        deleteOnSwipe();
    }

    private void initRecyclerView() {
        cartAdapter = new CartAdapter(eventClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(cartAdapter);
        getCart();
    }

    private EventClickListener eventClickListener = (position, viewId) -> updateCart(position);

    private void getCart() {
        productTransactionViewModel.getCart().observe(getViewLifecycleOwner(), new Observer<List<ProductModel>>() {
            @Override
            public void onChanged(List<ProductModel> productModels) {
                if (productModels != null) {
                    cartAdapter.setProductModels(productModels);
                    counTotal(productModels);
                }
            }
        });
    }

    private void updateCart(int position) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.product_quantity_input, requireView().findViewById(android.R.id.content), false);
        EditText inputQty = view.findViewById(R.id.edt_product_quantity);
        Util.confirmationDialog(getString(R.string.edit_product), "", getContext())
                .setView(view)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    if (inputQty.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getContext(), R.string.all_column_must_be_filled, Toast.LENGTH_SHORT).show();
                    } else {
                        productTransactionViewModel.updateCartByPosition(position, Integer.parseInt(inputQty.getText().toString().trim()));
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
                            productTransactionViewModel.deleteCartItemByPosition(viewHolder.getAdapterPosition());
                            cartAdapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), R.string.product_deletion_success, Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(getString(R.string.no), (dialogInterface, i) -> cartAdapter.notifyItemChanged(viewHolder.getAdapterPosition()))
                        .show();
            }
        })
                .attachToRecyclerView(recyclerView);
    }

    private void counTotal(List<ProductModel> productModels) {
        totalCounter = 0;

        for (ProductModel productModel : productModels) {
            totalCounter += productModel.getProductQuantity() * productModel.getProductPrice();
        }

        String total = "Rp " + totalCounter;

        tvTotal.setText(total);
    }

    private void initCustomerSpinner() {
        if (employee != null) {
            productTransactionViewModel.getAllCustomer(employee.getToken()).observe(getViewLifecycleOwner(), customerModels -> {
                if (customerModels != null) {
                    this.customerModels = customerModels;
                    String[] customerNames = new String[customerModels.size() + 2];
                    customerNames[0] = getString(R.string.choose_customer);
                    customerNames[1] = "-";
                    customerMap.put(1, -1);
                    for (int i = 2; i < customerNames.length; i++) {
                        customerMap.put(i, customerModels.get(i - 2).getId());
                        customerNames[i] = customerModels.get(i - 2).getName();
                    }
                    ArrayAdapter<String> petSizeAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, customerNames) {
                        @Override
                        public boolean areAllItemsEnabled() {
                            return false;
                        }

                        @Override
                        public boolean isEnabled(int position) {
                            String customerName = customerNames[position];
                            return !customerName.equals(getString(R.string.choose_customer));
                        }

                        @Override
                        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            if (convertView == null) {
                                LayoutInflater layoutInflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                convertView = Objects.requireNonNull(layoutInflater).inflate(android.R.layout.simple_spinner_dropdown_item, null);
                            }
                            String supplierName = customerNames[position];
                            TextView tvSpinnerTextList = convertView.findViewById(android.R.id.text1);
                            tvSpinnerTextList.setText(supplierName);
                            boolean disabled = !isEnabled(position);
                            if (disabled) {
                                tvSpinnerTextList.setTextColor(Color.GRAY);
                            } else {
                                tvSpinnerTextList.setTextColor(Color.BLACK);
                            }

                            return convertView;
                        }
                    };
                    petSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCustomer.setAdapter(petSizeAdapter);
                }
            });
        }
    }

    private void getEmployeeData() {
        productTransactionViewModel.getEmployee().observe(getViewLifecycleOwner(), new Observer<EmployeeModel>() {
            @Override
            public void onChanged(EmployeeModel employeeModel) {
                if (employeeModel != null) {
                    employee = employeeModel;
                    initCustomerSpinner();
                }
            }
        });
    }

    private void loadingStateHandler() {
        productTransactionViewModel.getIsLoadingEmployee().observe(getViewLifecycleOwner(), this::progressBarHandler);
        productTransactionViewModel.getIsLoadingCustomer().observe(getViewLifecycleOwner(), this::progressBarHandler);
        productTransactionViewModel.getIsLoading().observe(getViewLifecycleOwner(), this::progressBarHandler);
    }

    private void progressBarHandler(boolean status) {
        if (status) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_checkout)
    void btnClickCheckout(View view) {
        if (spinnerCustomer == null || customerMap == null) {
            return;
        }

        int spinnerPosition = spinnerCustomer.getSelectedItemPosition();
        if (customerMap.get(spinnerPosition) == null) {
            Toast.makeText(requireContext(), R.string.all_column_must_be_filled, Toast.LENGTH_SHORT).show();
            return;
        }

        ProductTransactionModel productTransactionModel = new ProductTransactionModel();
        productTransactionModel.setCreatedBy(employee.getId());
        productTransactionModel.setTotal(totalCounter);
        int id = customerMap.get(spinnerPosition);
        if (id != -1) {
            productTransactionModel.setCustomerId(id);
        }

        List<ProductModel> productModels = cartAdapter.getProductModels();
        List<ProductTransactionDetailModel> productTransactionDetailModels = new ArrayList<>();
        for (ProductModel productModel : productModels) {
            ProductTransactionDetailModel productTransactionDetailModel = new ProductTransactionDetailModel();
            productTransactionDetailModel.setCreatedBy(employee.getId());
            productTransactionDetailModel.setItemQty(productModel.getProductQuantity());
            productTransactionDetailModel.setProductId(productModel.getId());
            productTransactionDetailModels.add(productTransactionDetailModel);
        }

        productTransactionModel.setProductTransactionkDetails(productTransactionDetailModels);

        productTransactionViewModel.insert(employee.getToken(), productTransactionModel);

        productTransactionViewModel.getIsSuccess().observe(getViewLifecycleOwner(), new Observer<Object[]>() {
            @Override
            public void onChanged(Object[] objects) {
                if (objects != null) {
                    boolean status = (boolean) objects[0];
                    if (status) {
                        productTransactionViewModel.resetCart();
                        cartAdapter.setProductModels(new ArrayList<>());
                        tvTotal.setText("Rp 0");
                        spinnerCustomer.setSelection(0);
                    }
                    Toast.makeText(getContext(), (String) objects[1], Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
