package com.p3lj2.koveepetshop.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.model.PetModel;
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.viewmodel.PetViewModel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InsertPetActivity extends AppCompatActivity {

    @BindViews({R.id.spinner_pet_type, R.id.spinner_pet_size, R.id.spinner_customer_name})
    List<Spinner> spinners;

    @BindView(R.id.edt_pet_name)
    EditText edtPetName;

    @BindView(R.id.tv_birth_date)
    TextView tvBirthDate;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    private PetViewModel petViewModel;
    private EmployeeModel employee = new EmployeeModel();
    private HashMap<Integer, Integer> petTypeMap = new HashMap<>();
    private HashMap<Integer, Integer> petSizeMap = new HashMap<>();
    private HashMap<Integer, Integer> customerMap = new HashMap<>();
    private String birthDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_pet);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.insert_pet);

        petViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PetViewModel.class);

        initIsLoading();

        initSpinners();
    }

    @OnClick(R.id.btn_date_picker)
    public void onClickDatePicker(View view) {
        DatePickerDialog datePickerDialog;
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, (datePicker, i, i1, i2) -> {
            Calendar bDayCalendar = Calendar.getInstance();
            bDayCalendar.set(i, i1, i2);
            birthDate = Util.stdDateFormater(bDayCalendar.getTime());
            tvBirthDate.setText(Util.dateFormater(birthDate));
        },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void initSpinners() {
        petViewModel.getEmployee().observe(this, employeeDataModel -> {
            employee = employeeDataModel;

            petViewModel.getAllPetTypes(employee.getToken()).observe(InsertPetActivity.this, petTypeModels -> {
                String[] petTypes = new String[petTypeModels.size()];

                for (int i = 0; i < petTypeModels.size(); i++) {
                    petTypeMap.put(i, petTypeModels.get(i).getId());
                    petTypes[i] = petTypeModels.get(i).getType();
                }

                ArrayAdapter<String> petTypeAdapter = new ArrayAdapter<>(InsertPetActivity.this, android.R.layout.simple_spinner_item, petTypes);
                spinners.get(0).setAdapter(petTypeAdapter);
            });

            petViewModel.getAllPetSizes(employee.getToken()).observe(InsertPetActivity.this, petSizeModels -> {
                String[] petSizes = new String[petSizeModels.size()];

                for (int i = 0; i < petSizeModels.size(); i++) {
                    petSizeMap.put(i, petSizeModels.get(i).getId());
                    petSizes[i] = petSizeModels.get(i).getSize();
                }

                ArrayAdapter<String> petSizeAdapter = new ArrayAdapter<>(InsertPetActivity.this, android.R.layout.simple_spinner_item, petSizes);
                spinners.get(1).setAdapter(petSizeAdapter);
            });

            petViewModel.getAllCustomers(employee.getToken()).observe(InsertPetActivity.this, customerModels -> {
                String[] customers = new String[customerModels.size()];

                for (int i = 0; i < customerModels.size(); i++) {
                    customerMap.put(i, customerModels.get(i).getId());
                    customers[i] = customerModels.get(i).getName();
                }

                ArrayAdapter<String> customerAdapter = new ArrayAdapter<>(InsertPetActivity.this, android.R.layout.simple_spinner_item, customers);
                spinners.get(2).setAdapter(customerAdapter);
            });
        });

    }

    @OnClick(R.id.btn_insert)
    public void insertPet(View view) {
        PetModel petModel = new PetModel();
        petModel.setPetTypeId(petTypeMap.get(spinners.get(0).getSelectedItemPosition()));
        petModel.setPetSizeId(petSizeMap.get(spinners.get(1).getSelectedItemPosition()));
        petModel.setCustomerId(customerMap.get(spinners.get(2).getSelectedItemPosition()));

        String petName = edtPetName.getText().toString().trim();

        if (petName.isEmpty() || tvBirthDate.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, R.string.all_column_must_be_filled, Toast.LENGTH_SHORT).show();
            return;
        }

        petModel.setName(petName);
        petModel.setDateBirth(birthDate);

        petModel.setCreatedBy(employee.getId());

        petViewModel.insert(employee.getToken(), petModel);

        Toast.makeText(this, R.string.pet_inserted, Toast.LENGTH_SHORT).show();
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
        petViewModel.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        petViewModel.getEmployeeIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        petViewModel.getPetTypeIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        petViewModel.getPetSizeIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        petViewModel.getCustomerIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });
    }
}
