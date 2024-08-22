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
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bookingapp.R;
import com.bookingapp.databinding.FragmentLoginBinding;
import com.bookingapp.databinding.FragmentRegisterBinding;
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

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    private EditText emailEt;
    private EditText passwordEt;
    private EditText confirmPasswordEt;
    private EditText nameEt;
    private EditText surnameEt;
    private EditText addressEt;
    private EditText phoneEt;
    private RadioGroup userTypeRg;
    private Button registerButton;
    private User newUser;

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


    public RegisterFragment() {
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        emailEt = binding.etEmail;
        passwordEt = binding.etPassword;
        confirmPasswordEt = binding.etConfirmPassword;
        nameEt = binding.etFullName;
        surnameEt = binding.etSurname;
        registerButton = binding.btnRegister;
        addressEt = binding.etAddress;
        phoneEt = binding.etPhoneNumber;
        TextView loginLink = binding.tvLogin;
        userTypeRg = binding.radioGroupRegisterAs;

        loginLink.setClickable(true);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_nav_content_main);
                com.google.android.material.navigation.NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                Menu menu = navigationView.getMenu();
                MenuItem menuItem = menu.findItem(R.id.nav_login);
                NavigationUI.onNavDestinationSelected(menuItem, navController);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!addNewUser())
                    return;
                Call<User> call = ServiceUtils.userService.register(newUser);
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

    private boolean addNewUser() {
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();
        String confirmPassword = confirmPasswordEt.getText().toString();
        String name = nameEt.getText().toString();
        String surname = surnameEt.getText().toString();
        String address = addressEt.getText().toString();
        String phone = phoneEt.getText().toString();
        RadioButton userTypeRb = userTypeRg.findViewById(userTypeRg.getCheckedRadioButtonId());
        UserType userType = UserType.Owner;
        if (userTypeRb.getText().toString().equals("Guest"))
            userType = UserType.Guest;

        if (email.length() == 0 || password.length() == 0 || confirmPassword.length() == 0 ||
        !password.equals(confirmPassword) || name.length() == 0 || surname.length() == 0 ||
        address.length() == 0 || phone.length() < 9 || phone.length() > 10)
            return false;

        newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setName(name);
        newUser.setSurname(surname);
        newUser.setAddress(address);
        newUser.setPhone(phone);
        newUser.setType(userType);
        newUser.setIsBlocked(false);
        return true;
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
                    menuItem.getTitle().equals("Favourite Accommodations")
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
                    menuItem.getTitle().equals("Reservations")
            )
                menuItem.setVisible(true);
        }
    }
}