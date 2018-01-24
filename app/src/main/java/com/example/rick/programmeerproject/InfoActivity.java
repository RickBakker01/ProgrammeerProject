package com.example.rick.programmeerproject;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
public class InfoActivity extends AppCompatActivity {
    public Context context;
    TextView picture;
    TextView caption;
    TextView name;
    TextView status;
    TextView street;
    TextView city;
    TextView phone;

    String sId;
    String sCaption;
    String sName;
    String sStatus;
    String sStreet;
    String sCity;
    String sPhone;
    String sImageUrl;

    ImageView imageView;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            sStatus = intent.getStringExtra("status");
            sStreet = intent.getStringExtra("street");
            sCity = intent.getStringExtra("city");
            sPhone = intent.getStringExtra("phone");
            setInfo();
        }
    };
    private BroadcastReceiver mReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            sImageUrl = intent.getStringExtra("imageurl");
            sCaption = intent.getStringExtra("caption");
            if (!Objects.equals(sImageUrl, "null")) {
                setImage();
            } else {
                picture.setText("Sorry, no picture found");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter
                ("info"));

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver2, new IntentFilter
                ("image"));
        picture = findViewById(R.id.picture);
        caption = findViewById(R.id.caption);
        name = findViewById(R.id.name);
        status = findViewById(R.id.status);
        street = findViewById(R.id.street);
        city = findViewById(R.id.city);
        phone = findViewById(R.id.phone);
        imageView = findViewById(R.id.imageView);

        Intent intent = getIntent();
        sName = intent.getStringExtra("name");
        sId = intent.getStringExtra("id");
        getInfo();
        getImage();
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

    public void getInfo() {
        InfoAsyncTask asyncTask = new InfoAsyncTask(this);
        asyncTask.execute(sId);
    }

    public void getImage() {
        ImageAsyncTask ImageAsyncTask = new ImageAsyncTask(this);
        ImageAsyncTask.execute(sId);
    }

    public void setInfo() {
        name.setText(sName);
        status.setText(sStatus);
        street.setText(sStreet);
        city.setText(sCity);
        phone.setText(sPhone);
    }

    public void setImage() {
        Picasso.with(this).load(sImageUrl).resize(750, 750).into(imageView);
        caption.setText(sCaption);
    }
}

