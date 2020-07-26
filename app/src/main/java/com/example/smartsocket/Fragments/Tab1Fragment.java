package com.example.smartsocket.Fragments;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.smartsocket.Adapters.ProgressBarAdapter;
import com.example.smartsocket.R;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab1Fragment extends Fragment {
    private Float fixLimit = 100f;
    private int currentMonth;
    private int currentDay;
    private int currentYear;
    private Date today;
    private Calendar todayCal;
    private float currentTotal;
    private float previousTotal;
    private TextView totalReading;
    private TextView monthlyWarning;
    private TextView units;

    private ListView listview;
    private ArrayList<String> deviceList = new ArrayList<>();
    private ArrayList<String> consumptionList = new ArrayList<>();
    private ArrayList<String> wattList = new ArrayList<>();
    private ArrayList<String> statusList = new ArrayList<>();

    private  CircularProgressBar circularProgressBar;

    public Tab1Fragment() {
        // Required empty public constructor
    }
    @Override
    public void onResume(){
        super.onResume();
        circularProgressBar.invalidate();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        totalReading = view.findViewById(R.id.myTextProgress);
//        monthlyWarning = view.findViewById(R.id.WarningMessage);
        units =  view.findViewById(R.id.myUnitsProgress);


        ListView listView = view.findViewById(R.id.progressBarListView);
        final ProgressBarAdapter progressBarAdapter = new ProgressBarAdapter(getContext(), deviceList, consumptionList, wattList, statusList );
        listView.setAdapter(progressBarAdapter);



        today = new Date();
        todayCal = Calendar.getInstance();
        todayCal.setTime(today);
        currentYear = todayCal.get(Calendar.YEAR);
        currentMonth = todayCal.get(Calendar.MONTH);
        currentDay = todayCal.get(Calendar.DAY_OF_MONTH);
        circularProgressBar = view.findViewById(R.id.circularTotalProgressBar);

        totalReading.setGravity(Gravity.CENTER);
        totalReading.setText("0.0");

        DatabaseReference referenceDeviceListViewBar = FirebaseDatabase.getInstance().getReference().child("DeviceList");
        DatabaseReference referenceStatusListViewBar = FirebaseDatabase.getInstance().getReference().child("RealTimeStatus");
        DatabaseReference referenceReadingListViewBar = FirebaseDatabase.getInstance().getReference().child("RealTimeReading");
        DatabaseReference referenceProgressTotalBar = FirebaseDatabase.getInstance().getReference().child("CumulativeReading/Total");

        referenceDeviceListViewBar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    deviceList.add(snapshot.getValue().toString());
                    Log.e("Tab1Fragment", "onDataChange:  Device List" + snapshot.getValue().toString());
                    progressBarAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        referenceStatusListViewBar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int position = Integer.parseInt(snapshot.getKey().toString().substring(6)) - 1;
                    statusList.add(position, snapshot.getValue().toString());
                    Log.e("Tab1Fragment", "onDataChange:  Status List" + snapshot.getValue().toString());
                    progressBarAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        referenceReadingListViewBar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int position = Integer.parseInt(snapshot.getKey().toString().substring(6)) - 1;

                    try {
                        consumptionList.get(position);
                        consumptionList.set(position, snapshot.child("Units").getValue().toString());
                        wattList.set(position, snapshot.child("Watts").getValue().toString());
                    } catch (IndexOutOfBoundsException e) {
                        consumptionList.add(position, snapshot.child("Units").getValue().toString());
                        wattList.add(position, snapshot.child("Watts").getValue().toString());
                    }


                    Log.e("Tab1Fragment", "onDataChange the position of the List ******************: " + position );
                    Log.e("Tab1Fragment", "onDataChange Size of the List ******************: " + consumptionList.size() );
                    Log.e("Tab1Fragment", "onDataChange:  Units List" + snapshot.child("Units").getValue().toString());
                    Log.e("Tab1Fragment", "onDataChange:  Watts List" + snapshot.child("Watts").getValue().toString());


                    progressBarAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        referenceProgressTotalBar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentTotal = 0;
                try{
                    previousTotal = circularProgressBar.getProgress();
                }catch(Exception e){
                    previousTotal = 0;
                }

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String month = Objects.requireNonNull(snapshot.getKey()).trim();
                    String[] values = month.split("-");

                    if((currentYear== Integer.parseInt(values[2])) && ((currentMonth + 1) == Integer.parseInt(values[1]))  ) {
                        currentTotal += Float.parseFloat(Objects.requireNonNull(snapshot.getValue()).toString());
                        Log.e("Tab1Fragment", "onDataChange:  currentTotal" + currentTotal);
                    }

                }


                ValueAnimator animator = new ValueAnimator();
                animator.setObjectValues(previousTotal, currentTotal);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if(currentTotal > fixLimit) {
                            totalReading.setTextColor(0xFFFF0000);
                            units.setTextColor(0xFFFF0000);
                            totalReading.setText(String.valueOf(animation.getAnimatedValue()));
                        }else {
                            totalReading.setTextColor(0xFF134AD4);
                            units.setTextColor(0xFF134AD4);
                            totalReading.setText(String.valueOf(animation.getAnimatedValue()));
                        }

                    }
                });
                animator.setDuration(1000);
                animator.start();


                totalReading.setText(Objects.requireNonNull(String.format("%.2f", currentTotal)));
                if(currentTotal > fixLimit ){
//                    monthlyWarning.setVisibility(View.VISIBLE);
                    circularProgressBar.setProgressBarColor(0xFFE6A715);
                }else{
//                    monthlyWarning.setVisibility(View.INVISIBLE);
                    circularProgressBar.setProgressBarColor(0xFF134AD4);
                }
                circularProgressBar.invalidate();
                circularProgressBar.setIndeterminateMode(false);
                circularProgressBar.setProgressWithAnimation(currentTotal, (long) 1000); // =1s


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

// Set Progress Max
        circularProgressBar.setProgressMax(fixLimit);

// Set ProgressBar Color
        circularProgressBar.setProgressBarColor(0xFF134AD4);
//// or with gradient
//        circularProgressBar.setProgressBarColorStart(Color.BLUE);
//        circularProgressBar.setProgressBarColorEnd(Color.RED);
//        circularProgressBar.setProgressBarColorDirection(CircularProgressBar.GradientDirection.BOTTOM_TO_END);

// Set background ProgressBar Color
        circularProgressBar.setBackgroundProgressBarColor(0xFFAEB4B8);
//// or with gradient
//        circularProgressBar.setBackgroundProgressBarColorStart(Color.WHITE);
//        circularProgressBar.setBackgroundProgressBarColorEnd(Color.RED);
//        circularProgressBar.setBackgroundProgressBarColorDirection(CircularProgressBar.GradientDirection.TOP_TO_BOTTOM);

// Set Width
        circularProgressBar.setProgressBarWidth(15f); // in DP
        circularProgressBar.setBackgroundProgressBarWidth(15f); // in DP

// Other

        circularProgressBar.setRoundBorder(true);
        circularProgressBar.setStartAngle(180f);
        circularProgressBar.setProgressDirection(CircularProgressBar.ProgressDirection.TO_RIGHT);
        circularProgressBar.setIndeterminateMode(true);

        return view ;
    }
}
