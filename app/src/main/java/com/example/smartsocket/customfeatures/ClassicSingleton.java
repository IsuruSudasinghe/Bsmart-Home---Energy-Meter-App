package com.example.smartsocket.customfeatures;

import android.util.Log;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.data.Entry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClassicSingleton {
    private static ClassicSingleton instance = null;
    private ArrayList<Entry> yValues = new ArrayList();

    protected ClassicSingleton() {
        // Exists only to defeat instantiation.
    }
    public static ClassicSingleton getInstance() {
        if(instance == null) {
            instance = new ClassicSingleton();
        }
        return instance;
    }

    public void getCordinates(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("CumulativeReading/Device1");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float xVal = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String month = snapshot.getKey().toString().trim();
                    String[] values = month.split("-");

                    if (values[1].equals("7")) {
                        yValues.add(new Entry(xVal, Float.parseFloat(snapshot.getValue().toString())));
                        xVal++;
                        Log.e("ClassicSingleton", "onDataChange: val" + snapshot.getValue().toString() );
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public ArrayList<Entry> returnCordinates(){
        return yValues;
    }

    public Boolean isCordinatesEmpty(){
        return !yValues.isEmpty();
    }

}