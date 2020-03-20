package com.p3lj2.koveepetshop.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.p3lj2.koveepetshop.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CSMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_s_menu);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_pet)
    public void onClickPet(View view) {
        startActivity(new Intent(this, PetActivity.class));
    }
}
