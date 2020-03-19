package com.p3lj2.koveepetshop.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.viewmodel.EmployeeViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private EmployeeViewModel employeeViewModel;

    @BindViews({R.id.edt_username, R.id.edt_password})
    List<EditText> loginForms;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        employeeViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EmployeeViewModel.class);
    }

    @OnClick(R.id.btn_login)
    public void login(View view) {
        String username = loginForms.get(0).getText().toString().trim();
        String password = loginForms.get(1).getText().toString().trim();

        employeeViewModel.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                if (aBoolean) {
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        if (!username.isEmpty() && !password.isEmpty()) {
            employeeViewModel.login(username, password).observe(this, employeeModel -> {
                startActivity(new Intent(LoginActivity.this, OwnerMenuActivity.class));

                finish();
            });
        }
    }
}
