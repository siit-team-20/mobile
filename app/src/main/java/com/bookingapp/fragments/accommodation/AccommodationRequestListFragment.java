package com.bookingapp.fragments.accommodation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookingapp.R;
import com.bookingapp.adapters.AccommodationRequestListAdapter;
import com.bookingapp.adapters.NotificationListAdapter;
import com.bookingapp.adapters.ReservationListAdapter;
import com.bookingapp.databinding.FragmentAccommodationRequestListBinding;
import com.bookingapp.model.AccommodationRequest;
import com.bookingapp.model.Notification;
import com.bookingapp.model.ReservationStatus;
import com.bookingapp.model.ReservationWithAccommodation;
import com.bookingapp.model.UserType;
import com.bookingapp.service.ServiceUtils;
import com.bookingapp.service.UserInfo;

import org.json.JSONException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccommodationRequestListFragment extends ListFragment {
    private AccommodationRequestListAdapter adapter;
    private FragmentAccommodationRequestListBinding binding;
    private ArrayList<AccommodationRequest> accommodationRequests = new ArrayList<>();

    public AccommodationRequestListFragment(){

    }
    public static AccommodationRequestListFragment newInstance() {
        AccommodationRequestListFragment fragment = new AccommodationRequestListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("BookingApp", "onCraeteView Accommodation Request List Fragment");
        binding = FragmentAccommodationRequestListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("BookingApp", "onCreate Accommodation Request List Fragment");
        this.getListView().setDividerHeight(40);
        try {
            getDataFromClient();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void getDataFromClient() throws JSONException {
        Call<ArrayList<AccommodationRequest>> call = null;
        if (UserInfo.getType().equals(UserType.Admin))
            call = ServiceUtils.accommodationRequestService.getAll();
        call.enqueue(new Callback<ArrayList<AccommodationRequest>>() {
            @Override
            public void onResponse(Call<ArrayList<AccommodationRequest>> call, Response<ArrayList<AccommodationRequest>> response) {
                if (response.code() == 200) {
                    Log.d("REZ","Message received");
                    System.out.println(response.body());
                    accommodationRequests = response.body();
                    adapter = new AccommodationRequestListAdapter(getActivity(), getActivity().getSupportFragmentManager(), accommodationRequests);
                    setListAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else {
                    Log.d("REZ","Message received: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AccommodationRequest>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });

    }


}