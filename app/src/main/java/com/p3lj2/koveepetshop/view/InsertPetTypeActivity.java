package com.p3lj2.koveepetshop.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.PetTypeModel;
import com.p3lj2.koveepetshop.viewmodel.PetTypeViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InsertPetTypeActivity extends AppCompatActivity {

    @BindView(R.id.edt_pet_type)
    EditText edtPetType;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private PetTypeViewModel petTypeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_pet_type);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.insert_pet_type);

        petTypeViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PetTypeViewModel.class);
    }

    @OnClick(R.id.btn_insert)
    public void insertPetType(View view) {
        petTypeViewModel.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        petTypeViewModel.getIsLoadingEmployee().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        PetTypeModel petTypeModel = new PetTypeModel();
        petTypeModel.setType(edtPetType.getText().toString().trim());

        if (petTypeModel.getType().isEmpty()) {
            Toast.makeText(this, R.string.pet_type_cant_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        if (petTypeModel.getType().length() > 10) {
            Toast.makeText(this, R.string.max_10_chars, Toast.LENGTH_SHORT).show();
            return;
        }

        final String[] token = {""};

        petTypeViewModel.getEmployee().observe(this, employeeDataModel -> {
            petTypeModel.setCreatedBy(employeeDataModel.getId());
            token[0] = employeeDataModel.getToken();
        });

        petTypeViewModel.insert(token[0], petTypeModel);

        Toast.makeText(this, R.string.pet_type_inserted, Toast.LENGTH_SHORT).show();
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
