package com.bookingapp.fragments.account;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookingapp.R;
import com.bookingapp.databinding.FragmentAdminOwnerReviewPageBinding;
import com.bookingapp.fragments.FragmentTransition;
import com.bookingapp.fragments.accommodation.AdminAccommodationReviewListFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminOwnerReviewPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminOwnerReviewPageFragment extends Fragment {

    private FragmentAdminOwnerReviewPageBinding binding;

    public AdminOwnerReviewPageFragment() {
        // Required empty public constructor
    }


    public static AdminOwnerReviewPageFragment newInstance() {
        AdminOwnerReviewPageFragment fragment = new AdminOwnerReviewPageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            Log.i("Booking App", "OnCreateView Admin Owner Review Page Fragment");
            binding = FragmentAdminOwnerReviewPageBinding.inflate(inflater, container, false);
            View root = binding.getRoot();
            return root;
    }
    @Override
    public void onResume(){
        super.onResume();
        FragmentTransition.to(AdminOwnerReviewListFragment.newInstance(), getActivity(), false, R.id.scroll_admin_owner_review_list);
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }

}