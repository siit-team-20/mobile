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
import com.bookingapp.model.OwnerReview;

import java.util.ArrayList;

public class OwnerReviewListAdapter extends ArrayAdapter<OwnerReview> {
    private ArrayList<OwnerReview> aOwnersReviews;
    private Activity activity;
    private FragmentManager fragmentManager;
    public OwnerReviewListAdapter(Activity context, FragmentManager fragmentManager, ArrayList<OwnerReview> ownerReviews){
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

        if(ownerReview != null){
            guestEmail.setText(ownerReview.getGuestEmail());
            comment.setText(ownerReview.getComment());
            rating.setText(String.valueOf(ownerReview.getRating().ordinal() + 1));
            submitDate.setText(ownerReview.getSubmitDate().get(2) + "." + ownerReview.getSubmitDate().get(1) + "." + ownerReview.getSubmitDate().get(0) + ".");
        }

        return convertView;
    }







}
