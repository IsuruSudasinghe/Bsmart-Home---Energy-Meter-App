package com.example.smartsocket.Fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.smartsocket.customfeatures.MyMarkerView;
import com.example.smartsocket.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */

public class Tab3Fragment extends Fragment {

    private Spinner spinnerYear;
    private Spinner spinnerMonth;
    private Spinner spinnerDevice;

    private DatabaseReference mDatabase;
    private ListView listview;
    private LineChart mLineChart;
    private BarChart mBarChart;

    private ArrayList<Entry> yValuesLineChart = new ArrayList();
    private ArrayList<BarEntry> yValuesBarChart = new ArrayList();

    private Calendar myCal;
    private int daysInMonth ;
    private int currentMonth;
    private int currentDay;
    private int currentYear;
    private int userPromptYear ;
    private int userPromptMonth ;



    private String [] yearList = new String[10];
    private String [] monthList = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};


    private Date today;
    private Calendar todayCal;


    public Tab3Fragment() {
        // Required empty public constructor
    }


    private void  drawLineGraph( String path){
        String relativePath = "CumulativeReading" + "/" + path;
        DatabaseReference referenceLineC = FirebaseDatabase.getInstance().getReference().child(relativePath);
        referenceLineC.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //list.clear();
                yValuesLineChart.clear();
                //float xVal = 0;
                float[] yValLineC;

                myCal = new GregorianCalendar(userPromptYear, userPromptMonth -1, 1);
                daysInMonth = myCal.getActualMaximum(Calendar.DAY_OF_MONTH);
                Log.e("Tab3Fragment" ,"onDataChange: Current Month "+ currentMonth );
                Log.e("Tab3Fragment" ,"onDataChange: Current date "+ currentDay );
                Log.e("Tab3Fragment" ,"onDataChange: days in month "+ daysInMonth );
                Log.e("Tab3Fragment" ,"user prompt month "+ userPromptMonth );

                if((currentMonth + 1) == userPromptMonth){
                    yValLineC = new float[currentDay];
                }else{
                    yValLineC = new float[daysInMonth];
                }

                Arrays.fill(yValLineC, 0);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String month = Objects.requireNonNull(snapshot.getKey()).trim();
                    String[] values = month.split("-");

                    if (values[1].equals(Integer.toString(userPromptMonth))  && values[2].equals(Integer.toString(userPromptYear))) {
                        //list.add(snapshot.getValue().toString());
                        yValLineC[Integer.parseInt(values[0])-1] = Float.parseFloat(Objects.requireNonNull(snapshot.getValue()).toString());
                        //yValues.add(new Entry(xVal, Float.parseFloat(snapshot.getValue().toString())));
                        //xVal++;
                    }
                }
                //adapter.notifyDataSetChanged();
                for(int counter = 0; counter < yValLineC.length ; counter++  ){
                    yValuesLineChart.add(new Entry((counter +1), yValLineC[counter] ));
                }
                LineDataSet set1 = new LineDataSet(yValuesLineChart, "Daily Usage (Units)");

                set1.setColor(0xE1134AD4);
                set1.setLineWidth(3f);
                set1.setValueTextSize(10f);
                set1.setValueTextColor(Color.BLACK);
                set1.setDrawValues(false);

                set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                set1.setCubicIntensity(0.5f);

                set1.setDrawFilled(true);

                ArrayList<ILineDataSet> dataset = new ArrayList<>();
                dataset.add(set1);

                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_blue);
                set1.setFillDrawable(drawable);

                YAxis yl = mLineChart.getAxisLeft();
                yl.setAxisMinimum(0f);



                LineData data = new LineData(dataset);
                mLineChart.setTouchEnabled(true);

                Description description = new Description();
                description.setText("");
                mLineChart.setDescription(description);

                IMarker marker = new MyMarkerView(getContext(), R.layout.tvcontent);
                mLineChart.setDragEnabled(true);
                mLineChart.setScaleEnabled(true);
                mLineChart.setPinchZoom(true);
                mLineChart.setMarker(marker);
                mLineChart.setData(data);
                mLineChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void drawBarGraph(String path){
        String relativePath = "CumulativeReading" + "/" + path;
        DatabaseReference referenceBarC = FirebaseDatabase.getInstance().getReference().child(relativePath);
        referenceBarC.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                yValuesBarChart.clear();

                float[] yValBarC;
                if(currentYear == userPromptYear) {
                   yValBarC = new float[currentMonth +1];
                }else{
                    yValBarC = new float[12];
                }
                Arrays.fill(yValBarC, 0);

                myCal = new GregorianCalendar(userPromptYear, userPromptMonth -1, 1);
                daysInMonth = myCal.getActualMaximum(Calendar.DAY_OF_MONTH);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String month = Objects.requireNonNull(snapshot.getKey()).trim();
                    String[] values = month.split("-");

                    if(userPromptYear == Integer.parseInt(values[2]) ) {
                        yValBarC[Integer.parseInt(values[1]) - 1] += Float.parseFloat(Objects.requireNonNull(snapshot.getValue()).toString());
                    }
                }

                //adapter.notifyDataSetChanged();
                for(int counter = 0; counter < yValBarC.length ; counter++  ){
                    yValuesBarChart.add(new BarEntry((counter +1), yValBarC[counter] ));
                }

               BarDataSet set1 = new BarDataSet(yValuesBarChart, "Monthly Usage (Units)");
               set1.setColors(0x80134AD4);



                Description description = new Description();
                description.setText("");
                mBarChart.setDescription(description);



                BarData data = new BarData(set1);
                data.setBarWidth(0.9f);


                mBarChart.setData(data);
                mBarChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        int tempYear;
        final View view = inflater.inflate(R.layout.fragment_tab3, container, false);
        //listview = view.findViewById(R.id.listView);

        //final ArrayList<Entry> yValues = new ArrayList();
        mLineChart = view.findViewById(R.id.lineChart);
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(false);

        mBarChart = view.findViewById(R.id.BarChart);
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.setPinchZoom(false);
        mBarChart.setDrawGridBackground(true);

        today = new Date();
        todayCal = Calendar.getInstance();
        todayCal.setTime(today);
        currentYear = todayCal.get(Calendar.YEAR);
        currentMonth = todayCal.get(Calendar.MONTH);
        currentDay = todayCal.get(Calendar.DAY_OF_MONTH);

        //final ClassicSingleton instance = ClassicSingleton.getInstance();
        //final ArrayList<String> list = new ArrayList<>();
        //final ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), R.layout.list_item, list);

        //listview.setAdapter(adapter);
        final List<String> deviceList = new ArrayList<>();
        final List<String> deviceKeyList = new ArrayList<>();
        DatabaseReference referenceDevice = FirebaseDatabase.getInstance().getReference().child("DeviceList");
        referenceDevice.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deviceList.clear();
                deviceKeyList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String device = Objects.requireNonNull(snapshot.getValue().toString()).trim();
                    deviceList.add(device);
                    deviceKeyList.add(snapshot.getKey());
                }

                spinnerDevice = view.findViewById(R.id.spinnerDevice);
                ArrayAdapter<String> adapterDevice = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, deviceList);
                adapterDevice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDevice.setAdapter(adapterDevice);
                spinnerDevice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Log.e("Tab3Fragment", "onItemSelected: the dev key lis val is !!!!!!!!!!!!!!!!!!!! " + deviceKeyList.get(position)  );
                        drawLineGraph(deviceKeyList.get(position));
                        drawBarGraph(deviceKeyList.get(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        for(int yearCount = 0 ; yearCount <10 ;yearCount++){
            tempYear = currentYear - yearCount;
           yearList[yearCount] =  Integer.toString(tempYear);
            Log.e("Tab3Fragment", "onCreateView:  current Year" +  yearList[yearCount] );
        }





        spinnerYear = view.findViewById(R.id.spinnerYear);
        ArrayAdapter<String> adapterYear = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, yearList);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapterYear);
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    userPromptYear = Integer.parseInt(parent.getItemAtPosition(position).toString());
                    drawLineGraph("Device1");
                    drawBarGraph("Device1");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerMonth = view.findViewById(R.id.spinnerMonth);
        ArrayAdapter<String> adapterMonth = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, monthList);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapterMonth);
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    userPromptMonth= position + 1;
                    drawLineGraph("Device1");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //yValues = instance.returnCordinates();
        //Log.e("MenuDisplayActivity", "onCreate: " + instance.isCordinatesEmpty().toString());
        Log.e("Tab3Fragment", "onDataChange: HElooooooooooooo im hereeeeee beforeeeee ");

        this.drawLineGraph("Device1");
        this.drawBarGraph("Device1");

        return view;

    }
}
