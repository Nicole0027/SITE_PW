package com.example.car;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText emailLoginEditField;
    private EditText passwordLoginEditField;
    private TextView linkToSignupPage;
    private Button loginButton;
    private LinearLayout loginPage;
    private FirebaseAuth loginAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        linkToSignupPage = (TextView) findViewById(R.id.loginLink2);
        loginPage = (LinearLayout) findViewById(R.id.loginPage);
        emailLoginEditField = (EditText) findViewById(R.id.loginEmailEditText);
        passwordLoginEditField = (EditText) findViewById(R.id.loginPasswordEditText);
        loginButton = (Button) findViewById(R.id.loginButton);

        loginAuth = FirebaseAuth.getInstance();

        //if we click on the "SIGNUP" word it will start the Signup page
        linkToSignupPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToSignupPage = new Intent(v.getContext(), Signup.class);
                startActivity(intentToSignupPage);
            }
        });

        loginButton.setOnClickListener(view -> {
            loginUser();
        });


        //if we click anywhere on the login page and the keyboard is on the screen, it will close
        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(Login.this);
            }
        });

    }

    private void loginUser() {
        String email = emailLoginEditField.getText().toString();
        String password = passwordLoginEditField.getText().toString();

        if (email.isEmpty() || password.isEmpty() || (email.isEmpty() && password.isEmpty())) {
            Toast.makeText(Login.this, "Enter your credentials :(", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loginAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(Login.this, CarRemote.class));
                        Toast.makeText(Login.this, "You Logged In", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(Login.this, "Couldn't login :(", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }





    //region function that helps us to hide keyboard off the screen
        public static void hideSoftKeyboard(Activity activity)
        {

            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if(inputMethodManager.isAcceptingText()){
                inputMethodManager.hideSoftInputFromWindow(
                        activity.getCurrentFocus().getWindowToken(),
                        0
                );
            }
        }
    //endregion
}