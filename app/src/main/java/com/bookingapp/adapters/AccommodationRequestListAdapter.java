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
import com.bookingapp.model.Accommodation;
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
    private Button approveCreate;
    private Button rejectCreate;
    private Button approveUpdate;
    private Button rejectUpdate;

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
            approveCreate = convertView.findViewById(R.id.accommodation_request_approve_button);
            rejectCreate = convertView.findViewById(R.id.accommodation_request_reject_button);

            if(accommodationRequest != null) {
                imageView.setImageResource(R.drawable.a);
                name.setText(accommodationRequest.getNewAccommodation().getName() + ", " + accommodationRequest.getNewAccommodation().getAccommodationType().toString());
                location.setText(accommodationRequest.getNewAccommodation().getLocation());
                ownerEmail.setText(accommodationRequest.getNewAccommodation().getOwnerEmail());
                minGuests.setText(Integer.toString(accommodationRequest.getNewAccommodation().getMinGuests()));
                maxGuests.setText(Integer.toString(accommodationRequest.getNewAccommodation().getMaxGuests()));

                approveCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Call<AccommodationRequest> callRequest;
                        Call<Accommodation> callAccommodation;

                        callRequest = ServiceUtils.accommodationRequestService.delete(accommodationRequest.getId());
                        callRequest.enqueue(new Callback<AccommodationRequest>() {
                            @Override
                            public void onResponse(@NonNull Call<AccommodationRequest> call, @NonNull Response<AccommodationRequest> response) {
                                if (response.isSuccessful()) {
                                    Call<ArrayList<AccommodationRequest>> call1 = null;
                                    try {
                                        if (UserInfo.getType().equals(UserType.Admin)) {
                                            call1 = ServiceUtils.accommodationRequestService.getAll();
                                            call1.enqueue(new Callback<ArrayList<AccommodationRequest>>() {
                                                @Override
                                                public void onResponse(@NonNull Call<ArrayList<AccommodationRequest>> call, @NonNull Response<ArrayList<AccommodationRequest>> response) {
                                                    if (response.code() == 200) {
                                                        Log.d("REZ", "Message received");
                                                        System.out.println(response.body());
                                                        aAccommodationRequests = response.body();
                                                        notifyDataSetChanged();
                                                    } else {
                                                        Log.d("REZ", "Message received: " + response.code());
                                                    }
                                                }

                                                @Override
                                                public void onFailure(@NonNull Call<ArrayList<AccommodationRequest>> call, @NonNull Throwable t) {
                                                    Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
                                                }
                                            });
                                        }
                                    } catch (JSONException e) {
                                        Log.e("JSONException", e.getMessage());
                                    }
                                } else {
                                    Log.d("ApproveCreate", "Request failed: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<AccommodationRequest> call, @NonNull Throwable t) {
                                Log.d("ApproveCreate", "Request failed: " + t.getMessage());
                            }
                        });

                        accommodationRequest.getNewAccommodation().setIsApproved(true);

                        callAccommodation = ServiceUtils.accommodationService.update(accommodationRequest.getNewAccommodation().getId(), accommodationRequest.getNewAccommodation());
                        callAccommodation.enqueue(new Callback<Accommodation>() {
                            @Override
                            public void onResponse(@NonNull Call<Accommodation> call, @NonNull Response<Accommodation> response) {
                                if (response.isSuccessful()) {
                                    // Navigate or handle success
                                } else {
                                    Log.d("ApproveCreate UpdateAcc", "Request failed: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Accommodation> call, @NonNull Throwable t) {
                                Log.d("ApproveCreate UpdateAcc", "Request failed: " + t.getMessage());
                            }
                        });
                    }
                });
                rejectCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Call<AccommodationRequest> callRequest;
                        Call<Accommodation> callAccommodation;

                        callRequest = ServiceUtils.accommodationRequestService.delete(accommodationRequest.getId());
                        callRequest.enqueue(new Callback<AccommodationRequest>() {
                            @Override
                            public void onResponse(@NonNull Call<AccommodationRequest> call, @NonNull Response<AccommodationRequest> response) {
                                if (response.isSuccessful()) {
                                    Call<ArrayList<AccommodationRequest>> call1 = null;
                                    try {
                                        if (UserInfo.getType().equals(UserType.Admin)) {
                                            call1 = ServiceUtils.accommodationRequestService.getAll();
                                        }
                                    } catch (JSONException e) {
                                        Log.e("JSONException", e.getMessage());
                                    }

                                    if (call1 != null) {
                                        call1.enqueue(new Callback<ArrayList<AccommodationRequest>>() {
                                            @Override
                                            public void onResponse(@NonNull Call<ArrayList<AccommodationRequest>> call, @NonNull Response<ArrayList<AccommodationRequest>> response) {
                                                if (response.code() == 200) {
                                                    Log.d("REZ", "Message received");
                                                    System.out.println(response.body());
                                                    aAccommodationRequests = response.body();
                                                    notifyDataSetChanged();
                                                } else {
                                                    Log.d("REZ", "Message received: " + response.code());
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<ArrayList<AccommodationRequest>> call, @NonNull Throwable t) {
                                                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
                                            }
                                        });
                                    }
                                } else {
                                    Log.d("RejectCreate", "Request failed: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<AccommodationRequest> call, @NonNull Throwable t) {
                                Log.d("RejectCreate", "Request failed: " + t.getMessage());
                            }
                        });

                        callAccommodation = ServiceUtils.accommodationService.deleteOne(accommodationRequest.getNewAccommodation().getId());
                        callAccommodation.enqueue(new Callback<Accommodation>() {
                            @Override
                            public void onResponse(@NonNull Call<Accommodation> call, @NonNull Response<Accommodation> response) {
                                if (response.isSuccessful()) {
                                    // Navigate or handle success
                                } else {
                                    Log.d("RejectCreate DeleteAcc", "Request failed: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Accommodation> call, @NonNull Throwable t) {
                                Log.d("RejectCreate DeleteAcc", "Request failed: " + t.getMessage());
                            }
                        });
                    }
                });
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

            approveUpdate = convertView.findViewById(R.id.update_accommodation_request_approve_button);
            rejectUpdate = convertView.findViewById(R.id.update_accommodation_request_reject_button);

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



                approveUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Call<AccommodationRequest> callRequest;
                        Call<Accommodation> callAccommodation;

                        callRequest = ServiceUtils.accommodationRequestService.delete(accommodationRequest.getId());
                        callRequest.enqueue(new Callback<AccommodationRequest>() {
                            @Override
                            public void onResponse(@NonNull Call<AccommodationRequest> call, @NonNull Response<AccommodationRequest> response) {
                                if (response.isSuccessful()) {
                                    Call<ArrayList<AccommodationRequest>> call1 = null;
                                    try {
                                        if (UserInfo.getType().equals(UserType.Admin)) {
                                            call1 = ServiceUtils.accommodationRequestService.getAll();
                                        }
                                    } catch (JSONException e) {
                                        Log.e("JSONException", e.getMessage());
                                    }

                                    if (call1 != null) {
                                        call1.enqueue(new Callback<ArrayList<AccommodationRequest>>() {
                                            @Override
                                            public void onResponse(@NonNull Call<ArrayList<AccommodationRequest>> call, @NonNull Response<ArrayList<AccommodationRequest>> response) {
                                                if (response.code() == 200) {
                                                    Log.d("REZ", "Message received");
                                                    System.out.println(response.body());
                                                    aAccommodationRequests = response.body();
                                                    notifyDataSetChanged();
                                                } else {
                                                    Log.d("REZ", "Message received: " + response.code());
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<ArrayList<AccommodationRequest>> call, @NonNull Throwable t) {
                                                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
                                            }
                                        });
                                    }
                                } else {
                                    Log.d("ApproveUpdate", "Request failed: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<AccommodationRequest> call, @NonNull Throwable t) {
                                Log.d("ApproveUpdate", "Request failed: " + t.getMessage());
                            }
                        });

                        Call<Reservation> callReservation = ServiceUtils.reservationService.updateNew(accommodationRequest.getOldAccommodation().getId(),accommodationRequest.getNewAccommodation().getId());
                        callReservation.enqueue(new Callback<Reservation>() {
                            @Override
                            public void onResponse(@NonNull Call<Reservation> call, @NonNull Response<Reservation> response) {
                                if (response.isSuccessful()) {
                                    Log.d("ApproveUpdate UpdateReservation", "Reservations updated successfully");
                                } else {
                                    Log.d("ApproveUpdate UpdateReservation", "Request failed: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Reservation> call, @NonNull Throwable t) {
                                Log.d("ApproveUpdate UpdateReservation", "Request failed: " + t.getMessage());

                            }
                        });

                        callAccommodation = ServiceUtils.accommodationService.deleteOne(accommodationRequest.getOldAccommodation().getId());
                        callAccommodation.enqueue(new Callback<Accommodation>() {
                            @Override
                            public void onResponse(@NonNull Call<Accommodation> call, @NonNull Response<Accommodation> response) {
                                if (response.isSuccessful()) {
                                    // Navigacija/Obavestenje
                                    Log.d("DeleteOldAcc", "Old accommodation deleted successfully");

                                } else {
                                    Log.d("ApproveUpdate DeleteOld", "Request failed: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Accommodation> call, @NonNull Throwable t) {
                                Log.d("ApproveUpdate DeleteOld", "Request failed: " + t.getMessage());

                            }
                        });

                        accommodationRequest.getNewAccommodation().setIsApproved(true);

                        callAccommodation = ServiceUtils.accommodationService.update(accommodationRequest.getNewAccommodation().getId(), accommodationRequest.getNewAccommodation());
                        callAccommodation.enqueue(new Callback<Accommodation>() {
                            @Override
                            public void onResponse(@NonNull Call<Accommodation> call, @NonNull Response<Accommodation> response) {
                                if (response.isSuccessful()) {
                                    // Navigacija/Obavestenje
                                } else {
                                    Log.d("ApproveUpdate UpdateAcc", "Request failed: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Accommodation> call, @NonNull Throwable t) {
                                Log.d("ApproveUpdate UpdateAcc", "Request failed: " + t.getMessage());
                            }
                        });
                    }
                });

                rejectUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Call<AccommodationRequest> callRequest;
                        Call<Accommodation> callAccommodation;

                        callRequest = ServiceUtils.accommodationRequestService.delete(accommodationRequest.getId());
                        callRequest.enqueue(new Callback<AccommodationRequest>() {
                            @Override
                            public void onResponse(@NonNull Call<AccommodationRequest> call, @NonNull Response<AccommodationRequest> response) {
                                if (response.isSuccessful()) {
                                    Call<ArrayList<AccommodationRequest>> call1 = null;
                                    try {
                                        if (UserInfo.getType().equals(UserType.Admin)) {
                                            call1 = ServiceUtils.accommodationRequestService.getAll();
                                        }
                                    } catch (JSONException e) {
                                        Log.e("JSONException", e.getMessage());
                                    }

                                    if (call1 != null) {
                                        call1.enqueue(new Callback<ArrayList<AccommodationRequest>>() {
                                            @Override
                                            public void onResponse(@NonNull Call<ArrayList<AccommodationRequest>> call, @NonNull Response<ArrayList<AccommodationRequest>> response) {
                                                if (response.code() == 200) {
                                                    Log.d("REZ", "Message received");
                                                    System.out.println(response.body());
                                                    aAccommodationRequests = response.body();
                                                    notifyDataSetChanged();
                                                } else {
                                                    Log.d("REZ", "Message received: " + response.code());
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<ArrayList<AccommodationRequest>> call, @NonNull Throwable t) {
                                                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "error");
                                            }
                                        });
                                    }
                                } else {
                                    Log.d("RejectUpdate", "Request failed: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<AccommodationRequest> call, @NonNull Throwable t) {
                                Log.d("RejectUpdate", "Request failed: " + t.getMessage());
                            }
                        });

                        callAccommodation = ServiceUtils.accommodationService.deleteOne(accommodationRequest.getNewAccommodation().getId());
                        callAccommodation.enqueue(new Callback<Accommodation>() {
                            @Override
                            public void onResponse(@NonNull Call<Accommodation> call, @NonNull Response<Accommodation> response) {
                                if (response.isSuccessful()) {
                                    // Navigacija/Obavestenje
                                } else {
                                    Log.d("RejectCreate DeleteAcc", "Request failed: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Accommodation> call, @NonNull Throwable t) {
                                Log.d("RejectCreate DeleteAcc", "Request failed: " + t.getMessage());
                            }
                        });

                        accommodationRequest.getOldAccommodation().setIsApproved(true);

                        callAccommodation = ServiceUtils.accommodationService.update(accommodationRequest.getOldAccommodation().getId(), accommodationRequest.getOldAccommodation());
                        callAccommodation.enqueue(new Callback<Accommodation>() {
                            @Override
                            public void onResponse(@NonNull Call<Accommodation> call, @NonNull Response<Accommodation> response) {
                                if (response.isSuccessful()) {
                                    // Navigacija/Obavestenje
                                } else {
                                    Log.d("RejectUpdate UpdateOldAcc", "Request failed: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Accommodation> call, @NonNull Throwable t) {
                                Log.d("RejectUpdate UpdateOldAcc", "Request failed: " + t.getMessage());
                            }
                        });

                    }
                });
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


