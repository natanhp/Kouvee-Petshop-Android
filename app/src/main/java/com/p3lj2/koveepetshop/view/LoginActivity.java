package com.p3lj2.koveepetshop.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.viewmodel.EmployeeViewModel;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private EmployeeViewModel employeeViewModel;

    @BindViews({R.id.edt_username, R.id.edt_password})
    List<EditText> loginForms;

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

        if (!username.isEmpty() && !password.isEmpty()) {
            employeeViewModel.login(username, password);
        }
    }
}
