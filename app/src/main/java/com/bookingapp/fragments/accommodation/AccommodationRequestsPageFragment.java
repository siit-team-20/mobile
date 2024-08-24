package com.bookingapp.fragments.accommodation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookingapp.R;
import com.bookingapp.databinding.FragmentAccommodationRequestsPageBinding;
import com.bookingapp.databinding.FragmentNotificationsPageBinding;
import com.bookingapp.fragments.FragmentTransition;


public class AccommodationRequestsPageFragment extends Fragment {
    private FragmentAccommodationRequestsPageBinding binding;
    private String mParam1;
    private String mParam2;


    public static AccommodationRequestsPageFragment newInstance() {
        AccommodationRequestsPageFragment fragment = new AccommodationRequestsPageFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("BookingApp", "OnCreateView Accommodation Request List Fragment");
        binding = FragmentAccommodationRequestsPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
    @Override
    public void onResume(){
        super.onResume();
        FragmentTransition.to(AccommodationRequestListFragment.newInstance(), getActivity(), false, R.id.scroll_accommodation_requests_list);
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}