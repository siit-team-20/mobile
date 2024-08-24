package com.bookingapp.fragments.auth;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.view.MenuProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bookingapp.R;
import com.bookingapp.activities.HomeActivity;
import com.bookingapp.databinding.FragmentLoginBinding;
import com.bookingapp.fragments.FragmentTransition;
import com.bookingapp.fragments.accommodation.AccommodationListFragment;
import com.bookingapp.fragments.accommodation.AccommodationsPageFragment;
import com.bookingapp.model.Credentials;
import com.bookingapp.model.Notification;
import com.bookingapp.model.NotificationType;
import com.bookingapp.model.User;
import com.bookingapp.model.UserType;
import com.bookingapp.service.ServiceUtils;
import com.bookingapp.service.UserInfo;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private EditText emailEt;
    private EditText passwordEt;
    private Button loginButton;

    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean o) {
            if (o) {
                Toast.makeText(getActivity(), "Post notification permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Post notification permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    });

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        emailEt = binding.etEmail;
        passwordEt = binding.etPassword;
        loginButton = binding.btnLogin;
        TextView registerLink = binding.tvRegister;
        registerLink.setClickable(true);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_nav_content_main);
                com.google.android.material.navigation.NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                Menu menu = navigationView.getMenu();
                MenuItem menuItem = menu.findItem(R.id.nav_register);
                NavigationUI.onNavDestinationSelected(menuItem, navController);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString();
                String password = passwordEt.getText().toString();
                if (email.length() == 0 || password.length() == 0)
                    return;
                Credentials credentials = new Credentials(email, password);
                Call<User> call = ServiceUtils.userService.login(credentials);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 200) {
                            Log.d("REZ", "Message received");
                            System.out.println(response.body());
                            UserInfo.setToken(response.body().getToken());
                            try {
                                System.out.println(UserInfo.getEmail());
                                TextView user = getActivity().findViewById(R.id.user_name);
                                try {
                                    user.setText(UserInfo.getName() + " " + UserInfo.getSurname());
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                                if (UserInfo.getToken() != null) {
                                    try {
                                        if (UserInfo.getType().equals(UserType.Guest) || UserInfo.getType().equals(UserType.Owner)) {
                                            Call<ArrayList<Notification>> notificationsCall = ServiceUtils.notificationService.get(UserInfo.getEmail());
                                            notificationsCall.enqueue(new Callback<ArrayList<Notification>>() {
                                                @Override
                                                public void onResponse(Call<ArrayList<Notification>> call, Response<ArrayList<Notification>> response) {
                                                    if (response.code() == 200) {
                                                        Log.d("REZ","Message received");
                                                        System.out.println(response.body());
                                                        List<Notification> notifications = response.body();

                                                        for (Notification notification : notifications) {
                                                            String desc = "";
                                                            if (notification.getType().equals(NotificationType.ReservationCreated)) {
                                                                desc = "New reservation from " + notification.getOtherUserEmail();
                                                            }
                                                            else if (notification.getType().equals(NotificationType.ReservationCancelled)) {
                                                                desc = "Reservation from " + notification.getOtherUserEmail() + " was cancelled";
                                                            }
                                                            else if (notification.getType().equals(NotificationType.OwnerReviewAdded)) {
                                                                desc = notification.getOtherUserEmail() + " reviewed you";
                                                            }
                                                            else if (notification.getType().equals(NotificationType.AccommodationReviewAdded)) {
                                                                desc = notification.getOtherUserEmail() + " reviewed your accommodation";
                                                            }
                                                            else if (notification.getType().equals(NotificationType.ReservationResponse)) {
                                                                desc = notification.getOtherUserEmail() + " responded to your reservation request";
                                                            }
                                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "BookingApp")
                                                                    .setSmallIcon(R.drawable.baseline_notifications_24)
                                                                    .setContentTitle(getString(R.string.app_name))
                                                                    .setWhen(notification.getCreatedAtAsCalendar().getTimeInMillis())
                                                                    .setContentText(desc)
                                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                                            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);

                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                                                activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                                                            } else {
                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                    CharSequence name = getString(R.string.app_name);
                                                                    String description = "BookingApp Notification";
                                                                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                                                                    NotificationChannel channel = new NotificationChannel("test", name, importance);
                                                                    channel.setDescription(description);
                                                                    notificationManager.createNotificationChannel(channel);

                                                                    notificationManager.notify(Math.toIntExact(notification.getId()), builder.build());
                                                                }
                                                            }
                                                        }
                                                    }
                                                    else {
                                                        Log.d("REZ","Message received: "+response.code());
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ArrayList<Notification>> call, Throwable t) {
                                                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                                                }
                                            });
                                        }
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                Bundle args = new Bundle();
                                args.putBoolean("isOnHome", true);
                                switch (UserInfo.getType()) {
                                    case Guest: {
                                        prepareGuestMenu();
                                        NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_nav_content_main);
//                                        navController.navigate(R.id.nav_accommodations, args);
                                        navController.popBackStack();
                                        break;
                                    }
                                    case Owner: {
                                        prepareOwnerMenu();
                                        NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_nav_content_main);
//                                        navController.navigate(R.id.nav_accommodations, args);
                                        navController.popBackStack();
                                        break;
                                    }
                                    case Admin: {
                                        prepareAdminMenu();
                                        NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_nav_content_main);
//                                        navController.navigate(R.id.nav_accommodations, args);
                                        navController.popBackStack();
                                        break;
                                    }
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        else {
                            Log.d("REZ","Message received: "+response.code());
                            UserInfo.setToken(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                        UserInfo.setToken(null);
                    }
                });
            }
        });

        return root;
    }

    private void prepareGuestMenu() {
        com.google.android.material.navigation.NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (menuItem.getTitle().equals("Login") ||
                    menuItem.getTitle().equals("Register")
            )
                menuItem.setVisible(false);
            if (menuItem.getTitle().equals("Reservations") ||
                    menuItem.getTitle().equals("Account") ||
                    menuItem.getTitle().equals("Favourite Accommodations") ||
                    menuItem.getTitle().equals("Notifications")
            )
                menuItem.setVisible(true);
        }
    }

    private void prepareOwnerMenu() {
        com.google.android.material.navigation.NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (menuItem.getTitle().equals("Login") ||
                    menuItem.getTitle().equals("Register")
            )
                menuItem.setVisible(false);
            if (menuItem.getTitle().equals("Create Accommodation") ||
                    menuItem.getTitle().equals("Account") ||
                    menuItem.getTitle().equals("My Accommodations") ||
                    menuItem.getTitle().equals("Reservations") ||
                    menuItem.getTitle().equals("Notifications")
            )
                menuItem.setVisible(true);
        }
    }

    private void prepareAdminMenu() {
        com.google.android.material.navigation.NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (menuItem.getTitle().equals("Login") ||
                    menuItem.getTitle().equals("Register")
            )
                menuItem.setVisible(false);
            if (menuItem.getTitle().equals("Requests") ||
                    menuItem.getTitle().equals("Account") ||
                    menuItem.getTitle().equals("Accommodation Reviews") ||
                    menuItem.getTitle().equals("Owner Reviews")
            )
                menuItem.setVisible(true);
        }
    }
}
