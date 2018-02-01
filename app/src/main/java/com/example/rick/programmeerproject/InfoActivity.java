package com.example.rick.programmeerproject;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;
/**
 * This activity displays the info about the selected brewery
 */
public class InfoActivity extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener {
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public Context context;
    private float numStars;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Button save;
    private CheckBox checkBox;
    private DatabaseReference ref;
    private EditText brewComment;
    private ImageView brewPic;
    private Integer visits;
    private Integer uRating;
    private Integer uVisit;
    private RatingBar ratingBar;
    private String sCity;
    private String sPhone;
    private String sImageUrl;
    private String comment;
    private String sId;
    private String sCaption;
    private String sName;
    private String sStatus;
    private String sStreet;
    private String uComment;
    private TextView picture;
    private TextView caption;
    private final BroadcastReceiver mReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            sImageUrl = intent.getStringExtra("imageurl");
            sCaption = intent.getStringExtra("caption");
            setImage();
        }
    };
    private TextView gotologin;
    private TextView name;
    private TextView status;
    private TextView street;
    private TextView city;
    private TextView phone;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            sStatus = intent.getStringExtra("status");
            sStreet = intent.getStringExtra("street");
            sCity = intent.getStringExtra("city");
            sPhone = intent.getStringExtra("phone");
            setInfo();
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

        initialize();

        ratingBar.setOnRatingBarChangeListener(this);
        save.setOnClickListener(new MyListener());
        gotologin.setOnClickListener(new MyListener());

        Intent intent = getIntent();
        sName = intent.getStringExtra("name");
        sId = intent.getStringExtra("id");

        getInfo();
        getImage();
        user();
    }

    private void user() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (user != null) {
            String uid = user.getUid();
            user = FirebaseAuth.getInstance().getCurrentUser();
            ref = database.getReference(uid);
            gotologin.setVisibility(View.GONE);
            collect();
        } else {
            save.setVisibility(View.GONE);
        }
    }

    private void initialize() {
        picture = findViewById(R.id.picture);
        caption = findViewById(R.id.caption);
        name = findViewById(R.id.name);
        status = findViewById(R.id.status);
        street = findViewById(R.id.street);
        city = findViewById(R.id.city);
        phone = findViewById(R.id.phone);
        brewPic = findViewById(R.id.brewPic);
        ratingBar = findViewById(R.id.ratingBar);
        save = findViewById(R.id.save);
        checkBox = findViewById(R.id.checkBox);
        brewComment = findViewById(R.id.brewComment);
        gotologin = findViewById(R.id.gotologin);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        menu.findItem(R.id.action_sign_out).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (Objects.equals(String.valueOf(item), "Account")) {
            userRedirect();
        } else if (Objects.equals(String.valueOf(item), "Search")) {
            startActivity(new Intent(InfoActivity.this, SearchActivity.class));
        }
        return true;
    }

    public void userRedirect() {
        if (user == null) {
            Intent intent = new Intent(InfoActivity.this, LogInActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("name", sName);
            bundle.putString("uId", sId);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            startActivity(new Intent(this, MyAccActivity.class));
        }
    }

    private void getInfo() {
        InfoAsyncTask asyncTask = new InfoAsyncTask(this);
        asyncTask.execute(sId);
    }

    private void getImage() {
        ImageAsyncTask ImageAsyncTask = new ImageAsyncTask(this);
        ImageAsyncTask.execute(sId);
    }

    private void setInfo() {
        name.setText(sName);
        status.setText(sStatus);
        street.setText(sStreet);
        city.setText(sCity);
        phone.setText(sPhone);
    }

    private void setImage() {
        if (!Objects.equals(sImageUrl, "null")) {
            Picasso.with(this).load(sImageUrl).resize(750, 750).into(brewPic);
            picture.setText("");
        } else {
            Picasso.with(this).load(R.drawable.no_image).resize(750, 750).into(brewPic);
            picture.setText(R.string.freepik);
        }
        if (!Objects.equals(sCaption, "null")) {
            caption.setText(sCaption);
        }
    }

    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromTouch) {
        numStars = ratingBar.getRating();
        if (numStars > 0) {
            checkBox.setChecked(true);
        }
    }

    private void ratingToDB() {
        if (visits == 0 && numStars == 0 && Objects.equals(comment, "")) {
            Toast.makeText(this, R.string.rate, Toast.LENGTH_SHORT).show();
        } else {
            User user = new User(sId, (int) numStars, visits, comment);
            ref.child(sName).setValue(user);
            Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
        }
    }

    private void collect() {
        // Attach a listener to read the data
        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DataSnapshot users = dataSnapshot.child(sName);
                User user = users.getValue(User.class);
                if (dataSnapshot.hasChild(sName)) {
                    assert user != null;
                    sId = user.getID();
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

    private class MyListener implements View.OnClickListener {
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
                case R.id.gotologin:
                    userRedirect();
            }
        }
    }
}

