package com.example.rick.programmeerproject;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class SearchActivity extends AppCompatActivity {
    EditText searchText;
    Button search;
    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchText = findViewById(R.id.searchText);
        search = findViewById(R.id.search);
        search.setOnClickListener(new SearchActivity.myListener());
    }

    private class myListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.search:
                    city = searchText.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("city", city);
                    bundle.putInt("fromSearch", 0);
                    intent.putExtras(bundle);
                    Toast.makeText(getApplicationContext(), R.string.searching, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
            }
        }
    }
}
