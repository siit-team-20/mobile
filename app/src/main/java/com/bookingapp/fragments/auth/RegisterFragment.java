package com.bookingapp.fragments.auth;

import android.os.Bundle;

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

import com.bookingapp.R;
import com.bookingapp.databinding.FragmentLoginBinding;
import com.bookingapp.databinding.FragmentRegisterBinding;
import com.bookingapp.model.Credentials;
import com.bookingapp.model.User;
import com.bookingapp.model.UserType;
import com.bookingapp.service.ServiceUtils;
import com.bookingapp.service.UserInfo;

import org.json.JSONException;

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
                                switch (UserInfo.getType()) {
                                    case Guest: {
                                        prepareGuestMenu();
                                        break;
                                    }
                                    case Owner: {
                                        prepareOwnerMenu();
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

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);
        user.setAddress(address);
        user.setPhone(phone);
        user.setType(userType);
        user.setIsBlocked(false);
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
                    menuItem.getTitle().equals("Account") ||
                    menuItem.getTitle().equals("My Accommodations") ||
                    menuItem.getTitle().equals("Reservations")
            )
                menuItem.setVisible(true);
        }
    }
}