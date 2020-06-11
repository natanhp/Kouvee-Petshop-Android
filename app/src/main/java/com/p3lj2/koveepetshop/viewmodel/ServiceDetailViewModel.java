package com.p3lj2.koveepetshop.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.p3lj2.koveepetshop.model.CustomerModel;
import com.p3lj2.koveepetshop.model.EmployeeModel;
import com.p3lj2.koveepetshop.model.PetSizeModel;
import com.p3lj2.koveepetshop.model.PetTypeModel;
import com.p3lj2.koveepetshop.model.ServiceDetailComplete;
import com.p3lj2.koveepetshop.model.ServiceDetailModel;
import com.p3lj2.koveepetshop.model.ServiceModel;
import com.p3lj2.koveepetshop.model.ServiceTransactionModel;
import com.p3lj2.koveepetshop.repository.CustomerRepository;
import com.p3lj2.koveepetshop.repository.EmployeeRepository;
import com.p3lj2.koveepetshop.repository.PetSizeRepository;
import com.p3lj2.koveepetshop.repository.PetTypeRepository;
import com.p3lj2.koveepetshop.repository.ServiceDetailRepository;
import com.p3lj2.koveepetshop.repository.ServiceRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServiceDetailViewModel extends AndroidViewModel {
    private ServiceDetailRepository serviceDetailRepository;
    private EmployeeRepository employeeRepository;
    private ServiceRepository serviceRepository;
    private PetTypeRepository petTypeRepository;
    private PetSizeRepository petSizeRepository;
    private CustomerRepository customerRepository;
    private static MutableLiveData<Integer> selectedCustomer = new MutableLiveData<>();
    private static MutableLiveData<Integer> selectedPet = new MutableLiveData<>();
    private static MutableLiveData<List<ServiceDetailModel>> serviceDetails = new MutableLiveData<>();
    private static MutableLiveData<HashMap<Integer, Integer>> viewPositions = new MutableLiveData<>();
    private static MutableLiveData<List<ServiceDetailModel>> cart = new MutableLiveData<>();
    private static MutableLiveData<String> strTotal = new MutableLiveData<>();

    public ServiceDetailViewModel(@NonNull Application application) {
        super(application);

        serviceDetailRepository = new ServiceDetailRepository();
        employeeRepository = new EmployeeRepository(application);
        serviceRepository = new ServiceRepository();
        petTypeRepository = new PetTypeRepository();
        petSizeRepository = new PetSizeRepository();
        customerRepository = new CustomerRepository();
    }

    public LiveData<List<ServiceDetailComplete>> getAll() {
        return serviceDetailRepository.getAll();
    }

    public void insert(String bearerToken, ServiceDetailModel serviceDetailModel) {
        serviceDetailRepository.insert(bearerToken, serviceDetailModel);
    }

    public void update(String bearerToken, ServiceDetailModel serviceDetailModel) {
        serviceDetailRepository.update(bearerToken, serviceDetailModel);
    }

    public void delete(String bearerToken, int serviceDetailId) {
        serviceDetailRepository.delete(bearerToken, serviceDetailId);
    }

    public void insertTransaction(String bearerToken, ServiceTransactionModel serviceTransactionModel) {
        serviceDetailRepository.insertTransaction(bearerToken, serviceTransactionModel);
    }

    public LiveData<Boolean> getIsLoading() {
        return serviceDetailRepository.getIsLoading();
    }

    public LiveData<Object[]> getIsSuccess() {
        return serviceDetailRepository.getIsSuccess();
    }

    public void setSelectedCustomer(int id) {
        selectedCustomer.setValue(id);
    }

    public LiveData<Integer> getSelectedCustomer() {
        return selectedCustomer;
    }

    public void setSelectedPet(int id) {
        selectedPet.setValue(id);
    }

    public LiveData<Integer> getSelectedPet() {
        return selectedPet;
    }

    public void setServiceDetails(List<ServiceDetailModel> data) {
        serviceDetails.setValue(data);
    }

    public LiveData<List<ServiceDetailModel>> getSelectedServiceDetails() {
        return serviceDetails;
    }

    public void setViewPositions(int serviceId, int position) {
        HashMap<Integer, Integer> viewPositionsTmp = new HashMap<>();
        if (viewPositions.getValue() != null) {
            viewPositionsTmp.putAll(viewPositions.getValue());
        }
        viewPositionsTmp.put(serviceId, position);
        viewPositions.postValue(viewPositionsTmp);
    }

    public LiveData<HashMap<Integer, Integer>> getViewPositions() {
        return viewPositions;
    }

    public void setCart(ServiceDetailModel serviceDetailModel) {
        List<ServiceDetailModel> cartTmp = new ArrayList<>();
        if (cart.getValue() != null) {
            cartTmp.addAll(cart.getValue());
        }
        cartTmp.add(serviceDetailModel);
        cart.postValue(cartTmp);
    }

    public LiveData<List<ServiceDetailModel>> getCart() {
        return cart;
    }

    public void resetCart() {
        viewPositions = new MutableLiveData<>();
        cart = new MutableLiveData<>();
        serviceDetails = new MutableLiveData<>();
    }

    public void resetAll() {
        viewPositions = new MutableLiveData<>();
        cart = new MutableLiveData<>();
        serviceDetails = new MutableLiveData<>();
        selectedCustomer = new MutableLiveData<>();
        selectedPet = new MutableLiveData<>();
        strTotal = new MutableLiveData<>();
    }

    public void setStrTotal(String total) {
        strTotal.setValue(total);
    }

    public LiveData<String> getStrTotal() {
        return strTotal;
    }

    public LiveData<EmployeeModel> getEmployee() {
        return employeeRepository.getEmployee();
    }

    public LiveData<Boolean> getEmployeeIsLoading() {
        return employeeRepository.getIsLoading();
    }

    public LiveData<List<ServiceModel>> getAllServices(String bearerToken) {
        return serviceRepository.getAll(bearerToken);
    }

    public LiveData<Boolean> getServiceIsLoading() {
        return serviceRepository.getIsLoading();
    }

    public LiveData<List<PetTypeModel>> getAllPetTypes(String bearerToken) {
        return petTypeRepository.getAll(bearerToken);
    }

    public LiveData<Boolean> getPetTypeIsLoading() {
        return petTypeRepository.getIsLoading();
    }

    public LiveData<List<PetSizeModel>> getAllPetSizes(String bearerToken) {
        return petSizeRepository.getAll(bearerToken);
    }

    public LiveData<Boolean> getPetSizeIsLoading() {
        return petSizeRepository.getIsLoading();
    }

    public LiveData<List<CustomerModel>> getAllCustomer(String bearerToken) {
        return customerRepository.getAll(bearerToken);
    }

    public LiveData<Boolean> getCustomerIsLoading() {
        return customerRepository.getIsLoading();
    }
}
