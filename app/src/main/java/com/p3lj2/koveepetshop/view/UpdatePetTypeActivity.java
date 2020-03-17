package com.p3lj2.koveepetshop.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.EmployeeDataModel;
import com.p3lj2.koveepetshop.model.PetTypeModel;
import com.p3lj2.koveepetshop.viewmodel.PetTypeViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdatePetTypeActivity extends AppCompatActivity {

    @BindView(R.id.edt_pet_type)
    EditText edtPetType;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private PetTypeViewModel petTypeViewModel;
    private PetTypeModel petTypeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pet_type);

        ButterKnife.bind(this);

        petTypeViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(PetTypeViewModel.class);

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
        initView();
    }

    private void initView() {
        petTypeModel = getIntent().getParcelableExtra(PetTypeActivity.EXTRA_PET_TYPE);

        if (petTypeModel != null) {
            edtPetType.setText(petTypeModel.getType());
        }
    }

    private void handleProgressBar(boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_insert)
    public void onClickUpdate(View view) {
        String type = edtPetType.getText().toString().trim();

        if (type.isEmpty()) {
            Toast.makeText(this, R.string.pet_type_cant_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        petTypeModel.setType(type);
        final String[] token = {""};

        petTypeViewModel.getEmployee().observe(this, new Observer<EmployeeDataModel>() {
            @Override
            public void onChanged(EmployeeDataModel employeeDataModel) {
                if (employeeDataModel != null) {
                    petTypeModel.setUpdatedBy(employeeDataModel.getId());
                    token[0] = employeeDataModel.getToken();
                }
            }
        });

        petTypeViewModel.update(token[0], petTypeModel);

        Toast.makeText(this, R.string.pet_type_updated, Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }

}
