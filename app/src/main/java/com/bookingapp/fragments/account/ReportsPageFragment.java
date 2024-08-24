package com.bookingapp.fragments.account;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookingapp.R;
import com.bookingapp.databinding.FragmentAccommodationRequestsPageBinding;
import com.bookingapp.databinding.FragmentReportsPageBinding;
import com.bookingapp.fragments.FragmentTransition;
import com.bookingapp.fragments.accommodation.AccommodationRequestListFragment;
import com.bookingapp.fragments.accommodation.AccommodationRequestsPageFragment;

public class ReportsPageFragment extends Fragment {

    private FragmentReportsPageBinding binding;

    public static ReportsPageFragment newInstance() {
        ReportsPageFragment fragment = new ReportsPageFragment();

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
        Log.i("BookingApp", "OnCreateView Report Page Fragment");
        binding = FragmentReportsPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
    @Override
    public void onResume(){
        super.onResume();
        FragmentTransition.to(ReportListFragment.newInstance(), getActivity(), false, R.id.scroll_reports_list);
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}