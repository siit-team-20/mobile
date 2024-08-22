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
import com.bookingapp.model.Notification;
import com.bookingapp.model.NotificationType;
import com.bookingapp.model.OwnerReview;

import java.util.ArrayList;

public class NotificationListAdapter extends ArrayAdapter<Notification> {
    private ArrayList<Notification> aNotifications;
    private Activity activity;
    private FragmentManager fragmentManager;
    public NotificationListAdapter(Activity context, FragmentManager fragmentManager, ArrayList<Notification> notifications){
        super(context, R.layout.notification_card, notifications);
        aNotifications = notifications;
        activity = context;
        fragmentManager = fragmentManager;
    }
    @Override
    public int getCount() {
        return aNotifications.size();
    }
    @Nullable
    @Override
    public Notification getItem(int position){return aNotifications.get(position);}
    @Override
    public long getItemId(int position){return  position;}
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        Notification notification = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_card, parent, false);
        }
        LinearLayout notificationCard = convertView.findViewById(R.id.owner_review_card_item);
        TextView message = convertView.findViewById(R.id.notifcation_message);//moraju se dodati ostala polja
        TextView date = convertView.findViewById(R.id.notification_date);
        if(notification != null){
            String desc = "";
            if (notification.getType().equals(NotificationType.ReservationCreated)) {
                desc = "New reservation from " + notification.getOtherUserEmail();
            }
            else if (notification.getType().equals(NotificationType.ReservationCancelled)) {
                desc = "Reservation from " + notification.getOtherUserEmail() + " was cancelled";
            }
            else if (notification.getType().equals(NotificationType.OwnerReviewAdded)) {
                desc = notification.getOtherUserEmail() + " reviewed you";
            }
            else if (notification.getType().equals(NotificationType.AccommodationReviewAdded)) {
                desc = notification.getOtherUserEmail() + " reviewed your accommodation";
            }
            else if (notification.getType().equals(NotificationType.ReservationResponse)) {
                desc = notification.getOtherUserEmail() + " responded to your reservation request";
            }
            message.setText(desc);
            date.setText(notification.getCreatedAt().get(2) + "."+ notification.getCreatedAt().get(1)+"."+notification.getCreatedAt().get(0)+".");
        }

        return convertView;
    }


}
