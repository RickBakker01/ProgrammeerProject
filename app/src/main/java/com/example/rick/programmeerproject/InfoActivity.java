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
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;
public class InfoActivity extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener {
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public Context context;
    Button save;
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
    String comment;
    RatingBar ratingBar;
    Integer visits;
    float numStars;
    CheckBox checkBox;
    EditText brewComment;
    ImageView imageView;
    Integer uRating;
    Integer uVisit;
    String uComment;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();
    private DatabaseReference ref = database.getReference(uid);
    //    private DatabaseReference ref2 = ref.child(sName);
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

        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(this);

        save = findViewById(R.id.save);
        save.setOnClickListener(new InfoActivity.myListener());

        checkBox = findViewById(R.id.checkBox);

        brewComment = findViewById(R.id.brewComment);

        Intent intent = getIntent();
        sName = intent.getStringExtra("name");
        sId = intent.getStringExtra("id");
        getInfo();
        getImage();
        if (user != null) {
                        collect();
        }
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

    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromTouch) {
        numStars = ratingBar.getRating();
        if (numStars > 0) {
            checkBox.setChecked(true);
        }
    }

    public void ratingToDB() {
        User user = new User((int) numStars, visits, comment);
        Log.d("visit", String.valueOf(visits));
        ref.child(sName).setValue(user);
    }

    //    public void setVisited() {
    //        if (numStars != 0.0 || !Objects.equals(comment, "")) {
    //            checkBox.setChecked(true);
    //        }
    //    }

    public void collect() {
        // Attach a listener to read the data
        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DataSnapshot users = dataSnapshot.child(sName);
                User user = users.getValue(User.class);
                if (dataSnapshot.hasChild(sName)) {
                    assert user != null;
                    uRating = user.getRating();

                    uVisit = user.getVisit();

                    uComment = user.getComment();

                    ratingBar.setRating(uRating);
                    if (uVisit == 1) {
                        checkBox.setChecked(true);
                    }
                    if (!Objects.equals(uComment, "")) {
                        brewComment.setText(uComment);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        };
        ref.addValueEventListener(postListener);
    }

    private class myListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.save:
                    comment = brewComment.getText().toString();
                    if (checkBox.isChecked()) {
                        visits = 1;
                    } else {
                        visits = 0;
                    }
                    ratingToDB();
                    break;
            }
        }
    }
}

