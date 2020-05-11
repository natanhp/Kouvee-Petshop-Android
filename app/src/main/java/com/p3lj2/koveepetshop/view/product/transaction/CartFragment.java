package com.p3lj2.koveepetshop.view.product.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.adapter.CartAdapter;
import com.p3lj2.koveepetshop.model.ProductModel;
import com.p3lj2.koveepetshop.util.EventClickListener;
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.viewmodel.ProductTransactionViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.tv_total)
    TextView tvTotal;

    private ProductTransactionViewModel productTransactionViewModel;
    private CartAdapter cartAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        productTransactionViewModel = new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()).create(ProductTransactionViewModel.class);
        initRecyclerView();
        deleteOnSwipe();
    }

    private void initRecyclerView() {
        cartAdapter = new CartAdapter(eventClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(cartAdapter);
        getCart();
    }

    private EventClickListener eventClickListener = (position, viewId) -> updateCart(position);

    private void getCart() {
        productTransactionViewModel.getCart().observe(getViewLifecycleOwner(), new Observer<List<ProductModel>>() {
            @Override
            public void onChanged(List<ProductModel> productModels) {
                if (productModels != null) {
                    cartAdapter.setProductModels(productModels);
                    counTotal(productModels);
                }
            }
        });
    }

    private void updateCart(int position) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.product_quantity_input, requireView().findViewById(android.R.id.content), false);
        EditText inputQty = view.findViewById(R.id.edt_product_quantity);
        Util.confirmationDialog(getString(R.string.buy_product), "", getContext())
                .setView(view)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    if (inputQty.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getContext(), R.string.all_column_must_be_filled, Toast.LENGTH_SHORT).show();
                    } else {
                        productTransactionViewModel.updateCartByPosition(position, Integer.parseInt(inputQty.getText().toString().trim()));
                    }
                })
                .setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.cancel())
                .show();
    }

    private void deleteOnSwipe() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Util.confirmationDialog(getString(R.string.product_deletion), getString(R.string.product_deletion_confirmation), getContext())
                        .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                            productTransactionViewModel.deleteCartItemByPosition(viewHolder.getAdapterPosition());
                            cartAdapter.notifyDataSetChanged();
                            Toast.makeText(getContext(), R.string.product_deletion_success, Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(getString(R.string.no), (dialogInterface, i) -> cartAdapter.notifyItemChanged(viewHolder.getAdapterPosition()))
                        .show();
            }
        })
                .attachToRecyclerView(recyclerView);
    }

    private void counTotal(List<ProductModel> productModels) {
        double totalCounter = 0;

        for (ProductModel productModel : productModels) {
            totalCounter += productModel.getProductQuantity() * productModel.getProductPrice();
        }

        String total = "Rp " + totalCounter;

        tvTotal.setText(total);
    }
}
