package com.p3lj2.koveepetshop.view.product.guest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.adapter.ProductAdapter;
import com.p3lj2.koveepetshop.model.ProductResponseModel;
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.viewmodel.ProductViewModel;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductListActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private ProductAdapter productAdapter;
    private ProductViewModel productViewModel;
    private static int checkedButton = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        ButterKnife.bind(this);

        productViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(ProductViewModel.class);
        loadingStateHandler();
        initRecyclerView();
    }

    private void initRecyclerView() {
        productAdapter = new ProductAdapter(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(productAdapter);
        getProductData();
    }


    private void loadingStateHandler() {
        productViewModel.getIsLoading().observe(this, this::progressBarHandler);
    }

    private void getProductData() {
        productViewModel.getAll().observe(this, productResponseModels -> productAdapter.setProductResponseModels(productResponseModels));
    }

    private void progressBarHandler(boolean status) {
        if (status) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.product_list_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sort) {
            sortingDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sortingDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.sorting_layout, this.findViewById(android.R.id.content), false);
        RadioGroup rgSort = view.findViewById(R.id.rg_sort);
        if (checkedButton == 0) {
            checkedButton = R.id.rb_lower_price;
        }

        RadioButton radioButton = view.findViewById(checkedButton);
        radioButton.setChecked(true);

        rgSort.setOnCheckedChangeListener((radioGroup, i) -> checkedButton = i);
        Util.confirmationDialog(getString(R.string.sort_product), "", this)
                .setView(view)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    switch (checkedButton) {
                        case R.id.rb_lower_price:
                            sortBy("asc_price");
                            break;
                        case R.id.rb_higher_price:
                            sortBy("dsc_price");
                            break;
                        case R.id.rb_least_stock:
                            sortBy("asc_stock");
                            break;
                        case R.id.rb_more_stock:
                            sortBy("dsc_stock");
                            break;
                        default:
                            Toast.makeText(this, getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.cancel())
                .show();
    }

    private void sortBy(String order) {
        List<ProductResponseModel> productResponseModels = productAdapter.getProductResponseModels();

        Collections.sort(productResponseModels, (productResponseModel, t1) -> {
            double priceLeft = productResponseModel.getProductModel().getProductPrice();
            double priceRight = t1.getProductModel().getProductPrice();

            if (order.equals("asc_price")) {
                return Double.compare(priceLeft, priceRight);
            } else if (order.equals("dsc_price")) {
                return Double.compare(priceRight, priceLeft);
            }

            return 0;
        });

        Collections.sort(productResponseModels, (productResponseModel, t1) -> {
            int stockLeft = productResponseModel.getProductModel().getProductQuantity();
            int stockRight = t1.getProductModel().getProductQuantity();

            if (order.equals("asc_stock")) {
                return Integer.compare(stockLeft, stockRight);
            } else if (order.equals("dsc_stock")) {
                return Integer.compare(stockRight, stockLeft);
            }

            return 0;
        });

        productAdapter.setProductResponseModels(productResponseModels);
    }
}
