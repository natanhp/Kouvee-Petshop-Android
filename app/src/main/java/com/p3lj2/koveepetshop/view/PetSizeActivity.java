package com.p3lj2.koveepetshop.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.adapter.PetSizeAdapter;
import com.p3lj2.koveepetshop.model.EmployeeDataModel;
import com.p3lj2.koveepetshop.util.EventClickListener;
import com.p3lj2.koveepetshop.viewmodel.PetSizeViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetSizeActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.search_view)
    SearchView searchView;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    private PetSizeViewModel petSizeViewModel;
    private PetSizeAdapter petSizeAdapter;
    private EmployeeDataModel employee;
    static final String EXTRA_PET_SIZE = "com.p3lj2.koveepetshop.view.EXTRA_PET_SIZE";
    private static final int UPDATE_REQUEST = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_size);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.pet_size);

        petSizeViewModel = new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(this).getApplication()).create(PetSizeViewModel.class);

        createSize();

        petSizeViewModel.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        petSizeViewModel.getEmployeeIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        setUpRecyclerView();
        deleteOnSwipe();
//        searchViewHandler();
    }

    private void createSize() {
        floatingActionButton.setOnClickListener(view -> startActivity(new Intent(this, InsertPetSizeActivity.class)));
    }

    private void setUpRecyclerView() {
        petSizeAdapter = new PetSizeAdapter(itemUpdateListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(petSizeAdapter);

        getAllSizes();
    }

    private void getAllSizes() {
        petSizeViewModel.getEmployee().observe(this, employeeDataModel -> {
            employee = employeeDataModel;

            petSizeViewModel.getAll(employee.getToken()).observe(PetSizeActivity.this, petSizeModels -> petSizeAdapter.setPetSizeModels(petSizeModels));
        });
    }

    private void deleteOnSwipe() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                confirmationDialog(getString(R.string.pet_size_deletion), getString(R.string.pet_size_delete_confirmation))
                        .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                            getEmployee();
                            petSizeViewModel.delete(employee.getToken(),
                                    petSizeAdapter.getPetSizeModels().get(viewHolder.getAdapterPosition()).getId(),
                                    employee.getId());

                            petSizeAdapter.delete(viewHolder.getAdapterPosition());
                            Toast.makeText(PetSizeActivity.this, R.string.pet_type_deleted, Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(getString(R.string.no), (dialogInterface, i) -> petSizeAdapter.notifyItemChanged(viewHolder.getAdapterPosition()))
                        .show();
            }
        })
                .attachToRecyclerView(recyclerView);
    }

    private AlertDialog.Builder confirmationDialog(String title, String message) {
        return new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
    }

    private EventClickListener itemUpdateListener = position -> {
        Intent intent = new Intent(PetSizeActivity.this, UpdatePetSizeActivity.class);
        intent.putExtra(EXTRA_PET_SIZE, petSizeAdapter.getPetSizeModels().get(position));
        startActivityForResult(intent, UPDATE_REQUEST);
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_REQUEST && resultCode == Activity.RESULT_OK) {
            petSizeViewModel.getEmployee().observe(this, employeeDataModel -> petSizeViewModel.getAll(employeeDataModel.getToken()));
        }
    }

//    private void searchViewHandler() {
//        searchView.setEnabled(true);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                petTypeViewModel.search(employee.getToken(), query).observe(PetTypeActivity.this, petTypeModels -> petTypeAdapter.setPetTypeModels(petTypeModels));
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//
//        searchView.setOnCloseListener(() -> {
//            getEmployee();
//            petTypeViewModel.getAll(employee.getToken());
//            return false;
//        });
//    }

    private void getEmployee() {
        if (employee == null) {
            petSizeViewModel.getEmployee().observe(PetSizeActivity.this, employeeDataModel -> {
                if (employeeDataModel != null) {
                    employee = employeeDataModel;
                }
            });
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
