package com.bookingapp.fragments.accommodation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AccommodationsPageViewModel extends ViewModel {
    private final MutableLiveData<String> searchText;
    private final MutableLiveData<String> selectedSort;
    private final Set<String> selectedTypes;
    private final MutableLiveData<Set<String>> selectedTypesLiveData;
    private final MutableLiveData<String> selectedPrice;
    private final Set<String> selectedBenefits;
    private final MutableLiveData<Set<String>> selectedBenefitsLiveData;

    public AccommodationsPageViewModel() {
        searchText = new MutableLiveData<>();
        selectedSort = new MutableLiveData<>();
        selectedTypes = new HashSet<>();
        selectedTypesLiveData = new MutableLiveData<>();
        selectedPrice = new MutableLiveData<>();
        selectedBenefits = new HashSet<>();
        selectedBenefitsLiveData = new MutableLiveData<>();
    }

    public void addSelectedType(String selectedType) {
        selectedTypes.add(selectedType);
        selectedTypesLiveData.setValue(selectedTypes);
    }
    public void removeSelectedType(String selectedType) {
        selectedTypes.remove(selectedType);
        selectedTypesLiveData.setValue(selectedTypes);
    }
    public LiveData<Set<String>> getSelectedTypes() {
        return selectedTypesLiveData;
    }

    public void addSelectedBenefit(String selectedBenefit) {
        selectedBenefits.add(selectedBenefit);
        selectedBenefitsLiveData.setValue(selectedBenefits);
    }
    public void removeSelectedBenefit(String selectedBenefit) {
        selectedBenefits.remove(selectedBenefit);
        selectedBenefitsLiveData.setValue(selectedBenefits);
    }
    public LiveData<Set<String>> getSelectedBenefits() {
        return selectedBenefitsLiveData;
    }

    public void setSearchText(String searchText) {
        this.searchText.setValue(searchText);
    }
    public LiveData<String> getSearchText() {
        return searchText;
    }

    public void setSelectedPrice(String selectedPrice) {
        this.selectedPrice.setValue(selectedPrice);
    }
    public LiveData<String> getSelectedPrice() {
        return selectedPrice;
    }

    public void setSelectedSort(String selectedSort) {
        this.selectedSort.setValue(selectedSort);
    }
    public LiveData<String> getSelectedSort() {
        return selectedSort;
    }

}