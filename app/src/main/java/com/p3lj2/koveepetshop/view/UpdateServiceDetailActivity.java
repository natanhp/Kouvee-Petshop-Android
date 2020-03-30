package com.p3lj2.koveepetshop.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.EmployeeDataModel;
import com.p3lj2.koveepetshop.model.ServiceDetailComplete;
import com.p3lj2.koveepetshop.model.ServiceDetailModel;
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.viewmodel.ServiceDetailViewModel;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateServiceDetailActivity extends AppCompatActivity {

    @BindViews({R.id.spinner_service, R.id.spinner_pet_type, R.id.spinner_pet_size})
    List<Spinner> spinners;

    @BindView(R.id.edt_service_price)
    EditText edtServicePrice;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private ServiceDetailViewModel serviceDetailViewModel;
    private EmployeeDataModel employee = new EmployeeDataModel();
    private BidiMap<Integer, Integer> serviceNameMap = new DualHashBidiMap<>();
    private BidiMap<Integer, Integer> petTypeMap = new DualHashBidiMap<>();
    private BidiMap<Integer, Integer> petSizeMap = new DualHashBidiMap<>();
    private ServiceDetailModel serviceDetailModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_service_detail);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.insert_service_detail);

        serviceDetailViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ServiceDetailViewModel.class);

        initIsLoading();

        initViews();
    }

    private void initViews() {
        ServiceDetailComplete serviceDetailComplete = getIntent().getParcelableExtra(ServiceDetailActivity.EXTRA_SERVICE_DETAIL);

        serviceDetailViewModel.getEmployee().observe(this, employeeDataModel -> {
            employee = employeeDataModel;

            serviceDetailViewModel.getAllServices(employee.getToken()).observe(UpdateServiceDetailActivity.this, serviceModels -> {
                progressBar.setVisibility(View.VISIBLE);
                String[] serviceNames = new String[serviceModels.size()];

                for (int i = 0; i < serviceModels.size(); i++) {
                    serviceNameMap.put(i, serviceModels.get(i).getId());
                    serviceNames[i] = serviceModels.get(i).getServiceName();
                }

                ArrayAdapter<String> serviceNameAdapter = new ArrayAdapter<>(UpdateServiceDetailActivity.this, android.R.layout.simple_spinner_item, serviceNames);
                spinners.get(0).setAdapter(serviceNameAdapter);


                if (serviceDetailComplete != null) {
                    spinners.get(0).setSelection(Util.getKey(serviceNameMap, serviceDetailComplete.getServiceDetailModel().getServiceId()));
                }
                progressBar.setVisibility(View.GONE);

            });

            serviceDetailViewModel.getAllPetTypes(employee.getToken()).observe(UpdateServiceDetailActivity.this, petTypeModels -> {
                progressBar.setVisibility(View.VISIBLE);
                String [] petTypes = new String[petTypeModels.size()];

                for (int i = 0; i < petTypeModels.size(); i++) {
                    petTypeMap.put(i, petTypeModels.get(i).getId());
                    petTypes[i] = petTypeModels.get(i).getType();
                }

                ArrayAdapter<String> petTypeAdapter = new ArrayAdapter<>(UpdateServiceDetailActivity.this, android.R.layout.simple_spinner_item, petTypes);
                spinners.get(1).setAdapter(petTypeAdapter);

                if (serviceDetailComplete != null) {
                    spinners.get(1).setSelection(Util.getKey(petTypeMap, serviceDetailComplete.getServiceDetailModel().getPetTypeId()));
                }
                progressBar.setVisibility(View.GONE);

            });

            serviceDetailViewModel.getAllPetSizes(employee.getToken()).observe(UpdateServiceDetailActivity.this, petSizeModels -> {
                progressBar.setVisibility(View.VISIBLE);

                String [] petSizes = new String[petSizeModels.size()];

                for (int i = 0; i < petSizeModels.size(); i++) {
                    petSizeMap.put(i, petSizeModels.get(i).getId());
                    petSizes[i] = petSizeModels.get(i).getSize();
                }

                ArrayAdapter<String> petSizeAdapter = new ArrayAdapter<>(UpdateServiceDetailActivity.this, android.R.layout.simple_spinner_item, petSizes);
                spinners.get(2).setAdapter(petSizeAdapter);

                if (serviceDetailComplete != null) {
                    spinners.get(2).setSelection(Util.getKey(petSizeMap, serviceDetailComplete.getServiceDetailModel().getPetSizeId()));
                }
                progressBar.setVisibility(View.GONE);

            });
        });

        if (serviceDetailComplete != null) {
            edtServicePrice.setText(String.valueOf(serviceDetailComplete.getServiceDetailModel().getPrice()));
            serviceDetailModel = serviceDetailComplete.getServiceDetailModel();
        }
    }

    @OnClick(R.id.btn_update)
    public void insertServiceDetail(View view) {
        serviceDetailModel.setServiceId(serviceNameMap.get(spinners.get(0).getSelectedItemPosition()));
        serviceDetailModel.setPetTypeId(petTypeMap.get(spinners.get(1).getSelectedItemPosition()));
        serviceDetailModel.setPetSizeId(petSizeMap.get(spinners.get(2).getSelectedItemPosition()));

        String servicePrice = edtServicePrice.getText().toString().trim();

        if (servicePrice.isEmpty()) {
            Toast.makeText(this, R.string.price_cant_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        serviceDetailModel.setPrice(Double.parseDouble(servicePrice));

        serviceDetailModel.setCreatedBy(employee.getId());

        serviceDetailViewModel.insert(employee.getToken(), serviceDetailModel);

        Toast.makeText(this, R.string.service_detail_updated, Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }

    private void handleProgressBar(boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void initIsLoading() {
        serviceDetailViewModel.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        serviceDetailViewModel.getEmployeeIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        serviceDetailViewModel.getServiceIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        serviceDetailViewModel.getPetTypeIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });
    }
}
