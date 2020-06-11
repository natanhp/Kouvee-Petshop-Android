package com.p3lj2.koveepetshop.view.log;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.p3lj2.koveepetshop.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogFragment extends Fragment {


    public static LogFragment newInstance() {
        return new LogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.log_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.btn_product)
    public void btnProductOnClick(View view) {
        NavDirections navDirections = LogFragmentDirections.actionLogFragmentToProductLogFragment();
        Navigation.findNavController(requireView()).navigate(navDirections);
    }

    @OnClick(R.id.btn_supplier)
    public void btnSupplierClick(View view) {
        NavDirections navDirections = LogFragmentDirections.actionLogFragmentToSupplierLogFragment();
        Navigation.findNavController(requireView()).navigate(navDirections);
    }

    @OnClick(R.id.btn_employee)
    public void btnEmployeeClick(View view) {
        NavDirections navDirections = LogFragmentDirections.actionLogFragmentToEmployeeLogFragment();
        Navigation.findNavController(requireView()).navigate(navDirections);
    }
}
