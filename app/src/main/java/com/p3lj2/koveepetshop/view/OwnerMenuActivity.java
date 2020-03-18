package com.p3lj2.koveepetshop.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.p3lj2.koveepetshop.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class OwnerMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_menu);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_product)
    public void productOnClick(View view) {
        startActivity(new Intent(this, ProductActivity.class));
    }

    @OnClick(R.id.btn_pet_type)
    public void petTypeOnClick(View view) {
        startActivity(new Intent(this, PetTypeActivity.class));
    }

    @OnClick(R.id.btn_pet_size)
    public void petSizeOnClick(View view) {
        startActivity(new Intent(this, PetSizeActivity.class));
    }

    @OnClick(R.id.btn_supplier)
    public void supplierOnClick(View view) {
        startActivity(new Intent(this, SupplierActivity.class));
    }

    @OnClick(R.id.btn_service)
    public void serviceOnClick(View view) {
        startActivity(new Intent(this, ServiceActivity.class));
    }
}
