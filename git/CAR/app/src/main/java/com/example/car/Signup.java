package com.example.car;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class Signup extends AppCompatActivity {

    private EditText emailSignupEditField;
    private EditText passwordSignupEditField;
    private EditText confirmPasswordEditField;
    private Button signupButton;
    private LinearLayout signupPage;
    private TextView linkToLoginPage;
    private FirebaseAuth signupAuth;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupPage = (LinearLayout) findViewById(R.id.signupPage);
        linkToLoginPage = (TextView) findViewById(R.id.signupLink2);
        emailSignupEditField = (EditText) findViewById(R.id.signupEmailEditText);
        passwordSignupEditField = (EditText) findViewById(R.id.signupPasswordEditText);
        confirmPasswordEditField = (EditText) findViewById(R.id.signupConfirmPasswordEditText);

        signupButton = (Button) findViewById(R.id.signupButton);
        signupAuth = FirebaseAuth.getInstance();


        signupButton.setOnClickListener( view ->{
            createUser();
        });

        //if we click on the "LOGIN" word it will start the login page
        linkToLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToLoginPage = new Intent(v.getContext(), Login.class);
                startActivity(intentToLoginPage);
            }
        });
        //if we click anywhere on the login page and the keyboard is on the screen, it will close
        signupPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login.hideSoftKeyboard(Signup.this);
            }
        });
    }

    private void createUser()
    {
        //get data from the editfields
        String email = emailSignupEditField.getText().toString();
        String password = passwordSignupEditField.getText().toString();
        String confirmPassword = confirmPasswordEditField.getText().toString();


        //check if the fields are empty or not
        if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || (email.isEmpty() && password.isEmpty()) || (email.isEmpty() && confirmPassword.isEmpty()) || (password.isEmpty() && confirmPassword.isEmpty()) || (email.isEmpty() && password.isEmpty() && confirmPassword.isEmpty()))
        {
            Toast.makeText(Signup.this, "Complete all fields to continue", Toast.LENGTH_SHORT).show();
        }
        //check the length of data
        else if(email.length() < 6 || password.length() <6 || confirmPassword.length() <6 )
        {
            Toast.makeText(Signup.this, "Credential must be longer than 6 characters", Toast.LENGTH_SHORT).show();
        }
        else if(password.equals(confirmPassword))
        {
            signupAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful())
                    {
                        //account is created and we go to the login page
                        Toast.makeText(Signup.this, "Account created :)", Toast.LENGTH_SHORT).show();
                        Intent goToLoginAfterAccountCreated = new Intent(Signup.this, Login.class);
                        startActivity(goToLoginAfterAccountCreated);
                    }
                    else
                    {
                        Toast.makeText(Signup.this, "Couldn't sign up :(", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(Signup.this, "Password is entered wrong", Toast.LENGTH_SHORT).show();
        }
    }
}