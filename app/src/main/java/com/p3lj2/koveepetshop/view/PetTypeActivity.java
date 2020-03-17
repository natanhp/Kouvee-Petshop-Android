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
import com.p3lj2.koveepetshop.adapter.PetTypeAdapter;
import com.p3lj2.koveepetshop.model.EmployeeDataModel;
import com.p3lj2.koveepetshop.util.EventClickListener;
import com.p3lj2.koveepetshop.viewmodel.PetTypeViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetTypeActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.search_view)
    SearchView searchView;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    private PetTypeViewModel petTypeViewModel;
    private PetTypeAdapter petTypeAdapter;
    private EmployeeDataModel employee;
    static final String EXTRA_PET_TYPE = "com.p3lj2.koveepetshop.view.EXTRA_PET_TYPE";
    private static final int UPDATE_REQUEST = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_type);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.pet_type);

        petTypeViewModel = new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(this).getApplication()).create(PetTypeViewModel.class);

        createType();

        petTypeViewModel.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        petTypeViewModel.getIsLoadingEmployee().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        setUpRecyclerView();
        deleteOnSwipe();
        searchViewHandler();
    }

    private void createType() {
        floatingActionButton.setOnClickListener(view -> startActivity(new Intent(this, InsertPetTypeActivity.class)));
    }

    private void setUpRecyclerView() {
        petTypeAdapter = new PetTypeAdapter(itemUpdateListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(petTypeAdapter);

        getAllTypes();
    }

    private void getAllTypes() {
        petTypeViewModel.getEmployee().observe(this, employeeDataModel -> {
            employee = employeeDataModel;

            petTypeViewModel.getAll(employee.getToken()).observe(PetTypeActivity.this, petTypeModels -> petTypeAdapter.setPetTypeModels(petTypeModels));
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
                confirmationDialog(getString(R.string.pet_type_deletion), getString(R.string.pet_type_delete_confirmation))
                        .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                            getEmployee();
                            petTypeViewModel.delete(employee.getToken(),
                                    petTypeAdapter.getPetTypeModels().get(viewHolder.getAdapterPosition()).getId(),
                                    employee.getId());

                            petTypeAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                            Toast.makeText(PetTypeActivity.this, R.string.pet_type_deleted, Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(getString(R.string.no), (dialogInterface, i) -> petTypeAdapter.notifyItemChanged(viewHolder.getAdapterPosition()))
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
        Intent intent = new Intent(PetTypeActivity.this, UpdatePetTypeActivity.class);
            intent.putExtra(EXTRA_PET_TYPE, petTypeAdapter.getPetTypeModels().get(position));
            startActivityForResult(intent, UPDATE_REQUEST);
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_REQUEST && resultCode == Activity.RESULT_OK) {
            petTypeViewModel.getEmployee().observe(this, employeeDataModel -> petTypeViewModel.getAll(employeeDataModel.getToken()));
        }
    }

    private void searchViewHandler() {
        searchView.setEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                petTypeViewModel.search(employee.getToken(), query).observe(PetTypeActivity.this, petTypeModels -> petTypeAdapter.setPetTypeModels(petTypeModels));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(() -> {
            getEmployee();
            petTypeViewModel.getAll(employee.getToken());
            return false;
        });
    }

    private void getEmployee() {
        if (employee == null) {
            petTypeViewModel.getEmployee().observe(PetTypeActivity.this, employeeDataModel -> {
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
