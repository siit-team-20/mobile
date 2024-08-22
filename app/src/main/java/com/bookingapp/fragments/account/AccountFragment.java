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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bookingapp.R;
import com.bookingapp.adapters.AccommodationListAdapter;
import com.bookingapp.databinding.FragmentAccountBinding;
import com.bookingapp.fragments.accommodation.AccommodationListFragment;
import com.bookingapp.model.Accommodation;
import com.bookingapp.model.Reservation;
import com.bookingapp.model.ReservationWithAccommodation;
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
import retrofit2.http.Path;

public class AccountFragment extends Fragment {

    private boolean isPermissions = true;
    private String permission = android.Manifest.permission.READ_CONTACTS;
    private FragmentAccountBinding binding;
    private ActivityResultLauncher<String> mPermissionResult;

    private EditText nameText;
    private EditText surnameText;
    private EditText emailText;
    private EditText addressText;
    private EditText phoneText;
    private EditText newPasswordText;
    private EditText confirmPasswordText;
    private Button editButton;
    private Button deleteButton;
    private Button logOutButton;
    private Button saveChangesButton;
    private Button changePasswordButton;
    private Button cancelButton;
    private RelativeLayout newPasswordLayout;
    private RelativeLayout confirmPasswordLayout;
/*
    private static final String ARG_NAME = "name";
    private static final String ARG_SURNAME = "surname";
    private static final String ARG_EMAIL = "email";
    private static final String ARG_ADDRESS = "address";
    private static final String ARG_PHONE = "phone";*/

    private User user;
    private User updateUser;

    public AccountFragment() {
    }
    /*public static AccountFragment newInstance(String name, String surname, String email, String address, String phone) {
        AccountFragment fragment = new AccountFragment();

        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_SURNAME, surname);
        args.putString(ARG_EMAIL, email);
        args.putString(ARG_ADDRESS, address);
        args.putString(ARG_PHONE, phone);
        fragment.setArguments(args);
        return fragment;
    }*/


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
        saveChangesButton = (Button) binding.saveChangesButton;
        logOutButton = (Button) binding.logoutButton;
        deleteButton = (Button) binding.deleteButton;
        changePasswordButton = (Button) binding.changePasswordButton;
        cancelButton = (Button) binding.cancelButton;
        newPasswordLayout = (RelativeLayout) binding.newPasswordLayout;
        confirmPasswordLayout = (RelativeLayout) binding.confirmPasswordLayout;


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameText.setEnabled(true);
                surnameText.setEnabled(true);
                addressText.setEnabled(true);
                phoneText.setEnabled(true);
                saveChangesButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                changePasswordButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.INVISIBLE);
                logOutButton.setVisibility(View.INVISIBLE);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameText.setEnabled(false);
                surnameText.setEnabled(false);
                addressText.setEnabled(false);
                phoneText.setEnabled(false);
                saveChangesButton.setVisibility(View.INVISIBLE);
                cancelButton.setVisibility(View.INVISIBLE);
                changePasswordButton.setVisibility(View.INVISIBLE);
                changePasswordButton.setVisibility(View.INVISIBLE);
                newPasswordLayout.setVisibility(View.INVISIBLE);
                confirmPasswordLayout.setVisibility(View.INVISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                logOutButton.setVisibility(View.VISIBLE);
            }
        });
        saveChangesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Call<User> call;
                Log.d("BookingApp", "Update user");
                if(updateUser())
                    call = ServiceUtils.userService.update(user.getEmail(), updateUser);
                else
                    return;

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        if (response.code() == 200){
                            Log.d("REZ","Meesage recieved");
                            System.out.println(response.body());
                            User user1 = response.body();
                            System.out.println(user1);
                            UserInfo.setToken(response.body().getToken());

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
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    deleteUser();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPasswordLayout.setVisibility(View.VISIBLE);
                confirmPasswordLayout.setVisibility(View.VISIBLE);
                saveChangesButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
            }
        });

        getData();
    }


    private boolean updateUser() {
        try {
            String name = nameText.getText().toString();
            String surname = surnameText.getText().toString();
//            String newPassword = newPasswordText.getText().toString();
//            String confirmPassword = confirmPasswordText.getText().toString();
            String address = addressText.getText().toString();
            String phone = phoneText.getText().toString();
            if (name.isEmpty() && surname.isEmpty() && address.isEmpty() && phone.isEmpty()) {
                return false;
            }

            updateUser = new User();
            if (newPasswordLayout.getVisibility() == View.VISIBLE && confirmPasswordLayout.getVisibility() == View.VISIBLE) {
                String newPassword = newPasswordText.getText().toString();
                String confirmPassword = confirmPasswordText.getText().toString();

                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    return false;
                }

                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(getContext(), "Lozinke se ne poklapaju.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                updateUser.setPassword(newPassword);
            }
            else {
                updateUser.setPassword("");
            }

            updateUser.setEmail(user.getEmail());
            updateUser.setType(user.getType());
            updateUser.setIsBlocked(user.getIsBlocked());
            updateUser.setName(name);
            updateUser.setSurname(surname);
            updateUser.setAddress(address);
            updateUser.setPhone(phone);

            return true;
        }
        catch (Exception e) {
            Log.d("Error", e.getMessage());
            Log.d("Error", "Inputs not valid");
            return false;
        }
    }

    private void deleteUser() throws JSONException {
        String query = "";
        if (UserInfo.getType().equals(UserType.Guest)) {

            //provera dal gost ima approved
            ServiceUtils.reservationService.getGuestReservations(user.getEmail(),"Approved").enqueue(new Callback<ArrayList<ReservationWithAccommodation>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<ReservationWithAccommodation>> call, Response<ArrayList<ReservationWithAccommodation>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().isEmpty())
                            deleteWaitingReservationsAndAccount(user.getEmail(), "Guest");
                    } else {
                        Toast.makeText(getContext(), "You have future reservations!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<ReservationWithAccommodation>> call, @NonNull Throwable t) {
                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                }
            });
        } else if (user.getType().equals(UserType.Owner)) {

            //provera dal vlasnik ima approved
            ServiceUtils.reservationService.getOwnerReservations(user.getEmail(),"Approved").enqueue(new Callback<ArrayList<ReservationWithAccommodation>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<ReservationWithAccommodation>> call, @NonNull Response<ArrayList<ReservationWithAccommodation>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().isEmpty())
                            deleteWaitingReservationsAndAccount(user.getEmail(), "Guest");
                    } else {
                        Toast.makeText(getContext(), "You have future reservations!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<ReservationWithAccommodation>> call, @NonNull Throwable t) {
                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                }
            });
        }
    }

    private void deleteWaitingReservationsAndAccount(String email, String role) {

        if (role.equals("Guest")) {
            ServiceUtils.reservationService.deleteGuestReservations(email,"Waiting").enqueue(new Callback<ArrayList<ReservationWithAccommodation>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<ReservationWithAccommodation>> call, @NonNull Response<ArrayList<ReservationWithAccommodation>> response) {
                    if (response.isSuccessful()) {
                        deleteUserAccount(email, role);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<ReservationWithAccommodation>> call, @NonNull Throwable t) {
                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error deleting reservations");
                }
            });
        } else if (role.equals("Owner")) {
            ServiceUtils.reservationService.deleteOwnerReservations(email,"Waiting").enqueue(new Callback<ArrayList<ReservationWithAccommodation>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<ReservationWithAccommodation>> call, @NonNull Response<ArrayList<ReservationWithAccommodation>> response) {
                    if (response.isSuccessful()) {
                        deleteUserAccount(email, role);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<ReservationWithAccommodation>> call, @NonNull Throwable t) {
                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error deleting reservations");
                }
            });
        }
    }

    private void deleteUserAccount(String email, String role) {
        //prvo brisemo smestaje za vlasnika
        if (role.equals("Owner")) {
            ServiceUtils.accommodationService.delete(email).enqueue(new Callback<ArrayList<Accommodation>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Accommodation>> call, @NonNull Response<ArrayList<Accommodation>> response) {
                    if (response.isSuccessful()) {
                        // Nakon brisanja smeštaja, brišemo nalog
                        deleteAccount(email);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Accommodation>> call, @NonNull Throwable t) {
                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error deleting accommodations");
                }
            });
        } else {
            // Ako nije vlasnik, samo brišemo nalog
            deleteAccount(email);
        }
    }

    private void deleteAccount(String email) {
        ServiceUtils.userService.delete(email).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {

                } else {
                    Log.d("REZ","Message received: "+response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });
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
        Call<User> call = ServiceUtils.userService.getUser("owner@gmail.com");
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