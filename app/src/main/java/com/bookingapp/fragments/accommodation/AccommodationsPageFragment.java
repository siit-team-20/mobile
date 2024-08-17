package com.bookingapp.fragments.accommodation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.bookingapp.R;
import com.bookingapp.databinding.FragmentAccommodationsPageBinding;
import com.bookingapp.fragments.FragmentTransition;
import com.bookingapp.model.Accommodation;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AccommodationsPageFragment extends Fragment {
    private static final String ARG_IS_ON_HOME = "isOnHome";
    private Spinner spinner;
    private AccommodationsPageViewModel accommodationsViewModel;
    private FragmentAccommodationsPageBinding binding;
    private Button startDateButton;
    private Button endDateButton;
    private boolean isOnHome;

    public static AccommodationsPageFragment newInstance(boolean isOnHome) {
        AccommodationsPageFragment fragment = new AccommodationsPageFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_ON_HOME, isOnHome);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        accommodationsViewModel = new ViewModelProvider(requireActivity()).get(AccommodationsPageViewModel.class);

        binding = FragmentAccommodationsPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SearchView searchView = binding.searchText;
        accommodationsViewModel.getSearchText().observe(getViewLifecycleOwner(), searchView::setQueryHint);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                accommodationsViewModel.setSearchText(newText);
                return false;
            }
        });
        if (accommodationsViewModel.getSearchText().getValue() != null)
            searchView.setQuery(accommodationsViewModel.getSearchText().getValue(), false);

        EditText guestNumberInput = binding.searchGuestNumber;
        guestNumberInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    accommodationsViewModel.setGuestNumber(Integer.parseInt(s.toString()));
                }
                catch (Exception e) {
                    accommodationsViewModel.setGuestNumber(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        startDateButton = binding.searchStartDate;
        startDateButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onInputChange();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
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

        endDateButton = binding.searchEndDate;
        endDateButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onInputChange();
            }

            @Override
            public void afterTextChanged(Editable s) { }
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

        Button btnFilters = binding.btnFilters;
        btnFilters.setOnClickListener(v -> {
            Log.i("BookingApp", "Bottom Sheet Dialog");
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);

            RadioGroup priceFilter = dialogView.findViewById(R.id.accommodation_price_filter);
            priceFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton radioButton = group.findViewById(checkedId);
                    Log.d("Price Filter", "Checked id: " + radioButton.getText().toString());
                    accommodationsViewModel.setSelectedPrice(radioButton.getText().toString());
                }
            });
            accommodationsViewModel.setSelectedPrice("Any");
            String selectedPrice = accommodationsViewModel.getSelectedPrice().getValue();
            if (selectedPrice.equals("Any"))
                priceFilter.check(R.id.accommodation_price_any);
            else if (selectedPrice.equals("0 - 30e"))
                priceFilter.check(R.id.accommodation_price_0);
            else if (selectedPrice.equals("30 - 60e"))
                priceFilter.check(R.id.accommodation_price_1);
            else if (selectedPrice.equals("60 - 90e"))
                priceFilter.check(R.id.accommodation_price_2);
            else
                priceFilter.check(R.id.accommodation_price_3);

            CheckBox checkBoxHotel = dialogView.findViewById(R.id.accommodation_type_hotel);
            CheckBox checkBoxApartment = dialogView.findViewById(R.id.accommodation_type_apartment);
            CheckBox checkBoxMotel = dialogView.findViewById(R.id.accommodation_type_motel);
            CheckBox checkBoxStudio = dialogView.findViewById(R.id.accommodation_type_studio);
            Map<String, CheckBox> types = new HashMap<>();
            types.put("Hotel", checkBoxHotel);
            types.put("Apartment", checkBoxApartment);
            types.put("Motel", checkBoxMotel);
            types.put("Studio", checkBoxStudio);
            CompoundButton.OnCheckedChangeListener onCheckedChangeType = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        accommodationsViewModel.addSelectedType(buttonView.getText().toString());
                    else
                        accommodationsViewModel.removeSelectedType(buttonView.getText().toString());
                }
            };
            for (CheckBox checkBox : types.values()) {
                checkBox.setOnCheckedChangeListener(onCheckedChangeType);
            }

            Set<String> selectedTypes = accommodationsViewModel.getSelectedTypes().getValue();
            if (selectedTypes != null)
                for (String type : selectedTypes) {
                    types.get(type).setChecked(true);
                }

            CheckBox checkBoxWiFi = dialogView.findViewById(R.id.accommodation_benefit_wifi);
            CheckBox checkBoxParking = dialogView.findViewById(R.id.accommodation_benefit_parking);
            CheckBox checkBoxTV = dialogView.findViewById(R.id.accommodation_benefit_tv);
            CheckBox checkBoxFridge = dialogView.findViewById(R.id.accommodation_benefit_fridge);
            Map<String, CheckBox> benefits = new HashMap<>();
            benefits.put("Wi-Fi", checkBoxWiFi);
            benefits.put("Parking", checkBoxParking);
            benefits.put("TV", checkBoxTV);
            benefits.put("Fridge", checkBoxFridge);
            CompoundButton.OnCheckedChangeListener onCheckedChangeBenefit = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        accommodationsViewModel.addSelectedBenefit(buttonView.getText().toString());
                    else
                        accommodationsViewModel.removeSelectedBenefit(buttonView.getText().toString());
                }
            };
            for (CheckBox checkBox : benefits.values()) {
                checkBox.setOnCheckedChangeListener(onCheckedChangeBenefit);
            }

            Set<String> selectedBenefits = accommodationsViewModel.getSelectedBenefits().getValue();
            if (selectedBenefits != null)
                for (String benefit : selectedBenefits) {
                    benefits.get(benefit).setChecked(true);
                }

            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });

        spinner = binding.btnSort;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.sort_array));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        LiveData<String> selectedSort = accommodationsViewModel.getSelectedSort();
        int sortOption = 1;
        if (selectedSort.getValue() == null)
            sortOption = 0;
        else if (selectedSort.getValue().equals("Ascending"))
            sortOption = 0;
        spinner.setSelection(sortOption);

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isOnHome = getArguments().getBoolean(ARG_IS_ON_HOME);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        spinner.setSelection(spinner.getSelectedItemPosition(), false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String chosenOption = (String) parent.getItemAtPosition(position);
                accommodationsViewModel.setSelectedSort(chosenOption);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        FragmentTransition.to(AccommodationListFragment.newInstance(isOnHome), getActivity(), false, R.id.scroll_accommodations_list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void onInputChange() {
        try {
            LocalDate startDate = LocalDate.parse(startDateButton.getText().toString(), DateTimeFormatter.ofPattern("d.M.yyyy."));
            LocalDate endDate = LocalDate.parse(endDateButton.getText().toString(), DateTimeFormatter.ofPattern("d.M.yyyy."));
            if (!(startDate.isBefore(endDate)))
                return;
            accommodationsViewModel.setStartDate(startDate);
            accommodationsViewModel.setEndDate(endDate);
        }
        catch (Exception e) {
            return;
        }
    }

}