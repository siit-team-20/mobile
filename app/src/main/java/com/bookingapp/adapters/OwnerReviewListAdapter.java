package com.bookingapp.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.bookingapp.R;
import com.bookingapp.model.AccommodationReview;
import com.bookingapp.model.OwnerReview;
import com.bookingapp.model.UserType;
import com.bookingapp.service.ServiceUtils;
import com.bookingapp.service.UserInfo;

import org.json.JSONException;

import java.security.acl.Owner;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerReviewListAdapter extends ArrayAdapter<OwnerReview> {
    private ArrayList<OwnerReview> aOwnersReviews;
    private Activity activity;
    private FragmentManager fragmentManager;
    private boolean[] guestDeleteButtonVisibility = null;

    private boolean[] ownerReportButtonVisibility = null;

    public OwnerReviewListAdapter(Activity context, FragmentManager fragmentManager, ArrayList<OwnerReview> ownerReviews){
        super(context, R.layout.owner_review_card, ownerReviews);
        aOwnersReviews = ownerReviews;
        activity = context;
        fragmentManager = fragmentManager;
        this.guestDeleteButtonVisibility = new boolean[ownerReviews.size()];
        this.ownerReportButtonVisibility = new boolean[ownerReviews.size()];
    }
    @Override
    public int getCount() {
        return aOwnersReviews.size();
    }
    @Nullable
    @Override
    public OwnerReview getItem(int position){return aOwnersReviews.get(position);}
    @Override
    public long getItemId(int position){return  position;}
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        OwnerReview ownerReview = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.owner_review_card, parent, false);
        }
        LinearLayout ownerReviewCard = convertView.findViewById(R.id.owner_review_card_item);
        TextView guestEmail = convertView.findViewById(R.id.owner_review_guest_email);
        TextView comment = convertView.findViewById(R.id.owner_review_comment);
        TextView rating = convertView.findViewById(R.id.owner_review_rating_value);
        TextView submitDate = convertView.findViewById(R.id.owner_review_submit_date);
        Button deleteButton = convertView.findViewById(R.id.owner_review_delete_button);
        Button reportButton = convertView.findViewById(R.id.owner_review_report_button);

        if(ownerReview != null){
            guestEmail.setText(ownerReview.getGuestEmail());
            comment.setText(ownerReview.getComment());
            rating.setText(String.valueOf(ownerReview.getRating().ordinal() + 1));
            submitDate.setText(ownerReview.getSubmitDate().get(2) + "." + ownerReview.getSubmitDate().get(1) + "." + ownerReview.getSubmitDate().get(0) + ".");
            try {
                if (ownerReview.getGuestEmail().equals(UserInfo.getEmail()) && !ownerReview.getIsReported()) {
                    guestDeleteButtonVisibility[position] = true;
                }
            } catch (Exception e) {
                guestDeleteButtonVisibility[position] = false;
            }
            try {
                if (UserInfo.getEmail().equals(ownerReview.getOwnerEmail()) && !ownerReview.getIsReported()) {
                    ownerReportButtonVisibility[position] = true;
                }
            } catch (Exception e) {
                ownerReportButtonVisibility[position] = false;
            }

            if(guestDeleteButtonVisibility[position]) {
                deleteButton.setVisibility(View.VISIBLE);
            } else {
                deleteButton.setVisibility(View.GONE);
            }

            if(ownerReportButtonVisibility[position]) {
                reportButton.setVisibility(View.VISIBLE);
            } else {
                reportButton.setVisibility(View.GONE);
            }
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Call<OwnerReview> call = ServiceUtils.ownerReviewService.delete(ownerReview.getId());
                    call.enqueue(new Callback<OwnerReview>() {
                        @Override
                        public void onResponse(@NonNull Call<OwnerReview> call, Response<OwnerReview> response) {
                            if (response.isSuccessful()) {
                                Log.d("REZ","Message received");
                                System.out.println(response.body());
                                Call<ArrayList<OwnerReview>> callReviews = ServiceUtils.ownerReviewService.get(ownerReview.getOwnerEmail(), false);
                                callReviews.enqueue(new Callback<ArrayList<OwnerReview>>() {
                                    @Override
                                    public void onResponse(Call<ArrayList<OwnerReview>> call, Response<ArrayList<OwnerReview>> response) {
                                        if (response.code() == 200){
                                            Log.d("Reservations-Update","Message received");
                                            System.out.println(response.body());
                                            aOwnersReviews = response.body();
                                            guestDeleteButtonVisibility = new boolean[aOwnersReviews.size()];
                                            notifyDataSetChanged();
                                        }
                                        else {
                                            Log.d("Reservations-Update","Message received: "+response.code());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ArrayList<OwnerReview>> call, Throwable t) {
                                        Log.d("Reservations-Update", t.getMessage() != null?t.getMessage():"error");
                                    }
                                });
                            }
                            else {
                                Log.d("REZ","Message received: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<OwnerReview> call, @NonNull Throwable t) {
                            Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                }
            });

            reportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OwnerReview updatedOwnerReview = new OwnerReview();
                    updatedOwnerReview.setId(ownerReview.getId());
                    updatedOwnerReview.setGuestEmail(ownerReview.getGuestEmail());
                    updatedOwnerReview.setOwnerEmail(ownerReview.getOwnerEmail());
                    updatedOwnerReview.setComment(ownerReview.getComment());
                    updatedOwnerReview.setRating(ownerReview.getRating());
                    updatedOwnerReview.setIsReported(true);
                    updatedOwnerReview.setSubmitDate(ownerReview.getSubmitDate());
                    Call<OwnerReview> call = ServiceUtils.ownerReviewService.update(ownerReview.getId(), updatedOwnerReview);
                    call.enqueue(new Callback<OwnerReview>() {
                        @Override
                        public void onResponse(@NonNull Call<OwnerReview> call, Response<OwnerReview> response) {
                            if (response.isSuccessful()) {
                                Log.d("REZ","Message received");
                                System.out.println(response.body());
                                Call<ArrayList<OwnerReview>> callReviews = ServiceUtils.ownerReviewService.get(ownerReview.getOwnerEmail(), false);
                                callReviews.enqueue(new Callback<ArrayList<OwnerReview>>() {
                                    @Override
                                    public void onResponse(Call<ArrayList<OwnerReview>> call, Response<ArrayList<OwnerReview>> response) {
                                        if (response.code() == 200){
                                            Log.d("Reservations-Update","Message received");
                                            System.out.println(response.body());
                                            aOwnersReviews = response.body();
                                            guestDeleteButtonVisibility = new boolean[aOwnersReviews.size()];
                                            ownerReportButtonVisibility = new boolean[aOwnersReviews.size()];
                                            notifyDataSetChanged();
                                        }
                                        else {
                                            Log.d("Reservations-Update","Message received: "+response.code());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ArrayList<OwnerReview>> call, Throwable t) {
                                        Log.d("Reservations-Update", t.getMessage() != null?t.getMessage():"error");
                                    }
                                });
                            }
                            else {
                                Log.d("REZ","Message received: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<OwnerReview> call, @NonNull Throwable t) {
                            Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                }
            });
        }

        return convertView;
    }







}
