package com.example.smartsocket.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smartsocket.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;

public class DeviceStatusAdapter extends ArrayAdapter<String> {
    Context context;
    private ArrayList<String> deviceList;
    private ArrayList<String> statusList ;

    public DeviceStatusAdapter(Context c, ArrayList<String> deviceList,  ArrayList<String> statusList){
        super(c, R.layout.devicestatus_list, R.id.deviceName, deviceList);
        this.context = c;
        this.deviceList = deviceList;
        this.statusList = statusList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent){

        final int status;

        final LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View deviceStatus_List = layoutInflater.inflate(R.layout.devicestatus_list, parent, false);

        final TextView deviceName = deviceStatus_List.findViewById(R.id.deviceName);
        final ImageView devStatus = deviceStatus_List.findViewById(R.id.imageView);

        final Switch power = deviceStatus_List.findViewById(R.id.switchPower);

        final DatabaseReference referenceStatus = FirebaseDatabase.getInstance().getReference().child("RealTimeStatus");

        Integer [] deviceIconList = {R.drawable.ic_device_laptop,R.drawable.ic_device_iron,R.drawable.ic_device_fan,R.drawable.ic_device_television, R.drawable.ic_device_blender };

        ImageView deviceIcon = deviceStatus_List.findViewById(R.id.iconDevice);
        deviceIcon.setImageResource(deviceIconList[position]);

        try{
            status = Integer.parseInt( statusList.get(position));
            if(status == 1){
                devStatus.setImageResource(R.drawable.ic_online);
                power.setChecked(true);
            }else{
                devStatus.setImageResource(R.drawable.ic_offline);
                power.setChecked(false);
            }
        }catch (Exception e){
        }

        referenceStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int key = Integer.parseInt(snapshot.getKey().toString().substring(6)) - 1;
                    int status = Integer.parseInt( snapshot.getValue().toString());

                    if( key == position){
                        if(status == 1){
                            devStatus.setImageResource(R.drawable.ic_online);
                            power.setChecked(true);
                        }else{
                            devStatus.setImageResource(R.drawable.ic_offline);
                            power.setChecked(false);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        power.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String key = "Device" + String.valueOf( position + 1);

                if(isChecked){
                    referenceStatus.child(key).setValue("1");
                    devStatus.setImageResource(R.drawable.ic_online);

                }else {
                    referenceStatus.child(key).setValue("0");
                    devStatus.setImageResource(R.drawable.ic_offline);
                }
            }
        });

        deviceName.setText(deviceList.get(position));

        return deviceStatus_List;
    }
}
