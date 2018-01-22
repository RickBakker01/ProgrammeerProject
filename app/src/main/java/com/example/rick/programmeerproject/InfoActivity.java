package com.example.rick.programmeerproject;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
public class InfoActivity extends AppCompatActivity {
    TextView name;
    TextView status;
    TextView address;
    String lon;
    String lat;
    String bAddress;
    String bName;
    String bStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        name = findViewById(R.id.name);
        status = findViewById(R.id.status);
        address = findViewById(R.id.address);
        Intent intent = getIntent();

        bName = intent.getStringExtra("name");
        lon = intent.getStringExtra("lon");
        lat = intent.getStringExtra("lat");
        bStatus = intent.getStringExtra("status");

        try {
            getInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
        name.setText(bName);
        status.setText(bStatus);
        address.setText(bAddress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(InfoActivity.this, MyAccActivity.class));
        return true;
    }

    public void getInfo() throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        addresses = geocoder.getFromLocation(Double.valueOf(lat), Double.valueOf(lon), 1); //
        // Here 1 represent max
        // location result to returned, by documents it recommended 1 to 5
        bAddress = addresses.get(0).getAddressLine(0);
    }
}
