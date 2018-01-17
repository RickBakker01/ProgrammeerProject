package com.example.rick.programmeerproject;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LogInActivity extends AppCompatActivity {

    String locality = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        findViewById(R.id.button4).setOnClickListener(new myListener());
    }

    public class myListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button4:
                    //startActivity(new Intent(LogInActivity.this, RegisterActivity.class));
                    break;
            }
        }
    }
}

