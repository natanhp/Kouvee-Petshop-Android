package com.p3lj2.koveepetshop.view;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.iid.FirebaseInstanceId;
import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.model.FCMModel;
import com.p3lj2.koveepetshop.viewmodel.EmployeeViewModel;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {
    private EmployeeViewModel employeeViewModel;

    @BindViews({R.id.edt_username, R.id.edt_password})
    List<EditText> loginForms;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private String fcmToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        getFirebaseRegistrationId();
    }

    @OnClick(R.id.btn_login)
    public void login(View view) {
        String username = loginForms.get(0).getText().toString().trim();
        String password = loginForms.get(1).getText().toString().trim();

        if (!username.isEmpty() && !password.isEmpty()) {
            employeeViewModel.login(username, password).observe(this, employeeDataModel -> {
                if (employeeDataModel != null) {
                    if (employeeDataModel.getRole().equalsIgnoreCase("owner")) {
                        startActivity(new Intent(LoginActivity.this, OwnerMenuActivity.class));
                        saveFCMToken(fcmToken, employeeDataModel);
                        createNotificationChannel();
                        finish();
                    } else if (employeeDataModel.getRole().equalsIgnoreCase("cs")) {
                        startActivity(new Intent(LoginActivity.this, CSMenuActivity.class));
                        finish();
                    }
                }
            });
        } else {
            Toast.makeText(this, R.string.all_column_must_be_filled, Toast.LENGTH_SHORT).show();
        }

    }

    private void saveFCMToken(String token, EmployeeModel employeeDataModel) {
        if (employeeDataModel != null) {
            FCMModel fcmModel = new FCMModel();
            fcmModel.setEmployeeId(employeeDataModel.getId());
            fcmModel.setToken(token);

            employeeViewModel.insertFCMToken(employeeDataModel.getToken(), fcmModel);
        }
    }

    private void getFirebaseRegistrationId() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }

                    fcmToken = Objects.requireNonNull(task.getResult()).getToken();
                });
    }

    private void handleProgressBar(boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT > -Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.min_qty_warning);
            String description = getString(R.string.min_qty_warning_desc);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.min_qty_warning_id), name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
