package com.p3lj2.koveepetshop.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.CustomerModel;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.viewmodel.CustomerViewModel;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateCustomerActivity extends AppCompatActivity {

    @BindViews({R.id.edt_customer_name, R.id.edt_address, R.id.edt_phone_number})
    List<EditText> edtCustomers;

    @BindView(R.id.tv_birth_date)
    TextView tvBirthDate;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private CustomerViewModel customerViewModel;
    private EmployeeModel employee = new EmployeeModel();
    private String birthDate = "";
    private DatePickerDialog datePickerDialog;
    private CustomerModel customerModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_customer);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.update_customer);

        customerViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(CustomerViewModel.class);

        initIsLoading();
        getEmployee();
        initViews();
    }

    private void initViews() {
        customerModel = getIntent().getParcelableExtra(CustomersActivity.EXTRA_CUSTOMER);

        if (customerModel != null) {
            edtCustomers.get(0).setText(customerModel.getName());
            edtCustomers.get(1).setText(customerModel.getAddress());
            edtCustomers.get(2).setText(customerModel.getPhoneNumber());

            initDatePicker();

            birthDate = customerModel.getDateBirth();
            String[] dateTmp = birthDate.split("-", -1);
            datePickerDialog.updateDate(Integer.parseInt(dateTmp[0]), Integer.parseInt(dateTmp[1])-1, Integer.parseInt(dateTmp[2]));
            tvBirthDate.setText(Util.dateFormater(birthDate));
        }
    }

    private void initDatePicker() {
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, (datePicker, i, i1, i2) -> {
            Calendar bDayCalendar = Calendar.getInstance();
            bDayCalendar.set(i, i1, i2);
            birthDate = Util.stdDateFormater(bDayCalendar.getTime());
            tvBirthDate.setText(Util.dateFormater(birthDate));
        },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

    }

    @OnClick(R.id.btn_date_picker)
    public void onClickDatePicker(View view) {
        datePickerDialog.show();
    }

    @OnClick(R.id.btn_update)
    public void updatePet(View view) {
        String customerName = edtCustomers.get(0).getText().toString().trim();
        String customerAddress = edtCustomers.get(1).getText().toString().trim();
        String customerPhoneNumber = edtCustomers.get(2).getText().toString().trim();

        if (customerName.isEmpty() || tvBirthDate.getText().toString().trim().isEmpty() || customerAddress.isEmpty() || customerPhoneNumber.isEmpty()) {
            Toast.makeText(this, R.string.all_column_must_be_filled, Toast.LENGTH_SHORT).show();
            return;
        }

        customerModel.setName(customerName);
        customerModel.setAddress(customerAddress);
        customerModel.setPhoneNumber(customerPhoneNumber);
        customerModel.setDateBirth(birthDate);

        customerModel.setUpdatedBy(employee.getId());

        customerViewModel.update(employee.getToken(), customerModel);

        Toast.makeText(this, R.string.customer_updated, Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK);
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
        customerViewModel.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        customerViewModel.getEmployeeIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });
    }

    public void getEmployee() {
        customerViewModel.getEmployee().observe(this, employeeDataModel -> employee = employeeDataModel);
    }
}
