package com.example.rick.programmeerproject;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
public class RegisterActivity extends AppCompatActivity {
    Button register;
    EditText mEmail;
    EditText mPassword;
    EditText mPasswordConfirm;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.register);
        register.setOnClickListener(new myListener());

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mPasswordConfirm = findViewById(R.id.password_confirm);

        mAuth = FirebaseAuth.getInstance();
    }

    public void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new
                OnCompleteListener<AuthResult>() {

            @SuppressWarnings({"ThrowableResultOfMethodCallIgnored", "ConstantConditions"})
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    //If Firebase sends a message, the message is being displayed in a toast.
                    // Firebase sends messages when something goes wrong. For instance when an
                    // account is being added when it already exists.
                    if (!task.getException().getMessage().equals("")) {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //                            //When everything is fine, create new intent
                    // and get extras from the intent.
                    //                            Intent intent = getIntent();
                    //                            Bundle bundle = intent.getExtras();
                    //                            if (bundle == null) {
                    //                                //If the bundle is empty, user signed in
                    // via AccountActivity.
                    //                                finish();
                    //                                Toast.makeText(getApplicationContext(), R
                    // .string.registration_successful,
                    //                                        Toast.LENGTH_SHORT).show();
                    //                                startActivity(new Intent
                    // (getApplicationContext(), MainActivity.class));
                    //                            } else {
                    //If the bundle is not empty, user registered from the moodlist
                    // and must be redirected back to the personal moodlist after the user
                    // registered.
                    finish();
                    Toast.makeText(getApplicationContext(), R.string.registration_successful,
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });
    }

    //If the back button is pressed, the application goes back to the AccountActivity.
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), LogInActivity.class));
    }

    //A non-anonymous listener
    class myListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //Get the strings from the email and password EditTexts
            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();
            if (mPassword.length() > 6 && !email.matches("")) {
                //If password is longer than 6 characters and your email is filled in, continue.
                if (Objects.equals(mPassword.getText().toString(), mPasswordConfirm.getText()
                        //If password equals to the confirmed password, create user.
                        .toString())) {
                    createAccount(email, password);
                } else {
                    //Else show warning and clear confirmed password.
                    mPasswordConfirm.getText().clear();
                    Toast.makeText(RegisterActivity.this, R.string.different_passwords, Toast
                            .LENGTH_SHORT).show();
                }
            } else {
                //If the above is fine, check if not empty. Show warning when empty
                if (email.matches("") || password.matches("")) {
                    Toast.makeText(getApplicationContext(), R.string.empty, Toast.LENGTH_SHORT)
                            .show();
                } else {
                    //When password not long enough, show warning.
                    mPasswordConfirm.getText().clear();
                    Toast.makeText(RegisterActivity.this, R.string.to_short, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }
}
