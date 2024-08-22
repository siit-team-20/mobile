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
import android.widget.ListView;

import com.bookingapp.R;
import com.bookingapp.adapters.AccommodationReviewListAdapter;
import com.bookingapp.adapters.OwnerReviewListAdapter;
import com.bookingapp.databinding.FragmentAccommodationReviewListBinding;
import com.bookingapp.databinding.FragmentOwnerReviewListBinding;
import com.bookingapp.fragments.accommodation.AccommodationReviewListFragment;
import com.bookingapp.model.AccommodationReview;
import com.bookingapp.model.OwnerReview;
import com.bookingapp.service.ServiceUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OwnerReviewListFragment extends ListFragment {

    private static final String ARG_OWNER_EMAIL = "ownerEmail";
    private OwnerReviewListAdapter adapter;
    private FragmentOwnerReviewListBinding binding;
    private ArrayList<OwnerReview> ownerReviews = new ArrayList<>();
    private String ownerEmail;
    private ListView listView;

    public static OwnerReviewListFragment newInstance(String ownerEmail) {
        OwnerReviewListFragment fragment = new OwnerReviewListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_OWNER_EMAIL, ownerEmail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ownerEmail = getArguments().getString(ARG_OWNER_EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("BookingApp", "onCreateView Owner Review List Fragment");
        binding = FragmentOwnerReviewListBinding.inflate(inflater, container, false);
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
        Call<ArrayList<OwnerReview>> callReviews = ServiceUtils.ownerReviewService.get(ownerEmail, false);

        callReviews.enqueue(new Callback<ArrayList<OwnerReview>>() {
            @Override
            public void onResponse(Call<ArrayList<OwnerReview>> call, Response<ArrayList<OwnerReview>> response) {
                if (response.code() == 200) {
                    Log.d("Reviews-Get","Message received");
                    System.out.println(response.body());
                    ownerReviews = response.body();
                    adapter = new OwnerReviewListAdapter(getActivity(), getActivity().getSupportFragmentManager(), ownerReviews);
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
            public void onFailure(Call<ArrayList<OwnerReview>> call, Throwable t) {
                Log.d("Reviews-Get", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

}