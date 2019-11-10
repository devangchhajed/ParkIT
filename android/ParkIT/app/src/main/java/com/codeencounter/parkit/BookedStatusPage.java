package com.codeencounter.parkit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.codeencounter.parkit.Adapters.ParkingSuggestionList;
import com.codeencounter.parkit.Config.AppConfig;

public class BookedStatusPage extends AppCompatActivity {

    TextView getdirections,check_out,bookingid;
    ParkingSuggestionList pl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_status_page);

        getdirections = findViewById(R.id.direction);
        bookingid = findViewById(R.id.activity_booked_status_page_booking_id);
    check_out = findViewById(R.id.checkout);


        Bundle extras = getIntent().getExtras();
        int pos  = extras.getInt("pos");
        String bookid  = extras.getString("bookid");
        pl = AppConfig.savedParkingList.get(pos);
        bookingid.setText(bookid);

        getdirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+pl.getLat()+","+pl.getLng());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookedStatusPage.this,HomePage.class);
                startActivity(intent);
                finish();
            }
        });




    }
}
