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
import com.google.firebase.auth.FirebaseUser;
public class LogInActivity extends AppCompatActivity {
    //Buttons and EditTexts are being called.
    Button gotoregister;
    Button login;
    EditText mEmail;
    EditText mPassword;
    //Standard Firebase code.
    private FirebaseAuth mAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        login = findViewById(R.id.login);
        login.setOnClickListener(new myListener());

        gotoregister = findViewById(R.id.gotoregister);
        gotoregister.setOnClickListener(new myListener());

        mEmail = findViewById(R.id.user_email);
        mPassword = findViewById(R.id.user_password);

        mAuth = FirebaseAuth.getInstance();
        checkUser();
    }

    public void checkUser() {
        if (user != null) {
            startActivity(new Intent(this, MyAccActivity.class));
        }
    }

    public void signIn(String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new
                OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.sign_in_failed, Toast
                            .LENGTH_SHORT).show();
                    mPassword.getText().clear();
                } else {
                    //If the bundle is empty, user signed in via home.
                    Toast.makeText(getApplicationContext(), R.string.sign_in_successful, Toast
                            .LENGTH_SHORT).show();

                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void register() {
            finish();
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }

    public void login() {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        //If email or password is empty, show a warning
        if (email.matches("") || password.matches("")) {
            Toast.makeText(getApplicationContext(), R.string.empty, Toast.LENGTH_SHORT).show();
        } else {
            //When everything is fine, sign in.
            signIn(email, password);
        }
    }

    private class myListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.gotoregister:
                    register();
                    break;
                case R.id.login:
                    login();
                    break;
            }
        }
    }
}

