package com.p3lj2.koveepetshop.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.PetSizeModel;
import com.p3lj2.koveepetshop.viewmodel.PetSizeViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InsertPetSizeActivity extends AppCompatActivity {

    @BindView(R.id.edt_pet_size)
    EditText edtPetSize;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private PetSizeViewModel petSizeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_pet_size);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.insert_pet_size);

        petSizeViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PetSizeViewModel.class);
    }

    @OnClick(R.id.btn_insert)
    public void insertPetSize(View view) {
        petSizeViewModel.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        petSizeViewModel.getEmployeeIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        PetSizeModel petSizeModel = new PetSizeModel();
        petSizeModel.setSize(edtPetSize.getText().toString().trim());

        if (petSizeModel.getSize().isEmpty()) {
            Toast.makeText(this, R.string.pet_size_cant_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!petSizeModel.getSize().equalsIgnoreCase("small") && !petSizeModel.getSize().equalsIgnoreCase("medium") &&
                !petSizeModel.getSize().equalsIgnoreCase("large") && !petSizeModel.getSize().equalsIgnoreCase("extra large")) {
            Toast.makeText(this, R.string.pet_size_restriction_message, Toast.LENGTH_SHORT).show();
            return;
        }

        final String[] token = {""};

        petSizeViewModel.getEmployee().observe(this, employeeDataModel -> {
            petSizeModel.setCreatedBy(employeeDataModel.getId());
            token[0] = employeeDataModel.getToken();
        });

        petSizeViewModel.insert(token[0], petSizeModel);

        Toast.makeText(this, R.string.pet_size_inserted, Toast.LENGTH_SHORT).show();
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
