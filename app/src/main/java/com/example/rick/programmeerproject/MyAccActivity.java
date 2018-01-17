package com.example.rick.programmeerproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MyAccActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_acc);
        findViewById(R.id.button7).setOnClickListener(new myListener());
    }

    public class myListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button7:
                    startActivity(new Intent(MyAccActivity.this, InfoActivity.class));
                    break;
            }
        }
    }
}
