package com.p3lj2.koveepetshop.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.iid.FirebaseInstanceId;
import com.p3lj2.koveepetshop.dao.EmployeeDAO;
import com.p3lj2.koveepetshop.database.KouveeDatabase;
import com.p3lj2.koveepetshop.endpoint.EmployeeEndpoint;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.model.ResponseSchema;
import com.p3lj2.koveepetshop.util.RetrofitInstance;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeRepository {
    private EmployeeEndpoint employeeEndpoint;
    private EmployeeDAO employeeDAO;
    private static MutableLiveData<EmployeeModel> employeeDataModelLiveData = new MutableLiveData<>();
    private static MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public EmployeeRepository(Application application) {
        RetrofitInstance retrofitInstance = RetrofitInstance.getRetrofitInstance();
        employeeEndpoint = retrofitInstance.getRetrofit().create(EmployeeEndpoint.class);
        KouveeDatabase kouveeDatabase = KouveeDatabase.getInstance(application);
        employeeDAO = kouveeDatabase.employeeDAO();
    }

    public LiveData<EmployeeModel> login(String username, String password) {
        isLoading.setValue(true);
        employeeEndpoint.login(username, password).enqueue(new Callback<ResponseSchema<EmployeeModel>>() {
            @Override
            public void onResponse(@NotNull Call<ResponseSchema<EmployeeModel>> call, @NotNull Response<ResponseSchema<EmployeeModel>> response) {
                if (response.body() != null && response.code() == 200) {
                    EmployeeModel employeeModel = response.body().getData().get(0);

                    insert(employeeModel);
                    EmployeeRepository.employeeDataModelLiveData.postValue(employeeModel);
                } else {
                    Log.d("Login", "Login failed");
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseSchema<EmployeeModel>> call, @NotNull Throwable t) {
                Log.e("Login Error", Objects.requireNonNull(t.getMessage()));
            }
        });

        return employeeDataModelLiveData;
    }

    private static class InsertEmployeeAsyncTask extends AsyncTask<EmployeeModel, Void, Void> {
        private EmployeeDAO employeeDAO;

        private InsertEmployeeAsyncTask(EmployeeDAO employeeDAO) {
            this.employeeDAO = employeeDAO;
        }

        @Override
        protected Void doInBackground(EmployeeModel... employeeDataModels) {
            employeeDAO.insert(employeeDataModels[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            isLoading.setValue(false);
        }
    }

    private static class GetEmployeeAsyncTask extends AsyncTask<Void, Void, EmployeeModel> {
        private EmployeeDAO employeeDAO;

        private GetEmployeeAsyncTask(EmployeeDAO employeeDAO) {
            this.employeeDAO = employeeDAO;
        }

        @Override
        protected EmployeeModel doInBackground(Void... voids) {

            return employeeDAO.getEmployee();
        }

        @Override
        protected void onPostExecute(EmployeeModel employeeDataModel) {
            super.onPostExecute(employeeDataModel);
            employeeDataModelLiveData.postValue(employeeDataModel);
            isLoading.setValue(false);
        }
    }

    private static class DeleteEmployeeAsynckTask extends AsyncTask<EmployeeModel, Void, Void> {
        private EmployeeDAO employeeDAO;

        private DeleteEmployeeAsynckTask(EmployeeDAO employeeDAO) {
            this.employeeDAO = employeeDAO;
        }

        @Override
        protected Void doInBackground(EmployeeModel... employeeDataModels) {
            employeeDAO.delete(employeeDataModels[0]);
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            employeeDataModelLiveData.postValue(null);
            isLoading.postValue(false);
        }
    }

    private void insert(EmployeeModel employeeDataModel) {
        new InsertEmployeeAsyncTask(employeeDAO).execute(employeeDataModel);
    }

    public LiveData<EmployeeModel> getEmployee() {
        isLoading.setValue(true);
        new GetEmployeeAsyncTask(employeeDAO).execute();
        return employeeDataModelLiveData;
    }

    public void delete(EmployeeModel employeeDataModel) {
        isLoading.setValue(true);
        new DeleteEmployeeAsynckTask(employeeDAO).execute(employeeDataModel);
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
