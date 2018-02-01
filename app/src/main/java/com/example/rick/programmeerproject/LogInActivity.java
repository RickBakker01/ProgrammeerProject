package com.example.rick.programmeerproject;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
/**
 * With this activity the user can login or go to the registration activity
 */
public class LogInActivity extends AppCompatActivity {
    private Bundle bundle;
    private EditText mEmail;
    private EditText mPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Button login = findViewById(R.id.login);
        login.setOnClickListener(new MyListener());

        TextView gotoregister = findViewById(R.id.gotoregister);
        gotoregister.setOnClickListener(new MyListener());

        mEmail = findViewById(R.id.user_email);
        mPassword = findViewById(R.id.user_password);

        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        bundle = intent.getExtras();
    }

    private void signIn(String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new
                OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.sign_in_failed, Toast
                            .LENGTH_SHORT).show();
                    mPassword.getText().clear();
                } else {
                    //If the bundle is empty, user signed in via home.
                    Toast.makeText(getApplicationContext(), R.string.sign_in_successful, Toast
                            .LENGTH_SHORT).show();

                    finish();
                    if (bundle == null) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Intent intent2 = new Intent(getApplicationContext(), InfoActivity.class);
                        intent2.putExtra("id", bundle.getString("uId"));
                        intent2.putExtra("name", bundle.getString("name"));
                        startActivity(intent2);
                    }
                }
            }
        });
    }

    private void register() {
        finish();
        if (bundle != null) {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class).putExtras
                    (bundle));
        } else {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        }

    }

    private void login() {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        if (email.matches("") || password.matches("")) {
            Toast.makeText(getApplicationContext(), R.string.empty, Toast.LENGTH_SHORT).show();
        } else {
            signIn(email, password);
        }
    }

    private class MyListener implements View.OnClickListener {
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

