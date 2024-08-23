package com.bookingapp.fragments.accommodation;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bookingapp.R;
import com.bookingapp.databinding.FragmentAccommodationCreateBinding;
import com.bookingapp.model.Accommodation;
import com.bookingapp.model.AccommodationRequest;
import com.bookingapp.model.AccommodationRequestType;
import com.bookingapp.model.AccommodationType;
import com.bookingapp.model.DateRange;
import com.bookingapp.model.User;
import com.bookingapp.service.ServiceUtils;
import com.bookingapp.service.UserInfo;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationCreateFragment extends Fragment {
    private FragmentAccommodationCreateBinding binding;
    private static final String ARG_ID ="id";
    private static final String ARG_OWNER_EMAIL = "ownerEmail";
    private static final  String ARG_NAME = "name";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_LOCATION = "location";
    private static final String ARG_MIN_GUESTS = "minGuests";
    private static final String ARG_MAX_GUESTS = "maxGuests";
    private static final String ARG_ACCOMMODATION_TYPE = "accommodationType";
    private static final String ARG_BENEFITS = "benefits";
    private static final String ARG_AVAILABILITY_DATES = "availabilityDates";
    private static final String ARG_APPROVED = "isApproved";
    private static final String ARG_PRICE_BY_GUEST = "isPriceByGuest";
    private static final String ARG_AUTOMATIC_ACCEPTANCE = "isAutomaticAcceptance";
    private static final String ARG_RESERVATION_CANCELLATION_DEADLINE = "reservationCancellationDeadline";
    private Accommodation editAccommodation;
    private Accommodation oldAccommodation;
    private Accommodation newAccommodation;
    private EditText name;
    private EditText description;
    private EditText location;
//    private EditText ownerEmail;
    private RadioGroup accommodationType;
    private EditText benefits;
    private RadioGroup pricing;
    private EditText minGuests;
    private EditText maxGuests;
    private Button startDateButton;
    private Button endDateButton;
    private EditText price;
    private EditText reservationCancellationDeadline;
    private EditText ownerEmail;
    private View root;
    private List<List<Integer>> availabilityRangesIds = new ArrayList<>();
    private Button createButton;
    private Button updateButton;

    public AccommodationCreateFragment() {
    }

    public static AccommodationCreateFragment newInstance(Long id, String ownerEmail, String name, String description, String location,
                                                          int minGuests, int maxGuests, AccommodationType accommodationType,
                                                          List<String> benefits, List<DateRange> availabilityDates, boolean isApproved,
                                                          boolean isPriceByGuest, boolean isAutomaticAcceptance, int reservationCancellationDeadline) {
        AccommodationCreateFragment fragment = new AccommodationCreateFragment();
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
            oldAccommodation = new Accommodation();
            oldAccommodation.setId(getArguments().getLong(ARG_ID));
            oldAccommodation.setName(getArguments().getString(ARG_NAME));
            oldAccommodation.setDescription(getArguments().getString(ARG_DESCRIPTION));
            oldAccommodation.setLocation(getArguments().getString(ARG_LOCATION));
            oldAccommodation.setOwnerEmail(getArguments().getString(ARG_OWNER_EMAIL));
            oldAccommodation.setAccommodationType(AccommodationType.valueOf(getArguments().getString(ARG_ACCOMMODATION_TYPE)));
            oldAccommodation.setBenefits(Arrays.asList(getArguments().getStringArray(ARG_BENEFITS)));
            oldAccommodation.setIsApproved(getArguments().getBoolean(ARG_APPROVED));
            oldAccommodation.setIsAutomaticAcceptance(getArguments().getBoolean(ARG_AUTOMATIC_ACCEPTANCE));
            oldAccommodation.setIsPriceByGuest(getArguments().getBoolean(ARG_PRICE_BY_GUEST));
            oldAccommodation.setMinGuests(getArguments().getInt(ARG_MIN_GUESTS));
            oldAccommodation.setMaxGuests(getArguments().getInt(ARG_MAX_GUESTS));
            oldAccommodation.setReservationCancellationDeadline(getArguments().getInt(ARG_RESERVATION_CANCELLATION_DEADLINE));
            oldAccommodation.setAvailabilityDates(Arrays.asList(requireArguments().getParcelableArray(ARG_AVAILABILITY_DATES, DateRange.class)));


            editAccommodation = new Accommodation();
            editAccommodation.setId(getArguments().getLong(ARG_ID));
            editAccommodation.setName(getArguments().getString(ARG_NAME));
            editAccommodation.setDescription(getArguments().getString(ARG_DESCRIPTION));
            editAccommodation.setLocation(getArguments().getString(ARG_LOCATION));
            editAccommodation.setOwnerEmail(getArguments().getString(ARG_OWNER_EMAIL));
            editAccommodation.setAccommodationType(AccommodationType.valueOf(getArguments().getString(ARG_ACCOMMODATION_TYPE)));
            editAccommodation.setBenefits(Arrays.asList(getArguments().getStringArray(ARG_BENEFITS)));
            editAccommodation.setIsApproved(getArguments().getBoolean(ARG_APPROVED));
            editAccommodation.setIsAutomaticAcceptance(getArguments().getBoolean(ARG_AUTOMATIC_ACCEPTANCE));
            editAccommodation.setIsPriceByGuest(getArguments().getBoolean(ARG_PRICE_BY_GUEST));
            editAccommodation.setMinGuests(getArguments().getInt(ARG_MIN_GUESTS));
            editAccommodation.setMaxGuests(getArguments().getInt(ARG_MAX_GUESTS));
            editAccommodation.setReservationCancellationDeadline(getArguments().getInt(ARG_RESERVATION_CANCELLATION_DEADLINE));
            editAccommodation.setAvailabilityDates(Arrays.asList(requireArguments().getParcelableArray(ARG_AVAILABILITY_DATES, DateRange.class)));
        }
    }
    private void addNewDateRange(DateRange dateRange) {
        LinearLayout container = root.findViewById(R.id.availabilityRangesContainer);

        LinearLayout rangeLayout = new LinearLayout(getActivity());
        rangeLayout.setOrientation(LinearLayout.VERTICAL);
        rangeLayout.setPadding(0, 0, 0, 16);

        TextView textView = new TextView(getActivity());

        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        textView.setText("Availability range " + (availabilityRangesIds.size() + 2));

        textView.setTypeface(null, android.graphics.Typeface.BOLD);

        int paddingInDp = (int) (16 * getResources().getDisplayMetrics().density);
        textView.setPadding(0, paddingInDp, 0, 0);

        rangeLayout.addView(textView);

        MaterialButton startDateButton = new MaterialButton(getContext());
        startDateButton.setId(View.generateViewId());
        if(dateRange == null)
            startDateButton.setText("Pick Start Date");
        else {
            startDateButton.setText(dateRange.getStartDate().get(2) + "." + dateRange.getStartDate().get(1) + "." + dateRange.getStartDate().get(0) + ".");
        }
        startDateButton.setAllCaps(false);



        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.gravity = Gravity.END;

        startDateButton.setLayoutParams(layoutParams);

        rangeLayout.addView(startDateButton);

        MaterialButton endDateButton = new MaterialButton(getContext());
        endDateButton.setId(View.generateViewId());
        if(dateRange == null)
            endDateButton.setText("Pick End Date");
        else{
            endDateButton.setText(dateRange.getEndDate().get(2) + "." + dateRange.getEndDate().get(1) + "." + dateRange.getEndDate().get(0) + ".");
        }
        endDateButton.setAllCaps(false);

        LinearLayout.LayoutParams layoutParamsEnd = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParamsEnd.gravity = Gravity.END;

        endDateButton.setLayoutParams(layoutParamsEnd);

        rangeLayout.addView(endDateButton);

        EditText priceInput = new EditText(getActivity());
        priceInput.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        priceInput.setId(View.generateViewId());
        priceInput.setHint("Price");
        if(dateRange != null)
            priceInput.setText(String.valueOf(dateRange.getPrice()));

        priceInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        rangeLayout.addView(priceInput);

        List<Integer> rangeIds = new ArrayList<>();
        rangeIds.add(startDateButton.getId());
        rangeIds.add(endDateButton.getId());
        rangeIds.add(priceInput.getId());
        availabilityRangesIds.add(rangeIds);
        Log.d("Range", availabilityRangesIds.toString());

        MaterialButton removeButton = new MaterialButton(getContext());
        removeButton.setText("Remove");
        LinearLayout.LayoutParams layoutParamsRemove = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParamsRemove.gravity = Gravity.END;
        removeButton.setLayoutParams(layoutParamsRemove);
        removeButton.setOnClickListener(v -> {
            container.removeView(rangeLayout);
            availabilityRangesIds.remove(rangeIds);
            Log.d("Range", availabilityRangesIds.toString());
        });
        rangeLayout.addView(removeButton);

        int colorPrimary = ContextCompat.getColor(getContext(), R.color.pink_300);
        int colorOnPrimary = ContextCompat.getColor(getContext(), R.color.white);
        ColorStateList colorStateList = ColorStateList.valueOf(colorPrimary);

        startDateButton.setBackgroundTintList(colorStateList);
        startDateButton.setTextColor(colorOnPrimary);
        startDateButton.setStrokeColor(colorStateList);
        startDateButton.setInsetTop(dpToPx(6, getContext()));
        startDateButton.setInsetBottom(dpToPx(6, getContext()));

        endDateButton.setBackgroundTintList(colorStateList);
        endDateButton.setTextColor(colorOnPrimary);
        endDateButton.setStrokeColor(colorStateList);
        endDateButton.setInsetTop(dpToPx(6, getContext()));
        endDateButton.setInsetBottom(dpToPx(6, getContext()));

        removeButton.setBackgroundTintList(colorStateList);
        removeButton.setTextColor(colorOnPrimary);
        removeButton.setStrokeColor(colorStateList);
        removeButton.setInsetTop(dpToPx(6, getContext()));
        removeButton.setInsetBottom(dpToPx(6, getContext()));

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDateButton.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year + ".");
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDateButton.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year + ".");
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        container.addView(rangeLayout);
    }

    private int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccommodationCreateBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        Button addButton = root.findViewById(R.id.btnAddNewDateRange);
        addButton.setOnClickListener(v -> addNewDateRange(null));

        startDateButton = root.findViewById(R.id.startDate);
        endDateButton = root.findViewById(R.id.endDate);
        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDateButton.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year + ".");
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDateButton.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year + ".");
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        return root;
    }
    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ownerEmail = binding.ownerEmail;
        try {
            ownerEmail.setText(UserInfo.getEmail());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        name = binding.name;
        location = binding.location;
        description = binding.description;
        benefits = binding.benefits;
        accommodationType = binding.accommodationType;
        minGuests = binding.minGuests;
        maxGuests = binding.maxGuests;
        pricing = binding.accommodationPricing;
        reservationCancellationDeadline = binding.cancellationDeadline;
        startDateButton = binding.startDate;
        endDateButton = binding.endDate;
        price = binding.etPrice;

        if (editAccommodation != null) {
            ownerEmail.setText(editAccommodation.getOwnerEmail());
            name.setText(editAccommodation.getName());
            location.setText(editAccommodation.getLocation());
            description.setText(editAccommodation.getDescription());
            benefits.setText(String.join(", ", editAccommodation.getBenefits()));
            minGuests.setText(String.valueOf(editAccommodation.getMinGuests()));
            maxGuests.setText(String.valueOf(editAccommodation.getMaxGuests()));
            reservationCancellationDeadline.setText(String.valueOf(editAccommodation.getReservationCancellationDeadline()));

            for (int i = 0; i < accommodationType.getChildCount(); i++) {
                RadioButton rb = (RadioButton) accommodationType.getChildAt(i);
                if (rb.getText().toString().equals(editAccommodation.getAccommodationType().toString())) {
                    rb.setChecked(true);
                    break;
                }
            }
            for (int i = 0; i < pricing.getChildCount(); i++) {
                RadioButton rb = (RadioButton) pricing.getChildAt(i);
                if (rb.getText().toString().equals(editAccommodation.getIsPriceByGuest() ? "Per guest" : "Fixed price")) {
                    rb.setChecked(true);
                    break;
                }
            }

            List<Integer> startDate = editAccommodation.getAvailabilityDates().get(0).getStartDate();
            List<Integer> endDate = editAccommodation.getAvailabilityDates().get(0).getEndDate();

            startDateButton.setText(startDate.get(2)+"." + startDate.get(1) + "." + startDate.get(0) + ".");
            endDateButton.setText(endDate.get(2)+"." + endDate.get(1) + "." + endDate.get(0) + ".");
            price.setText(String.valueOf(editAccommodation.getAvailabilityDates().get(0).getPrice()));
            for (int i=1; i< editAccommodation.getAvailabilityDates().size(); i++) {
                addNewDateRange(editAccommodation.getAvailabilityDates().get(i));
            }
        }

        createButton = binding.btnSubmit;
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Accommodation> call;
                    Log.d("BookingApp", "Add accommodation call");
                    if (addNewAccommodation()) {
                        call = ServiceUtils.accommodationService.add(newAccommodation);
                    }
                    else
                        return;
                call.enqueue(new Callback<Accommodation>() {
                    @Override
                    public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                        if (response.code() == 201){
                            Log.d("REZ","Message received");
                            System.out.println(response.body());
                            Accommodation accommodation = response.body();
                            AccommodationRequest accommodationRequest = new AccommodationRequest();
                            accommodationRequest.setNewAccommodation(accommodation);
                            accommodationRequest.setType(AccommodationRequestType.Created);
                            Call<AccommodationRequest> accommodationRequestCall = ServiceUtils.accommodationRequestService.add(accommodationRequest);
                            accommodationRequestCall.enqueue(new Callback<AccommodationRequest>() {
                                @Override
                                public void onResponse(Call<AccommodationRequest> call, Response<AccommodationRequest> response) {
                                    if (response.code() == 201){
                                        Log.d("REZ","Message received");
                                        System.out.println(response.body());

                                    }
                                    else {
                                        Log.d("REZ","Message received: "+response.code());
                                    }
                                }

                                @Override
                                public void onFailure(Call<AccommodationRequest> call, Throwable t) {
                                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                                }
                            });
                        }
                        else {
                            Log.d("REZ","Message received: "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Accommodation> call, @NonNull Throwable t) {
                        Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                    }
                });
            }
        });
        updateButton = binding.updateButton;
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAccommodation();
            }
        });

        if(editAccommodation != null){
            createButton.setVisibility(View.GONE);
        }
        else {
            updateButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private boolean getEditAccommodation() {

        try {
            String name = this.name.getText().toString();
            String location = this.location.getText().toString();
            String description = this.description.getText().toString();
            String benefits = this.benefits.getText().toString();

            Integer minGuests = Integer.valueOf(this.minGuests.getText().toString());
            Integer maxGuests = Integer.valueOf(this.maxGuests.getText().toString());
            if (minGuests > maxGuests)
                return false;

            Integer reservationDeadline = Integer.valueOf(this.reservationCancellationDeadline.getText().toString());
            RadioButton accommodationTypeRb = this.accommodationType.findViewById(this.accommodationType.getCheckedRadioButtonId());
            String accommodationType = accommodationTypeRb.getText().toString();
            RadioButton pricingRb = this.pricing.findViewById(this.pricing.getCheckedRadioButtonId());
            String pricing = pricingRb.getText().toString();

            List<DateRange> availabilityRanges = new ArrayList<>();
            DateRange dateRange = new DateRange();
            LocalDate startDate = LocalDate.parse(this.startDateButton.getText().toString(), DateTimeFormatter.ofPattern("d.M.yyyy."));
            LocalDate endDate = LocalDate.parse(this.endDateButton.getText().toString(), DateTimeFormatter.ofPattern("d.M.yyyy."));
            if (!(startDate.isBefore(endDate)))
                return false;
            dateRange.setStartDateFromDate(startDate);
            dateRange.setEndDateFromDate(endDate);
            Double price = Double.valueOf(this.price.getText().toString());
            dateRange.setPrice(price);
            availabilityRanges.add(dateRange);

            for (List<Integer> availabilityRange : availabilityRangesIds) {
                dateRange = new DateRange();
                Button startDateButton = root.findViewById(availabilityRange.get(0));
                Button endDateButton = root.findViewById(availabilityRange.get(1));
                startDate = LocalDate.parse(startDateButton.getText().toString(), DateTimeFormatter.ofPattern("d.M.yyyy."));
                endDate = LocalDate.parse(endDateButton.getText().toString(), DateTimeFormatter.ofPattern("d.M.yyyy."));
                if (!(startDate.isBefore(endDate)))
                    return false;
                dateRange.setStartDateFromDate(startDate);
                dateRange.setEndDateFromDate(endDate);
                EditText priceEditText = root.findViewById(availabilityRange.get(2));
                price = Double.valueOf(priceEditText.getText().toString());
                dateRange.setPrice(price);
                availabilityRanges.add(dateRange);
            }

            for (int i = 0; i < availabilityRanges.size(); i++) {
                if (!availabilityRanges.get(i).getStartDateAsDate().isBefore(availabilityRanges.get(i).getEndDateAsDate())) {
                    return false;
                }
                for (int j = i + 1; j < availabilityRanges.size(); j++) {
                    if (DateRange.isOverlapping(
                            availabilityRanges.get(i).getStartDateAsDate(),
                            availabilityRanges.get(i).getEndDateAsDate(),
                            availabilityRanges.get(j).getStartDateAsDate(),
                            availabilityRanges.get(j).getEndDateAsDate())) {
                        Log.d("Error", "Dates are overlapping " + i + ", " + j);
                        return false;
                    }
                }
            }

            if (name.length() < 5 || location.length() < 5 || description.length() < 5
                    || benefits.length() ==0 || minGuests < 1 || maxGuests > 30 || minGuests > 30 || reservationDeadline < 0 || reservationDeadline > 60) {
                return false;
            }

            editAccommodation.setName(name);
            editAccommodation.setLocation(location);
            editAccommodation.setDescription(description);
            List<String> benefitsList = new ArrayList<>();
            for (String benefit : benefits.split(",")) {
                benefitsList.add(benefit.trim());
            }
            editAccommodation.setOwnerEmail(UserInfo.getEmail());
            editAccommodation.setBenefits(benefitsList);
            editAccommodation.setMinGuests(minGuests);
            editAccommodation.setMaxGuests(maxGuests);
            editAccommodation.setReservationCancellationDeadline(reservationDeadline);
            editAccommodation.setAccommodationType(AccommodationType.valueOf(accommodationType));
            if (pricing.equals("Per guest"))
                editAccommodation.setIsPriceByGuest(true);
            else
                editAccommodation.setIsPriceByGuest(false);
            editAccommodation.setAvailabilityDates(availabilityRanges);
            editAccommodation.setIsApproved(false);

            //editAccommodation.setIsAutomaticAcceptance();

            editAccommodation.setId(null);
            return true;
        }
        catch (Exception e) {
            Log.d("Error", e.getMessage());
            Log.d("Error", "Inputs not valid");
            return false;
        }

    }

    private void updateAccommodation() {
        if (editAccommodation != null) {

            Call<Accommodation> call = null;
            Log.d("BookingApp", "Add accommodation call");
            if (getEditAccommodation()) {
                call = ServiceUtils.accommodationService.add(editAccommodation);
            }
            else
                return;

            call.enqueue(new Callback<Accommodation>() {
                @Override
                public void onResponse(@NonNull Call<Accommodation> call, @NonNull Response<Accommodation> response) {
                    if (response.isSuccessful()) {

                        AccommodationRequest accommodationRequest = new AccommodationRequest(null, oldAccommodation, response.body(), AccommodationRequestType.Updated);

                        ServiceUtils.accommodationRequestService.add(accommodationRequest).enqueue(new Callback<AccommodationRequest>() {
                            @Override
                            public void onResponse(@NonNull Call<AccommodationRequest> call, @NonNull Response<AccommodationRequest> response) {
                                if (response.isSuccessful()) {
//                                    NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_nav_content_main);
//                                    navController.navigate(R.id.nav_accommodations);
                                } else {
                                    Log.d("Update", "Request failed: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<AccommodationRequest> call, @NonNull Throwable t) {
                                Log.d("Update", "Request failed: " + t.getMessage());
                            }
                        });
                    } else {
                        Log.d("REZ", "error update accomm: " + response.code());
                    }
                }
                @Override
                public void onFailure(@NonNull Call<Accommodation> call, @NonNull Throwable t) {
                    Log.d("REZ", "Error: " + t.getMessage());
                }
            });

            oldAccommodation.setIsApproved(false);
            call = ServiceUtils.accommodationService.update(oldAccommodation.getId(), oldAccommodation);
            call.enqueue(new Callback<Accommodation>() {
                @Override
                public void onResponse(@NonNull Call<Accommodation> call, @NonNull Response<Accommodation> response) {

                    Log.d("REZ","Old accomm update");
                }

                @Override
                public void onFailure(@NonNull Call<Accommodation> call, @NonNull Throwable t) {
                    Log.d("REZ", "Error: " + t.getMessage());
                }
            });
        }
    }

    private boolean addNewAccommodation() {
        try {
            String name = this.name.getText().toString();
            String location = this.location.getText().toString();
            String description = this.description.getText().toString();
            String benefits = this.benefits.getText().toString();

            Integer minGuests = Integer.valueOf(this.minGuests.getText().toString());
            Integer maxGuests = Integer.valueOf(this.maxGuests.getText().toString());
            if (minGuests > maxGuests)
                return false;

            Integer reservationDeadline = Integer.valueOf(this.reservationCancellationDeadline.getText().toString());
            RadioButton accommodationTypeRb = this.accommodationType.findViewById(this.accommodationType.getCheckedRadioButtonId());
            String accommodationType = accommodationTypeRb.getText().toString();
            RadioButton pricingRb = this.pricing.findViewById(this.pricing.getCheckedRadioButtonId());
            String pricing = pricingRb.getText().toString();

            List<DateRange> availabilityRanges = new ArrayList<>();
            DateRange dateRange = new DateRange();
            LocalDate startDate = LocalDate.parse(this.startDateButton.getText().toString(), DateTimeFormatter.ofPattern("d.M.yyyy."));
            LocalDate endDate = LocalDate.parse(this.endDateButton.getText().toString(), DateTimeFormatter.ofPattern("d.M.yyyy."));
            if (!(startDate.isBefore(endDate)))
                return false;
            dateRange.setStartDateFromDate(startDate);
            dateRange.setEndDateFromDate(endDate);
            Double price = Double.valueOf(this.price.getText().toString());
            dateRange.setPrice(price);
            availabilityRanges.add(dateRange);

            for (List<Integer> availabilityRange : availabilityRangesIds) {
                dateRange = new DateRange();
                Button startDateButton = root.findViewById(availabilityRange.get(0));
                Button endDateButton = root.findViewById(availabilityRange.get(1));
                startDate = LocalDate.parse(startDateButton.getText().toString(), DateTimeFormatter.ofPattern("d.M.yyyy."));
                endDate = LocalDate.parse(endDateButton.getText().toString(), DateTimeFormatter.ofPattern("d.M.yyyy."));
                if (!(startDate.isBefore(endDate)))
                    return false;
                dateRange.setStartDateFromDate(startDate);
                dateRange.setEndDateFromDate(endDate);
                EditText priceEditText = root.findViewById(availabilityRange.get(2));
                price = Double.valueOf(priceEditText.getText().toString());
                dateRange.setPrice(price);
                availabilityRanges.add(dateRange);
            }

            for (int i = 0; i < availabilityRanges.size(); i++) {
                if (!availabilityRanges.get(i).getStartDateAsDate().isBefore(availabilityRanges.get(i).getEndDateAsDate())) {
                    return false;
                }
                for (int j = i + 1; j < availabilityRanges.size(); j++) {
                    if (DateRange.isOverlapping(
                            availabilityRanges.get(i).getStartDateAsDate(),
                            availabilityRanges.get(i).getEndDateAsDate(),
                            availabilityRanges.get(j).getStartDateAsDate(),
                            availabilityRanges.get(j).getEndDateAsDate())) {
                        Log.d("Error", "Dates are overlapping " + i + ", " + j);
                        return false;
                    }
                }
            }

            if (name.length() < 5 || location.length() < 5 || description.length() < 5
            || benefits.length() ==0 || minGuests < 1 || maxGuests > 30 || minGuests > 30 || reservationDeadline < 0 || reservationDeadline > 60) {
                return false;
            }

            newAccommodation = new Accommodation();
            newAccommodation.setName(name);
            newAccommodation.setLocation(location);
            newAccommodation.setDescription(description);
            List<String> benefitsList = new ArrayList<>();
            for (String benefit : benefits.split(",")) {
                benefitsList.add(benefit.trim());
            }
            newAccommodation.setOwnerEmail(UserInfo.getEmail());
            newAccommodation.setBenefits(benefitsList);
            newAccommodation.setMinGuests(minGuests);
            newAccommodation.setMaxGuests(maxGuests);
            newAccommodation.setReservationCancellationDeadline(reservationDeadline);
            newAccommodation.setAccommodationType(AccommodationType.valueOf(accommodationType));
            if (pricing.equals("Per guest"))
                newAccommodation.setIsPriceByGuest(true);
            else
                newAccommodation.setIsPriceByGuest(false);
            newAccommodation.setAvailabilityDates(availabilityRanges);
            newAccommodation.setIsApproved(false);
            newAccommodation.setIsAutomaticAcceptance(false);
            return true;
        }
        catch (Exception e) {
            Log.d("Error", e.getMessage());
            Log.d("Error", "Inputs not valid");
            return false;
        }
    }
}