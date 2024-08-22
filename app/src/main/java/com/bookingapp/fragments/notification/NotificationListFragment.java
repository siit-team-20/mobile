package com.bookingapp.fragments.notification;

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
import com.bookingapp.adapters.AccommodationListAdapter;
import com.bookingapp.adapters.AccommodationReviewListAdapter;
import com.bookingapp.adapters.NotificationListAdapter;
import com.bookingapp.adapters.ReservationListAdapter;
import com.bookingapp.databinding.FragmentNotificationListBinding;
import com.bookingapp.model.Accommodation;
import com.bookingapp.model.AccommodationReview;
import com.bookingapp.model.FavouriteAccommodationWithAccommodation;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationListFragment extends ListFragment {

    private NotificationListAdapter adapter;
    private FragmentNotificationListBinding binding;
    private ArrayList<Notification> notifications = new ArrayList<Notification>();

    public NotificationListFragment() {
    }
    public static NotificationListFragment newInstance() {
        NotificationListFragment fragment = new NotificationListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("BookingApp", "onCreateView Notification List Fragment");
        binding = FragmentNotificationListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("BookingApp", "onCreate Accommodation List Fragment");
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
        if (UserInfo.getToken() != null) {
            try {
                if (UserInfo.getType().equals(UserType.Guest) || UserInfo.getType().equals(UserType.Owner)) {
                    Call<ArrayList<Notification>> callReviews = ServiceUtils.notificationService.get(UserInfo.getEmail());
                    callReviews.enqueue(new Callback<ArrayList<Notification>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Notification>> call, Response<ArrayList<Notification>> response) {
                            if (response.code() == 200) {
                                Log.d("Notifications-Get","Message received");
                                System.out.println(response.body());
                                notifications = response.body();
                                adapter = new NotificationListAdapter(getActivity(), getActivity().getSupportFragmentManager(), notifications);
                                setListAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                            else {
                                Log.d("Reviews-Get","Message received: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Notification>> call, Throwable t) {
                            Log.d("Reviews-Get", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                }
            }
            catch(JSONException e){
                throw new RuntimeException(e);
            }
        }
    }
}