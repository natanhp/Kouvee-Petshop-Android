package com.p3lj2.koveepetshop.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.iid.FirebaseInstanceId;
import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.view.log.LogActivity;
import com.p3lj2.koveepetshop.view.restock.RestockActivity;
import com.p3lj2.koveepetshop.viewmodel.EmployeeViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.ContentValues.TAG;

public class OwnerMenuActivity extends AppCompatActivity {
    private EmployeeViewModel employeeViewModel;
    private EmployeeModel employeeData;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_menu);
        Objects.requireNonNull(getSupportActionBar()).hide();

        ButterKnife.bind(this);

        employeeViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EmployeeViewModel.class);

        employeeViewModel.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        employeeViewModel.getFCMIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });
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

    @OnClick(R.id.btn_log)
    public void logOnClick(View view) {
        startActivity(new Intent(this, LogActivity.class));
    }

    @OnClick(R.id.btn_logout)
    public void logoutOnClick(View view) {
        Util.confirmationDialog(getString(R.string.logout), getString(R.string.logout_confirmation), this)
                .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                    employeeViewModel.getEmployee().observe(this, employeeDataModel -> {
                        if (employeeDataModel != null) {
                            employeeData = employeeDataModel;
                        }
                    });

                    do {
                        if (employeeData != null) {
                            employeeViewModel.delete(employeeData);
                            deleteFCMToken();
                            startActivity(new Intent(OwnerMenuActivity.this, LoginActivity.class));
                            finish();
                        }
                    } while (employeeData == null);

                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }

    @OnClick(R.id.btn_restock)
    public void productRestockClick(View view) {
        startActivity(new Intent(this, RestockActivity.class));
    }

    private void handleProgressBar(boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void deleteFCMToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }

                    employeeViewModel.deleteFCMToken(employeeData.getToken(), Objects.requireNonNull(task.getResult()).getToken());
                });
    }
}
