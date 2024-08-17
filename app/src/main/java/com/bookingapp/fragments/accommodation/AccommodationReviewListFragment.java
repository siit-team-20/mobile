package com.bookingapp.fragments.accommodation;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bookingapp.R;
import com.bookingapp.adapters.AccommodationReviewListAdapter;
import com.bookingapp.databinding.FragmentAccommodationReviewListBinding;
import com.bookingapp.model.AccommodationReview;

import java.util.ArrayList;

public class AccommodationReviewListFragment extends ListFragment {
    private AccommodationReviewListAdapter adapter;
    private FragmentAccommodationReviewListBinding binding;
    private MenuProvider menuProvider;
    private ArrayList<AccommodationReview> accommodationReviews = new ArrayList<AccommodationReview>();
    private ArrayList<AccommodationReview> filteredAccommodationReviews = new ArrayList<AccommodationReview>();



    public static AccommodationReviewListFragment newInstance(){
        AccommodationReviewListFragment fragment = new AccommodationReviewListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        Log.i("BookingApp", "onCreateView Accommodation Review List Fragment");
        binding = FragmentAccommodationReviewListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.getListView().setDividerHeight(2);
    }

    private void addMenu() {
        menuProvider = new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.accommodations_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_nav_content_main);
                return NavigationUI.onNavDestinationSelected(menuItem, navController);
            }
        };

        requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
