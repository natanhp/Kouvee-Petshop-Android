package com.p3lj2.koveepetshop.view.service.transaction;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.p3lj2.koveepetshop.R;
import com.p3lj2.koveepetshop.adapter.ServiceListAdapterNew;
import com.p3lj2.koveepetshop.model.CustomerModel;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.model.PetModel;
import com.p3lj2.koveepetshop.model.ServiceDetailModel;
import com.p3lj2.koveepetshop.util.EventClickListener;
import com.p3lj2.koveepetshop.util.Util;
import com.p3lj2.koveepetshop.viewmodel.ServiceDetailViewModel;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class ServiceListFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindViews({R.id.spinner_customer_name, R.id.spinner_pet})
    List<Spinner> spinners;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ServiceDetailViewModel serviceDetailViewModel;
    private EmployeeModel employee;
    private List<CustomerModel> customerModels = new ArrayList<>();
    private List<PetModel> petModels = new ArrayList<>();
    private BidiMap<Integer, Integer> customerMap = new DualHashBidiMap<>();
    private BidiMap<Integer, Integer> petMap = new DualHashBidiMap<>();
    private ServiceListAdapterNew serviceListAdapterNew;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_service_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        serviceDetailViewModel = new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication()).create(ServiceDetailViewModel.class);
        loadingStateHandler();
        getEmployeeData();
        initRecyclerView();
        initSpinner();
    }

    private void initRecyclerView() {
        serviceListAdapterNew = new ServiceListAdapterNew(eventClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(serviceListAdapterNew);

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                disableClickViewHandler();
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void disableClickViewHandler() {
//        Don't change the anonymous function to lambda expression
        serviceDetailViewModel.getViewPositions().observe(getViewLifecycleOwner(), new Observer<HashMap<Integer, Integer>>() {
            @Override
            public void onChanged(HashMap<Integer, Integer> positions) {
                for (int position : positions.values()) {
                    if (recyclerView.findViewHolderForAdapterPosition(position) != null) {
                        ImageButton btnAdd = Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(position)).itemView.findViewById(R.id.btn_add);
                        btnAdd.setEnabled(false);
                        Glide.with(Objects.requireNonNull(recyclerView.findViewHolderForAdapterPosition(position)).itemView)
                                .load(R.drawable.ic_add_shopping_cart_gray_24dp)
                                .into(btnAdd);
                    }
                }
            }
        });
    }

    private void initSpinner() {
//        disableClickViewHandler();
    }

    private EventClickListener eventClickListener = new EventClickListener() {
        @Override
        public void onEventClick(int position, @Nullable Integer viewId) {
            serviceDetailViewModel.setCart(serviceListAdapterNew.getServiceDetailModels().get(position));
            serviceDetailViewModel.setViewPositions(serviceListAdapterNew.getServiceDetailModels().get(position).getId(), position);
        }
    };

    private void initCustomerSpinner() {
        if (employee != null) {
            serviceDetailViewModel.getAllCustomer(employee.getToken()).observe(getViewLifecycleOwner(), customerModels -> {
                if (customerModels != null) {
                    this.customerModels = customerModels;
                    String[] customerNames = new String[customerModels.size() + 1];
                    customerNames[0] = getString(R.string.choose_customer);
                    for (int i = 1; i < customerNames.length; i++) {
                        customerMap.put(i, customerModels.get(i - 1).getId());
                        customerNames[i] = customerModels.get(i - 1).getName();
                    }
                    ArrayAdapter<String> petSizeAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, customerNames) {
                        @Override
                        public boolean areAllItemsEnabled() {
                            return false;
                        }

                        @Override
                        public boolean isEnabled(int position) {
                            String customerName = customerNames[position];
                            return !customerName.equals(getString(R.string.choose_customer));
                        }

                        @Override
                        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            if (convertView == null) {
                                LayoutInflater layoutInflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                convertView = Objects.requireNonNull(layoutInflater).inflate(android.R.layout.simple_spinner_dropdown_item, null);
                            }
                            String supplierName = customerNames[position];
                            TextView tvSpinnerTextList = convertView.findViewById(android.R.id.text1);
                            tvSpinnerTextList.setText(supplierName);
                            boolean disabled = !isEnabled(position);
                            if (disabled) {
                                tvSpinnerTextList.setTextColor(Color.GRAY);
                            } else {
                                tvSpinnerTextList.setTextColor(Color.BLACK);
                            }

                            return convertView;
                        }
                    };
                    petSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinners.get(0).setAdapter(petSizeAdapter);
                    spinners.get(0).setOnItemSelectedListener(this);
                    serviceDetailViewModel.getSelectedCustomer().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            if (integer != null) {
                                spinners.get(0).setSelection(Util.getKey(customerMap, integer), false);
                            }
                        }
                    });
                }
            });
        }
    }

    private void initPetSpinner(List<PetModel> petModels) {
        if (petModels != null) {
            this.petModels = petModels;
            String[] petNames = new String[petModels.size() + 1];
            petNames[0] = getString(R.string.choose_pet);
            petMap.put(1, -1);
            for (int i = 1; i < petNames.length; i++) {
                petMap.put(i, petModels.get(i - 1).getId());
                petNames[i] = petModels.get(i - 1).getName();
            }
            ArrayAdapter<String> petAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, petNames) {
                @Override
                public boolean areAllItemsEnabled() {
                    return false;
                }

                @Override
                public boolean isEnabled(int position) {
                    String petName = petNames[position];
                    return !petName.equals(getString(R.string.choose_pet));
                }

                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    if (convertView == null) {
                        LayoutInflater layoutInflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = Objects.requireNonNull(layoutInflater).inflate(android.R.layout.simple_spinner_dropdown_item, null);
                    }
                    String petName = petNames[position];
                    TextView tvSpinnerTextList = convertView.findViewById(android.R.id.text1);
                    tvSpinnerTextList.setText(petName);
                    boolean disabled = !isEnabled(position);
                    if (disabled) {
                        tvSpinnerTextList.setTextColor(Color.GRAY);
                    } else {
                        tvSpinnerTextList.setTextColor(Color.BLACK);
                    }

                    return convertView;
                }
            };
            petAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinners.get(1).setAdapter(petAdapter);
            spinners.get(1).setOnItemSelectedListener(this);
            serviceDetailViewModel.getSelectedPet().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    if (integer != null) {
                        try {
                            spinners.get(1).setSelection(Util.getKey(petMap, integer), false);
                        } catch (NullPointerException e) {
                            spinners.get(1).setSelection(0, false);
                            serviceDetailViewModel.resetCart();
                        }
                    }
                }
            });
        }
    }

    private void getEmployeeData() {
        serviceDetailViewModel.getEmployee().observe(getViewLifecycleOwner(), new Observer<EmployeeModel>() {
            @Override
            public void onChanged(EmployeeModel employeeModel) {
                if (employeeModel != null) {
                    employee = employeeModel;
                    initCustomerSpinner();
                }
            }
        });
    }

    private void loadingStateHandler() {
        serviceDetailViewModel.getEmployeeIsLoading().observe(getViewLifecycleOwner(), this::progressBarHandler);
        serviceDetailViewModel.getCustomerIsLoading().observe(getViewLifecycleOwner(), this::progressBarHandler);
    }


    private void progressBarHandler(boolean status) {
        if (status) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int viewId = adapterView.getId();

        switch (viewId) {
            case R.id.spinner_customer_name:
                if (i - 1 >= 0) {
                    serviceDetailViewModel.setSelectedCustomer(customerModels.get(i - 1).getId());
                    initPetSpinner(customerModels.get(i - 1).getPets());
                    serviceListAdapterNew.setServiceDetailCompletes(new ArrayList<>());
                }
                break;
            case R.id.spinner_pet:
                if (i - 1 >= 0) {
                    serviceDetailViewModel.setSelectedPet(petModels.get(i - 1).getId());
                    serviceDetailViewModel.setServiceDetails(petModels.get(i - 1).getServiceDetails());
                    serviceDetailViewModel.getSelectedServiceDetails().observe(getViewLifecycleOwner(), new Observer<List<ServiceDetailModel>>() {
                        @Override
                        public void onChanged(List<ServiceDetailModel> serviceDetailModels) {
                            serviceListAdapterNew.setServiceDetailCompletes(serviceDetailModels);

                            recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                @Override
                                public void onGlobalLayout() {
                                    disableClickViewHandler();
                                    recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                }
                            });
                        }
                    });
                }
                break;
            default:
                Toast.makeText(getContext(), getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
