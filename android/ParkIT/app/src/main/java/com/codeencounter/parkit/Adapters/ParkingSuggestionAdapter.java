package com.codeencounter.parkit.Adapters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codeencounter.parkit.R;

import java.util.List;

public class ParkingSuggestionAdapter extends ArrayAdapter<ParkingSuggestionList>  {

    private List<ParkingSuggestionList> listItems;
    private Context context;

    public ParkingSuggestionAdapter(List<ParkingSuggestionList> data, Context context) {
        super(context, R.layout.parkinglotsuggestion_recycler_item, data);
        this.listItems = data;
        this.context=context;
        Log.e("AAdap", listItems.size()+"=");

    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ParkingSuggestionList user = listItems.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.parkinglotsuggestion_recycler_item, parent, false);
        }


        TextView name = convertView.findViewById(R.id.parkinglotsuggestion_recycler_item_parking_name);
        name.setText(user.getName());


        TextView address = convertView.findViewById(R.id.parkinglotsuggestion_recycler_item_address);
        address.setText(user.getAddress());

        TextView cost = convertView.findViewById(R.id.parkinglotsuggestion_recycler_item_cost);
        cost.setText(user.getPrice_per_hour());

        TextView rating = convertView.findViewById(R.id.parkinglotsuggestion_recycler_item_time);
        rating.setText(user.getRatings());



        Log.e("Adapter", user.getName());


        return convertView;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
