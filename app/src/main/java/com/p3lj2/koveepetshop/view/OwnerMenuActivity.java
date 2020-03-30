package com.p3lj2.koveepetshop.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.viewmodel.EmployeeViewModel;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class OwnerMenuActivity extends AppCompatActivity {
    private EmployeeViewModel employeeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_menu);

        ButterKnife.bind(this);

        employeeViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EmployeeViewModel.class);
    }

    @OnClick(R.id.btn_product)
    public void productOnClick(View view) {
        startActivity(new Intent(this, ProductActivity.class));
    }

    @OnClick(R.id.btn_pet_type)
    public void petTypeOnClick(View view) {
        startActivity(new Intent(this, PetTypeActivity.class));
    }

    @OnClick(R.id.btn_pet_size)
    public void petSizeOnClick(View view) {
        startActivity(new Intent(this, PetSizeActivity.class));
    }

    @OnClick(R.id.btn_supplier)
    public void supplierOnClick(View view) {
        startActivity(new Intent(this, SupplierActivity.class));
    }

    @OnClick(R.id.btn_service)
    public void serviceOnClick(View view) {
        startActivity(new Intent(this, ServiceActivity.class));
    }

    @OnClick(R.id.btn_service_detail)
    public void serviceDetailOnClick(View view) {
        startActivity(new Intent(this, ServiceDetailActivity.class));
    }

    @OnClick(R.id.btn_logout)
    public void logoutOnClick(View view) {
        Util.confirmationDialog(getString(R.string.logout), getString(R.string.logout_confirmation),this)
                .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                    employeeViewModel.getEmployee().observe(this, employeeDataModel -> {
                        employeeViewModel.delete(employeeDataModel);
                        startActivity(new Intent(OwnerMenuActivity.this, LoginActivity.class));
                        finish();
                    });
                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }
}
