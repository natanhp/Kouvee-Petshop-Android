package com.p3lj2.koveepetshop.view.log;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.p3lj2.koveepetshop.R;

public class LogActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_activity);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.logFragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.log_nav_host);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, LogFragment.newInstance())
//                    .commitNow();
//        }
    }
}
