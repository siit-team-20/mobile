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
import com.bookingapp.service.ServiceUtils;
import com.bookingapp.service.UserInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOwnerReviewListAdapter extends ArrayAdapter<OwnerReview> {
    private ArrayList<OwnerReview> aOwnersReviews;
    private Activity activity;
    private FragmentManager fragmentManager;

    public AdminOwnerReviewListAdapter(Activity context, FragmentManager fragmentManager, ArrayList<OwnerReview> ownerReviews){
        super(context, R.layout.owner_review_card, ownerReviews);
        aOwnersReviews = ownerReviews;
        activity = context;
        fragmentManager = fragmentManager;
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
        Button approveButton = convertView.findViewById(R.id.approveButton);
        Button adminDeleteButton = convertView.findViewById(R.id.adminDeleteButton);

        if(ownerReview != null){
            guestEmail.setText(ownerReview.getGuestEmail());
            comment.setText(ownerReview.getComment());
            rating.setText(String.valueOf(ownerReview.getRating().ordinal() + 1));
            submitDate.setText(ownerReview.getSubmitDate().get(2) + "." + ownerReview.getSubmitDate().get(1) + "." + ownerReview.getSubmitDate().get(0) + ".");
            deleteButton.setVisibility(View.GONE);
            reportButton.setVisibility(View.GONE);
            approveButton.setVisibility(View.VISIBLE);
            adminDeleteButton.setVisibility(View.VISIBLE);

            approveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ownerReview.setIsReported(false);
                    Call<OwnerReview> ownerReviewCall = ServiceUtils.ownerReviewService.update(ownerReview.getId(), ownerReview);
                    ownerReviewCall.enqueue(new Callback<OwnerReview>() {
                        @Override
                        public void onResponse(Call<OwnerReview> call, Response<OwnerReview> response) {
                            if (response.isSuccessful()) {
                                System.out.println(response.body());
                                Call<ArrayList<OwnerReview>> callReviews = ServiceUtils.ownerReviewService.get(true);

                                callReviews.enqueue(new Callback<ArrayList<OwnerReview>>() {
                                    @Override
                                    public void onResponse(Call<ArrayList<OwnerReview>> call, Response<ArrayList<OwnerReview>> response) {
                                        if (response.code() == 200) {
                                            Log.d("Reviews-Get","Message received");
                                            System.out.println(response.body());
                                            aOwnersReviews = response.body();
                                            notifyDataSetChanged();
                                        }
                                        else {
                                            Log.d("Reviews-Get","Message received: "+response.code());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ArrayList<OwnerReview>> call, Throwable t) {
                                        Log.d("Reviews-Get", t.getMessage() != null?t.getMessage():"error");
                                    }
                                });
                            }
                            else {
                                Log.d("REZ","Message received: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<OwnerReview> call, Throwable t) {
                            Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                }
            });

            adminDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Call<OwnerReview> ownerReviewCall = ServiceUtils.ownerReviewService.delete(ownerReview.getId());
                    ownerReviewCall.enqueue(new Callback<OwnerReview>() {
                        @Override
                        public void onResponse(Call<OwnerReview> call, Response<OwnerReview> response) {
                            if (response.isSuccessful()) {
                                System.out.println(response.body());
                                Call<ArrayList<OwnerReview>> callReviews = ServiceUtils.ownerReviewService.get(true);

                                callReviews.enqueue(new Callback<ArrayList<OwnerReview>>() {
                                    @Override
                                    public void onResponse(Call<ArrayList<OwnerReview>> call, Response<ArrayList<OwnerReview>> response) {
                                        if (response.code() == 200) {
                                            Log.d("Reviews-Get","Message received");
                                            System.out.println(response.body());
                                            aOwnersReviews = response.body();
                                            notifyDataSetChanged();
                                        }
                                        else {
                                            Log.d("Reviews-Get","Message received: "+response.code());
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<ArrayList<OwnerReview>> call, @NonNull Throwable t) {
                                        Log.d("Reviews-Get", t.getMessage() != null?t.getMessage():"error");
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
