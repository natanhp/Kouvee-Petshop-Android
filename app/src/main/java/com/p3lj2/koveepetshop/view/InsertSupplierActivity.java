package com.p3lj2.koveepetshop.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.SupplierModel;
import com.p3lj2.koveepetshop.viewmodel.SupplierViewModel;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InsertSupplierActivity extends AppCompatActivity {

    @BindViews({R.id.edt_supplier_name, R.id.edt_phone_number, R.id.edt_address})
    List<EditText> supplierForm;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private SupplierViewModel supplierViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_supplier);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.insert_supplier);

        supplierViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(SupplierViewModel.class);
    }

    @OnClick(R.id.btn_insert)
    public void insertSupplier(View view) {
        supplierViewModel.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        supplierViewModel.getEmployeeIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        SupplierModel supplierModel = new SupplierModel();

        String name = supplierForm.get(0).getText().toString().trim();
        String phoneNumber = supplierForm.get(1).getText().toString().trim();
        String address = supplierForm.get(2).getText().toString().trim();

        if (name.isEmpty() && phoneNumber.isEmpty() && address.isEmpty()) {
            Toast.makeText(this, R.string.all_column_must_be_filled, Toast.LENGTH_SHORT).show();
            return;
        }

        if (phoneNumber.length() > 15) {
            Toast.makeText(this, R.string.phone_number_max, Toast.LENGTH_SHORT).show();
            return;
        }

        supplierModel.setName(name);
        supplierModel.setPhoneNumber(phoneNumber);
        supplierModel.setAddress(address);

        final String[] token = {""};

        supplierViewModel.getEmployee().observe(this, employeeDataModel -> {
            supplierModel.setCreatedBy(employeeDataModel.getId());
            token[0] = employeeDataModel.getToken();
        });

        supplierViewModel.insert(token[0], supplierModel);

        Toast.makeText(this, R.string.supplier_inserted, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void handleProgressBar(boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
