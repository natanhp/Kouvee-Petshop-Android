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
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.viewmodel.ProductViewModel;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateProductActivity extends AppCompatActivity {

    @BindViews({R.id.edt_product_name, R.id.edt_product_quantity, R.id.edt_product_price, R.id.edt_minimum_qty,
            R.id.edt_product_measurement})
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
    private ProductResponseModel tmpProductResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.edit_product);

        checkReadExternalFilePermission();

        productViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ProductViewModel.class);

        initViews();
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
            String imagePath = Util.getUriPath(image, this);
            tvImagePath.setText(imagePath);
            productResponseModel.setImageUrl(imagePath);
        }
    }

    @OnClick(R.id.btn_update)
    public void updateProduct(View view) {

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
        int productQuantity = Integer.parseInt(strProductQuantity);
        String strProductPrice = productForm.get(2).getText().toString().trim();
        double productPrice = Double.parseDouble(strProductPrice);
        String strMinimumQuantity = productForm.get(3).getText().toString().trim();
        int minimumQuantity = Integer.parseInt(strMinimumQuantity);
        String productMeasurement = productForm.get(4).getText().toString().trim();
        final String[] bearerToken = {""};

        if (!productName.isEmpty() && !productMeasurement.isEmpty() && !strProductQuantity.isEmpty() && !strProductPrice.isEmpty() &&
                !strMinimumQuantity.isEmpty()) {
            productModel.setProductName(productName);
            productModel.setProductQuantity(productQuantity);
            productModel.setProductPrice(productPrice);
            productModel.setMinimumQty(minimumQuantity);
            productModel.setMeassurement(productMeasurement);
            productModel.setId(tmpProductResponse.getProductModel().getId());

            productViewModel.getEmployee().observe(this, employeeDataModel -> {
                if (employeeDataModel != null) {
                    productModel.setUpdatedBy(employeeDataModel.getId());
                    bearerToken[0] = employeeDataModel.getToken();
                }
            });

            productResponseModel.setProductModel(productModel);
            productViewModel.update(bearerToken[0], productResponseModel);

            Toast.makeText(this, R.string.product_updated, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.all_column_must_be_filled, Toast.LENGTH_SHORT).show();
        }

        setResult(Activity.RESULT_OK, new Intent());

        finish();
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

    private void initViews() {
        tmpProductResponse = getIntent().getParcelableExtra(ProductActivity.EXTRA_PRODUCT);

        if (tmpProductResponse != null) {
            productForm.get(0).setText(tmpProductResponse.getProductModel().getProductName());
            productForm.get(1).setText(String.valueOf(tmpProductResponse.getProductModel().getProductQuantity()));
            productForm.get(2).setText(String.valueOf(tmpProductResponse.getProductModel().getProductPrice()));
            productForm.get(3).setText(String.valueOf(tmpProductResponse.getProductModel().getMinimumQty()));
            productForm.get(4).setText(tmpProductResponse.getProductModel().getMeassurement());
        }
    }
}
