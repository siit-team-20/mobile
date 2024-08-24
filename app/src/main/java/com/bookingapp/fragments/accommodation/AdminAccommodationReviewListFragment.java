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
import android.widget.ListView;

import com.bookingapp.R;
import com.bookingapp.adapters.AccommodationReviewListAdapter;
import com.bookingapp.adapters.AdminAccommodationReviewListAdapter;
import com.bookingapp.databinding.FragmentAccommodationReviewListBinding;
import com.bookingapp.databinding.FragmentAdminAccommodationReviewListBinding;
import com.bookingapp.model.AccommodationReview;
import com.bookingapp.service.ServiceUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminAccommodationReviewListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminAccommodationReviewListFragment extends ListFragment {

    private AdminAccommodationReviewListAdapter adapter;
    private FragmentAdminAccommodationReviewListBinding binding;
    private ArrayList<AccommodationReview> accommodationReviews = new ArrayList<>();
    private ListView listView;
    public AdminAccommodationReviewListFragment() {
        // Required empty public constructor
    }


    public static AdminAccommodationReviewListFragment newInstance() {
        AdminAccommodationReviewListFragment fragment = new AdminAccommodationReviewListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("BookingApp", "onCreateView Admin Accommodation Review List Fragment");
        binding = FragmentAdminAccommodationReviewListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        listView = this.getListView();
        this.getListView().setDividerHeight(2);
        getDataFromClient();
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
    private void getDataFromClient() {
        Call<ArrayList<AccommodationReview>> callReviews = ServiceUtils.accommodationReviewService.get(true);

        callReviews.enqueue(new Callback<ArrayList<AccommodationReview>>() {
            @Override
            public void onResponse(Call<ArrayList<AccommodationReview>> call, Response<ArrayList<AccommodationReview>> response) {
                if (response.code() == 200) {
                    Log.d("Reviews-Get","Message received");
                    System.out.println(response.body());
                    accommodationReviews = response.body();
                    adapter = new AdminAccommodationReviewListAdapter(getActivity(), getActivity().getSupportFragmentManager(), accommodationReviews);
                    setListAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else {
                    Log.d("Reviews-Get","Message received: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AccommodationReview>> call, Throwable t) {
                Log.d("Reviews-Get", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
}