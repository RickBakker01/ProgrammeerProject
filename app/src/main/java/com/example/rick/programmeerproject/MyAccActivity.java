package com.example.rick.programmeerproject;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class MyAccActivity extends AppCompatActivity {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    DatabaseReference ref = database.getReference(uid);
    ArrayList<String> brewList = new ArrayList<>();
    ListView lv;
    String selectedBrewery;
    String uID;
    TextView info;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_acc);
        lv = findViewById(R.id.breweryList);
        info = findViewById(R.id.info);
        lv.setOnItemClickListener(new LVListener());
        lv.setOnItemLongClickListener(new LVLongListener());
        collect();
        getCurrentUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_account).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MyAccActivity.this, MainActivity.class));
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(MyAccActivity.this, MainActivity.class));
    }

    public void getCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            info.setText("Hello, " + email + ".");
        }
    }

    public void collect() {
        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String name = postSnapshot.getKey();
                    brewList.add(name);
                    populate();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        ref.addValueEventListener(postListener);
    }

    public void collectID() {
        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                DataSnapshot users = snapshot.child(selectedBrewery);
                User user = users.getValue(User.class);
                if (user != null) {
                    uID = user.getID();
                    Intent intent = new Intent(MyAccActivity.this, InfoActivity.class);
                    intent.putExtra("name", selectedBrewery);
                    intent.putExtra("id", uID);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        ref.addListenerForSingleValueEvent(postListener);
    }

    public void populate() {
        arrayAdapter = new ArrayAdapter<>(this, R.layout.custom_list, brewList);
        lv.setAdapter(arrayAdapter);
    }

    private class LVListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            selectedBrewery = brewList.get(i);
            collectID();

        }
    }

    private class LVLongListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MyAccActivity.this);
            builder.setMessage("Are you sure you want to remove this brewery?");
            builder.setCancelable(true);

            builder.setPositiveButton("Remove item", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    ref.child(brewList.get(i)).removeValue();
                    brewList.clear();
                    arrayAdapter.notifyDataSetChanged();
                }
            });
            AlertDialog alert1 = builder.create();
            alert1.show();
            return true;
        }
    }
}
