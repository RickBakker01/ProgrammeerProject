package com.example.rick.programmeerproject;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class MyAccActivity extends AppCompatActivity {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    DatabaseReference ref = database.getReference(uid);
    ArrayList<String> brewList = new ArrayList<>();
    ListView lv;
    String selectedBrewery;
    Integer uID;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_acc);
        findViewById(R.id.sign_out).setOnClickListener(new myListener());
        lv = findViewById(R.id.breweryList);
        lv.setOnItemClickListener(new LVListener());
        lv.setOnItemLongClickListener(new LVLongListener());
        collect();
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
                    uID = Integer.valueOf(user.getID());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        ref.addValueEventListener(postListener);
    }


    public void populate() {
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout
                .simple_list_item_1, brewList);
        Log.d("listview", String.valueOf(brewList));
        lv.setAdapter(arrayAdapter);
    }

    private class LVListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            selectedBrewery=brewList.get(i);
            collectID();
            Intent intent = new Intent(view.getContext(), InfoActivity.class);
            intent.putExtra("name", selectedBrewery);
            intent.putExtra("id", uID);
            startActivity(intent);
        }
    }

    private class LVLongListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MyAccActivity.this);
            builder.setMessage("Are you sure you want to remove this brewery?");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Remove item",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ref.child(brewList.get(i)).removeValue();
                            brewList.clear();
                            arrayAdapter.notifyDataSetChanged();
                            Log.d("Arrayyyss", brewList.toString());
                        }
                    });
            AlertDialog alert1 = builder.create();
            alert1.show();
            return true;
        }
    }


    public class myListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.sign_out:
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MyAccActivity.this, MainActivity.class));
                    break;
            }
        }
    }
}
