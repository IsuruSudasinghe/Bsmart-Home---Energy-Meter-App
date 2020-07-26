package com.example.smartsocket.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.smartsocket.Adapters.DeviceStatusAdapter;
import com.example.smartsocket.Adapters.ProgressBarAdapter;
import com.example.smartsocket.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab2Fragment extends Fragment {

    private ArrayList<String> deviceList = new ArrayList<>();
    private ArrayList<String> statusList = new ArrayList<>();

    public Tab2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_tab2, container, false);

        ListView listView = view.findViewById(R.id.deviceListView);
        final DeviceStatusAdapter deviceBarAdapter = new DeviceStatusAdapter(getContext(), deviceList,  statusList );
        listView.setAdapter(deviceBarAdapter);

        DatabaseReference referenceDeviceListViewBar = FirebaseDatabase.getInstance().getReference().child("DeviceList");
        DatabaseReference referenceStatusListViewBar = FirebaseDatabase.getInstance().getReference().child("RealTimeStatus");

        referenceDeviceListViewBar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int key = Integer.parseInt(snapshot.getKey().toString().substring(6)) -1 ;
                    try{
                        deviceList.get(key);
                        deviceList.set(key,snapshot.getValue().toString());
                    }catch(IndexOutOfBoundsException e){
                        deviceList.add(snapshot.getValue().toString());
                    }

                    Log.e("Tab1Fragment", "onDataChange:  Device List" + snapshot.getValue().toString());
                    deviceBarAdapter.notifyDataSetChanged();
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
                    deviceBarAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return  view;
    }
}
