package com.bookingapp.fragments.account;

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
import com.bookingapp.adapters.ReportListAdapter;
import com.bookingapp.databinding.FragmentAccommodationRequestListBinding;
import com.bookingapp.databinding.FragmentReportListBinding;
import com.bookingapp.fragments.accommodation.AccommodationRequestListFragment;
import com.bookingapp.model.AccommodationRequest;
import com.bookingapp.model.Report;
import com.bookingapp.model.UserType;
import com.bookingapp.service.ServiceUtils;
import com.bookingapp.service.UserInfo;

import org.json.JSONException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportListFragment extends ListFragment {

    private ReportListAdapter adapter;
    private FragmentReportListBinding binding;
    private ArrayList<Report> reports = new ArrayList<>();

    public ReportListFragment(){

    }
    public static ReportListFragment newInstance() {
        ReportListFragment fragment = new ReportListFragment();
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
        binding = FragmentReportListBinding.inflate(inflater, container, false);
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
        Call<ArrayList<Report>> call = null;
        if (UserInfo.getType().equals(UserType.Admin))
            call = ServiceUtils.reportService.getAll();
        call.enqueue(new Callback<ArrayList<Report>>() {
            @Override
            public void onResponse(Call<ArrayList<Report>> call, Response<ArrayList<Report>> response) {
                if (response.code() == 200) {
                    Log.d("REZ","Message received");
                    System.out.println(response.body());
                    reports = response.body();
                    adapter = new ReportListAdapter(getActivity(), getActivity().getSupportFragmentManager(), reports);
                    setListAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else {
                    Log.d("REZ","Message received: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Report>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });

    }
}