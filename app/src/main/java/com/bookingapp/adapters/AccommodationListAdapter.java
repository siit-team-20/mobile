package com.bookingapp.adapters;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
import com.bookingapp.model.AccommodationReview;
import com.bookingapp.model.DateRange;
import com.bookingapp.model.FavouriteAccommodationWithAccommodation;
import com.bookingapp.model.Rating;
import com.bookingapp.model.Reservation;
import com.bookingapp.model.UserType;
import com.bookingapp.service.ServiceUtils;
import com.bookingapp.service.UserInfo;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationListAdapter extends ArrayAdapter<Accommodation> {
    private ArrayList<Accommodation> aAccommodations;
    private Activity activity;
    private FragmentManager fragmentManager;
    private boolean isOnFavourites;

    public AccommodationListAdapter(Activity context, FragmentManager fragmentManager, ArrayList<Accommodation> accommodations, boolean isOnFavourites) {
        super(context, R.layout.accommodation_card, accommodations);
        aAccommodations = accommodations;
        activity = context;
        fragmentManager = fragmentManager;
        this.isOnFavourites = isOnFavourites;
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
        ImageButton favouritesButton = convertView.findViewById(R.id.add_to_favourites_button);
        TextView averageRating = convertView.findViewById(R.id.accommodation_average_rating);
//        TextView minGuests = convertView.findViewById(R.id.accommodation_min_guests);
//        TextView maxGuests = convertView.findViewById(R.id.accommodation_max_guests);

        if(accommodation != null) {
            //String uri = "@drawable/" + product.getImagePath();
            //Resources resources = getContext().getResources();
            //final int resourceId = resources.getIdentifier(uri, "drawable", getContext().getPackageName());
            imageView.setImageResource(R.drawable.a);
            Call<ArrayList<AccommodationReview>> call = ServiceUtils.accommodationReviewService.get(accommodation.getId(), false);
            call.enqueue(new Callback<ArrayList<AccommodationReview>>() {
                @Override
                public void onResponse(Call<ArrayList<AccommodationReview>> call, Response<ArrayList<AccommodationReview>> response) {
                    if (response.code() == 200) {
                        Log.d("Reviews-Get","Message received");
                        System.out.println(response.body());
                        List<AccommodationReview> accommodationReviews = response.body();
                        Long counter = 0L;
                        Long sum = 0L;
                        for(int i = 0; i < accommodationReviews.size();i++){
                            counter++;
                            if(accommodationReviews.get(i).getRating().equals(Rating.one))
                                sum += 1;
                            else if(accommodationReviews.get(i).getRating().equals(Rating.two))
                                sum += 2;
                            else if(accommodationReviews.get(i).getRating().equals(Rating.three))
                                sum += 3;
                            else if(accommodationReviews.get(i).getRating().equals(Rating.four))
                                sum += 4;
                            else if(accommodationReviews.get(i).getRating().equals(Rating.five))
                                sum += 5;
                        }
                        if(counter == 0L){
                            averageRating.setText("Average Rating: None");
                            return;
                        }
                        averageRating.setText("Average Rating: " + sum/counter);
                    }
                    else {
                        Log.d("Reviews-Get","Message received: "+response.code());
                        averageRating.setText("Average Rating: None");
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<AccommodationReview>> call, Throwable t) {
                    Log.d("Reviews-Get", t.getMessage() != null?t.getMessage():"error");
                    averageRating.setText("Rating: None");
                }
            });
            name.setText(accommodation.getName());
            location.setText(accommodation.getLocation());


            if (UserInfo.getToken() != null) {
                try {
                    if (UserInfo.getType().equals(UserType.Guest) && !isOnFavourites) {
                        favouritesButton.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            favouritesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FavouriteAccommodationWithAccommodation favouriteAccommodation = new FavouriteAccommodationWithAccommodation();
                    favouriteAccommodation.setAccommodation(accommodation);
                    try {
                        Call<ArrayList<FavouriteAccommodationWithAccommodation>> call = ServiceUtils.favouriteAccommodationService.get(UserInfo.getEmail());
                        call.enqueue(new Callback<ArrayList<FavouriteAccommodationWithAccommodation>>() {
                            @Override
                            public void onResponse(Call<ArrayList<FavouriteAccommodationWithAccommodation>> call, Response<ArrayList<FavouriteAccommodationWithAccommodation>> response) {
                                if (response.code() == 200){
                                    Log.d("Favourite Accommodation-Get","Message received");
                                    System.out.println(response.body());
                                    ArrayList<FavouriteAccommodationWithAccommodation> favouriteAccommodations = response.body();
                                    boolean alreadyFavourite = false;
                                    for (FavouriteAccommodationWithAccommodation favouriteAcc : favouriteAccommodations) {
                                        if (favouriteAcc.getAccommodation().getId().equals(favouriteAccommodation.getAccommodation().getId()))
                                            alreadyFavourite = true;
                                    }
                                    try {
                                        favouriteAccommodation.setGuestEmail(UserInfo.getEmail());
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                    if (!alreadyFavourite) {
                                        Call<FavouriteAccommodationWithAccommodation> callPost = ServiceUtils.favouriteAccommodationService.add(favouriteAccommodation);
                                        callPost.enqueue(new Callback<FavouriteAccommodationWithAccommodation>() {
                                            @Override
                                            public void onResponse(Call<FavouriteAccommodationWithAccommodation> call, Response<FavouriteAccommodationWithAccommodation> response) {
                                                if (response.code() == 201){
                                                    Log.d("Favourite Accommodation-New","Message received");
                                                    System.out.println(response.body());
                                                }
                                                else {
                                                    Log.d("Favourite Accommodation-New","Message received: "+response.code());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<FavouriteAccommodationWithAccommodation> call, Throwable t) {
                                                Log.d("Favourite Accommodation-New", t.getMessage() != null?t.getMessage():"error");
                                            }
                                        });
                                    }

                                }
                                else {
                                    Log.d("Favourite Accommodation-Get","Message received: "+response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<ArrayList<FavouriteAccommodationWithAccommodation>> call, Throwable t) {
                                Log.d("Favourite Accommodation-Get", t.getMessage() != null?t.getMessage():"error");
                            }
                        });

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            accommodationCard.setOnClickListener(v -> {
                Log.i("BookingApp", "Clicked: " + accommodation.getName() + ", id: " + accommodation.getId().toString());
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
                NavController navController = Navigation.findNavController(activity, R.id.fragment_nav_content_main);
                navController.navigate(R.id.nav_accommodation_detail, args);
            });
        }

        return convertView;
    }


}
