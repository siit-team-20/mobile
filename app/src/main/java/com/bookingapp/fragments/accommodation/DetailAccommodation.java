package com.bookingapp.fragments.accommodation;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bookingapp.R;
import com.bookingapp.fragments.FragmentTransition;
import com.bookingapp.model.Accommodation;
import com.bookingapp.model.AccommodationRequest;
import com.bookingapp.model.AccommodationRequestType;
import com.bookingapp.model.AccommodationReview;
import com.bookingapp.model.AccommodationType;
import com.bookingapp.model.DateRange;
import com.bookingapp.model.Notification;
import com.bookingapp.model.NotificationType;
import com.bookingapp.model.OwnerReview;
import com.bookingapp.model.Rating;
import com.bookingapp.model.Reservation;
import com.bookingapp.model.ReservationStatus;
import com.bookingapp.model.ReservationWithAccommodation;
import com.bookingapp.model.User;
import com.bookingapp.model.UserType;
import com.bookingapp.service.ServiceUtils;
import com.bookingapp.service.UserInfo;

import org.json.JSONException;

import java.security.acl.Owner;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailAccommodation extends Fragment {
    private static final String ARG_ID = "id";
    private static final String ARG_NAME = "name";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_LOCATION = "location";
    private static final String ARG_OWNER_EMAIL = "ownerEmail";
    private static final String ARG_ACCOMMODATION_TYPE = "accommodationType";
    private static final String ARG_BENEFITS = "benefits";
    private static final String ARG_APPROVED = "isApproved";
    private static final String ARG_AUTOMATIC_ACCEPTANCE = "isAutomaticAcceptance";
    private static final String ARG_PRICE_BY_GUEST = "isPriceByGuest";
    private static final String ARG_MIN_GUESTS = "minGuests";
    private static final String ARG_MAX_GUESTS = "maxGuests";
    private static final String ARG_RESERVATION_CANCELLATION_DEADLINE = "reservationCancellationDeadline";
    private static final String ARG_AVAILABILITY_DATES = "availabilityDates";

    private Accommodation accommodation = new Accommodation();
    private ArrayList<ReservationWithAccommodation> approvedReservations = new ArrayList<>();
    private ArrayList<ReservationWithAccommodation> waitingReservations = new ArrayList<>();

    //private TextView id;
    private TextView name;
    private TextView description;
    private TextView location;
    private TextView ownerEmail;
    private TextView accommodationType;
    private TextView benefits;
//    private TextView isApproved;
    private TextView isAutomaticAcceptance;
    private TextView isPriceByGuest;
    private TextView minGuests;
    private TextView maxGuests;
    private TextView reservationCancellationDeadline;
    private TextView availabilityDates;
    private TextView reservedDates;
    private ImageView imageView;
    private ImageButton editAccommodationButton;
    private Button reservationStartDateButton;
    private Button reservationEndDateButton;
    private LocalDate reservationStartDate;
    private LocalDate reservationEndDate;
    private TextView reservationGuestNumber;
    private TextView reservationCalculatedPrice;
    private double calculatedPrice;
    private Button reserveButton;
    private Reservation newReservation;
    private RatingBar accommodationRatingBar;
    private EditText accommodationCommentEt;
    private Button accommodationReviewButton;
    private AccommodationReview newAccommodationReview;
    private RatingBar ownerRatingBar;
    private EditText ownerCommentEt;
    private Button ownerReviewButton;
    private OwnerReview newOwnerReview;

    public DetailAccommodation() { }

    public static DetailAccommodation newInstance(
            Long id, String ownerEmail, String name, String description, String location,
            int minGuests, int maxGuests, AccommodationType accommodationType,
            List<String> benefits, List<DateRange> availabilityDates, boolean isApproved,
            boolean isPriceByGuest, boolean isAutomaticAcceptance, int reservationCancellationDeadline) {
        DetailAccommodation fragment = new DetailAccommodation();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putString(ARG_NAME, name);
        args.putString(ARG_DESCRIPTION, description);
        args.putString(ARG_LOCATION, location);
        args.putString(ARG_OWNER_EMAIL, ownerEmail);
        args.putString(ARG_ACCOMMODATION_TYPE, accommodationType.toString());
        args.putStringArrayList(ARG_BENEFITS, new ArrayList<>(benefits));
        args.putBoolean(ARG_APPROVED, isApproved);
        args.putBoolean(ARG_AUTOMATIC_ACCEPTANCE, isAutomaticAcceptance);
        args.putBoolean(ARG_PRICE_BY_GUEST, isPriceByGuest);
        args.putInt(ARG_MIN_GUESTS, minGuests);
        args.putInt(ARG_MAX_GUESTS, maxGuests);
        args.putInt(ARG_RESERVATION_CANCELLATION_DEADLINE, reservationCancellationDeadline);
        args.putParcelableArrayList(ARG_AVAILABILITY_DATES, new ArrayList<>(availabilityDates));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            accommodation.setId(getArguments().getLong(ARG_ID));
            accommodation.setName(getArguments().getString(ARG_NAME));
            accommodation.setDescription(getArguments().getString(ARG_DESCRIPTION));
            accommodation.setLocation(getArguments().getString(ARG_LOCATION));
            accommodation.setOwnerEmail(getArguments().getString(ARG_OWNER_EMAIL));
            accommodation.setAccommodationType(AccommodationType.valueOf(getArguments().getString(ARG_ACCOMMODATION_TYPE)));
            accommodation.setBenefits(Arrays.asList(getArguments().getStringArray(ARG_BENEFITS)));
            accommodation.setIsApproved(getArguments().getBoolean(ARG_APPROVED));
            accommodation.setIsAutomaticAcceptance(getArguments().getBoolean(ARG_AUTOMATIC_ACCEPTANCE));
            accommodation.setIsPriceByGuest(getArguments().getBoolean(ARG_PRICE_BY_GUEST));
            accommodation.setMinGuests(getArguments().getInt(ARG_MIN_GUESTS));
            accommodation.setMaxGuests(getArguments().getInt(ARG_MAX_GUESTS));
            accommodation.setReservationCancellationDeadline(getArguments().getInt(ARG_RESERVATION_CANCELLATION_DEADLINE));
            accommodation.setAvailabilityDates(Arrays.asList(requireArguments().getParcelableArray(ARG_AVAILABILITY_DATES, DateRange.class)));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromClient();
        if(accommodation.getId() != null){
            Call<Accommodation> call = ServiceUtils.accommodationService.getById(accommodation.getId());
            call.enqueue(new Callback<Accommodation>() {
                @Override
                public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                    if (response.code() == 200){
                        Log.d("BookingApp","GET BY ID");
                        accommodation = response.body();
//                        id.setText(accommodation.getId().toString());
                        name.setText(accommodation.getName());
                        description.setText(accommodation.getDescription());
                        location.setText(accommodation.getLocation());
                        ownerEmail.setText(Html.fromHtml("<a href='#'>" + accommodation.getOwnerEmail() + "</a>", Html.FROM_HTML_MODE_LEGACY));
                        ownerEmail.setClickable(true);
                        ownerEmail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_nav_content_main);
                                com.google.android.material.navigation.NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                                Menu menu = navigationView.getMenu();
                                MenuItem menuItem = menu.findItem(R.id.nav_user_account);
                                NavigationUI.onNavDestinationSelected(menuItem, navController);
                                Bundle args = new Bundle();
                                args.putString("userEmail", accommodation.getOwnerEmail());
                                navController.navigate(R.id.nav_user_account, args,
                                        new NavOptions.Builder()
                                        .setEnterAnim(android.R.animator.fade_in)
                                        .setExitAnim(android.R.animator.fade_out).setPopUpTo(R.id.nav_accommodations, false)
                                        .build());
                            }
                        });
                        accommodationType.setText(accommodation.getAccommodationType().toString());
                        benefits.setText(benefitsToString());
//                        isApproved.setText(String.valueOf(accommodation.getIsApproved()));
                        isAutomaticAcceptance.setText(isAutomaticAcceptanceToString());
                        isPriceByGuest.setText(isPriceByGuestToString());
                        minGuests.setText(String.valueOf(accommodation.getMinGuests()));
                        maxGuests.setText(String.valueOf(accommodation.getMaxGuests()));
                        reservationCancellationDeadline.setText(String.valueOf(accommodation.getReservationCancellationDeadline()));
                        availabilityDates.setText(availabilityDatesToString());
                        reservedDates.setText(reservedDatesToString());
//                        String uri = "@drawable/" + product.getImagePath();
//                        Resources resources = getContext().getResources();
//                        final int resourceId = resources.getIdentifier(uri, "drawable", getContext().getPackageName());
                        imageView.setImageResource(R.drawable.a);

                    }else{
                        Log.d("BookingApp","Message received: "+response.code());
                    }
                }

                @Override
                public void onFailure(Call<Accommodation> call, Throwable t) {
                    Log.d("BookingApp", t.getMessage() != null?t.getMessage():"error");
                }
            });
        }
        FragmentTransition.to(AccommodationReviewListFragment.newInstance(accommodation.getId()), getActivity(), false, R.id.scroll_accommodation_reviews_list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("BookingApp", "DetailAccommodation onCreateView()");
        View view = inflater.inflate(R.layout.fragment_detail_accommodation, container, false);
        getDataFromClient();

        try {
            if (UserInfo.getToken() == null || !UserInfo.getType().equals(UserType.Guest)) {
                LinearLayout reservationLayout = view.findViewById(R.id.reservation_layout);
                reservationLayout.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        accommodationRatingBar = view.findViewById(R.id.accommodation_rating_bar);
        accommodationCommentEt = view.findViewById(R.id.accommodation_comment_input);
        accommodationReviewButton = view.findViewById(R.id.submit_accommodation_review_button);
        accommodationReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<AccommodationReview> accommodationReviewCall = null;
                if (addNewAccommodationReview()) {
                    accommodationReviewCall = ServiceUtils.accommodationReviewService.add(newAccommodationReview);
                }
                else
                    return;
                accommodationReviewCall.enqueue(new Callback<AccommodationReview>() {
                    @Override
                    public void onResponse(Call<AccommodationReview> call, Response<AccommodationReview> response) {
                        if (response.code() == 201){
                            Log.d("Accommodation Review - Create","Message received");
                            System.out.println(response.body());
                            accommodationRatingBar.setRating(0);
                            accommodationCommentEt.setText("");
                            Toast.makeText(getContext(), "Successfully left a review!", Toast.LENGTH_SHORT).show();

                            Notification notification = new Notification();
                            notification.setUserEmail(accommodation.getOwnerEmail());
                            try {
                                notification.setOtherUserEmail(UserInfo.getEmail());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            notification.setType(NotificationType.AccommodationReviewAdded);
                            List<Integer> now = new ArrayList<>();
                            now.add(LocalDateTime.now().getYear());
                            now.add(LocalDateTime.now().getMonthValue());
                            now.add(LocalDateTime.now().getDayOfMonth());
                            now.add(LocalDateTime.now().getHour());
                            now.add(LocalDateTime.now().getMinute());
                            notification.setCreatedAt(now);
                            Call<Notification> notificationCall = ServiceUtils.notificationService.add(notification);
                            notificationCall.enqueue(new Callback<Notification>() {
                                @Override
                                public void onResponse(Call<Notification> call, Response<Notification> response) {
                                    if (response.code() == 201) {
                                        Log.d("Notification-New","Message received");
                                        System.out.println(response.body());
                                    }
                                    else {
                                        Log.d("Notification-New","Message received: "+response.code());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Notification> call, Throwable t) {
                                    Log.d("Notification-New", t.getMessage() != null?t.getMessage():"error");
                                }
                            });
                        }
                        else {
                            Log.d("Accommodation Review - Create","Message received: "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<AccommodationReview> call, Throwable t) {
                        Log.d("Accommodation Review - Create", t.getMessage() != null?t.getMessage():"error");
                    }
                });
            }
        });

        ownerRatingBar = view.findViewById(R.id.owner_rating_bar);
        ownerCommentEt = view.findViewById(R.id.owner_comment_input);
        ownerReviewButton = view.findViewById(R.id.submit_owner_review_button);
        ownerReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<OwnerReview> ownerReviewCall = null;
                if (addNewOwnerReview()) {
                    ownerReviewCall = ServiceUtils.ownerReviewService.add(newOwnerReview);
                }
                else
                    return;
                ownerReviewCall.enqueue(new Callback<OwnerReview>() {
                    @Override
                    public void onResponse(Call<OwnerReview> call, Response<OwnerReview> response) {
                        if (response.code() == 201){
                            Log.d("Owner Review - Create","Message received");
                            System.out.println(response.body());
                            ownerRatingBar.setRating(0);
                            ownerCommentEt.setText("");
                            Toast.makeText(getContext(), "Successfully left a review!", Toast.LENGTH_SHORT).show();

                            Notification notification = new Notification();
                            notification.setUserEmail(accommodation.getOwnerEmail());
                            try {
                                notification.setOtherUserEmail(UserInfo.getEmail());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            notification.setType(NotificationType.OwnerReviewAdded);
                            List<Integer> now = new ArrayList<>();
                            now.add(LocalDateTime.now().getYear());
                            now.add(LocalDateTime.now().getMonthValue());
                            now.add(LocalDateTime.now().getDayOfMonth());
                            now.add(LocalDateTime.now().getHour());
                            now.add(LocalDateTime.now().getMinute());
                            notification.setCreatedAt(now);
                            Call<Notification> notificationCall = ServiceUtils.notificationService.add(notification);
                            notificationCall.enqueue(new Callback<Notification>() {
                                @Override
                                public void onResponse(Call<Notification> call, Response<Notification> response) {
                                    if (response.code() == 201) {
                                        Log.d("Notification-New","Message received");
                                        System.out.println(response.body());
                                    }
                                    else {
                                        Log.d("Notification-New","Message received: "+response.code());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Notification> call, Throwable t) {
                                    Log.d("Notification-New", t.getMessage() != null?t.getMessage():"error");
                                }
                            });
                        }
                        else {
                            Log.d("Owner Review - Create","Message received: "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<OwnerReview> call, Throwable t) {
                        Log.d("Owner Review - Create", t.getMessage() != null?t.getMessage():"error");
                    }
                });
            }
        });

        LinearLayout rateAccommodationView = view.findViewById(R.id.rate_accommodation_layout);
        if (UserInfo.getToken() != null) {
            try {
                if (UserInfo.getType().equals(UserType.Guest)) {
                    Call<ArrayList<ReservationWithAccommodation>> reservationWithAccommodationCall = ServiceUtils.reservationService.get(ReservationStatus.Finished.toString(), (long) 7, UserInfo.getEmail(), accommodation.getId());
                    reservationWithAccommodationCall.enqueue(new Callback<ArrayList<ReservationWithAccommodation>>() {
                        @Override
                        public void onResponse(Call<ArrayList<ReservationWithAccommodation>> call, Response<ArrayList<ReservationWithAccommodation>> response) {
                            if (response.code() == 200){
                                Log.d("Reservations-New","Message received");
                                System.out.println(response.body());
                                if (response.body().size() > 0)
                                    rateAccommodationView.setVisibility(View.VISIBLE);
                            }
                            else {
                                Log.d("Reservations-New","Message received: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<ReservationWithAccommodation>> call, Throwable t) {
                            Log.d("Reservations-New", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        LinearLayout rateOwnerView = view.findViewById(R.id.rate_owner_layout);
        if (UserInfo.getToken() != null) {
            try {
                if (UserInfo.getType().equals(UserType.Guest)) {
                    Call<ArrayList<ReservationWithAccommodation>> reservationWithAccommodationCall = ServiceUtils.reservationService.get(accommodation.getOwnerEmail(), ReservationStatus.Finished.toString(), UserInfo.getEmail());
                    reservationWithAccommodationCall.enqueue(new Callback<ArrayList<ReservationWithAccommodation>>() {
                        @Override
                        public void onResponse(Call<ArrayList<ReservationWithAccommodation>> call, Response<ArrayList<ReservationWithAccommodation>> response) {
                            if (response.code() == 200){
                                Log.d("Reservations-New","Message received");
                                System.out.println(response.body());
                                if (response.body().size() > 0)
                                    rateOwnerView.setVisibility(View.VISIBLE);
                            }
                            else {
                                Log.d("Reservations-New","Message received: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<ReservationWithAccommodation>> call, Throwable t) {
                            Log.d("Reservations-New", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        reservationGuestNumber = view.findViewById(R.id.reservation_guest_number);
        reservationGuestNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onInputChange();
            }
        });
        reservationCalculatedPrice = view.findViewById(R.id.reservation_calculated_price);

        reserveButton = view.findViewById(R.id.reservation_reserve_button);
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Reservation> call;
                Log.d("BookingApp", "Create reservation call");
                if (Double.parseDouble(reservationCalculatedPrice.getText().toString()) == 0)
                    return;
                try {
                    getNewReservation();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                call = ServiceUtils.reservationService.add(newReservation);
                call.enqueue(new Callback<Reservation>() {
                    @Override
                    public void onResponse(@NonNull Call<Reservation> call, @NonNull Response<Reservation> response) {
                        if (response.code() == 201){
                            Log.d("Reservations-New","Message received");
                            System.out.println(response.body());
                            Reservation reservation = response.body();
                            onResume();
                            reservationStartDateButton.setText("Pick Start Date");
                            reservationEndDateButton.setText("Pick End Date");
                            reservationGuestNumber.setText("");
                            Toast.makeText(getContext(), "Successfully made a reservation!", Toast.LENGTH_SHORT).show();

                            Notification notification = new Notification();
                            notification.setUserEmail(accommodation.getOwnerEmail());
                            try {
                                notification.setOtherUserEmail(UserInfo.getEmail());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            notification.setType(NotificationType.ReservationCreated);
                            List<Integer> now = new ArrayList<>();
                            now.add(LocalDateTime.now().getYear());
                            now.add(LocalDateTime.now().getMonthValue());
                            now.add(LocalDateTime.now().getDayOfMonth());
                            now.add(LocalDateTime.now().getHour());
                            now.add(LocalDateTime.now().getMinute());
                            notification.setCreatedAt(now);
                            Call<Notification> notificationCall = ServiceUtils.notificationService.add(notification);
                            notificationCall.enqueue(new Callback<Notification>() {
                                @Override
                                public void onResponse(Call<Notification> call, Response<Notification> response) {
                                    if (response.code() == 201) {
                                        Log.d("Notification-New","Message received");
                                        System.out.println(response.body());
                                    }
                                    else {
                                        Log.d("Notification-New","Message received: "+response.code());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Notification> call, Throwable t) {
                                    Log.d("Notification-New", t.getMessage() != null?t.getMessage():"error");
                                }
                            });
                            //getActivity().getSupportFragmentManager().popBackStack();
                        }
                        else {
                            Log.d("Reservations-New","Message received: "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Reservation> call, Throwable t) {
                        Log.d("Reservations-New", t.getMessage() != null?t.getMessage():"error");
                    }
                });
            }
        });

        reservationStartDateButton = view.findViewById(R.id.reservation_start_date_button);
        reservationStartDateButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onInputChange();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
        reservationStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        reservationStartDateButton.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year + ".");
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        reservationEndDateButton = view.findViewById(R.id.reservation_end_date_button);
        reservationEndDateButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onInputChange();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        reservationEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        reservationEndDateButton.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year + ".");
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


//        id = view.findViewById(R.id.accommodation_id);
//        id.setText(accommodation.getId().toString());

        name = view.findViewById(R.id.accommodation_name);
        name.setText(accommodation.getName());

        description = view.findViewById(R.id.accommodation_description);
        description.setText(accommodation.getDescription());

        location = view.findViewById(R.id.accommodation_location);
        location.setText(accommodation.getLocation());

        ownerEmail = view.findViewById(R.id.accommodation_owner_email);
        ownerEmail.setText(accommodation.getOwnerEmail());

        accommodationType = view.findViewById(R.id.accommodation_accommodation_type);
        accommodationType.setText(accommodation.getAccommodationType().toString());

        benefits = view.findViewById(R.id.accommodation_benefits);
        benefits.setText(benefitsToString());

//        isApproved = view.findViewById(R.id.accommodation_approved);
//        isApproved.setText(String.valueOf(accommodation.getIsApproved()));

        isAutomaticAcceptance = view.findViewById(R.id.accommodation_automatic_acceptance);
        isAutomaticAcceptance.setText(isAutomaticAcceptanceToString());

        isPriceByGuest = view.findViewById(R.id.accommodation_price_by_guest);
        isPriceByGuest.setText(isPriceByGuestToString());

        minGuests = view.findViewById(R.id.accommodation_min_guests);
        minGuests.setText(String.valueOf(accommodation.getMinGuests()));

        maxGuests = view.findViewById(R.id.accommodation_max_guests);
        maxGuests.setText(String.valueOf(accommodation.getMaxGuests()));

        reservationCancellationDeadline = view.findViewById(R.id.accommodation_reservation_cancellation_deadline);
        reservationCancellationDeadline.setText(String.valueOf(accommodation.getReservationCancellationDeadline()));

        availabilityDates = view.findViewById(R.id.accommodation_availability_dates);
        availabilityDates.setText(availabilityDatesToString());

        reservedDates = view.findViewById(R.id.accommodation_reserved_dates);
        reservedDates.setText(reservedDatesToString());

        imageView = view.findViewById(R.id.accommodation_image);
//        String uri = "@drawable/" + product.getImagePath();
//        Resources resources = getContext().getResources();
//        final int resourceId = resources.getIdentifier(uri, "drawable", getContext().getPackageName());
        imageView.setImageResource(R.drawable.a);

//        deleteAccommodation = view.findViewById(R.id.deleteAccommodationButton);
//        deleteAccommodation.setOnClickListener(v -> {
//            Log.d("BookingApp", "DELETE ACCOMMODATION WITH ID " + accommodation.getId().toString());
//            Call<ResponseBody> call = ServiceUtils.accommodationService.deleteById(accommodation.getId());
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    if (response.code() == 200){
//                        Log.d("BookingApp","Message received");
//                        System.out.println(response.body());
//                        NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_nav_content_main);
//                        navController.navigate(R.id.nav_accommodations);
//                    }else{
//                        Log.d("BookingApp","Message received: "+response.code());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    Log.d("ShopApp", t.getMessage() != null?t.getMessage():"error");
//                }
//            });
//        });

        editAccommodationButton = view.findViewById(R.id.editAccommodationButton);
        editAccommodationButton.setOnClickListener(v -> {
            Log.d("BookingApp", "EDIT ACCOMMODATION WITH ID " + accommodation.getId().toString());
            Bundle args = new Bundle();
            args.putLong("id", accommodation.getId());
            args.putString("name", accommodation.getName());
            args.putString("description", accommodation.getDescription());
            args.putString("location", accommodation.getLocation());
            args.putString("ownerEmail", accommodation.getOwnerEmail());
            args.putString("accommodationType", accommodation.getAccommodationType().toString());
            args.putStringArray("benefits", accommodation.getBenefits().toArray(new String[0]));
            args.putBoolean("isApproved", accommodation.getIsApproved());
            args.putBoolean("isAutomaticAcceptance", accommodation.getIsAutomaticAcceptance());
            args.putBoolean("isPriceByGuest", accommodation.getIsPriceByGuest());
            args.putInt("minGuests", accommodation.getMinGuests());
            args.putInt("maxGuests", accommodation.getMaxGuests());
            args.putInt("reservationCancellationDeadline", accommodation.getReservationCancellationDeadline());
            args.putParcelableArray("availabilityDates", accommodation.getAvailabilityDates().toArray(new DateRange[accommodation.getAvailabilityDates().size()]));
            NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_nav_content_main);
            navController.navigate(R.id.nav_create_accommodation, args);
        });

        try {
            if(UserInfo.getToken() != null)
                if(UserInfo.getEmail().equals(accommodation.getOwnerEmail()) && accommodation.getIsApproved())
                {
                    editAccommodationButton.setVisibility(View.VISIBLE);
                }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return view;
    }

    private String benefitsToString() {
        String benefitsString = "";
        for (String benefit : accommodation.getBenefits()) {
            benefitsString += benefit + ", ";
        }
        benefitsString = benefitsString.substring(0, benefitsString.length() - 2);
        return benefitsString;
    }

    private String isAutomaticAcceptanceToString() {
        if (accommodation.getIsAutomaticAcceptance())
            return "Automatic";
        return "Manual";
    }

    private String isPriceByGuestToString() {
        if (accommodation.getIsPriceByGuest())
            return "Per guest";
        return "Per day";
    }

    private String availabilityDatesToString() {
        String availabilityDatesString = "";
        for (DateRange availabilityDate : accommodation.getAvailabilityDates()) {
            String startDate = availabilityDate.getStartDate().get(2) + "." + availabilityDate.getStartDate().get(1) + "." + availabilityDate.getStartDate().get(0) + ".";
            String endDate = availabilityDate.getEndDate().get(2) + "." + availabilityDate.getEndDate().get(1) + "." + availabilityDate.getEndDate().get(0) + ".";
            availabilityDatesString += startDate + " to " + endDate + " - " + availabilityDate.getPrice() + "e\n";
        }
        if (availabilityDatesString.length() == 0)
            return "None";
        availabilityDatesString = availabilityDatesString.substring(0, availabilityDatesString.length() - 1);
        return availabilityDatesString;
    }

    private String reservedDatesToString() {
        String reservedDatesString = "";
        for (ReservationWithAccommodation reservation : approvedReservations) {
            String startDate = reservation.getDate().get(2) + "." + reservation.getDate().get(1) + "." + reservation.getDate().get(0) + ".";
            reservedDatesString += "From: " + startDate + " for " + reservation.getDays() + "day(s)\n";
        }
        for (ReservationWithAccommodation reservation : waitingReservations) {
            String startDate = reservation.getDate().get(2) + "." + reservation.getDate().get(1) + "." + reservation.getDate().get(0) + ".";
            reservedDatesString += "From: " + startDate + " for " + reservation.getDays() + "day(s)\n";
        }
        if (reservedDatesString.length() == 0)
            return "None";
        reservedDatesString = reservedDatesString.substring(0, reservedDatesString.length() - 1);
        return reservedDatesString;
    }

    private void getNewReservation() throws JSONException {
        int guestNumber = -1;
        try {
            guestNumber = Integer.parseInt(reservationGuestNumber.getText().toString());
        }
        catch (Exception e) {
            return;
        }
        newReservation = new Reservation();
        newReservation.setAccommodationId(accommodation.getId());
        List<Integer> reservationDate = new ArrayList<Integer>();
        reservationDate.add(reservationStartDate.getYear());
        reservationDate.add(reservationStartDate.getMonthValue());
        reservationDate.add(reservationStartDate.getDayOfMonth());
        newReservation.setDate(reservationDate);
        newReservation.setDays((int) Duration.between(reservationStartDate.atStartOfDay(), reservationEndDate.atStartOfDay()).toDays());
        newReservation.setStatus(ReservationStatus.Waiting);
        if (accommodation.getIsAutomaticAcceptance())
            newReservation.setStatus(ReservationStatus.Approved);
        newReservation.setGuestEmail(UserInfo.getEmail());
        newReservation.setGuestNumber(guestNumber);
        newReservation.setPrice(Double.parseDouble(reservationCalculatedPrice.getText().toString()));
    }

    private void getDataFromClient() {
        Call<ArrayList<ReservationWithAccommodation>> call = ServiceUtils.reservationService.get(accommodation.getId(), ReservationStatus.Approved.name());
        call.enqueue(new Callback<ArrayList<ReservationWithAccommodation>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ReservationWithAccommodation>> call, @NonNull Response<ArrayList<ReservationWithAccommodation>> response) {
                if (response.code() == 200) {
                    Log.d("Reservations-Approved","Message received");
                    System.out.println(response.body());
                    approvedReservations = response.body();
                    reservedDates.setText(reservedDatesToString());
                }
                else {
                    Log.d("Reservations-Approved","Message received: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ReservationWithAccommodation>> call, Throwable t) {
                Log.d("Reservations-Approved", t.getMessage() != null?t.getMessage():"error");
            }
        });

        call = ServiceUtils.reservationService.get(accommodation.getId(), ReservationStatus.Waiting.name());
        call.enqueue(new Callback<ArrayList<ReservationWithAccommodation>>() {
            @Override
            public void onResponse(Call<ArrayList<ReservationWithAccommodation>> call, Response<ArrayList<ReservationWithAccommodation>> response) {
                if (response.code() == 200) {
                    Log.d("Reservations-Waiting","Message received");
                    System.out.println(response.body());
                    waitingReservations = response.body();
                    reservedDates.setText(reservedDatesToString());
                }
                else {
                    Log.d("Reservations-Waiting","Message received: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ReservationWithAccommodation>> call, Throwable t) {
                Log.d("Reservations-Waiting", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }

    private void onInputChange() {
        try {
            reservationStartDate = LocalDate.parse(reservationStartDateButton.getText().toString(), DateTimeFormatter.ofPattern("d.M.yyyy."));
            reservationEndDate = LocalDate.parse(reservationEndDateButton.getText().toString(), DateTimeFormatter.ofPattern("d.M.yyyy."));
        }
        catch (Exception e) {
            calculatedPrice = 0;
            reservationCalculatedPrice.setText(String.valueOf(calculatedPrice));
            return;
        }
        if (reservationGuestNumber.getText().toString().length() == 0) {
            calculatedPrice = 0;
            reservationCalculatedPrice.setText(String.valueOf(calculatedPrice));
            return;
        }

        calculatedPrice = 0;
        boolean validRange = false;
        boolean validStart = false;
        boolean validStartBeforeTomorrow = false;
        boolean validGuestNumber = false;
        boolean validAvailable = true;

        if (reservationStartDate.isBefore(reservationEndDate))
            validStart = true;

        for (ReservationWithAccommodation reservation : approvedReservations) {
            LocalDate rStartDate = LocalDate.of(reservation.getDate().get(0), reservation.getDate().get(1), reservation.getDate().get(2));
            LocalDate rEndDate = LocalDate.of(reservation.getDate().get(0), reservation.getDate().get(1), reservation.getDate().get(2));
            rEndDate = rEndDate.plusDays(reservation.getDays());
            if (DateRange.isOverlapping(rStartDate, rEndDate, reservationStartDate, reservationEndDate)) {
                validAvailable = false;
                calculatedPrice = 0;
                reservationCalculatedPrice.setText(String.valueOf(calculatedPrice));
                break;
            }
        }

        for (ReservationWithAccommodation reservation : waitingReservations) {
            LocalDate rStartDate = LocalDate.of(reservation.getDate().get(0), reservation.getDate().get(1), reservation.getDate().get(2));
            LocalDate rEndDate = LocalDate.of(reservation.getDate().get(0), reservation.getDate().get(1), reservation.getDate().get(2));
            rEndDate = rEndDate.plusDays(reservation.getDays());
            if (DateRange.isOverlapping(rStartDate, rEndDate, reservationStartDate, reservationEndDate)) {
                validAvailable = false;
                calculatedPrice = 0;
                reservationCalculatedPrice.setText(String.valueOf(calculatedPrice));
                break;
            }
        }

        LocalDate tomorrow = LocalDate.now();
        tomorrow = tomorrow.plusDays(1);

        if (!reservationStartDate.isBefore(tomorrow)) {
            validStartBeforeTomorrow = true;
        }
        int guestNumber = 0;
        try {
            guestNumber = Integer.parseInt(reservationGuestNumber.getText().toString());
        }
        catch (Exception e) {
            calculatedPrice = 0;
            reservationCalculatedPrice.setText(String.valueOf(calculatedPrice));
            return;
        }
        if (guestNumber >= accommodation.getMinGuests() && guestNumber <= accommodation.getMaxGuests()) {
            validGuestNumber = true;
        }

        for (int i = 0; i < accommodation.getAvailabilityDates().size(); i++) {

            double priceCurrent = accommodation.getAvailabilityDates().get(i).getPrice();
            List<Integer> startDateList = accommodation.getAvailabilityDates().get(i).getStartDate();
            LocalDate startDateCurrent = LocalDate.of(startDateList.get(0), startDateList.get(1), startDateList.get(2));
            List<Integer> endDateList = accommodation.getAvailabilityDates().get(i).getEndDate();
            LocalDate endDateCurrent = LocalDate.of(endDateList.get(0), endDateList.get(1), endDateList.get(2));

            if (DateRange.isBetween(startDateCurrent, endDateCurrent, reservationStartDate, reservationEndDate)) {
                int days = (int) Duration.between(reservationStartDate.atStartOfDay(), reservationEndDate.atStartOfDay()).toDays();
                if (validStart && validGuestNumber && validStartBeforeTomorrow && validAvailable)
                    calculatedPrice += days * priceCurrent;
                validRange = true;
                break;
            }
            else {
                if (i != accommodation.getAvailabilityDates().size() - 1) {
                    double pricePast = accommodation.getAvailabilityDates().get(i).getPrice();
                    List<Integer> startDatePastList = accommodation.getAvailabilityDates().get(i).getStartDate();
                    LocalDate startDatePast = LocalDate.of(startDatePastList.get(0), startDatePastList.get(1), startDatePastList.get(2));
                    List<Integer> endDatePastList = accommodation.getAvailabilityDates().get(i).getEndDate();
                    LocalDate endDatePast = LocalDate.of(endDatePastList.get(0), endDatePastList.get(1), endDatePastList.get(2));

                    if (!reservationStartDate.isBefore(startDateCurrent) && reservationStartDate.isBefore(endDateCurrent)) {
                        int days = (int) Duration.between(reservationStartDate.atStartOfDay(), endDateCurrent.atStartOfDay()).toDays();
                        if (validStart && validGuestNumber && validStartBeforeTomorrow && validAvailable)
                            calculatedPrice += days * priceCurrent;

                        for (int j = i + 1; j < accommodation.getAvailabilityDates().size(); j++) {
                            double priceNext = accommodation.getAvailabilityDates().get(j).getPrice();
                            List<Integer> startDateNextList = accommodation.getAvailabilityDates().get(j).getStartDate();
                            LocalDate startDateNext = LocalDate.of(startDateNextList.get(0), startDateNextList.get(1), startDateNextList.get(2));
                            List<Integer> endDateNextList = accommodation.getAvailabilityDates().get(j).getEndDate();
                            LocalDate endDateNext = LocalDate.of(endDateNextList.get(0), endDateNextList.get(1), endDateNextList.get(2));

                            if (startDateNext.isEqual(endDatePast)) {
                                if (!reservationEndDate.isBefore(startDateNext) && !reservationEndDate.isAfter(endDateNext)) {
                                    days = (int) Duration.between(startDateNext.atStartOfDay(), reservationEndDate.atStartOfDay()).toDays();
                                    if (validStart && validGuestNumber && validStartBeforeTomorrow && validAvailable)
                                        calculatedPrice += days * priceNext;
                                    validRange = true;
                                    break;
                                }
                                else {
                                    days = (int) Duration.between(startDateNext.atStartOfDay(), endDateNext.atStartOfDay()).toDays();
                                    if (validStart && validGuestNumber && validStartBeforeTomorrow && validAvailable)
                                        calculatedPrice += days * priceNext;
                                }
                            }
                            startDatePast = startDateNext;
                            endDatePast = endDateNext;
                            pricePast = priceNext;
                        }
                    }
                }
            }
        }
        if (accommodation.getIsPriceByGuest())
            calculatedPrice *= Integer.parseInt(reservationGuestNumber.getText().toString());
        reservationCalculatedPrice.setText(String.valueOf(calculatedPrice));
    }

    private boolean addNewAccommodationReview() {
        try {
            Integer rating = (int) this.accommodationRatingBar.getRating();
            String comment = this.accommodationCommentEt.getText().toString();
            if (rating < 1 || rating > 5 || comment.length() == 0)
                return false;
            newAccommodationReview = new AccommodationReview();
            if (rating.equals(5))
                newAccommodationReview.setRating(Rating.five);
            else if (rating.equals(4))
                newAccommodationReview.setRating(Rating.four);
            else if (rating.equals(3))
                newAccommodationReview.setRating(Rating.three);
            else if (rating.equals(2))
                newAccommodationReview.setRating(Rating.two);
            else if (rating.equals(1))
                newAccommodationReview.setRating(Rating.one);
            newAccommodationReview.setGuestEmail(UserInfo.getEmail());
            newAccommodationReview.setComment(comment);
            newAccommodationReview.setAccommodationId(accommodation.getId());
            newAccommodationReview.setIsApproved(false);
            List<Integer> date = new ArrayList<>();
            date.add(LocalDate.now().getYear());
            date.add(LocalDate.now().getMonthValue());
            date.add(LocalDate.now().getDayOfMonth());
            newAccommodationReview.setSubmitDate(date);
            return true;
        }
        catch (Exception e) {
            Log.d("Error", e.getMessage());
            Log.d("Error", "Inputs not valid");
            return false;
        }
    }

    private boolean addNewOwnerReview() {
        try {
            Integer rating = (int) this.ownerRatingBar.getRating();
            String comment = this.ownerCommentEt.getText().toString();
            if (rating < 1 || rating > 5 || comment.length() == 0)
                return false;
            newOwnerReview = new OwnerReview();
            if (rating.equals(5))
                newOwnerReview.setRating(Rating.five);
            else if (rating.equals(4))
                newOwnerReview.setRating(Rating.four);
            else if (rating.equals(3))
                newOwnerReview.setRating(Rating.three);
            else if (rating.equals(2))
                newOwnerReview.setRating(Rating.two);
            else if (rating.equals(1))
                newOwnerReview.setRating(Rating.one);
            newOwnerReview.setGuestEmail(UserInfo.getEmail());
            newOwnerReview.setComment(comment);
            newOwnerReview.setOwnerEmail(accommodation.getOwnerEmail());
            newOwnerReview.setIsReported(false);
            List<Integer> date = new ArrayList<>();
            date.add(LocalDate.now().getYear());
            date.add(LocalDate.now().getMonthValue());
            date.add(LocalDate.now().getDayOfMonth());
            newOwnerReview.setSubmitDate(date);
            return true;
        }
        catch (Exception e) {
            Log.d("Error", e.getMessage());
            Log.d("Error", "Inputs not valid");
            return false;
        }
    }
}
