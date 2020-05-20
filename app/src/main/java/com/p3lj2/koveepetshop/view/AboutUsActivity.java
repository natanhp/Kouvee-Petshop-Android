package com.p3lj2.koveepetshop.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.p3lj2.koveepetshop.R;

import java.util.Objects;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Objects.requireNonNull(getSupportActionBar()).hide();

    }
}
