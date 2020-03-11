package com.p3lj2.koveepetshop.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.p3lj2.koveepetshop.dao.EmployeeDAO;
import com.p3lj2.koveepetshop.database.KouveeDatabase;
import com.p3lj2.koveepetshop.endpoint.EmployeeEndpoint;
import com.p3lj2.koveepetshop.model.EmployeeDataModel;
import com.p3lj2.koveepetshop.model.EmployeeLoginSchema;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.util.RetrofitInstance;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeRepository {
    private EmployeeEndpoint employeeEndpoint;
    private EmployeeDAO employeeDAO;
    private static MutableLiveData<EmployeeDataModel> employeeDataModelLiveData = new MutableLiveData<>();
    private static MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public EmployeeRepository(Application application) {
        RetrofitInstance retrofitInstance = RetrofitInstance.getRetrofitInstance();
        employeeEndpoint = retrofitInstance.getRetrofit().create(EmployeeEndpoint.class);
        KouveeDatabase kouveeDatabase = KouveeDatabase.getInstance(application);
        employeeDAO = kouveeDatabase.employeeDAO();
    }

    public void login(String username, String password) {
        isLoading.setValue(true);
        employeeEndpoint.login(username, password).enqueue(new Callback<EmployeeLoginSchema>() {
            @Override
            public void onResponse(@NotNull Call<EmployeeLoginSchema> call, @NotNull Response<EmployeeLoginSchema> response) {
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
                } else {
                    Log.d("Login", "Login failed");
                }
            }

            @Override
            public void onFailure(@NotNull Call<EmployeeLoginSchema> call, @NotNull Throwable t) {
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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            isLoading.setValue(false);
        }
    }

    private static class GetEmployeeAsyncTask extends AsyncTask<Void, Void, EmployeeDataModel> {
        private EmployeeDAO employeeDAO;

        private GetEmployeeAsyncTask(EmployeeDAO employeeDAO) {
            this.employeeDAO = employeeDAO;
        }

        @Override
        protected EmployeeDataModel doInBackground(Void... voids) {

            return employeeDAO.getEmployee();
        }

        @Override
        protected void onPostExecute(EmployeeDataModel employeeDataModel) {
            super.onPostExecute(employeeDataModel);
            employeeDataModelLiveData.postValue(employeeDataModel);
            isLoading.setValue(false);
        }
    }

    private void insert(EmployeeDataModel employeeDataModel) {
        new InsertEmployeeAsyncTask(employeeDAO).execute(employeeDataModel);
    }

    public LiveData<EmployeeDataModel> getEmployee() {
        isLoading.setValue(true);
        new GetEmployeeAsyncTask(employeeDAO).execute();
        return employeeDataModelLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
