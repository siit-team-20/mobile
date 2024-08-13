package com.bookingapp.fragments.reservation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ReservationsPageViewModel extends ViewModel {
    private final MutableLiveData<String> searchText;
    private final MutableLiveData<LocalDate> startDate;
    private final MutableLiveData<LocalDate> endDate;
    private final Set<String> selectedStatuses;
    private final MutableLiveData<Set<String>> selectedStatusesLiveData;

    public ReservationsPageViewModel() {
        searchText = new MutableLiveData<>();
        startDate = new MutableLiveData<>();
        endDate = new MutableLiveData<>();
        selectedStatuses = new HashSet<>();
        selectedStatusesLiveData = new MutableLiveData<>();
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate.setValue(startDate);
    }
    public LiveData<LocalDate> getStartDate() {
        return startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate.setValue(endDate);
    }
    public LiveData<LocalDate> getEndDate() {
        return endDate;
    }

    public void setSearchText(String searchText) {
        this.searchText.setValue(searchText);
    }
    public LiveData<String> getSearchText() {
        return searchText;
    }

    public void addSelectedStatus(String selectedType) {
        selectedStatuses.add(selectedType);
        selectedStatusesLiveData.setValue(selectedStatuses);
    }
    public void removeSelectedStatus(String selectedType) {
        selectedStatuses.remove(selectedType);
        selectedStatusesLiveData.setValue(selectedStatuses);
    }
    public LiveData<Set<String>> getSelectedStatuses() {
        return selectedStatusesLiveData;
    }
}