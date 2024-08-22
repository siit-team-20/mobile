package com.bookingapp.fragments.notification;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookingapp.R;
import com.bookingapp.databinding.FragmentAccommodationReviewListBinding;
import com.bookingapp.databinding.FragmentNotificationListBinding;
import com.bookingapp.databinding.FragmentNotificationsPageBinding;
import com.bookingapp.fragments.FragmentTransition;
import com.bookingapp.model.NotificationType;

import java.util.List;


public class NotificationsPageFragment extends Fragment {

    private static final String ARG_NOTIFICATION_TYPE = "notificationType";
    private static final String ARG_SUBMIT_DATE = "submitDate";
    private FragmentNotificationsPageBinding binding;
    private NotificationType notificationType;
    private List<Integer> submitDate;
    private String mParam1;
    private String mParam2;
    public static NotificationsPageFragment newInstance() {
        NotificationsPageFragment fragment = new NotificationsPageFragment();
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.i("BookingApp", "onCreateView Notification List Fragment");
        binding = FragmentNotificationsPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;

    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
        }

    }
    @Override
    public void onResume(){
        super.onResume();
        FragmentTransition.to(NotificationListFragment.newInstance(), getActivity(), false, R.id.scroll_notifications_list );
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }



}