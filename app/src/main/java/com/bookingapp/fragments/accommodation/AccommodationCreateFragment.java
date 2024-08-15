package com.bookingapp.fragments.accommodation;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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

import com.bookingapp.R;
import com.bookingapp.databinding.FragmentAccommodationCreateBinding;
import com.bookingapp.model.Accommodation;
import com.bookingapp.model.AccommodationType;
import com.bookingapp.model.DateRange;
import com.bookingapp.service.ServiceUtils;
import com.google.android.material.button.MaterialButton;

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
    private View root;
    private List<List<Integer>> availabilityRangesIds = new ArrayList<>();
    private Button createButton;

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
    private void addNewDateRange() {
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
        startDateButton.setText("Pick Start Date");
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
        endDateButton.setText("Pick End Date");
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccommodationCreateBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        Button addButton = root.findViewById(R.id.btnAddNewDateRange);
        addButton.setOnClickListener(v -> addNewDateRange());

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

        createButton = binding.btnSubmit;
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Accommodation> call;
//                if(editProduct != null) {
//                    Log.d("ShopApp", "Edit product call");
//                    editProduct();
//                    call = ClientUtils.productService.edit(editProduct);
//                }
//                else {
                    Log.d("BookingApp", "Add accommodation call");
                    if (addNewAccommodation()) {
                        call = ServiceUtils.accommodationService.add(newAccommodation);
                    }
                    else
                        return;
//                }
                call.enqueue(new Callback<Accommodation>() {
                    @Override
                    public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                        if (response.code() == 201){
                            Log.d("REZ","Message received");
                            System.out.println(response.body());
                            Accommodation accommodation = response.body();
                            //getActivity().getSupportFragmentManager().popBackStack();
                        }
                        else {
                            Log.d("REZ","Message received: "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Accommodation> call, Throwable t) {
                        Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void editAccommodation(){
//        String title = titleText.getText().toString();
//        String description = descrText.getText().toString();
//        if (title.length() == 0 && description.length() == 0) {
//            return;
//        }
//        editProduct.setTitle(title);
//        editProduct.setDescription(description);
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
            newAccommodation.setOwnerEmail("owner@gmail.com");
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