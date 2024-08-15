package com.bookingapp.fragments.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bookingapp.R;
import com.bookingapp.databinding.FragmentAccommodationCreateBinding;
import com.bookingapp.databinding.FragmentLoginBinding;
import com.bookingapp.model.Credentials;

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
                String password = emailEt.getText().toString();
                if (email.length() == 0 || password.length() == 0)
                    return;
                Credentials credentials = new Credentials(email, password);
            }
        });

        return root;
    }
}