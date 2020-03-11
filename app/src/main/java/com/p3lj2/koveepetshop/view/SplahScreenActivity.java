package com.p3lj2.koveepetshop.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.EmployeeDataModel;
import com.p3lj2.koveepetshop.viewmodel.EmployeeViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplahScreenActivity extends AppCompatActivity {
    private EmployeeViewModel employeeViewModel;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splah_screen);

        ButterKnife.bind(this);

        employeeViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EmployeeViewModel.class);

        employeeViewModel.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                if (aBoolean) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        employeeViewModel.getEmployee().observe(this, employeeDataModel -> {
            if (employeeDataModel == null) {
                startActivity(new Intent(SplahScreenActivity.this, LoginActivity.class));

                finish();
            } else {
                if (employeeDataModel.getRole().equals("Owner")) {
                    startActivity(new Intent(SplahScreenActivity.this, OwnerActivity.class));
                }

                finish();
            }
        });
    }
}
