package com.bookingapp.fragments.accommodation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookingapp.R;
import com.bookingapp.databinding.FragmentAdminAccommodationReviewPageBinding;
import com.bookingapp.fragments.FragmentTransition;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminAccommodationReviewPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminAccommodationReviewPageFragment extends Fragment {

    private FragmentAdminAccommodationReviewPageBinding binding;

    public AdminAccommodationReviewPageFragment() {
    }


    public static AdminAccommodationReviewPageFragment newInstance(Long accommodationId) {
        AdminAccommodationReviewPageFragment fragment = new AdminAccommodationReviewPageFragment();
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
        // Inflate the layout for this fragment
        Log.i("BookingApp", "OnCreateView Admin Accommodation Review Page Fragment");
        binding = FragmentAdminAccommodationReviewPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
    @Override
    public void onResume(){
        super.onResume();
        FragmentTransition.to(AdminAccommodationReviewListFragment.newInstance(), getActivity(), false, R.id.scroll_admin_accommodation_reviews_list);
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}