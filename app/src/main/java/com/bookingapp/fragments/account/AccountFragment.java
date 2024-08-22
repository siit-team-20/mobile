package com.bookingapp.fragments.account;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bookingapp.R;
import com.bookingapp.adapters.AccommodationListAdapter;
import com.bookingapp.databinding.FragmentAccountBinding;
import com.bookingapp.fragments.FragmentTransition;
import com.bookingapp.fragments.accommodation.AccommodationListFragment;
import com.bookingapp.model.Accommodation;
import com.bookingapp.model.User;
import com.bookingapp.service.ServiceUtils;
import com.bookingapp.service.UserInfo;

import org.json.JSONException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;

public class AccountFragment extends Fragment {

    private static final String ARG_USER_EMAIL = "userEmail";
    private boolean isPermissions = true;
    private String permission = android.Manifest.permission.READ_CONTACTS;
    private FragmentAccountBinding binding;
    private ActivityResultLauncher<String> mPermissionResult;

    private EditText nameText;
    private EditText surnameText;
    private EditText emailText;
    private EditText addressText;
    private EditText phoneText;
    private Button editButton;
    private Button deleteButton;
    private Button logOutButton;
    private Button saveChangesButton;
    private Button changePasswordButton;
    private User user;
    private User updateUser;
    private String userEmail;

    public AccountFragment() {
    }
    public static AccountFragment newInstance(String userEmail) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_EMAIL, userEmail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userEmail = getArguments().getString(ARG_USER_EMAIL);
        }
        if (getArguments().getString(ARG_USER_EMAIL) == null) {
            try {
                userEmail = UserInfo.getEmail();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nameText = (EditText) binding.name;
        surnameText = (EditText) binding.surname;
        emailText = (EditText) binding.email;
        //passwordText = (EditText) binding.password;
        addressText = (EditText) binding.address;
        phoneText = (EditText) binding.phone;

        /*if(updateUser != null) {
            nameText.setText(updateUser.getName());
            surnameText.setText(updateUser.getSurname());
            emailText.setText(updateUser.getEmail());
            //passwordText.setText(updateUser.getPassword());
            addressText.setText(updateUser.getAddress());
            phoneText.setText(updateUser.getPhone());
        }*/


        editButton = (Button) binding.editButton;
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameText.setEnabled(true);
                surnameText.setEnabled(true);
                addressText.setEnabled(true);
                emailText.setEnabled(true);
                phoneText.setEnabled(true);
            }
        });
        /*saveChangesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Call<User> call;
                Log.d("BookingApp", "Update user");
                updateUser();
                call = ServiceUtils.userService.update(updateUser, user.getEmail());

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 200){
                            Log.d("REZ","Meesage recieved");
                            System.out.println(response.body());
                            User user1 = response.body();
                            System.out.println(user1);
                            //getActivity().getSupportFragmentManager().popBackStack();
                        }else{
                            Log.d("REZ","Meesage recieved: "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                    }
                });

            }
        });*/
        getData();
    }

    private void updateUser(){
        String name = nameText.getText().toString();
        String surname = surnameText.getText().toString();
        String email = emailText.getText().toString();
        //String password = passwordText.getText().toString();
        String address = addressText.getText().toString();
        String phone = phoneText.getText().toString();
        if (name.length() == 0 && surname.length() == 0) {
            return;
        }
        updateUser.setName(name);
        updateUser.setSurname(surname);

    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getData() {
        Call<User> call = ServiceUtils.userService.getUser(userEmail);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    Log.d("REZ","Message received");
                    System.out.println(response.body());
                    user = response.body();
                    nameText.setText(user.getName());
                    surnameText.setText(user.getSurname());
                    emailText.setText(user.getEmail());
                    addressText.setText(user.getAddress());
                    phoneText.setText(user.getPhone());
                    FragmentTransition.to(OwnerReviewListFragment.newInstance(userEmail), getActivity(), false, R.id.scroll_owner_reviews_list);
                }
                else {
                    Log.d("REZ","Message received: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

/*
    private void addMenu()
    {
        MenuProvider menuProvider = new MenuProvider()
        {

            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater)
            {
                menu.clear();
                menuInflater.inflate(R.menu.toolbar_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem)
            {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_nav_content_main);
                return NavigationUI.onNavDestinationSelected(menuItem, navController);
            }
        };

        requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }*/

}