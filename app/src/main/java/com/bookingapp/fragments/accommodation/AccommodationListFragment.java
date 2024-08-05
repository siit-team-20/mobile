package com.bookingapp.fragments.accommodation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bookingapp.adapters.AccommodationListAdapter;
import com.bookingapp.databinding.FragmentAccommodationListBinding;
import com.bookingapp.model.Accommodation;
import com.bookingapp.service.ServiceUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationListFragment extends ListFragment {
    private AccommodationListAdapter adapter;
    private FragmentAccommodationListBinding binding;
    private MenuProvider menuProvider;
    private ArrayList<Accommodation> accommodations = new ArrayList<>();

    public static AccommodationListFragment newInstance() {
        AccommodationListFragment fragment = new AccommodationListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i("BookingApp", "onCreateView Accommodation List Fragment");
        binding = FragmentAccommodationListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //addMenu();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("BookingApp", "onCreate Accommodation List Fragment");
        this.getListView().setDividerHeight(2);
        getDataFromClient();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromClient();
    }

//    private void addMenu() {
//        menuProvider = new MenuProvider() {
//            @Override
//            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
//                menu.clear();
//                menuInflater.inflate(R.menu.products_menu, menu);
//            }
//
//            @Override
//            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
//                NavController navController = Navigation.findNavController(getActivity(), R.id.fragment_nav_content_main);
//                // Nakon toga, koristi se NavigationUI.onNavDestinationSelected(item, navController)
//                // kako bi se omogućila integracija između MenuItem-a i odredišta unutar aplikacije
//                // definisanih unutar navigacionog grafa (NavGraph).
//                // Ova funkcija proverava da li je odabrana stavka izbornika povezana s nekim
//                // odredištem unutar navigacionog grafa i pokreće tu navigaciju ako postoji
//                // odgovarajuće podudaranje.
//                return NavigationUI.onNavDestinationSelected(menuItem, navController);
//            }
//        };
//
//        requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getDataFromClient(){
        Call<ArrayList<Accommodation>> call = ServiceUtils.accommodationService.getAll();
        call.enqueue(new Callback<ArrayList<Accommodation>>() {
            @Override
            public void onResponse(Call<ArrayList<Accommodation>> call, Response<ArrayList<Accommodation>> response) {
                if (response.code() == 200) {
                    Log.d("REZ","Message received");
                    System.out.println(response.body());
                    accommodations = response.body();
                    adapter = new AccommodationListAdapter(getActivity(), getActivity().getSupportFragmentManager(), accommodations);
                    setListAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else {
                    Log.d("REZ","Message received: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Accommodation>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
            }
        });

    }


}