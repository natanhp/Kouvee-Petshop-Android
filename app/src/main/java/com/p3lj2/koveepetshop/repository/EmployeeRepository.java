package com.p3lj2.koveepetshop.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.p3lj2.koveepetshop.dao.EmployeeDAO;
import com.p3lj2.koveepetshop.database.KouveeDatabase;
import com.p3lj2.koveepetshop.endpoint.EmployeeEndpoint;
import com.p3lj2.koveepetshop.model.EmployeeDataModel;
import com.p3lj2.koveepetshop.model.EmployeeLoginSchema;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.util.RetrofitInstance;

import java.util.Objects;

import lombok.Getter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeRepository {
    private RetrofitInstance retrofitInstance;
    private EmployeeEndpoint employeeEndpoint;
    private Application application;
    private EmployeeDAO employeeDAO;

    @Getter
    private LiveData<EmployeeModel> employeeModelLiveData;

    public EmployeeRepository(Application application) {
        this.application = application;
        retrofitInstance = RetrofitInstance.getRetrofitInstance();
        employeeEndpoint = retrofitInstance.getRetrofit().create(EmployeeEndpoint.class);
        KouveeDatabase kouveeDatabase = KouveeDatabase.getInstance(application);
        employeeDAO = kouveeDatabase.employeeDAO();
    }

    public void login(String username, String password) {
        employeeEndpoint.login(username, password).enqueue(new Callback<EmployeeLoginSchema>() {
            @Override
            public void onResponse(Call<EmployeeLoginSchema> call, Response<EmployeeLoginSchema> response) {
                if (response.body() != null && response.code() == 200) {
                    EmployeeModel employeeModel = response.body().getEmployee();

                    EmployeeDataModel employeeDataModel = new EmployeeDataModel();
                    employeeDataModel.setId(employeeModel.getId());
                    employeeDataModel.setName(employeeModel.getName());
                    employeeDataModel.setDateBirth(employeeModel.getDateBirth());
                    employeeDataModel.setAddress(employeeModel.getAddress());
                    employeeDataModel.setPhoneNumber(employeeModel.getPhoneNumber());
                    employeeDataModel.setRole(employeeModel.getRole());
                    employeeDataModel.setUsername(employeeModel.getUsername());
                    employeeDataModel.setPassword(employeeModel.getPassword());
                    employeeDataModel.setToken(response.body().getToken());

                    insert(employeeDataModel);

                    //                    TODO=Move to the next activity
                } else {
                    Log.d("Login", "Login failed");
                }
            }

            @Override
            public void onFailure(Call<EmployeeLoginSchema> call, Throwable t) {
                Log.e("Login Error", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private static class InsertEmployeeAsyncTask extends AsyncTask<EmployeeDataModel, Void, Void> {
        private EmployeeDAO employeeDAO;

        private InsertEmployeeAsyncTask(EmployeeDAO employeeDAO) {
            this.employeeDAO = employeeDAO;
        }

        @Override
        protected Void doInBackground(EmployeeDataModel... employeeDataModels) {
            employeeDAO.insert(employeeDataModels[0]);

            return null;
        }
    }

    private void insert(EmployeeDataModel employeeDataModel) {
        new InsertEmployeeAsyncTask(employeeDAO).execute(employeeDataModel);
    }
}
