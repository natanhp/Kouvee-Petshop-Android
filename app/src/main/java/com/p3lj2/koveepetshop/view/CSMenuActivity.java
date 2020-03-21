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

public class CSMenuActivity extends AppCompatActivity {
    private EmployeeViewModel employeeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_s_menu);

        ButterKnife.bind(this);

        employeeViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EmployeeViewModel.class);
    }

    @OnClick(R.id.btn_pet)
    public void onClickPet(View view) {
        startActivity(new Intent(this, PetActivity.class));
    }

    @OnClick(R.id.btn_customer)
    public void onClickCustomer(View view) {
        startActivity(new Intent(this, CustomersActivity.class));
    }

    @OnClick(R.id.btn_logout)
    public void logoutOnClick(View view) {
        Util.confirmationDialog(getString(R.string.logout), getString(R.string.logout_confirmation),this)
                .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                    employeeViewModel.getEmployee().observe(this, employeeDataModel -> {
                        employeeViewModel.delete(employeeDataModel);
                        startActivity(new Intent(CSMenuActivity.this, LoginActivity.class));
                        finish();
                    });
                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }
}
