package com.bookingapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bookingapp.R;
import com.bookingapp.fragments.FragmentTransition;
import com.bookingapp.fragments.accommodation.AccommodationListFragment;
import com.bookingapp.fragments.accommodation.AccommodationsPageFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FragmentTransition.to(AccommodationsPageFragment.newInstance(), HomeActivity.this, false, R.id.activity_main);
    }
}