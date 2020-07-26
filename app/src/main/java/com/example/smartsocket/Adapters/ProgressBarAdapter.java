package com.example.smartsocket.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartsocket.R;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;


public class ProgressBarAdapter extends ArrayAdapter<String>  {
    Context context;
    private ArrayList<String> deviceList;
    private ArrayList<String> consumptionList ;
    private ArrayList<String> wattList ;
    private ArrayList<String> statusList ;

    public ProgressBarAdapter(Context c, ArrayList<String> deviceList, ArrayList<String> consumptionList, ArrayList<String> wattList, ArrayList<String> statusList){
        super(c, R.layout.progresbar_list, R.id.wattReading, wattList);
        this.context = c;
        this.deviceList = deviceList;
        this.consumptionList = consumptionList;
        this.wattList = wattList;
        this.statusList = statusList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        Integer [] deviceIconList = {R.drawable.ic_device_laptop,R.drawable.ic_device_iron,R.drawable.ic_device_fan,R.drawable.ic_device_television, R.drawable.ic_device_blender };
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View progresbar_List = layoutInflater.inflate(R.layout.progresbar_list, parent, false);
        TextView wattReading = progresbar_List.findViewById(R.id.wattReading);
        TextView unitReading = progresbar_List.findViewById(R.id.progressbarVal);
        TextView deviceName = progresbar_List.findViewById(R.id.deviceName);
        ImageView devStatus = progresbar_List.findViewById(R.id.imageView);
        ImageView devIcon = progresbar_List.findViewById(R.id.deviceIcon);
        CircularProgressBar progressbar = progresbar_List.findViewById(R.id.circularTotalProgressBar);

        int status;
        float unitVal, wattVal;

        Log.e("ProgressBarAdapter", "getView: the position of the List ******************: " + position );
        Log.e("ProgressBarAdapter", "getView: Size of the status List ******************: " + statusList.size() );

        unitVal = Float.parseFloat(consumptionList.get(position));
        wattVal = Float.parseFloat(wattList.get(position));


        wattReading.setText(String.valueOf(wattVal));
        unitReading.setText(String.valueOf(unitVal));


        devIcon.setImageResource(deviceIconList[position]);


        try{
            status = Integer.parseInt( statusList.get(position));
            if(status == 1){
                devStatus.setImageResource(R.drawable.ic_online);
            }else{
                devStatus.setImageResource(R.drawable.ic_offline);
            }
        }catch (Exception e){

        }

        try {
            deviceName.setText(deviceList.get(position));
        }catch (Exception e){

        }


        progressbar.setProgressBarColor(0xFF365DEB);
        progressbar.setBackgroundProgressBarColor(0xFFAEB4B8);
        progressbar.setProgressBarWidth(10f); // in DP
        progressbar.setBackgroundProgressBarWidth(10f); // in DP
        progressbar.setRoundBorder(true);
        progressbar.setStartAngle(180f);
        progressbar.setProgressDirection(CircularProgressBar.ProgressDirection.TO_RIGHT);
        progressbar.setProgress(Float.parseFloat(consumptionList.get(position)));
        return progresbar_List;
    }
}
