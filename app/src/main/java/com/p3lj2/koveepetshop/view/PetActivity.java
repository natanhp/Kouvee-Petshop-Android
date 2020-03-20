package com.p3lj2.koveepetshop.view;

import android.app.Activity;
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
import com.p3lj2.koveepetshop.adapter.PetAdapter;
import com.p3lj2.koveepetshop.model.EmployeeDataModel;
import com.p3lj2.koveepetshop.util.EventClickListener;
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.viewmodel.PetViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.search_view)
    SearchView searchView;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    private PetViewModel petViewModel;
    private PetAdapter petAdapter;
    private EmployeeDataModel employee;
    static final String EXTRA_PET = "com.p3lj2.koveepetshop.view.EXTRA_PET";
    private static final int UPDATE_REQUEST = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);

        ButterKnife.bind(this);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.pet);

        petViewModel = new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(this).getApplication()).create(PetViewModel.class);

        createPet();

        petViewModel.getIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        petViewModel.getEmployeeIsLoading().observe(this, aBoolean -> {
            if (aBoolean != null) {
                handleProgressBar(aBoolean);
            }
        });

        setUpRecyclerView();
        deleteOnSwipe();
//        searchViewHandler();
    }

    private void createPet() {
        floatingActionButton.setOnClickListener(view -> startActivityForResult(new Intent(this, InsertPetActivity.class), UPDATE_REQUEST));
    }

    private void setUpRecyclerView() {
        petAdapter = new PetAdapter(itemUpdateListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(petAdapter);

        getAllPets();
    }

    private void getAllPets() {
        petViewModel.getEmployee().observe(this, employeeDataModel -> {
            employee = employeeDataModel;

            petViewModel.getAll(employee.getToken()).observe(this, petCompletes -> petAdapter.setPetCompletes(petCompletes));
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
                Util.confirmationDialog(getString(R.string.pet_deletion), getString(R.string.delete_pet_confirmation), PetActivity.this)
                        .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                            getEmployee();
                            petViewModel.delete(employee.getToken(),
                                    petAdapter.getPetCompletes().get(viewHolder.getAdapterPosition()).getPet().getId(),
                                    employee.getId());

                            petAdapter.delete(viewHolder.getAdapterPosition());
                            Toast.makeText(PetActivity.this, R.string.pet_deleted, Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(getString(R.string.no), (dialogInterface, i) -> petAdapter.notifyItemChanged(viewHolder.getAdapterPosition()))
                        .show();
            }
        })
                .attachToRecyclerView(recyclerView);
    }

    private EventClickListener itemUpdateListener = position -> {
        Intent intent = new Intent(this, UpdatePetActivity.class);
        intent.putExtra(EXTRA_PET, petAdapter.getPetCompletes().get(position));
        startActivityForResult(intent, UPDATE_REQUEST);
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_REQUEST && resultCode == Activity.RESULT_OK) {
            petViewModel.getEmployee().observe(this, employeeDataModel -> petViewModel.getAll(employeeDataModel.getToken()));
        }
    }

    private void searchViewHandler() {
        searchView.setEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                petViewModel.search(employee.getToken(), query).observe(PetActivity.this, petCompletes -> petAdapter.setPetCompletes(petCompletes));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(() -> {
            getEmployee();
            petViewModel.getAll(employee.getToken());
            return false;
        });
    }

    private void getEmployee() {
        if (employee == null) {
            petViewModel.getEmployee().observe(this, employeeDataModel -> {
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
