package com.example.weather.ui.activity;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weather.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    public Integer REQUEST_EXIT = 9;
    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    Button signUpButton;
    Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();


        signUpButton = findViewById(R.id.welcomeSignUpButton);
        signInButton = findViewById(R.id.welcomeSignInButton);

        signInButton.setVisibility(INVISIBLE);
        signUpButton.setVisibility(INVISIBLE);

        if (mAuth.getCurrentUser() != null) {
            mAuth.getCurrentUser().reload().addOnSuccessListener(aVoid -> {

                currentUser = mAuth.getCurrentUser();


                if (currentUser != null && currentUser.isEmailVerified()) {


                    System.out.println("Email Verified : " + currentUser.isEmailVerified());

                    Intent MainActivity = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(MainActivity);
                    MainActivity.this.finish();


                }
            });

        } else {

            signInButton.setVisibility(VISIBLE);
            signUpButton.setVisibility(VISIBLE);

            System.out.println("user not available");

        }

        signUpButton.setOnClickListener(view -> {


            Intent signUpIntent = new Intent(MainActivity.this, SignUp.class);

            startActivity(signUpIntent);


        });


        signInButton.setOnClickListener(view -> {


            Intent signInIntent = new Intent(MainActivity.this, SignIn.class);

            startActivityForResult(signInIntent, REQUEST_EXIT);


        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EXIT) {
            if (resultCode == RESULT_OK) {
                this.finish();

            }
        }
    }



}