package com.bookingapp.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;

import com.bookingapp.R;
import com.bookingapp.model.AccommodationRequest;
import com.bookingapp.model.AccommodationRequestType;
import com.bookingapp.model.Notification;
import com.bookingapp.model.NotificationType;
import com.bookingapp.model.Reservation;
import com.bookingapp.model.ReservationStatus;
import com.bookingapp.model.ReservationWithAccommodation;
import com.bookingapp.model.UserType;
import com.bookingapp.service.ServiceUtils;
import com.bookingapp.service.UserInfo;

import org.json.JSONException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationRequestListAdapter extends ArrayAdapter<AccommodationRequest> {
    private ArrayList<AccommodationRequest> aAccommodationRequests;
    private Activity activity;
    private FragmentManager fragmentManager;
    private boolean[] createViewVisibility = null;

    public AccommodationRequestListAdapter(Activity context, FragmentManager fragmentManager, ArrayList<AccommodationRequest> accommodationRequests) {
        super(context, R.layout.accommodation_request_card, accommodationRequests);
        aAccommodationRequests = accommodationRequests;
        activity = context;
        fragmentManager = fragmentManager;
        this.createViewVisibility = new boolean[accommodationRequests.size()];
    }
    @Override
    public int getCount() {
        return aAccommodationRequests.size();
    }
    @Nullable
    @Override
    public AccommodationRequest getItem(int position){return aAccommodationRequests.get(position);}
    @Override
    public long getItemId(int position){return  position;}
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        AccommodationRequest accommodationRequest = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.accommodation_request_card,
                    parent, false);
        }

        if (accommodationRequest.getType().equals(AccommodationRequestType.Created)) {
            createViewVisibility[position] = true;
            ImageView imageView = convertView.findViewById(R.id.accommodation_request_image);
            TextView name = convertView.findViewById(R.id.accommodation_request_name_and_type);
            TextView location = convertView.findViewById(R.id.accommodation_request_location);
            TextView ownerEmail = convertView.findViewById(R.id.accommodation_request_owner_email);
            TextView minGuests = convertView.findViewById(R.id.accommodation_request_min_guests);
            TextView maxGuests = convertView.findViewById(R.id.accommodation_request_max_guests);
            Button approveButton = convertView.findViewById(R.id.accommodation_request_approve_button);
            Button rejectButton = convertView.findViewById(R.id.accommodation_request_reject_button);

            if(accommodationRequest != null) {
                //String uri = "@drawable/" + product.getImagePath();
                //Resources resources = getContext().getResources();
                //final int resourceId = resources.getIdentifier(uri, "drawable", getContext().getPackageName());
                imageView.setImageResource(R.drawable.a);
                name.setText(accommodationRequest.getNewAccommodation().getName() +", " +accommodationRequest.getNewAccommodation().getAccommodationType().toString());
                location.setText(accommodationRequest.getNewAccommodation().getLocation());
                ownerEmail.setText(accommodationRequest.getNewAccommodation().getOwnerEmail());
                minGuests.setText(Integer.toString(accommodationRequest.getNewAccommodation().getMinGuests()));
                maxGuests.setText(Integer.toString(accommodationRequest.getNewAccommodation().getMaxGuests()));
//
//
//          approveButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Reservation res = new Reservation();
//                    res.setId(reservation.getId());
//                    res.setGuestEmail(reservation.getGuestEmail());
//                    res.setAccommodationId(reservation.getAccommodation().getId());
//                    res.setDate(reservation.getDate());
//                    res.setDays(reservation.getDays());
//                    res.setGuestNumber(reservation.getGuestNumber());
//                    res.setPrice(reservation.getPrice());
//                    res.setStatus(ReservationStatus.Cancelled);
//                    Call<Reservation> call = ServiceUtils.reservationService.update(res.getId(), res);
//                    call.enqueue(new Callback<Reservation>() {
//                        @Override
//                        public void onResponse(Call<Reservation> call, Response<Reservation> response) {
//                            if (response.code() == 200){
//                                Log.d("Reservations-Update","Message received");
//                                System.out.println(response.body());
//                                reservation.setStatus(ReservationStatus.Cancelled);
//                                for (int i = 0; i < rReservations.size(); i++) {
//                                    if (rReservations.get(i).getId() == res.getId()) {
//                                        rReservations.set(i, reservation);
//                                        break;
//                                    }
//                                }
//                                notifyDataSetChanged();
//                            }
//                            else {
//                                Log.d("Reservations-Update","Message received: "+response.code());
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Reservation> call, Throwable t) {
//                            Log.d("Reservations-Update", t.getMessage() != null?t.getMessage():"error");
//                        }
//                    });
//                }
//            });

//            rejectButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Reservation res = new Reservation();
//                    res.setId(reservation.getId());
//                    res.setGuestEmail(reservation.getGuestEmail());
//                    res.setAccommodationId(reservation.getAccommodation().getId());
//                    res.setDate(reservation.getDate());
//                    res.setDays(reservation.getDays());
//                    res.setGuestNumber(reservation.getGuestNumber());
//                    res.setPrice(reservation.getPrice());
//                    res.setStatus(ReservationStatus.Cancelled);
//                    Call<Reservation> call = ServiceUtils.reservationService.update(res.getId(), res);
//                    call.enqueue(new Callback<Reservation>() {
//                        @Override
//                        public void onResponse(Call<Reservation> call, Response<Reservation> response) {
//                            if (response.code() == 200){
//                                Log.d("Reservations-Update","Message received");
//                                System.out.println(response.body());
//                                reservation.setStatus(ReservationStatus.Cancelled);
//                                for (int i = 0; i < rReservations.size(); i++) {
//                                    if (rReservations.get(i).getId() == res.getId()) {
//                                        rReservations.set(i, reservation);
//                                        break;
//                                    }
//                                }
//                                notifyDataSetChanged();
//                            }
//                            else {
//                                Log.d("Reservations-Update","Message received: "+response.code());
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Reservation> call, Throwable t) {
//                            Log.d("Reservations-Update", t.getMessage() != null?t.getMessage():"error");
//                        }
//                    });
//                }
//            });

            }
        }
        else {
            ImageView oldImageView = convertView.findViewById(R.id.old_accommodation_request_image);
            TextView oldName = convertView.findViewById(R.id.old_accommodation_request_name_and_type);
            TextView oldLocation = convertView.findViewById(R.id.old_accommodation_request_location);
            TextView oldOwnerEmail = convertView.findViewById(R.id.old_accommodation_request_owner_email);
            TextView oldMinGuests = convertView.findViewById(R.id.old_accommodation_request_min_guests);
            TextView oldMaxGuests = convertView.findViewById(R.id.old_accommodation_request_max_guests);

            ImageView newImageView = convertView.findViewById(R.id.new_accommodation_request_image);
            TextView newName = convertView.findViewById(R.id.new_accommodation_request_name_and_type);
            TextView newLocation = convertView.findViewById(R.id.new_accommodation_request_location);
            TextView newOwnerEmail = convertView.findViewById(R.id.new_accommodation_request_owner_email);
            TextView newMinGuests = convertView.findViewById(R.id.new_accommodation_request_min_guests);
            TextView newMaxGuests = convertView.findViewById(R.id.new_accommodation_request_max_guests);

            Button approveButton = convertView.findViewById(R.id.update_accommodation_request_approve_button);
            Button rejectButton = convertView.findViewById(R.id.update_accommodation_request_reject_button);

            if(accommodationRequest != null) {
                //String uri = "@drawable/" + product.getImagePath();
                //Resources resources = getContext().getResources();
                //final int resourceId = resources.getIdentifier(uri, "drawable", getContext().getPackageName());

                oldImageView.setImageResource(R.drawable.a);
                oldName.setText(accommodationRequest.getOldAccommodation().getName() +", " +accommodationRequest.getOldAccommodation().getAccommodationType().toString());
                oldLocation.setText(accommodationRequest.getOldAccommodation().getLocation());
                oldOwnerEmail.setText(accommodationRequest.getOldAccommodation().getOwnerEmail());
                oldMinGuests.setText(Integer.toString(accommodationRequest.getOldAccommodation().getMinGuests()));
                oldMaxGuests.setText(Integer.toString(accommodationRequest.getOldAccommodation().getMaxGuests()));

                newImageView.setImageResource(R.drawable.a);
                newName.setText(accommodationRequest.getNewAccommodation().getName() +", " +accommodationRequest.getNewAccommodation().getAccommodationType().toString());
                newLocation.setText(accommodationRequest.getNewAccommodation().getLocation());
                newOwnerEmail.setText(accommodationRequest.getNewAccommodation().getOwnerEmail());
                newMinGuests.setText(Integer.toString(accommodationRequest.getNewAccommodation().getMinGuests()));
                newMaxGuests.setText(Integer.toString(accommodationRequest.getNewAccommodation().getMaxGuests()));
            }
        }

        LinearLayout createLayout = convertView.findViewById(R.id.create_request_item);
        LinearLayout updateLayout = convertView.findViewById(R.id.update_request_item);
        if (createViewVisibility[position]) {
            createLayout.setVisibility(View.VISIBLE);
            updateLayout.setVisibility(View.GONE);
        } else {
            createLayout.setVisibility(View.GONE);
            updateLayout.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}

