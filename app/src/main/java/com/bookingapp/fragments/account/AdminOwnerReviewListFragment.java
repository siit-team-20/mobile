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
import com.bookingapp.adapters.AdminOwnerReviewListAdapter;
import com.bookingapp.adapters.OwnerReviewListAdapter;
import com.bookingapp.databinding.FragmentAdminOwnerReviewListBinding;
import com.bookingapp.databinding.FragmentOwnerReviewListBinding;
import com.bookingapp.model.OwnerReview;
import com.bookingapp.service.ServiceUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminOwnerReviewListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminOwnerReviewListFragment extends ListFragment {

    private AdminOwnerReviewListAdapter adapter;
    private FragmentAdminOwnerReviewListBinding binding;
    private ArrayList<OwnerReview> ownerReviews = new ArrayList<>();
    private ListView listView;

    public AdminOwnerReviewListFragment() {
        // Required empty public constructor
    }


    public static AdminOwnerReviewListFragment newInstance() {
        AdminOwnerReviewListFragment fragment = new AdminOwnerReviewListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("BookingApp", "onCreateView Admin Owner Review List Fragment");
        binding = FragmentAdminOwnerReviewListBinding.inflate(inflater, container, false);
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
        Call<ArrayList<OwnerReview>> callReviews = ServiceUtils.ownerReviewService.get(true);

        callReviews.enqueue(new Callback<ArrayList<OwnerReview>>() {
            @Override
            public void onResponse(Call<ArrayList<OwnerReview>> call, Response<ArrayList<OwnerReview>> response) {
                if (response.code() == 200) {
                    Log.d("Reviews-Get","Message received");
                    System.out.println(response.body());
                    ownerReviews = response.body();
                    adapter = new AdminOwnerReviewListAdapter(getActivity(), getActivity().getSupportFragmentManager(), ownerReviews);
                    setListAdapter(adapter);
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