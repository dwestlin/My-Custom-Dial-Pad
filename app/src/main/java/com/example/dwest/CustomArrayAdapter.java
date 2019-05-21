package com.example.dwest;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<CallData> {

    private List<CallData> callData;

    public CustomArrayAdapter(Context context, ArrayList<CallData> cd) {
        super(context, 0, cd);
        callData = cd;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_item,parent,false);

        CallData cd = callData.get(position);


        TextView number = (TextView) listItem.findViewById(R.id.text1);
        number.setText(cd.getNumber());

        TextView date = (TextView) listItem.findViewById(R.id.text2);
        date.setText(cd.getDate());

        TextView GPSPos = (TextView) listItem.findViewById(R.id.text3);
        GPSPos.setText(cd.getLatitude()+","+cd.getLongitude());

        return listItem;
    }


}
