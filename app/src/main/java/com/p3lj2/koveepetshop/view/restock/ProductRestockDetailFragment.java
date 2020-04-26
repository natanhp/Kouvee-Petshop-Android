package com.p3lj2.koveepetshop.view.restock;

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
import com.p3lj2.koveepetshop.adapter.RestockAdapter;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.model.ProductModel;
import com.p3lj2.koveepetshop.model.ProductRestockDetail;
import com.p3lj2.koveepetshop.model.ProductRestockModel;
import com.p3lj2.koveepetshop.util.EventClickListener;
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.viewmodel.ProductRestockViewModel;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProductRestockDetailFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.spinner_supplier)
    Spinner spinnerSupplier;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private ProductRestockViewModel productRestockViewModel;
    private RestockAdapter restockAdapter;
    private EmployeeModel employee;
    private BidiMap<Integer, Integer> supplierMap = new DualHashBidiMap<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_restock_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        productRestockViewModel = new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()).create(ProductRestockViewModel.class);
        loadingStateHandler();
        getEmployeeData();
        initRecyclerView();
        deleteOnSwipe();
    }

    private void initRecyclerView() {
        restockAdapter = new RestockAdapter(eventClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(restockAdapter);
        getBookedProductData();
        restockAdapter.setUpdateable(true);
    }

    private void getBookedProductData() {
        productRestockViewModel.getBookedProductModel().observe(getViewLifecycleOwner(), new Observer<List<ProductModel>>() {
            @Override
            public void onChanged(List<ProductModel> productModels) {
                if (productModels != null) {
                    restockAdapter.setProductModels(productModels);
                }
            }
        });
    }

    private EventClickListener eventClickListener = (position, viewId) -> restockDialog(position);

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
                            productRestockViewModel.deleteBookedProductByPosition(viewHolder.getAdapterPosition());
                            restockAdapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), R.string.product_deletion_success, Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(getString(R.string.no), (dialogInterface, i) -> restockAdapter.notifyItemChanged(viewHolder.getAdapterPosition()))
                        .show();
            }
        })
                .attachToRecyclerView(recyclerView);
    }

    private void restockDialog(int position) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.restock_input, requireView().findViewById(android.R.id.content), false);
        EditText inputRestock = view.findViewById(R.id.edt_product_quantity);
        Util.confirmationDialog(getString(R.string.product_restock), "", getContext())
                .setView(view)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    if (inputRestock.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getContext(), R.string.all_column_must_be_filled, Toast.LENGTH_SHORT).show();
                    } else {
                        productRestockViewModel.updateeBookedProductByPosition(position, Integer.parseInt(inputRestock.getText().toString().trim()));
                        restockAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), R.string.product_updated, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.cancel())
                .show();
    }

    private void initSupplierSpinner() {
        if (employee != null) {
            productRestockViewModel.getAllSuppliers(employee.getToken()).observe(getViewLifecycleOwner(), supplierModels -> {
                if (supplierModels != null) {
                    String[] supplierNames = new String[supplierModels.size() + 1];
                    supplierNames[0] = getString(R.string.choose_supplier);
                    for (int i = 1; i < supplierNames.length; i++) {
                        supplierMap.put(i, supplierModels.get(i - 1).getIdSupplier());
                        supplierNames[i] = supplierModels.get(i - 1).getName();
                    }
                    ArrayAdapter<String> petSizeAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, supplierNames) {
                        @Override
                        public boolean areAllItemsEnabled() {
                            return false;
                        }

                        @Override
                        public boolean isEnabled(int position) {
                            String supplierName = supplierNames[position];
                            return !supplierName.equals(getString(R.string.choose_supplier));
                        }

                        @Override
                        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            if (convertView == null) {
                                LayoutInflater layoutInflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                convertView = Objects.requireNonNull(layoutInflater).inflate(android.R.layout.simple_spinner_dropdown_item, null);
                            }
                            String supplierName = supplierNames[position];
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
                    spinnerSupplier.setAdapter(petSizeAdapter);
                }
            });
        }
    }

    private void getEmployeeData() {
        productRestockViewModel.getAllEmployees().observe(getViewLifecycleOwner(), new Observer<EmployeeModel>() {
            @Override
            public void onChanged(EmployeeModel employeeModel) {
                if (employeeModel != null) {
                    employee = employeeModel;
                    initSupplierSpinner();
                }
            }
        });
    }

    private void loadingStateHandler() {
        productRestockViewModel.getEmployeeIsLoading().observe(getViewLifecycleOwner(), this::progressBarHandler);
        productRestockViewModel.getSupplierIsLoading().observe(getViewLifecycleOwner(), this::progressBarHandler);
        productRestockViewModel.getIsLoading().observe(getViewLifecycleOwner(), this::progressBarHandler);
    }

    private void progressBarHandler(boolean status) {
        if (status) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_process)
    void onClickProcess(View view) {
        if (restockAdapter.getItemCount() <= 0) {
            Toast.makeText(getContext(), R.string.no_booked_product, Toast.LENGTH_SHORT).show();
        } else {
            if (employee != null) {
                int spinnerPosition = spinnerSupplier.getSelectedItemPosition();
                int supplierId = supplierMap.getKey(spinnerPosition);
                List<ProductRestockDetail> productRestockDetails = new ArrayList<>();
                for (ProductModel productModel : restockAdapter.getProductModels()) {
                    ProductRestockDetail productRestockDetail = new ProductRestockDetail();
                    productRestockDetail.setProductId(productModel.getId());
                    productRestockDetail.setItemQty(productModel.getProductQuantity());
                    productRestockDetail.setCreatedBy(employee.getId());

                    productRestockDetails.add(productRestockDetail);
                }

                ProductRestockModel productRestockModel = new ProductRestockModel();
                productRestockModel.setCreatedBy(employee.getId());
                productRestockModel.setSupplierId(supplierId);
                productRestockModel.setProductRestockDetails(productRestockDetails);
                productRestockViewModel.insert(employee.getToken(), productRestockModel);

                productRestockViewModel.clearProductRestockDetail();
                restockAdapter.setProductModels(new ArrayList<>());
                spinnerSupplier.setSelection(0);
            }
        }
    }
}
