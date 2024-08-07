package com.bookingapp.fragments.accommodation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccommodationsPageViewModel extends ViewModel {
    private final MutableLiveData<String> searchText;

    public AccommodationsPageViewModel() {
        searchText = new MutableLiveData<>();
        searchText.setValue("This is search help!");
    }

    public LiveData<String> getText(){
        return searchText;
    }

}