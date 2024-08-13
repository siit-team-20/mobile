package com.bookingapp.fragments.reservation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
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
import com.bookingapp.adapters.ReservationListAdapter;
import com.bookingapp.databinding.FragmentAccommodationListBinding;
import com.bookingapp.databinding.FragmentReservationListBinding;
import com.bookingapp.fragments.accommodation.AccommodationListFragment;
import com.bookingapp.fragments.accommodation.AccommodationsPageViewModel;
import com.bookingapp.model.Accommodation;
import com.bookingapp.model.DateRange;
import com.bookingapp.model.ReservationStatus;
import com.bookingapp.model.ReservationWithAccommodation;
import com.bookingapp.service.ServiceUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationListFragment extends ListFragment {

    private ReservationListAdapter adapter;
    private ReservationsPageViewModel reservationsViewModel;
    private FragmentReservationListBinding binding;
//    private MenuProvider menuProvider;
    private ArrayList<ReservationWithAccommodation> reservations = new ArrayList<>();
    private ArrayList<ReservationWithAccommodation> filteredReservations = new ArrayList<>();

    public static ReservationListFragment newInstance() {
        ReservationListFragment fragment = new ReservationListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i("BookingApp", "onCreateView Reservation List Fragment");
        binding = FragmentReservationListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    private void applyFilters(Set<String> statuses, String search, LocalDate startDate, LocalDate endDate) {
        if (adapter == null)
            return;
        filteredReservations.clear();
        for (ReservationWithAccommodation reservation : reservations)
            filteredReservations.add(reservation);
        List<ReservationWithAccommodation> toRemove = new ArrayList<>();

        for (ReservationWithAccommodation reservation : reservations) {
            boolean isStatusOkay = false;
            if (statuses != null) {
                if (statuses.size() == 0)
                    isStatusOkay = true;
                for (String status : statuses) {
                    if (reservation.getStatus().name().equals(status)) {
                        isStatusOkay = true;
                        break;
                    }
                }
                if (!isStatusOkay)
                    toRemove.add(reservation);
            }
        }

        if (search != null) {
            for (ReservationWithAccommodation reservation : reservations) {
                if (!reservation.getAccommodation().getName().toLowerCase().contains(search.toLowerCase()))
                    toRemove.add(reservation);
            }
        }

        if (startDate != null && endDate != null) {
            for (ReservationWithAccommodation reservation : reservations) {
                boolean isDateOkay = false;
                if (!reservation.getDateAsDate().isBefore(startDate) && !reservation.getDateAsDate().plusDays(reservation.getDays()).isAfter(endDate))
                    isDateOkay = true;
                if (!isDateOkay) {
                    toRemove.add(reservation);
                }
            }
        }

        filteredReservations.removeAll(toRemove);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("BookingApp", "onCreate Accommodation List Fragment");
        this.getListView().setDividerHeight(2);
        getDataFromClient();
        reservationsViewModel = new ViewModelProvider(requireActivity()).get(ReservationsPageViewModel.class);
        reservationsViewModel.getSelectedStatuses().observe(getViewLifecycleOwner(), statuses -> {
            applyFilters(
                    statuses,
                    reservationsViewModel.getSearchText().getValue(),
                    reservationsViewModel.getStartDate().getValue(),
                    reservationsViewModel.getEndDate().getValue()
            );
        });
        reservationsViewModel.getSearchText().observe(getViewLifecycleOwner(), search -> {
            applyFilters(
                    reservationsViewModel.getSelectedStatuses().getValue(),
                    search,
                    reservationsViewModel.getStartDate().getValue(),
                    reservationsViewModel.getEndDate().getValue()
                    );
        });
        reservationsViewModel.getStartDate().observe(getViewLifecycleOwner(), startDate -> {
            applyFilters(
                    reservationsViewModel.getSelectedStatuses().getValue(),
                    reservationsViewModel.getSearchText().getValue(),
                    startDate,
                    reservationsViewModel.getEndDate().getValue()
            );
        });
        reservationsViewModel.getEndDate().observe(getViewLifecycleOwner(), endDate -> {
            applyFilters(
                    reservationsViewModel.getSelectedStatuses().getValue(),
                    reservationsViewModel.getSearchText().getValue(),
                    reservationsViewModel.getStartDate().getValue(),
                    endDate
            );
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromClient();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getDataFromClient(){
        Call<ArrayList<ReservationWithAccommodation>> call = ServiceUtils.reservationService.getAll();
        call.enqueue(new Callback<ArrayList<ReservationWithAccommodation>>() {
            @Override
            public void onResponse(Call<ArrayList<ReservationWithAccommodation>> call, Response<ArrayList<ReservationWithAccommodation>> response) {
                if (response.code() == 200) {
                    Log.d("REZ","Message received");
                    System.out.println(response.body());
                    reservations = response.body();
                    filteredReservations = new ArrayList<>(reservations);
                    Set<String> guestEmails = new HashSet<>();
                    for (ReservationWithAccommodation reservation : filteredReservations) {
                        guestEmails.add(reservation.getGuestEmail());
                    }
                    for (String guestEmail : guestEmails) {
                        call = ServiceUtils.reservationService.get(guestEmail, ReservationStatus.Cancelled.toString());
                        call.enqueue(new Callback<ArrayList<ReservationWithAccommodation>>() {
                            @Override
                            public void onResponse(Call<ArrayList<ReservationWithAccommodation>> call, Response<ArrayList<ReservationWithAccommodation>> response) {
                                if (response.code() == 200) {
                                    Log.d("REZ", "Message received");
                                    System.out.println(response.body());
                                    Integer cancelledTimes = response.body().size();
                                    for (ReservationWithAccommodation reservation : filteredReservations) {
                                        if (reservation.getGuestEmail().equals(guestEmail))
                                            reservation.setCancelledTimes(cancelledTimes);
                                    }
                                    adapter.notifyDataSetChanged();
                                }

                                else {
                                    Log.d("REZ","Message received: "+response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<ArrayList<ReservationWithAccommodation>> call, Throwable t) {
                                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                            }
                        });
                    }
                    adapter = new ReservationListAdapter(getActivity(), getActivity().getSupportFragmentManager(), filteredReservations);
                    setListAdapter(adapter);
                    applyFilters(
                            reservationsViewModel.getSelectedStatuses().getValue(),
                            reservationsViewModel.getSearchText().getValue(),
                            reservationsViewModel.getStartDate().getValue(),
                            reservationsViewModel.getEndDate().getValue()
                    );
                }
                else {
                    Log.d("REZ","Message received: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ReservationWithAccommodation>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
}