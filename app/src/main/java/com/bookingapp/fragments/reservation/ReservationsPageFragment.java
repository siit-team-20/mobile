package com.bookingapp.fragments.reservation;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.bookingapp.R;
import com.bookingapp.databinding.FragmentAccommodationsPageBinding;
import com.bookingapp.databinding.FragmentReservationsPageBinding;
import com.bookingapp.fragments.FragmentTransition;
import com.bookingapp.fragments.accommodation.AccommodationListFragment;
import com.bookingapp.fragments.accommodation.AccommodationsPageFragment;
import com.bookingapp.fragments.accommodation.AccommodationsPageViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ReservationsPageFragment extends Fragment {
    private ReservationsPageViewModel reservationsViewModel;
    private FragmentReservationsPageBinding binding;
    private Button startDateButton;
    private Button endDateButton;

    public static ReservationsPageFragment newInstance() {
        return new ReservationsPageFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        reservationsViewModel = new ViewModelProvider(requireActivity()).get(ReservationsPageViewModel.class);

        binding = FragmentReservationsPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SearchView searchView = binding.searchText;
        reservationsViewModel.getSearchText().observe(getViewLifecycleOwner(), searchView::setQueryHint);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                reservationsViewModel.setSearchText(newText);
                return false;
            }
        });
        if (reservationsViewModel.getSearchText().getValue() != null)
            searchView.setQuery(reservationsViewModel.getSearchText().getValue(), false);

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
            View dialogView = getLayoutInflater().inflate(R.layout.reservation_filter, null);

            CheckBox checkBoxWaiting = dialogView.findViewById(R.id.reservation_status_waiting);
            CheckBox checkBoxApproved = dialogView.findViewById(R.id.reservation_status_approved);
            CheckBox checkBoxRejected = dialogView.findViewById(R.id.reservation_status_rejected);
            Map<String, CheckBox> types = new HashMap<>();
            types.put("Waiting", checkBoxWaiting);
            types.put("Approved", checkBoxApproved);
            types.put("Rejected", checkBoxRejected);
            CompoundButton.OnCheckedChangeListener onCheckedChangeType = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        reservationsViewModel.addSelectedStatus(buttonView.getText().toString());
                    else
                        reservationsViewModel.removeSelectedStatus(buttonView.getText().toString());
                }
            };
            for (CheckBox checkBox : types.values()) {
                checkBox.setOnCheckedChangeListener(onCheckedChangeType);
            }

            Set<String> selectedTypes = reservationsViewModel.getSelectedStatuses().getValue();
            if (selectedTypes != null)
                for (String type : selectedTypes) {
                    types.get(type).setChecked(true);
                }

            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        FragmentTransition.to(ReservationListFragment.newInstance(), getActivity(), false, R.id.scroll_reservations_list);
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
            reservationsViewModel.setStartDate(startDate);
            reservationsViewModel.setEndDate(endDate);
        }
        catch (Exception e) {
            return;
        }
    }

}