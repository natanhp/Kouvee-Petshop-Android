package com.p3lj2.koveepetshop.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.model.EmployeeDataModel;
import com.p3lj2.koveepetshop.model.ProductModel;
import com.p3lj2.koveepetshop.model.ProductResponseModel;
import com.p3lj2.koveepetshop.viewmodel.ProductViewModel;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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

        checkReadExternalFilePermission();

        productViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ProductViewModel.class);
    }

    @OnClick(R.id.btn_file)
    public void pickImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri image = null;
            if (data != null) {
                image = data.getData();
            }
            String imagePath = getUriPath(image);
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
        int productQuantity = Integer.valueOf(productForm.get(1).getText().toString().trim());
        double productPrice = Double.valueOf(productForm.get(2).getText().toString().trim());
        int minimumQuantity = Integer.valueOf(productForm.get(3).getText().toString().trim());
        String productMeasurement = productForm.get(4).getText().toString().trim();
        final String[] bearerToken = {""};

        if (!productName.isEmpty() && !productMeasurement.isEmpty()) {
            productModel.setProductName(productName);
            productModel.setProductQuantity(productQuantity);
            productModel.setProductPrice(productPrice);
            productModel.setMinimumQty(minimumQuantity);
            productModel.setMeassurement(productMeasurement);

            productViewModel.getEmployee().observe(this, employeeDataModel -> {
                if (employeeDataModel != null) {
                    productModel.setCreatedBy(employeeDataModel.getId());
                    bearerToken[0] = employeeDataModel.getToken();
                }
            });
        }

        productResponseModel.setProductModel(productModel);
        productViewModel.insert(bearerToken[0], productResponseModel);

        finish();
    }

    private String getUriPath(Uri uri) {
        String wholeID = DocumentsContract.getDocumentId(uri);

        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);

        String filePath = "";

        int columnIndex = 0;

        if (cursor != null) {
            columnIndex = cursor.getColumnIndex(column[0]);
        }

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();

        return filePath;
    }

    private void checkReadExternalFilePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
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
