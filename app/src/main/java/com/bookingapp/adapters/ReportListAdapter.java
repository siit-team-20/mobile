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

import com.bookingapp.R;
import com.bookingapp.model.AccommodationRequest;
import com.bookingapp.model.AccommodationRequestType;
import com.bookingapp.model.Report;
import com.bookingapp.model.Reservation;
import com.bookingapp.model.User;
import com.bookingapp.model.UserType;
import com.bookingapp.service.ServiceUtils;
import com.bookingapp.service.UserInfo;

import org.json.JSONException;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportListAdapter extends ArrayAdapter<Report> {
    private ArrayList<Report> aReports;
    private Activity activity;
    private FragmentManager fragmentManager;

    public ReportListAdapter(Activity context, FragmentManager fragmentManager, ArrayList<Report> reports) {
        super(context, R.layout.report_card, reports);
        aReports = reports;
        activity = context;
        fragmentManager = fragmentManager;
    }
    @Override
    public int getCount() {
        return aReports.size();
    }
    @Nullable
    @Override
    public Report getItem(int position){return aReports.get(position);}
    @Override
    public long getItemId(int position){return  position;}
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        Report report = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.report_card,
                    parent, false);
        }

        TextView reportText = convertView.findViewById(R.id.reportText);
        Button blockButton = convertView.findViewById(R.id.blockButton);
        Button dismissButton = convertView.findViewById(R.id.dismissButton);

        if(report != null) {
            reportText.setText(report.getReporterEmail() + " has reported " + report.getReportedEmail() + "!");
        }
        blockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<User> userCall = ServiceUtils.userService.getUser(report.getReportedEmail());
                userCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            System.out.println(response.body());
                            User user = new User();
                            user.setEmail(response.body().getEmail());
                            user.setPassword("");
                            user.setName(response.body().getName());
                            user.setSurname(response.body().getSurname());
                            user.setAddress(response.body().getAddress());
                            user.setPhone(response.body().getPhone());
                            user.setType(response.body().getType());
                            user.setIsBlocked(true);
                            Call<User> updateUserCall = ServiceUtils.userService.update(user.getEmail(), user);
                            updateUserCall.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if (response.isSuccessful()) {
                                        System.out.println(response.body());
                                        Call<Report> reportCall = ServiceUtils.reportService.delete(report.getId());
                                        reportCall.enqueue(new Callback<Report>() {
                                            @Override
                                            public void onResponse(@NonNull Call<Report> reportCall1, @NonNull Response<Report> response) {
                                                if (response.isSuccessful()) {
                                                    Call<ArrayList<Report>> call = null;
                                                    try {
                                                        if (UserInfo.getType().equals(UserType.Admin))
                                                            call = ServiceUtils.reportService.getAll();
                                                    } catch (JSONException e) {
                                                        throw new RuntimeException(e);
                                                    }
                                                    call.enqueue(new Callback<ArrayList<Report>>() {
                                                        @Override
                                                        public void onResponse(Call<ArrayList<Report>> call, Response<ArrayList<Report>> response) {
                                                            if (response.code() == 200) {
                                                                Log.d("REZ","Message received");
                                                                System.out.println(response.body());
                                                                aReports = response.body();
                                                                notifyDataSetChanged();
                                                            }
                                                            else {
                                                                Log.d("REZ","Message received: "+response.code());
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<ArrayList<Report>> call, Throwable t) {
                                                            Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                                                        }
                                                    });
                                                } else {
                                                    Log.d("REZ","Message received: "+response.code());
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<Report> call, @NonNull Throwable t) {
                                                Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                                            }
                                        });
                                    }
                                    else {
                                        Log.d("REZ","Message received: "+response.code());
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                                }
                            });
                        }
                        else {
                            Log.d("REZ","Message received: "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                    }
                });
            }
        });

        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Report> reportCall = ServiceUtils.reportService.delete(report.getId());
                reportCall.enqueue(new Callback<Report>() {
                    @Override
                    public void onResponse(@NonNull Call<Report> reportCall1, @NonNull Response<Report> response) {
                        if (response.isSuccessful()) {
                            Call<ArrayList<Report>> call = null;
                            try {
                                if (UserInfo.getType().equals(UserType.Admin))
                                    call = ServiceUtils.reportService.getAll();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            call.enqueue(new Callback<ArrayList<Report>>() {
                                @Override
                                public void onResponse(Call<ArrayList<Report>> call, Response<ArrayList<Report>> response) {
                                    if (response.code() == 200) {
                                        Log.d("REZ","Message received");
                                        System.out.println(response.body());
                                        aReports = response.body();
                                        notifyDataSetChanged();
                                    }
                                    else {
                                        Log.d("REZ","Message received: "+response.code());
                                    }
                                }

                                @Override
                                public void onFailure(Call<ArrayList<Report>> call, Throwable t) {
                                    Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                                }
                            });
                        } else {
                            Log.d("REZ","Message received: "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Report> call, @NonNull Throwable t) {
                        Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                    }
                });
            }
        });

        return convertView;
    }
}
