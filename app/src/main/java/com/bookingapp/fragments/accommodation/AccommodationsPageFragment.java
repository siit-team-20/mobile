package com.bookingapp.fragments.accommodation;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.bookingapp.R;
import com.bookingapp.databinding.FragmentAccommodationsPageBinding;
import com.bookingapp.fragments.FragmentTransition;
import com.bookingapp.model.Accommodation;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class AccommodationsPageFragment extends Fragment {
    private Spinner spinner;
    private AccommodationsPageViewModel accommodationsViewModel;
    private FragmentAccommodationsPageBinding binding;

    public static AccommodationsPageFragment newInstance() {
        return new AccommodationsPageFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        accommodationsViewModel = new ViewModelProvider(this).get(AccommodationsPageViewModel.class);

        binding = FragmentAccommodationsPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SearchView searchView = binding.searchText;
        accommodationsViewModel.getText().observe(getViewLifecycleOwner(), searchView::setQueryHint);

        Button btnFilters = binding.btnFilters;
        btnFilters.setOnClickListener(v -> {
            Log.i("BookingApp", "Bottom Sheet Dialog");
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.FullScreenBottomSheetDialog);
            View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_filter, null);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();
        });

        spinner = binding.btnSort;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.sort_array));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        spinner.setSelection(spinner.getSelectedItemPosition(), false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage("Change the sort option?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.v("BookingApp", (String) parent.getItemAtPosition(position));
                                ((TextView) parent.getChildAt(0)).setTextColor(Color.MAGENTA);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = dialog.create();
                alert.show();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        FragmentTransition.to(AccommodationListFragment.newInstance(), getActivity(), false, R.id.scroll_accommodations_list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}