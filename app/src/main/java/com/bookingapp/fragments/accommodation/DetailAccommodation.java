package com.bookingapp.fragments.accommodation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookingapp.R;
import com.bookingapp.model.Accommodation;
import com.bookingapp.model.AccommodationType;
import com.bookingapp.model.DateRange;
import com.bookingapp.service.ServiceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private TextView id;
    private TextView name;
    private TextView description;
    private TextView location;
    private TextView ownerEmail;
    private TextView accommodationType;
    private TextView benefits;
    private TextView isApproved;
    private TextView isAutomaticAcceptance;
    private TextView isPriceByGuest;
    private TextView minGuests;
    private TextView maxGuests;
    private TextView reservationCancellationDeadline;
    private TextView availabilityDates;
    private ImageView imageView;
    private ImageButton deleteAccommodation;
    private ImageButton editAccommodation;

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
        if(accommodation.getId() != null){
            Call<Accommodation> call = ServiceUtils.accommodationService.getById(accommodation.getId());
            call.enqueue(new Callback<Accommodation>() {
                @Override
                public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                    if (response.code() == 200){
                        Log.d("BookingApp","GET BY ID");
                        accommodation = response.body();
                        name.setText(accommodation.getName());
                        description.setText(accommodation.getDescription());
                        location.setText(accommodation.getLocation());
                        ownerEmail.setText(accommodation.getOwnerEmail());
                        accommodationType.setText(accommodation.getAccommodationType().toString());
                        benefits.setText(benefitsToString(accommodation.getBenefits()));
                        isApproved.setText(String.valueOf(accommodation.getIsApproved()));
                        isAutomaticAcceptance.setText(String.valueOf(accommodation.getIsAutomaticAcceptance()));
                        isPriceByGuest.setText(String.valueOf(accommodation.getIsPriceByGuest()));
                        minGuests.setText(String.valueOf(accommodation.getMinGuests()));
                        maxGuests.setText(String.valueOf(accommodation.getMaxGuests()));
                        reservationCancellationDeadline.setText(String.valueOf(accommodation.getReservationCancellationDeadline()));
                        availabilityDates.setText(availabilityDatesToString(accommodation.getAvailabilityDates()));
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("BookingApp", "DetailAccommodation onCreateView()");
        View view = inflater.inflate(R.layout.fragment_detail_accommodation, container, false);

        id = view.findViewById(R.id.accommodation_id);
        id.setText(accommodation.getId().toString());

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
        benefits.setText(benefitsToString(accommodation.getBenefits()));

        isApproved = view.findViewById(R.id.accommodation_approved);
        isApproved.setText(String.valueOf(accommodation.getIsApproved()));

        isAutomaticAcceptance = view.findViewById(R.id.accommodation_automatic_acceptance);
        isAutomaticAcceptance.setText(String.valueOf(accommodation.getIsAutomaticAcceptance()));

        isPriceByGuest = view.findViewById(R.id.accommodation_price_by_guest);
        isPriceByGuest.setText(String.valueOf(accommodation.getIsPriceByGuest()));

        minGuests = view.findViewById(R.id.accommodation_min_guests);
        minGuests.setText(String.valueOf(accommodation.getMinGuests()));

        maxGuests = view.findViewById(R.id.accommodation_max_guests);
        maxGuests.setText(String.valueOf(accommodation.getMaxGuests()));

        reservationCancellationDeadline = view.findViewById(R.id.accommodation_reservation_cancellation_deadline);
        reservationCancellationDeadline.setText(String.valueOf(accommodation.getReservationCancellationDeadline()));

        availabilityDates = view.findViewById(R.id.accommodation_availability_dates);
        availabilityDates.setText(availabilityDatesToString(accommodation.getAvailabilityDates()));

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

//        editAccommodation = view.findViewById(R.id.editAccommodationButton);
//        editAccommodation.setOnClickListener(v -> {
//            Log.d("BookingApp", "EDIT ACCOMMODATION WITH ID " + accommodation.getId().toString());
//            Bundle args = new Bundle();
//            args.putLong("id", product.getId());
//            args.putString("title", product.getTitle());
//            args.putString("description", product.getDescription());
//            args.putString("image", product.getImagePath());
//            NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_nav_content_main);
//            navController.navigate(R.id.nav_product_edit, args);
//        });
        return view;
    }

    private String benefitsToString(List<String> benefits) {
        String benefitsString = "";
        for (String benefit : accommodation.getBenefits()) {
            benefitsString += benefit + ", ";
        }
        benefitsString = benefitsString.substring(0, benefitsString.length() - 2);
        return benefitsString;
    }

    private String availabilityDatesToString(List<DateRange> availabilityDates) {
        String availabilityDatesString = "";
        for (DateRange availabilityDate : accommodation.getAvailabilityDates()) {
            availabilityDatesString += availabilityDate.getStartDate().toString() + " - " + availabilityDate.getEndDate().toString() + " - " + availabilityDate.getPrice() + "e\n";
        }
        availabilityDatesString = availabilityDatesString.substring(0, availabilityDatesString.length() - 1);
        return availabilityDatesString;
    }

}
