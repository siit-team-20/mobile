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
import com.bookingapp.service.ServiceUtils;
import com.bookingapp.service.UserInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAccommodationReviewListAdapter extends ArrayAdapter<AccommodationReview> {

    private ArrayList<AccommodationReview> aAccommodationsReviews;
    private Activity activity;
    private FragmentManager fragmentManager;
    private Button approveUpdate;
    private Button deleteUpdate;

    public AdminAccommodationReviewListAdapter(Activity context, FragmentManager fragmentManager, ArrayList<AccommodationReview> accommodationReviews){
        super(context, R.layout.accommodation_review_card, accommodationReviews);
        aAccommodationsReviews = accommodationReviews;
        activity = context;
        fragmentManager = fragmentManager;
    }
    @Override
    public int getCount() {
        return aAccommodationsReviews.size();
    }

    @Nullable
    @Override
    public AccommodationReview getItem(int position){return aAccommodationsReviews.get(position);}
    @Override
    public long getItemId(int position){return  position;}
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        AccommodationReview accommodationReview = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.accommodation_review_card, parent, false);
        }
        LinearLayout accommodationReviewCard = convertView.findViewById(R.id.accommodation_review_card_item);
        TextView guestEmail = convertView.findViewById(R.id.accommodation_review_guest_email);
        TextView comment = convertView.findViewById(R.id.accommodation_review_comment);
        TextView rating = convertView.findViewById(R.id.accommodation_review_rating_value);
        TextView submitDate = convertView.findViewById(R.id.accommodation_review_submit_date);
        Button deleteButton = convertView.findViewById(R.id.delete_button);
        Button adminDeleteButton = convertView.findViewById(R.id.admin_delete_button);
        Button approveButton = convertView.findViewById(R.id.approveButton);

        if(accommodationReview != null){
            guestEmail.setText(accommodationReview.getGuestEmail());
            comment.setText(accommodationReview.getComment());
            rating.setText(String.valueOf(accommodationReview.getRating().ordinal() + 1));
            submitDate.setText(accommodationReview.getSubmitDate().get(2) + "." + accommodationReview.getSubmitDate().get(1) + "." + accommodationReview.getSubmitDate().get(0) + ".");
            deleteButton.setVisibility(View.GONE);
            approveButton.setVisibility(View.VISIBLE);
            adminDeleteButton.setVisibility(View.VISIBLE);

            approveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    accommodationReview.setIsApproved(true);
                    Call<AccommodationReview> accommodationReviewCall = ServiceUtils.accommodationReviewService.update(accommodationReview.getId(), accommodationReview);
                    accommodationReviewCall.enqueue(new Callback<AccommodationReview>() {
                        @Override
                        public void onResponse(Call<AccommodationReview> call, Response<AccommodationReview> response) {
                            if (response.isSuccessful()) {
                                System.out.println(response.body());
                                Call<ArrayList<AccommodationReview>> callReviews = ServiceUtils.accommodationReviewService.get(true);

                                callReviews.enqueue(new Callback<ArrayList<AccommodationReview>>() {
                                    @Override
                                    public void onResponse(Call<ArrayList<AccommodationReview>> call, Response<ArrayList<AccommodationReview>> response) {
                                        if (response.code() == 200) {
                                            Log.d("Reviews-Get","Message received");
                                            System.out.println(response.body());
                                            aAccommodationsReviews = response.body();
                                            notifyDataSetChanged();
                                        }
                                        else {
                                            Log.d("Reviews-Get","Message received: "+response.code());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ArrayList<AccommodationReview>> call, Throwable t) {
                                        Log.d("Reviews-Get", t.getMessage() != null?t.getMessage():"error");
                                    }
                                });
                            }
                            else {
                                Log.d("REZ","Message received: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<AccommodationReview> call, Throwable t) {
                            Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                }
            });

            adminDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Call<AccommodationReview> accommodationReviewCall = ServiceUtils.accommodationReviewService.delete(accommodationReview.getId());
                    accommodationReviewCall.enqueue(new Callback<AccommodationReview>() {
                        @Override
                        public void onResponse(Call<AccommodationReview> call, Response<AccommodationReview> response) {
                            if (response.isSuccessful()) {
                                System.out.println(response.body());
                                Call<ArrayList<AccommodationReview>> callReviews = ServiceUtils.accommodationReviewService.get(true);

                                callReviews.enqueue(new Callback<ArrayList<AccommodationReview>>() {
                                    @Override
                                    public void onResponse(Call<ArrayList<AccommodationReview>> call, Response<ArrayList<AccommodationReview>> response) {
                                        if (response.code() == 200) {
                                            Log.d("Reviews-Get","Message received");
                                            System.out.println(response.body());
                                            aAccommodationsReviews = response.body();
                                            notifyDataSetChanged();
                                        }
                                        else {
                                            Log.d("Reviews-Get","Message received: "+response.code());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ArrayList<AccommodationReview>> call, Throwable t) {
                                        Log.d("Reviews-Get", t.getMessage() != null?t.getMessage():"error");
                                    }
                                });
                            }
                            else {
                                Log.d("REZ","Message received: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<AccommodationReview> call, Throwable t) {
                            Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                }
            });
        }

        return convertView;
    }

}
