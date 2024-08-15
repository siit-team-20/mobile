package com.bookingapp.fragments.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;

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

import com.bookingapp.R;
import com.bookingapp.databinding.FragmentLoginBinding;
import com.bookingapp.model.Credentials;
import com.bookingapp.model.User;
import com.bookingapp.service.ServiceUtils;
import com.bookingapp.service.UserInfo;

import org.json.JSONException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private EditText emailEt;
    private EditText passwordEt;
    private Button loginButton;

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
                                switch (UserInfo.getType()) {
                                    case Guest: {
                                        prepareGuestMenu();
                                        break;
                                    }
                                    case Owner: {
                                        prepareOwnerMenu();
                                        break;
                                    }
                                    case Admin: {
                                        prepareAdminMenu();
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
                    menuItem.getTitle().equals("Account")
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
                    menuItem.getTitle().equals("Account")
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
        }
    }
}
