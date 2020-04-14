package com.p3lj2.koveepetshop.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.ProductModel;
import com.p3lj2.koveepetshop.model.ProductResponseModel;
import com.p3lj2.koveepetshop.util.FilePath;
import com.p3lj2.koveepetshop.viewmodel.ProductViewModel;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InsertProductActivity extends AppCompatActivity {

    @BindViews({R.id.edt_product_name, R.id.edt_product_quantity, R.id.edt_product_price, R.id.edt_minimum_qty, R.id.edt_product_measurement})
    List<EditText> productForm;

    @BindView(R.id.tv_image_path)
    TextView tvImagePath;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    public static final int PICK_IMAGE = 1;
    public static final int MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 2;
    private ProductModel productModel = new ProductModel();
    private ProductResponseModel productResponseModel = new ProductResponseModel();
    private ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_product);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.insert_product);

        checkReadExternalFilePermission();

        productViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ProductViewModel.class);
    }

    @OnClick(R.id.btn_file)
    public void pickImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri image = null;
            if (data != null) {
                image = data.getData();
            }

            String imagePath = FilePath.getPath(this, image);
            tvImagePath.setText(imagePath);
            productResponseModel.setImageUrl(imagePath);
        }
    }

    @OnClick(R.id.btn_insert)
    public void insertProduct(View view) {

        productViewModel.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        productViewModel.getEmployeeLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        String productName = productForm.get(0).getText().toString().trim();
        String strProductQuantity = productForm.get(1).getText().toString().trim();
        String strProductPrice = productForm.get(2).getText().toString().trim();
        String strMinimumQuantity = productForm.get(3).getText().toString().trim();
        String productMeasurement = productForm.get(4).getText().toString().trim();
        final String[] bearerToken = {""};

        if (!productName.isEmpty() && !productMeasurement.isEmpty() && !strProductQuantity.isEmpty() && !strProductPrice.isEmpty() &&
                !strMinimumQuantity.isEmpty()) {
            int productQuantity = Integer.parseInt(strProductQuantity);
            double productPrice = Double.parseDouble(strProductPrice);
            int minimumQuantity = Integer.parseInt(strMinimumQuantity);

            productModel.setProductName(productName);
            productModel.setProductQuantity(productQuantity);
            productModel.setProductPrice(productPrice);
            productModel.setMinimumQty(minimumQuantity);
            productModel.setMeassurement(productMeasurement);
            productResponseModel.setProductModel(productModel);

            productViewModel.getEmployee().observe(this, employeeDataModel -> {
                if (employeeDataModel != null) {
                    productModel.setCreatedBy(employeeDataModel.getId());
                    bearerToken[0] = employeeDataModel.getToken();
                }
            });

            do {
                if (!bearerToken[0].isEmpty()) {
                    productViewModel.insert(bearerToken[0], productResponseModel);
                }
            } while (bearerToken[0].isEmpty());

            Toast.makeText(this, R.string.product_created, Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, R.string.all_column_must_be_filled, Toast.LENGTH_SHORT).show();
        }

    }

    private void checkReadExternalFilePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }
    }

    private void handleProgressBar(boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
