package com.bookingapp.fragments.accommodation;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bookingapp.R;
import com.bookingapp.adapters.AccommodationListAdapter;
import com.bookingapp.adapters.AccommodationReviewListAdapter;
import com.bookingapp.databinding.FragmentAccommodationReviewListBinding;
import com.bookingapp.model.AccommodationReview;
import com.bookingapp.service.ServiceUtils;

import org.json.JSONException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationReviewListFragment extends ListFragment {
    private static final String ARG_ACCOMMODATION_ID = "accommodationId";
    private AccommodationReviewListAdapter adapter;
    private FragmentAccommodationReviewListBinding binding;
    private ArrayList<AccommodationReview> accommodationReviews = new ArrayList<>();
    private Long accommodationId;
    private ListView listView;

    public static AccommodationReviewListFragment newInstance(Long accommodationId){
        AccommodationReviewListFragment fragment = new AccommodationReviewListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ACCOMMODATION_ID, accommodationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            accommodationId = getArguments().getLong(ARG_ACCOMMODATION_ID);
        }
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
        Call<ArrayList<AccommodationReview>> callReviews = ServiceUtils.accommodationReviewService.get(accommodationId, false);

        callReviews.enqueue(new Callback<ArrayList<AccommodationReview>>() {
            @Override
            public void onResponse(Call<ArrayList<AccommodationReview>> call, Response<ArrayList<AccommodationReview>> response) {
                if (response.code() == 200) {
                    Log.d("Reviews-Get","Message received");
                    System.out.println(response.body());
                    accommodationReviews = response.body();
                    adapter = new AccommodationReviewListAdapter(getActivity(), getActivity().getSupportFragmentManager(), accommodationReviews);
                    setListAdapter(adapter);
                    int totalHeight = 0;
                    for (int i = 0; i < adapter.getCount(); i++) {
                        View listItem = adapter.getView(i, null, listView);
                        listItem.measure(0, 0);
                        totalHeight += listItem.getMeasuredHeight();
                    }
                    ViewGroup.LayoutParams params = listView.getLayoutParams();
                    params.height = totalHeight + (listView.getDividerHeight() * adapter.getCount() - 1);
                    listView.setLayoutParams(params);
                    listView.requestLayout();
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
