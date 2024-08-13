package com.bookingapp.fragments.accommodation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bookingapp.R;
import com.bookingapp.databinding.FragmentAccommodationCreateBinding;
import com.bookingapp.model.Accommodation;
import com.bookingapp.model.AccommodationType;
import com.bookingapp.model.DateRange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationCreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationCreateFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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

    // TODO: Rename and change types of parameters

    private Accommodation editAccommodation;
    private Accommodation newAccommodation;

    private EditText name;
    private TextView description;
    private TextView pricePerGuest;
    private TextView location;
//    private TextView ownerEmail;
    private RadioGroup accommodationType;
    private TextView benefits;
//    private CheckBox isApproved;
    private CheckBox isAutomaticAcceptance;
    private CheckBox isPriceByGuest;
    private TextView minGuests;
    private TextView maxGuests;
    private TextView reservationCancellationDeadline;
//    private TextView availabilityDates;
    private TextView reservedDates;
    private TextView reservationCalculatedPrice;
    private View root;






    public AccommodationCreateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment AccommodationCreateFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        // Create a new LinearLayout for the range
        LinearLayout rangeLayout = new LinearLayout(getContext());
        rangeLayout.setOrientation(LinearLayout.VERTICAL);
        rangeLayout.setPadding(0, 0, 0, 16); // Add spacing between ranges

        // Create and add start date input
        EditText startDateInput = new EditText(getContext());
        startDateInput.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        startDateInput.setHint("Availability start");
        startDateInput.setInputType(InputType.TYPE_CLASS_DATETIME );
        rangeLayout.addView(startDateInput);

        // Create and add end date input
        EditText endDateInput = new EditText(getContext());
        endDateInput.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        endDateInput.setHint("Availability end");
        endDateInput.setInputType(InputType.TYPE_CLASS_DATETIME );
        rangeLayout.addView(endDateInput);

        // Create and add price input
        EditText priceInput = new EditText(getContext());
        priceInput.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        priceInput.setHint("Price");
        priceInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        rangeLayout.addView(priceInput);

        // Create and add remove button
        Button removeButton = new Button(getContext());
        removeButton.setText("Remove");
        removeButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        removeButton.setOnClickListener(v -> container.removeView(rangeLayout));
        rangeLayout.addView(removeButton);

        // Add the new range layout to the container
        container.addView(rangeLayout);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccommodationCreateBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        Button addButton = root.findViewById(R.id.btnAddNewDateRange);
        addButton.setOnClickListener(v -> addNewDateRange());
        return root;
    }
    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        name = (EditText) binding.name;
        location = (EditText) binding.name;
        description = (EditText) binding.name;
        accommodationType =  binding.accommodationType; /*???????*/
        isAutomaticAcceptance = (CheckBox) binding.automaticAcceptance;
        minGuests = (EditText)  binding.minGuests;
        maxGuests = (EditText) binding.maxGuests;
        isPriceByGuest = (CheckBox) binding.pricePerGuest;






    }
}