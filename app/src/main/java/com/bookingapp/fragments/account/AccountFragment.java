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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.bookingapp.fragments.FragmentTransition;
import com.bookingapp.fragments.accommodation.AccommodationListFragment;
import com.bookingapp.model.Accommodation;
import com.bookingapp.model.AccommodationReview;
import com.bookingapp.model.OwnerReview;
import com.bookingapp.model.Rating;
import com.bookingapp.model.Report;
import com.bookingapp.model.Reservation;
import com.bookingapp.model.ReservationStatus;
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
    private static final String ARG_USER_EMAIL = "userEmail";
    private String userEmail;
    private TextView averageRating;

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
    private Button reportButton;
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

    public static AccountFragment newInstance(String userEmail) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_EMAIL, userEmail);
        fragment.setArguments(args);
        return fragment;
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

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nameText = (EditText) binding.name;
        surnameText = (EditText) binding.surname;
        emailText = (EditText) binding.email;
        newPasswordText = (EditText) binding.newPassword;
        confirmPasswordText = (EditText) binding.confirmPassword;
        addressText = (EditText) binding.address;
        phoneText = (EditText) binding.phone;
        averageRating = (TextView)binding.averageRating;

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
        reportButton = binding.reportButton;

        try {
            if (UserInfo.getToken() != null) {
                if (!UserInfo.getEmail().equals(userEmail)) {
                    editButton.setVisibility(View.GONE);
                    logOutButton.setVisibility(View.GONE);
                }
                if (!UserInfo.getEmail().equals(userEmail) || UserInfo.getType().equals(UserType.Admin)) {
                    deleteButton.setVisibility(View.GONE);
                }
            }
            else {
                deleteButton.setVisibility(View.GONE);
                editButton.setVisibility(View.GONE);
                logOutButton.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        try {
            if (UserInfo.getType().equals(UserType.Admin) || UserInfo.getEmail().equals(userEmail)) {
                reportButton.setVisibility(View.GONE);
            }
            else if (UserInfo.getType().equals(UserType.Guest)) {
                Call<ArrayList<ReservationWithAccommodation>> reservationWithAccommodationCall = ServiceUtils.reservationService.get(userEmail, ReservationStatus.Finished.toString(), UserInfo.getEmail());
                reservationWithAccommodationCall.enqueue(new Callback<ArrayList<ReservationWithAccommodation>>() {
                    @Override
                    public void onResponse(Call<ArrayList<ReservationWithAccommodation>> call, Response<ArrayList<ReservationWithAccommodation>> response) {
                        if (response.code() == 200){
                            Log.d("Reservations-Update","Message received");
                            System.out.println(response.body());
                            if (response.body().size() > 0)
                                reportButton.setVisibility(View.VISIBLE);
                            else
                                reportButton.setVisibility(View.GONE);
                        }
                        else {
                            reportButton.setVisibility(View.GONE);
                            Log.d("Reservations-Update","Message received: "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<ReservationWithAccommodation>> call, Throwable t) {
                        reportButton.setVisibility(View.GONE);
                        Log.d("Reservations-Update", t.getMessage() != null?t.getMessage():"error");
                    }
                });
            }
            else if (UserInfo.getType().equals(UserType.Owner)) {
                Call<ArrayList<ReservationWithAccommodation>> reservationWithAccommodationCall = ServiceUtils.reservationService.get(UserInfo.getEmail(), ReservationStatus.Finished.toString(), userEmail);
                reservationWithAccommodationCall.enqueue(new Callback<ArrayList<ReservationWithAccommodation>>() {
                    @Override
                    public void onResponse(Call<ArrayList<ReservationWithAccommodation>> call, Response<ArrayList<ReservationWithAccommodation>> response) {
                        if (response.code() == 200){
                            Log.d("Reservations-Update","Message received");
                            System.out.println(response.body());
                            if (response.body().size() > 0)
                                reportButton.setVisibility(View.VISIBLE);
                            else
                                reportButton.setVisibility(View.GONE);
                        }
                        else {
                            reportButton.setVisibility(View.GONE);
                            Log.d("Reservations-Update","Message received: "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<ReservationWithAccommodation>> call, Throwable t) {
                        reportButton.setVisibility(View.GONE);
                        Log.d("Reservations-Update", t.getMessage() != null?t.getMessage():"error");
                    }
                });
            }
        } catch (Exception e) {
            reportButton.setVisibility(View.GONE);
        }

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Report report = new Report();
                try {
                    report.setReporterEmail(UserInfo.getEmail());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                report.setReportedEmail(userEmail);
                Call<Report> reportCall = ServiceUtils.reportService.add(report);
                reportCall.enqueue(new Callback<Report>() {
                    @Override
                    public void onResponse(@NonNull Call<Report> call, @NonNull Response<Report> response) {
                        if (response.code() == 200){
                            Log.d("Reservations-Update","Message received");
                            System.out.println(response.body());
                            Toast.makeText(getContext(), "Successfully reported user!", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Log.d("Reservations-Update","Message received: "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Report> call, @NonNull Throwable t) {
                        Log.d("Reservations-Update", t.getMessage() != null?t.getMessage():"error");
                    }
                });
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo.setToken(null);
                TextView user = getActivity().findViewById(R.id.user_name);
                user.setText("");

                com.google.android.material.navigation.NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                Menu menu = navigationView.getMenu();
                for (int i = 0; i < menu.size(); i++) {
                    MenuItem menuItem = menu.getItem(i);
                    if (menuItem.getTitle().equals("Accommodations") ||
                            menuItem.getTitle().equals("Register") ||
                            menuItem.getTitle().equals("Login")
                    )
                        menuItem.setVisible(true);
                    else
                        menuItem.setVisible(false);
                }
                NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_nav_content_main);
                navController.popBackStack();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameText.setEnabled(true);
                surnameText.setEnabled(true);
                addressText.setEnabled(true);
                phoneText.setEnabled(true);
                editButton.setVisibility(View.GONE);
                saveChangesButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                changePasswordButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.GONE);
                logOutButton.setVisibility(View.GONE);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameText.setEnabled(false);
                surnameText.setEnabled(false);
                addressText.setEnabled(false);
                phoneText.setEnabled(false);
                editButton.setVisibility(View.VISIBLE);
                saveChangesButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
                changePasswordButton.setVisibility(View.GONE);
                changePasswordButton.setVisibility(View.GONE);
                newPasswordLayout.setVisibility(View.GONE);
                confirmPasswordLayout.setVisibility(View.GONE);
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
                            Toast.makeText(getContext(), "Successfully updated your profile!", Toast.LENGTH_SHORT).show();

                            //getActivity().getSupportFragmentManager().popBackStack();
                        }else{
                            Log.d("REZ","Meesage recieved: "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
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
                            deleteWaitingReservationsAndAccount(user.getEmail(), "Owner");
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
            ServiceUtils.accommodationService.delete(email).enqueue(new Callback<Accommodation>() {
                @Override
                public void onResponse(@NonNull Call<Accommodation> call, @NonNull Response<Accommodation> response) {
                    if (response.isSuccessful()) {
                        // Nakon brisanja smeštaja, brišemo nalog
                        deleteAccount(email);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Accommodation> call, @NonNull Throwable t) {
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
        Call<ArrayList<OwnerReview>> callResponse = ServiceUtils.ownerReviewService.get(userEmail,false);
        callResponse.enqueue(new Callback<ArrayList<OwnerReview>>() {
            @Override
            public void onResponse(Call<ArrayList<OwnerReview>> call, Response<ArrayList<OwnerReview>> response) {
                if (response.code() == 200) {
                    Log.d("Reviews-Get","Message received");
                    System.out.println(response.body());
                    List<OwnerReview> ownerReviews = response.body();
                    Long counter = 0L;
                    Long sum = 0L;
                    for(int i = 0; i < ownerReviews.size();i++){
                        counter++;
                        if(ownerReviews.get(i).getRating().equals(Rating.one))
                            sum += 1;
                        else if(ownerReviews.get(i).getRating().equals(Rating.two))
                            sum += 2;
                        else if(ownerReviews.get(i).getRating().equals(Rating.three))
                            sum += 3;
                        else if(ownerReviews.get(i).getRating().equals(Rating.four))
                            sum += 4;
                        else if(ownerReviews.get(i).getRating().equals(Rating.five))
                            sum += 5;
                    }
                    if(counter == 0L){
                        averageRating.setText("None");
                        return;
                    }
                    averageRating.setText(""+sum/counter);
                }
                else {
                    Log.d("Reviews-Get","Message received: "+response.code());
                    averageRating.setText("None");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<OwnerReview>> call, Throwable t) {
                Log.d("Reviews-Get", t.getMessage() != null?t.getMessage():"error");
                averageRating.setText("None");
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