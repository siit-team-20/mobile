package com.bookingapp.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bookingapp.R;
import com.bookingapp.model.Accommodation;

import java.util.ArrayList;

public class AccommodationListAdapter extends ArrayAdapter<Accommodation> {
    private ArrayList<Accommodation> aAccommodations;
    private Activity activity;
    private FragmentManager fragmentManager;

    public AccommodationListAdapter(Activity context, FragmentManager fragmentManager, ArrayList<Accommodation> accommodations) {
        super(context, R.layout.accommodation_card, accommodations);
        aAccommodations = accommodations;
        activity = context;
        fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return aAccommodations.size();
    }

    @Nullable
    @Override
    public Accommodation getItem(int position) {
        return aAccommodations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Accommodation accommodation = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.accommodation_card,
                    parent, false);
        }
        LinearLayout accommodationCard = convertView.findViewById(R.id.accommodation_card_item);
        ImageView imageView = convertView.findViewById(R.id.accommodation_image);
        TextView name = convertView.findViewById(R.id.accommodation_name);
        TextView location = convertView.findViewById(R.id.accommodation_location);
//        TextView minGuests = convertView.findViewById(R.id.accommodation_min_guests);
//        TextView maxGuests = convertView.findViewById(R.id.accommodation_max_guests);

        if(accommodation != null) {
            //String uri = "@drawable/" + product.getImagePath();
            //Resources resources = getContext().getResources();
            //final int resourceId = resources.getIdentifier(uri, "drawable", getContext().getPackageName());
            imageView.setImageResource(R.drawable.a);

            name.setText(accommodation.getName());
            location.setText(accommodation.getLocation());
//            minGuests.setText(String.valueOf(accommodation.getMinGuests()));
//            maxGuests.setText(String.valueOf(accommodation.getMaxGuests()));
            accommodationCard.setOnClickListener(v -> {
                Log.i("BookingApp", "Clicked: " + accommodation.getName() + ", id: " + accommodation.getId().toString());
                Bundle args = new Bundle();
                args.putLong("id", accommodation.getId());
                args.putString("name", accommodation.getName());
                args.putString("description", accommodation.getDescription());
                args.putString("location", accommodation.getLocation());
                args.putString("ownerEmail", accommodation.getOwnerEmail());
                args.putString("accommodationType", accommodation.getAccommodationType().toString());
                args.putStringArrayList("benefits", new ArrayList<>(accommodation.getBenefits()));
                args.putBoolean("isApproved", accommodation.getIsApproved());
                args.putBoolean("isAutomaticAcceptance", accommodation.getIsAutomaticAcceptance());
                args.putBoolean("isPriceByGuest", accommodation.getIsPriceByGuest());
                args.putInt("minGuests", accommodation.getMinGuests());
                args.putInt("maxGuests", accommodation.getMaxGuests());
                args.putInt("reservationCancellationDeadline", accommodation.getReservationCancellationDeadline());
                args.putParcelableArrayList("availabilityDates", new ArrayList<>(accommodation.getAvailabilityDates()));
//                NavController navController = Navigation.findNavController(activity, R.id.fragment_nav_content_main);
//                navController.navigate(R.id.nav_product_detail, args);

            });
        }

        return convertView;
    }


}
