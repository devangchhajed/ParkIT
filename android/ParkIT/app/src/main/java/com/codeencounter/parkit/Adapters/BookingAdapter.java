package com.codeencounter.parkit.Adapters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codeencounter.parkit.R;

import java.util.List;

import static com.codeencounter.parkit.R.layout.parkinglotsuggestion_recycler_item;

public class BookingAdapter extends ArrayAdapter<BookingHistoryRecords>  {

    private List<BookingHistoryRecords> listItems;
    private Context context;

    public BookingAdapter(List<BookingHistoryRecords> data, Context context) {
        super(context, parkinglotsuggestion_recycler_item,data);

        this.listItems = data;
        this.context=context;

    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        BookingHistoryRecords user = listItems.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(parkinglotsuggestion_recycler_item, parent, false);
        }


        TextView name = convertView.findViewById(R.id.parkinglotsuggestion_recycler_item_parking_name);
        name.setText(user.getParkinglotid());


        TextView address = convertView.findViewById(R.id.parkinglotsuggestion_recycler_item_address);
        address.setText(user.getCheckin());

        TextView cost = convertView.findViewById(R.id.parkinglotsuggestion_recycler_item_cost);
        cost.setText(user.getCheckout());

        TextView rating = convertView.findViewById(R.id.parkinglotsuggestion_recycler_item_time);
        rating.setText("Rs. "+user.getCost());





        return convertView;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
