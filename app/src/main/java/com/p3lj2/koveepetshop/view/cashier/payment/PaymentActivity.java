package com.p3lj2.koveepetshop.view.cashier.payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.view.LoginActivity;
import com.p3lj2.koveepetshop.viewmodel.EmployeeViewModel;

public class PaymentActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private EmployeeViewModel employeeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_product_transaction_list)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        employeeViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EmployeeViewModel.class);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            Util.confirmationDialog(getString(R.string.logout), getString(R.string.logout_confirmation), this)
                    .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> employeeViewModel.getEmployee().observe(this, employeeDataModel -> {
                        employeeViewModel.delete(employeeDataModel);
                        startActivity(new Intent(PaymentActivity.this, LoginActivity.class));
                        finish();
                    }))
                    .setNegativeButton(getString(R.string.no), null)
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
