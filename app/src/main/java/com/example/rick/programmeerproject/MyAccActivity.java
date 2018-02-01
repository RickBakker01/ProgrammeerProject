package com.example.rick.programmeerproject;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
/**
 * This activity shows the users email and the breweries that he or she visited
 */
public class MyAccActivity extends AppCompatActivity {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<String> brewList = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    ListView lv;
    String uid = user.getUid();
    String selectedBrewery;
    String uID;
    TextView info;
    DatabaseReference ref = database.getReference(uid);

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
        Toast.makeText(this, R.string.signed_out, Toast.LENGTH_SHORT).show();
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
            info.setText(String.format("%s %s%s", getString(R.string.hello), email, getString(R
                    .string.dot)));
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
                    newActivity();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        ref.addListenerForSingleValueEvent(postListener);
    }

    public void newActivity(){
        Intent intent = new Intent(MyAccActivity.this, InfoActivity.class);
        intent.putExtra("name", selectedBrewery);
        intent.putExtra("id", uID);
        startActivity(intent);
        finish();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(MyAccActivity.this, R.style
                    .AlertDialogTheme);
            builder.setMessage(R.string.message).setCancelable(true);
            builder.setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {

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
