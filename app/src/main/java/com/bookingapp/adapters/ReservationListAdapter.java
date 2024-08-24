package com.bookingapp.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bookingapp.R;
import com.bookingapp.activities.HomeActivity;
import com.bookingapp.model.Accommodation;
import com.bookingapp.model.DateRange;
import com.bookingapp.model.Notification;
import com.bookingapp.model.NotificationType;
import com.bookingapp.model.Reservation;
import com.bookingapp.model.ReservationStatus;
import com.bookingapp.model.ReservationWithAccommodation;
import com.bookingapp.model.UserType;
import com.bookingapp.service.ServiceUtils;
import com.bookingapp.service.UserInfo;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationListAdapter extends ArrayAdapter<ReservationWithAccommodation> {
    private ArrayList<ReservationWithAccommodation> rReservations;
    private Activity activity;
    private FragmentManager fragmentManager;
    private boolean[] viewVisibility = null;
    private boolean[] acceptRejectViewVisibility = null;

    public ReservationListAdapter(Activity context, FragmentManager fragmentManager, ArrayList<ReservationWithAccommodation> reservations) {
        super(context, R.layout.reservation_card, reservations);
        rReservations = reservations;
        activity = context;
        fragmentManager = fragmentManager;
        this.viewVisibility = new boolean[reservations.size()];
        this.acceptRejectViewVisibility = new boolean[reservations.size()];
    }

    @Override
    public int getCount() {
        return rReservations.size();
    }

    @Nullable
    @Override
    public ReservationWithAccommodation getItem(int position) {
        return rReservations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReservationWithAccommodation reservation = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reservation_card,
                    parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.reservation_accommodation_image);
        TextView name = convertView.findViewById(R.id.reservation_accommodation_name_and_type);
        TextView location = convertView.findViewById(R.id.reservation_accommodation_location);
        TextView startDate = convertView.findViewById(R.id.reservation_start_date);
        TextView duration = convertView.findViewById(R.id.reservation_duration);
        TextView price = convertView.findViewById(R.id.reservation_price);
        TextView ownerEmail = convertView.findViewById(R.id.reservation_owner_email);
        TextView guestEmail = convertView.findViewById(R.id.reservation_guest_email);
        TextView guestNumber = convertView.findViewById(R.id.reservation_guest_number);
        TextView status = convertView.findViewById(R.id.reservation_status);
        TextView deadline = convertView.findViewById(R.id.reservation_cancellation_deadline);
        TextView cancelledTimes = convertView.findViewById(R.id.reservation_cancelled_times);
        Button cancelButton = convertView.findViewById(R.id.reservation_cancel_button);
        Button acceptButton = convertView.findViewById(R.id.reservation_accept_button);
        Button rejectButton = convertView.findViewById(R.id.reservation_reject_button);

        if(reservation != null) {
            //String uri = "@drawable/" + product.getImagePath();
            //Resources resources = getContext().getResources();
            //final int resourceId = resources.getIdentifier(uri, "drawable", getContext().getPackageName());
            imageView.setImageResource(R.drawable.a);

            name.setText(reservation.getAccommodation().getName() + ", " + reservation.getAccommodation().getAccommodationType().toString());
            location.setText(reservation.getAccommodation().getLocation());
            startDate.setText(reservation.getDateAsDate().toString());
            duration.setText(Integer.toString(reservation.getDays()));
            price.setText(Double.toString(reservation.getPrice()));
            ownerEmail.setText(Html.fromHtml("<a href='#'>" + reservation.getAccommodation().getOwnerEmail() + "</a>", Html.FROM_HTML_MODE_LEGACY));
            ownerEmail.setClickable(true);
            ownerEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController navController = Navigation.findNavController(activity, R.id.fragment_nav_content_main);
                    com.google.android.material.navigation.NavigationView navigationView = activity.findViewById(R.id.nav_view);
                    Menu menu = navigationView.getMenu();
                    MenuItem menuItem = menu.findItem(R.id.nav_user_account);
                    NavigationUI.onNavDestinationSelected(menuItem, navController);
                    Bundle args = new Bundle();
                    args.putString("userEmail", reservation.getAccommodation().getOwnerEmail());
                    navController.navigate(R.id.nav_user_account, args,
                            new NavOptions.Builder()
                                    .setEnterAnim(android.R.animator.fade_in)
                                    .setExitAnim(android.R.animator.fade_out).setPopUpTo(R.id.nav_accommodations, false)
                                    .build());
                }
            });
            guestEmail.setText(Html.fromHtml("<a href='#'>" + reservation.getGuestEmail() + "</a>", Html.FROM_HTML_MODE_LEGACY));
            guestEmail.setClickable(true);
            guestEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController navController = Navigation.findNavController(activity, R.id.fragment_nav_content_main);
                    com.google.android.material.navigation.NavigationView navigationView = activity.findViewById(R.id.nav_view);
                    Menu menu = navigationView.getMenu();
                    MenuItem menuItem = menu.findItem(R.id.nav_user_account);
                    NavigationUI.onNavDestinationSelected(menuItem, navController);
                    Bundle args = new Bundle();
                    args.putString("userEmail", reservation.getGuestEmail());
                    navController.navigate(R.id.nav_user_account, args,
                            new NavOptions.Builder()
                                    .setEnterAnim(android.R.animator.fade_in)
                                    .setExitAnim(android.R.animator.fade_out).setPopUpTo(R.id.nav_accommodations, false)
                                    .build());
                }
            });
            guestNumber.setText(Integer.toString(reservation.getGuestNumber()));
            status.setText(reservation.getStatus().toString());
            deadline.setText(Integer.toString(reservation.getAccommodation().getReservationCancellationDeadline()));
            cancelledTimes.setText(Integer.toString(reservation.getCancelledTimes()));
            try {
                if (reservation.getStatus().equals(ReservationStatus.Waiting) && UserInfo.getType().equals(UserType.Guest)) {
                    viewVisibility[position] = true;
                    cancelButton.setEnabled(reservation.getShowCancelButton());
                }
                else {
                    viewVisibility[position] = false;
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            try {
                if(reservation.getStatus().equals(ReservationStatus.Waiting) && UserInfo.getType().equals(UserType.Owner)) {
                    acceptRejectViewVisibility[position] = true;
                }
                else {
                    acceptRejectViewVisibility[position] = false;
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if(acceptRejectViewVisibility[position]){
                acceptButton.setVisibility(View.VISIBLE);
                rejectButton.setVisibility(View.VISIBLE);
            }
            else {
                acceptButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.GONE);
            }
            if(viewVisibility[position]) {
                cancelButton.setVisibility(View.VISIBLE);
            } else {
                cancelButton.setVisibility(View.GONE);
            }
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Reservation res = new Reservation();
                    res.setId(reservation.getId());
                    res.setGuestEmail(reservation.getGuestEmail());
                    res.setAccommodationId(reservation.getAccommodation().getId());
                    res.setDate(reservation.getDate());
                    res.setDays(reservation.getDays());
                    res.setGuestNumber(reservation.getGuestNumber());
                    res.setPrice(reservation.getPrice());
                    res.setStatus(ReservationStatus.Cancelled);
                    Call<Reservation> call = ServiceUtils.reservationService.update(res.getId(), res);
                    call.enqueue(new Callback<Reservation>() {
                        @Override
                        public void onResponse(@NonNull Call<Reservation> call, @NonNull Response<Reservation> response) {
                            if (response.code() == 200){
                                Log.d("Reservations-Update","Message received");
                                System.out.println(response.body());
                                reservation.setStatus(ReservationStatus.Cancelled);
                                for (int i = 0; i < rReservations.size(); i++) {
                                    if (rReservations.get(i).getId() == res.getId()) {
                                        rReservations.set(i, reservation);
                                        break;
                                    }
                                }
                                Notification notification = new Notification();
                                notification.setUserEmail(reservation.getAccommodation().getOwnerEmail());
                                try {
                                    notification.setOtherUserEmail(UserInfo.getEmail());
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                notification.setType(NotificationType.ReservationCancelled);
                                List<Integer> now = new ArrayList<>();
                                now.add(LocalDateTime.now().getYear());
                                now.add(LocalDateTime.now().getMonthValue());
                                now.add(LocalDateTime.now().getDayOfMonth());
                                now.add(LocalDateTime.now().getHour());
                                now.add(LocalDateTime.now().getMinute());
                                notification.setCreatedAt(now);
                                Call<Notification> notificationCall = ServiceUtils.notificationService.add(notification);
                                notificationCall.enqueue(new Callback<Notification>() {
                                    @Override
                                    public void onResponse(Call<Notification> call, Response<Notification> response) {
                                        if (response.code() == 201) {
                                            Log.d("Notification-New","Message received");
                                            System.out.println(response.body());
                                        }
                                        else {
                                            Log.d("Notification-New","Message received: "+response.code());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Notification> call, Throwable t) {
                                        Log.d("Notification-New", t.getMessage() != null?t.getMessage():"error");
                                    }
                                });
                                notifyDataSetChanged();
                            }
                            else {
                                Log.d("Reservations-Update","Message received: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Reservation> call, @NonNull Throwable t) {
                            Log.d("Reservations-Update", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                }
            });
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Reservation res = new Reservation();
                    res.setId(reservation.getId());
                    res.setGuestEmail(reservation.getGuestEmail());
                    res.setAccommodationId(reservation.getAccommodation().getId());
                    res.setDate(reservation.getDate());
                    res.setDays(reservation.getDays());
                    res.setGuestNumber(reservation.getGuestNumber());
                    res.setPrice(reservation.getPrice());
                    res.setStatus(ReservationStatus.Approved);
                    Call<Reservation> call = ServiceUtils.reservationService.update(res.getId(), res);
                    call.enqueue(new Callback<Reservation>() {
                        @Override
                        public void onResponse(@NonNull Call<Reservation> call, @NonNull Response<Reservation> response) {
                            if (response.code() == 200){
                                Log.d("Reservations-Update","Message received");
                                reservation.setStatus(ReservationStatus.Approved);
                                for (int i = 0; i < rReservations.size(); i++) {
                                    if (rReservations.get(i).getId() == res.getId()) {
                                        rReservations.set(i, reservation);
                                        break;
                                    }
                                }
                                Notification notification = new Notification();
                                notification.setUserEmail(reservation.getGuestEmail());
                                try {
                                    notification.setOtherUserEmail(UserInfo.getEmail());
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                notification.setType(NotificationType.ReservationResponse);
                                List<Integer> now = new ArrayList<>();
                                now.add(LocalDateTime.now().getYear());
                                now.add(LocalDateTime.now().getMonthValue());
                                now.add(LocalDateTime.now().getDayOfMonth());
                                now.add(LocalDateTime.now().getHour());
                                now.add(LocalDateTime.now().getMinute());
                                notification.setCreatedAt(now);
                                Call<Notification> notificationCall = ServiceUtils.notificationService.add(notification);
                                notificationCall.enqueue(new Callback<Notification>() {
                                    @Override
                                    public void onResponse(Call<Notification> call, Response<Notification> response) {
                                        if (response.code() == 201) {
                                            Log.d("Notification-New","Message received");
                                            System.out.println(response.body());
                                        }
                                        else {
                                            Log.d("Notification-New","Message received: "+response.code());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Notification> call, Throwable t) {
                                        Log.d("Notification-New", t.getMessage() != null?t.getMessage():"error");
                                    }
                                });
                                notifyDataSetChanged();
                            }
                            else {
                                Log.d("Reservations-Accept","Message received: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Reservation> call, @NonNull Throwable t) {
                            Log.d("Reservations-Accept", t.getMessage() != null?t.getMessage():"error");
                        }
                    });


                }
            });
            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Reservation res = new Reservation();
                    res.setId(reservation.getId());
                    res.setGuestEmail(reservation.getGuestEmail());
                    res.setAccommodationId(reservation.getAccommodation().getId());
                    res.setDate(reservation.getDate());
                    res.setDays(reservation.getDays());
                    res.setGuestNumber(reservation.getGuestNumber());
                    res.setPrice(reservation.getPrice());
                    res.setStatus(ReservationStatus.Rejected);
                    Call<Reservation> call = ServiceUtils.reservationService.update(res.getId(), res);
                    call.enqueue(new Callback<Reservation>() {
                        @Override
                        public void onResponse(@NonNull Call<Reservation> call, @NonNull Response<Reservation> response) {
                            if (response.code() == 200){
                                Log.d("Reservations-Update","Message received");
                                reservation.setStatus(ReservationStatus.Rejected);
                                for (int i = 0; i < rReservations.size(); i++) {
                                    if (rReservations.get(i).getId() == res.getId()) {
                                        rReservations.set(i, reservation);
                                        break;
                                    }
                                }
                                Notification notification = new Notification();
                                notification.setUserEmail(reservation.getGuestEmail());
                                try {
                                    notification.setOtherUserEmail(UserInfo.getEmail());
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                                notification.setType(NotificationType.ReservationResponse);
                                List<Integer> now = new ArrayList<>();
                                now.add(LocalDateTime.now().getYear());
                                now.add(LocalDateTime.now().getMonthValue());
                                now.add(LocalDateTime.now().getDayOfMonth());
                                now.add(LocalDateTime.now().getHour());
                                now.add(LocalDateTime.now().getMinute());
                                notification.setCreatedAt(now);
                                Call<Notification> notificationCall = ServiceUtils.notificationService.add(notification);
                                notificationCall.enqueue(new Callback<Notification>() {
                                    @Override
                                    public void onResponse(Call<Notification> call, Response<Notification> response) {
                                        if (response.code() == 201) {
                                            Log.d("Notification-New","Message received");
                                            System.out.println(response.body());
                                        }
                                        else {
                                            Log.d("Notification-New","Message received: "+response.code());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Notification> call, Throwable t) {
                                        Log.d("Notification-New", t.getMessage() != null?t.getMessage():"error");
                                    }
                                });
                                notifyDataSetChanged();
                            }
                            else {
                                Log.d("Reservations-Reject","Message received: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Reservation> call, @NonNull Throwable t) {
                            Log.d("Reservations-Reject", t.getMessage() != null?t.getMessage():"error");

                        }
                    });
                }
            });
        }

        return convertView;
    }


}
