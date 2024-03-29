package com.p3lj2.koveepetshop.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.ServiceModel;
import com.p3lj2.koveepetshop.viewmodel.ServiceViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InsertServiceActivity extends AppCompatActivity {

    @BindView(R.id.edt_service)
    EditText edtService;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private ServiceViewModel serviceViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_service);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.insert_service);

        serviceViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ServiceViewModel.class);
    }

    @OnClick(R.id.btn_insert)
    public void insertService(View view) {
        serviceViewModel.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        serviceViewModel.getEmployeeIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        ServiceModel serviceModel = new ServiceModel();
        serviceModel.setServiceName(edtService.getText().toString().trim());

        if (serviceModel.getServiceName().isEmpty()) {
            Toast.makeText(this, R.string.service_cant_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        final String[] token = {""};

        serviceViewModel.getEmployee().observe(this, employeeDataModel -> {
            serviceModel.setCreatedBy(employeeDataModel.getId());
            token[0] = employeeDataModel.getToken();
        });

        serviceViewModel.insert(token[0], serviceModel);

        Toast.makeText(this, R.string.service_inserted, Toast.LENGTH_SHORT).show();
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
