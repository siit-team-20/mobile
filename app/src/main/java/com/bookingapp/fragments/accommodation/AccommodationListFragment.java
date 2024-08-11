package com.bookingapp.fragments.accommodation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bookingapp.R;
import com.bookingapp.adapters.AccommodationListAdapter;
import com.bookingapp.databinding.FragmentAccommodationListBinding;
import com.bookingapp.model.Accommodation;
import com.bookingapp.model.DateRange;
import com.bookingapp.service.ServiceUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationListFragment extends ListFragment {
    private AccommodationListAdapter adapter;
    private AccommodationsPageViewModel accommodationsViewModel;
    private FragmentAccommodationListBinding binding;
    private MenuProvider menuProvider;
    private ArrayList<Accommodation> accommodations = new ArrayList<>();
    private ArrayList<Accommodation> filteredAccommodations = new ArrayList<>();

    public static AccommodationListFragment newInstance() {
        AccommodationListFragment fragment = new AccommodationListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i("BookingApp", "onCreateView Accommodation List Fragment");
        binding = FragmentAccommodationListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        addMenu();
        return root;
    }

    private void applySort(String sort) {
        if (sort == null || adapter == null)
            return;
        if (sort.equals("Ascending")) {
            filteredAccommodations.sort(new Comparator<Accommodation>() {
                @Override
                public int compare(Accommodation o1, Accommodation o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }
        if (sort.equals("Descending")) {
            filteredAccommodations.sort(new Comparator<Accommodation>() {
                @Override
                public int compare(Accommodation o1, Accommodation o2) {
                    return o2.getName().compareTo(o1.getName());
                }
            });
        }
        adapter.notifyDataSetChanged();
    }

    private void applyFilters(Set<String> types, Set<String> benefits, String price) {
        if (adapter == null)
            return;
        filteredAccommodations.clear();
        for (Accommodation accommodation : accommodations)
            filteredAccommodations.add(accommodation);
        List<Accommodation> toRemove = new ArrayList<>();

        for (Accommodation accommodation : accommodations) {
            boolean isTypeOkay = false;
            if (types != null) {
                if (types.size() == 0)
                    isTypeOkay = true;
                for (String type : types) {
                    if (accommodation.getAccommodationType().name().equals(type)) {
                        isTypeOkay = true;
                        break;
                    }
                }
                if (!isTypeOkay)
                    toRemove.add(accommodation);
            }
        }

        for (Accommodation accommodation : accommodations) {
            boolean containsAllBenefits = true;
            if (benefits != null) {
                for (String benefit : benefits) {
                    boolean containsBenefit = false;
                    for (String accommodationBenefit : accommodation.getBenefits()) {
                        if (accommodationBenefit.toLowerCase().contains(benefit.toLowerCase())) {
                            containsBenefit = true;
                            break;
                        }
                    }
                    if (!containsBenefit) {
                        containsAllBenefits = false;
                        break;
                    }
                }
                if (!containsAllBenefits)
                    toRemove.add(accommodation);
            }
        }

        if (price != null) {
            if (!price.equals("Any")) {
                int lowerPrice;
                int upperPrice;
                if (price.equals("90e +")) {
                    lowerPrice = 90;
                    upperPrice = Integer.MAX_VALUE;
                }
                else {
                    lowerPrice = Integer.parseInt(price.split(" ")[0].trim());
                    upperPrice = Integer.parseInt(price.split(" ")[2].trim().replace("e", ""));
                }
                for (Accommodation accommodation : accommodations) {
                    boolean isPriceOkay = false;
                    for (DateRange dateRange : accommodation.getAvailabilityDates()) {
                        if (dateRange.getPrice() >= lowerPrice && dateRange.getPrice() <= upperPrice && dateRange.getEndDateAsDate().isAfter(LocalDate.now())) {
                            isPriceOkay = true;
                            break;
                        }
                    }
                    if (!isPriceOkay) {
                        toRemove.add(accommodation);
                    }
                }
            }
        }

        filteredAccommodations.removeAll(toRemove);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("BookingApp", "onCreate Accommodation List Fragment");
        this.getListView().setDividerHeight(2);
        getDataFromClient();
        accommodationsViewModel = new ViewModelProvider(requireActivity()).get(AccommodationsPageViewModel.class);
        accommodationsViewModel.getSelectedSort().observe(getViewLifecycleOwner(), sort -> {
            applySort(sort);
        });
        accommodationsViewModel.getSelectedTypes().observe(getViewLifecycleOwner(), types -> {
            applyFilters(types, accommodationsViewModel.getSelectedBenefits().getValue(), accommodationsViewModel.getSelectedPrice().getValue());
            if (accommodationsViewModel.getSelectedSort().getValue() == null)
                accommodationsViewModel.setSelectedSort("Ascending");
            else
                accommodationsViewModel.setSelectedSort(accommodationsViewModel.getSelectedSort().getValue());
        });
        accommodationsViewModel.getSelectedBenefits().observe(getViewLifecycleOwner(), benefits -> {
            applyFilters(accommodationsViewModel.getSelectedTypes().getValue(), benefits, accommodationsViewModel.getSelectedPrice().getValue());
            if (accommodationsViewModel.getSelectedSort().getValue() == null)
                accommodationsViewModel.setSelectedSort("Ascending");
            else
                accommodationsViewModel.setSelectedSort(accommodationsViewModel.getSelectedSort().getValue());
        });
        accommodationsViewModel.getSelectedPrice().observe(getViewLifecycleOwner(), price -> {
            applyFilters(accommodationsViewModel.getSelectedTypes().getValue(), accommodationsViewModel.getSelectedBenefits().getValue(), price);
            if (accommodationsViewModel.getSelectedSort().getValue() == null)
                accommodationsViewModel.setSelectedSort("Ascending");
            else
                accommodationsViewModel.setSelectedSort(accommodationsViewModel.getSelectedSort().getValue());
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromClient();
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

    private void getDataFromClient(){
        Call<ArrayList<Accommodation>> call = ServiceUtils.accommodationService.getAll();
        call.enqueue(new Callback<ArrayList<Accommodation>>() {
            @Override
            public void onResponse(Call<ArrayList<Accommodation>> call, Response<ArrayList<Accommodation>> response) {
                if (response.code() == 200) {
                    Log.d("REZ","Message received");
                    System.out.println(response.body());
                    accommodations = response.body();
                    filteredAccommodations = new ArrayList<>(accommodations);
                    adapter = new AccommodationListAdapter(getActivity(), getActivity().getSupportFragmentManager(), filteredAccommodations);
                    setListAdapter(adapter);
                    if (accommodationsViewModel.getSelectedSort().getValue() == null)
                        accommodationsViewModel.setSelectedSort("Ascending");
                    else
                        accommodationsViewModel.setSelectedSort(accommodationsViewModel.getSelectedSort().getValue());
                    applyFilters(accommodationsViewModel.getSelectedTypes().getValue(), accommodationsViewModel.getSelectedBenefits().getValue(), accommodationsViewModel.getSelectedPrice().getValue());
                    applySort(accommodationsViewModel.getSelectedSort().getValue());
                }
                else {
                    Log.d("REZ","Message received: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Accommodation>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }


}