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
import com.bookingapp.model.FavouriteAccommodationWithAccommodation;
import com.bookingapp.model.UserType;
import com.bookingapp.service.FavouriteAccommodationService;
import com.bookingapp.service.ServiceUtils;
import com.bookingapp.service.UserInfo;

import org.json.JSONException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationListFragment extends ListFragment {
    private static final String ARG_IS_ON_HOME = "isOnHome";
    private static final String ARG_IS_ON_FAVOURITES = "isOnFavourites";
    private AccommodationListAdapter adapter;
    private AccommodationsPageViewModel accommodationsViewModel;
    private FragmentAccommodationListBinding binding;
    private MenuProvider menuProvider;
    private ArrayList<Accommodation> accommodations = new ArrayList<>();
    private ArrayList<FavouriteAccommodationWithAccommodation> favouriteAccommodations = new ArrayList<>();
    private ArrayList<Accommodation> filteredAccommodations = new ArrayList<>();
    private boolean isOnHome;
    private boolean isOnFavourites;

    public static AccommodationListFragment newInstance(boolean isOnHome, boolean isOnFavourites) {
        AccommodationListFragment fragment = new AccommodationListFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_ON_HOME, isOnHome);
        args.putBoolean(ARG_IS_ON_FAVOURITES, isOnFavourites);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i("BookingApp", "onCreateView Accommodation List Fragment");
        binding = FragmentAccommodationListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //addMenu();
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isOnHome = getArguments().getBoolean(ARG_IS_ON_HOME);
            isOnFavourites = getArguments().getBoolean(ARG_IS_ON_FAVOURITES);
        }
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

    private void applyFilters(Set<String> types, Set<String> benefits, String price, String search, Integer guestNumber, LocalDate startDate, LocalDate endDate) {
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

        if (search != null) {
            for (Accommodation accommodation : accommodations) {
                if (!accommodation.getLocation().toLowerCase().contains(search.toLowerCase()))
                    toRemove.add(accommodation);
            }
        }

        if (guestNumber != null) {
            for (Accommodation accommodation : accommodations) {
                if (!(guestNumber >= accommodation.getMinGuests() && guestNumber <= accommodation.getMaxGuests())) {
                    toRemove.add(accommodation);
                }
            }
        }

        if (startDate != null && endDate != null) {
            for (Accommodation accommodation : accommodations) {
                boolean isDateOkay = false;
                for (int i = 0; i < accommodation.getAvailabilityDates().size(); i++) {
                    DateRange dateRange = accommodation.getAvailabilityDates().get(i);
                    if (DateRange.isBetween(dateRange.getStartDateAsDate(), dateRange.getEndDateAsDate(), startDate, endDate) && dateRange.getEndDateAsDate().isAfter(LocalDate.now())) {
                        isDateOkay = true;
                        break;
                    }
                    else {
                        if (i != accommodation.getAvailabilityDates().size() - 1) {
                            DateRange dateRangePast = accommodation.getAvailabilityDates().get(i);
                            if (!startDate.isBefore(dateRange.getStartDateAsDate()) && startDate.isBefore(dateRange.getEndDateAsDate())) {
                                for (int j = i + 1; j < accommodation.getAvailabilityDates().size(); j++) {
                                    DateRange dateRangeNext = accommodation.getAvailabilityDates().get(j);
                                    if (dateRangeNext.getStartDateAsDate().isEqual(dateRangePast.getEndDateAsDate())) {
                                        if (!endDate.isBefore(dateRangeNext.getStartDateAsDate()) && !endDate.isAfter(dateRangeNext.getEndDateAsDate())) {
                                            isDateOkay = true;
                                            break;
                                        }
                                    }
                                    dateRangePast = dateRangeNext;
                                }
                            }
                        }
                    }
                }
                if (!isDateOkay) {
                    toRemove.add(accommodation);
                }
            }
        }

        filteredAccommodations.removeAll(toRemove);
        adapter.notifyDataSetChanged();
    }

    private void applySortAndFilter(Set<String> types, Set<String> benefits, String price, String search, Integer guestNumber, LocalDate startDate, LocalDate endDate) {
        applyFilters(types, benefits, price, search, guestNumber, startDate, endDate);
        if (accommodationsViewModel.getSelectedSort().getValue() == null)
            accommodationsViewModel.setSelectedSort("Ascending");
        else
            accommodationsViewModel.setSelectedSort(accommodationsViewModel.getSelectedSort().getValue());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("BookingApp", "onCreate Accommodation List Fragment");
        this.getListView().setDividerHeight(2);
        try {
            getDataFromClient();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        accommodationsViewModel = new ViewModelProvider(requireActivity()).get(AccommodationsPageViewModel.class);
        accommodationsViewModel.getSelectedSort().observe(getViewLifecycleOwner(), sort -> {
            applySort(sort);
        });
        accommodationsViewModel.getSelectedTypes().observe(getViewLifecycleOwner(), types -> {
            applySortAndFilter(
                    types,
                    accommodationsViewModel.getSelectedBenefits().getValue(),
                    accommodationsViewModel.getSelectedPrice().getValue(),
                    accommodationsViewModel.getSearchText().getValue(),
                    accommodationsViewModel.getGuestNumber().getValue(),
                    accommodationsViewModel.getStartDate().getValue(),
                    accommodationsViewModel.getEndDate().getValue()
            );
        });
        accommodationsViewModel.getSelectedBenefits().observe(getViewLifecycleOwner(), benefits -> {
            applySortAndFilter(
                    accommodationsViewModel.getSelectedTypes().getValue(),
                    benefits,
                    accommodationsViewModel.getSelectedPrice().getValue(),
                    accommodationsViewModel.getSearchText().getValue(),
                    accommodationsViewModel.getGuestNumber().getValue(),
                    accommodationsViewModel.getStartDate().getValue(),
                    accommodationsViewModel.getEndDate().getValue()
            );
        });
        accommodationsViewModel.getSelectedPrice().observe(getViewLifecycleOwner(), price -> {
            applySortAndFilter(
                    accommodationsViewModel.getSelectedTypes().getValue(),
                    accommodationsViewModel.getSelectedBenefits().getValue(),
                    price,
                    accommodationsViewModel.getSearchText().getValue(),
                    accommodationsViewModel.getGuestNumber().getValue(),
                    accommodationsViewModel.getStartDate().getValue(),
                    accommodationsViewModel.getEndDate().getValue()
            );
        });
        accommodationsViewModel.getSearchText().observe(getViewLifecycleOwner(), search -> {
            applySortAndFilter(
                    accommodationsViewModel.getSelectedTypes().getValue(),
                    accommodationsViewModel.getSelectedBenefits().getValue(),
                    accommodationsViewModel.getSelectedPrice().getValue(),
                    search,
                    accommodationsViewModel.getGuestNumber().getValue(),
                    accommodationsViewModel.getStartDate().getValue(),
                    accommodationsViewModel.getEndDate().getValue()
            );
        });
        accommodationsViewModel.getGuestNumber().observe(getViewLifecycleOwner(), guestNumber -> {
            applySortAndFilter(
                    accommodationsViewModel.getSelectedTypes().getValue(),
                    accommodationsViewModel.getSelectedBenefits().getValue(),
                    accommodationsViewModel.getSelectedPrice().getValue(),
                    accommodationsViewModel.getSearchText().getValue(),
                    guestNumber,
                    accommodationsViewModel.getStartDate().getValue(),
                    accommodationsViewModel.getEndDate().getValue()
            );
        });
        accommodationsViewModel.getStartDate().observe(getViewLifecycleOwner(), startDate -> {
            applySortAndFilter(
                    accommodationsViewModel.getSelectedTypes().getValue(),
                    accommodationsViewModel.getSelectedBenefits().getValue(),
                    accommodationsViewModel.getSelectedPrice().getValue(),
                    accommodationsViewModel.getSearchText().getValue(),
                    accommodationsViewModel.getGuestNumber().getValue(),
                    startDate,
                    accommodationsViewModel.getEndDate().getValue()
            );
        });
        accommodationsViewModel.getEndDate().observe(getViewLifecycleOwner(), endDate -> {
            applySortAndFilter(
                    accommodationsViewModel.getSelectedTypes().getValue(),
                    accommodationsViewModel.getSelectedBenefits().getValue(),
                    accommodationsViewModel.getSelectedPrice().getValue(),
                    accommodationsViewModel.getSearchText().getValue(),
                    accommodationsViewModel.getGuestNumber().getValue(),
                    accommodationsViewModel.getStartDate().getValue(),
                    endDate
            );
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            getDataFromClient();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
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

    private void getDataFromClient() throws JSONException {
        Call<ArrayList<Accommodation>> call = ServiceUtils.accommodationService.getAll();
        Call<ArrayList<FavouriteAccommodationWithAccommodation>> callFavourites = null;
        if (UserInfo.getToken() == null) {
            call = ServiceUtils.accommodationService.get(true);
        }
        else {
            if (UserInfo.getType().equals(UserType.Guest) && isOnFavourites) {
                callFavourites = ServiceUtils.favouriteAccommodationService.get(UserInfo.getEmail());
                callFavourites.enqueue(new Callback<ArrayList<FavouriteAccommodationWithAccommodation>>() {
                    @Override
                    public void onResponse(Call<ArrayList<FavouriteAccommodationWithAccommodation>> call, Response<ArrayList<FavouriteAccommodationWithAccommodation>> response) {
                        if (response.code() == 200) {
                            Log.d("REZ","Message received");
                            System.out.println(response.body());
                            favouriteAccommodations = response.body();
                            accommodations = new ArrayList<>();
                            for (FavouriteAccommodationWithAccommodation favouriteAccommodation : favouriteAccommodations) {
                                accommodations.add(favouriteAccommodation.getAccommodation());
                            }
                            filteredAccommodations = new ArrayList<>(accommodations);
                            adapter = new AccommodationListAdapter(getActivity(), getActivity().getSupportFragmentManager(), filteredAccommodations);
                            setListAdapter(adapter);
                            if (accommodationsViewModel.getSelectedSort().getValue() == null)
                                accommodationsViewModel.setSelectedSort("Ascending");
                            else
                                accommodationsViewModel.setSelectedSort(accommodationsViewModel.getSelectedSort().getValue());
                            applyFilters(
                                    accommodationsViewModel.getSelectedTypes().getValue(),
                                    accommodationsViewModel.getSelectedBenefits().getValue(),
                                    accommodationsViewModel.getSelectedPrice().getValue(),
                                    accommodationsViewModel.getSearchText().getValue(),
                                    accommodationsViewModel.getGuestNumber().getValue(),
                                    accommodationsViewModel.getStartDate().getValue(),
                                    accommodationsViewModel.getEndDate().getValue()
                            );
                            applySort(accommodationsViewModel.getSelectedSort().getValue());
                        }
                        else {
                            Log.d("REZ","Message received: "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<FavouriteAccommodationWithAccommodation>> call, Throwable t) {
                        Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                    }
                });
            }
            else if (UserInfo.getType().equals(UserType.Owner) && !isOnHome)
                call = ServiceUtils.accommodationService.get(UserInfo.getEmail());
            else if (!UserInfo.getType().equals(UserType.Admin))
                call = ServiceUtils.accommodationService.get(true);
        }

        Callback<ArrayList<Accommodation>> callback = new Callback<ArrayList<Accommodation>>() {
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
                    applyFilters(
                            accommodationsViewModel.getSelectedTypes().getValue(),
                            accommodationsViewModel.getSelectedBenefits().getValue(),
                            accommodationsViewModel.getSelectedPrice().getValue(),
                            accommodationsViewModel.getSearchText().getValue(),
                            accommodationsViewModel.getGuestNumber().getValue(),
                            accommodationsViewModel.getStartDate().getValue(),
                            accommodationsViewModel.getEndDate().getValue()
                    );
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
        };

        if (!isOnFavourites)
            call.enqueue(callback);
    }
}