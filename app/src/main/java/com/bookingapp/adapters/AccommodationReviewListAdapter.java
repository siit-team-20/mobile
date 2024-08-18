package com.bookingapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.bookingapp.R;
import com.bookingapp.model.AccommodationReview;
import com.bookingapp.model.Rating;

import java.util.ArrayList;

public class AccommodationReviewListAdapter extends ArrayAdapter<AccommodationReview> {
    private ArrayList<AccommodationReview> aAccommodationsReviews;
    private Activity activity;
    private FragmentManager fragmentManager;

    public AccommodationReviewListAdapter(Activity context, FragmentManager fragmentManager, ArrayList<AccommodationReview> accommodationReviews){
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

        if(accommodationReview != null){
            guestEmail.setText(accommodationReview.getGuestEmail());
            comment.setText(accommodationReview.getComment());
            rating.setText(String.valueOf(accommodationReview.getRating().ordinal() + 1));
            submitDate.setText(accommodationReview.getSubmitDate().get(2) + "." + accommodationReview.getSubmitDate().get(1) + "." + accommodationReview.getSubmitDate().get(0) + ".");
        }

        return convertView;
    }

}
