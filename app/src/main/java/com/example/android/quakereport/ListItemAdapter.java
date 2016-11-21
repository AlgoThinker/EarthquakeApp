package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by tusharsharma on 30/07/16.
 */
public class ListItemAdapter extends ArrayAdapter<ListItemClass> {

    private static final String LOCATION_SEPARATOR = " of ";

    public ListItemAdapter(Context context, ArrayList<ListItemClass> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if(listItemView==null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);

        ListItemClass listItem = getItem(position);

        String formattedMag = formatMagnitude(listItem.getMagnitude());

        TextView magnitude = (TextView) listItemView.findViewById(R.id.mag);
        magnitude.setText(formattedMag);


        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();
        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(listItem.getMagnitude());
        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);


        String placeWithOffset = listItem.getPlace();

        TextView placeOffset = (TextView) listItemView.findViewById(R.id.placeOffset);
        TextView place = (TextView) listItemView.findViewById(R.id.place);


        if(placeWithOffset.contains(LOCATION_SEPARATOR)) {
            String[] placeDetails = placeWithOffset.split(LOCATION_SEPARATOR);
            placeOffset.setText(placeDetails[0].trim() + LOCATION_SEPARATOR);
            place.setText(placeDetails[1].trim());

        }else{
            placeOffset.setText(R.string.NoOffsetString);
            place.setText(placeWithOffset);
        }

        //////

        Date dateObject = new Date(listItem.getTimeInMillisecs());

        TextView date = (TextView)listItemView.findViewById(R.id.date);

        String formattedDate = formatDate(dateObject);

        date.setText(formattedDate);

        TextView time = (TextView)listItemView.findViewById(R.id.time);

        String formattedTime = formatTime(dateObject);

        time.setText(formattedTime);


        return listItemView;
    }

    private String formatDate(Date dateObject){
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd ,yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject){
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        return dateFormat.format(dateObject);
    }



    private String formatMagnitude(double mag){
        DecimalFormat decimalFormatter = new DecimalFormat("0.0");
        return decimalFormatter.format(mag);
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
