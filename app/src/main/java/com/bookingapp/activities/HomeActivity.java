package com.bookingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bookingapp.R;
import com.bookingapp.databinding.ActivityHomeBinding;
import com.bookingapp.fragments.FragmentTransition;
import com.bookingapp.fragments.accommodation.AccommodationListFragment;
import com.bookingapp.fragments.accommodation.AccommodationsPageFragment;
import com.bookingapp.service.UserInfo;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

import java.util.HashSet;
import java.util.Set;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Set<Integer> topLevelDestinations = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("BookingApp", "HomeActivity onCreate()");
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        binding.activityHomeBase.floatingActionButton.setOnClickListener(v -> {
//            Log.i("BookingApp", "Floating Action Button");
//            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
//            intent.putExtra("title", "Cart");
//            startActivity(intent);
//        });

        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        toolbar = binding.activityHomeBase.toolbar;

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger);
            actionBar.setHomeButtonEnabled(true);
        }

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

//        topLevelDestinations.add(R.id.nav_language);
//        topLevelDestinations.add(R.id.nav_settings);

        navController = Navigation.findNavController(this, R.id.fragment_nav_content_main);
//        Bundle defaultBundle = new Bundle();
//        defaultBundle.putBoolean("isOnHome", true);
//        navController.setGraph(R.navigation.base_navigation, defaultBundle);
        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            Log.i("BookingApp", "Destination Changed");
            int id = navDestination.getId();
            boolean isTopLevelDestination = topLevelDestinations.contains(id);

            if (!isTopLevelDestination) {
                switch (id) {
                    case 1000079:
                        navController.navigate(R.id.nav_accommodations);
                        //Toast.makeText(HomeActivity.this, "Products", Toast.LENGTH_SHORT).show();
                        break;
                    case 1000086:
                        navController.navigate(R.id.nav_reservations);
                        //Toast.makeText(HomeActivity.this, "Products", Toast.LENGTH_SHORT).show();
                        break;
                    case 1000005:
                        navController.navigate(R.id.nav_create_accommodation);
                        //Toast.makeText(HomeActivity.this, "Products", Toast.LENGTH_SHORT).show();
                        break;
                    case 1000049:
                        navController.navigate(R.id.nav_login);
                        //Toast.makeText(HomeActivity.this, "Products", Toast.LENGTH_SHORT).show();
                        break;
                    case 1000027:
                        navController.navigate(R.id.nav_register);
                        //Toast.makeText(HomeActivity.this, "Products", Toast.LENGTH_SHORT).show();
                        break;
                    case 1000018:
                        navController.navigate(R.id.nav_account);
                        //Toast.makeText(HomeActivity.this, "Products", Toast.LENGTH_SHORT).show();
                        break;
                    case 1000035:
                        navController.navigate(R.id.nav_my_accommodations);
                        //Toast.makeText(HomeActivity.this, "Products", Toast.LENGTH_SHORT).show();
                        break;
                }
                drawer.closeDrawers();
            }
//            else {
//                switch (id) {
//                    case R.id.nav_settings:
//                        Toast.makeText(HomeActivity.this, "Settings", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.nav_language:
//                        Toast.makeText(HomeActivity.this, "Language", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
        });

        mAppBarConfiguration = new AppBarConfiguration
                .Builder(
                    R.id.nav_accommodations, R.id.nav_accommodation_detail,
                    R.id.nav_reservations, R.id.nav_create_accommodation,
                    R.id.nav_login, R.id.nav_register,
                    R.id.nav_account, R.id.nav_my_accommodations
                )
                .setOpenableLayout(drawer)
                .build();

        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        navController = Navigation.findNavController(this, R.id.fragment_nav_content_main);
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }

    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this, R.id.fragment_nav_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

}