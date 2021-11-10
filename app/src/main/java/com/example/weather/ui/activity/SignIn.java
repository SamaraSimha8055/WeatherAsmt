package com.example.weather.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.weather.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    private static final String TAG = "SignIn";
    public FirebaseAuth mAuth;
    EditText emailTextInput;
    EditText passwordTextInput;
    Button signInButton;
    Button forgotPasswordButton;
    Button sendVerifyMailAgainButton;
    TextView errorView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        emailTextInput = findViewById(R.id.signInEmailTextInput);
        passwordTextInput = findViewById(R.id.signInPasswordTextInput);
        signInButton = findViewById(R.id.signInButton);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        sendVerifyMailAgainButton = findViewById(R.id.verifyEmailAgainButton);
        errorView = findViewById(R.id.signInErrorView);

        sendVerifyMailAgainButton.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(view -> {

            if (emailTextInput.getText().toString().contentEquals("")) {


                errorView.setText("Email cant be empty");


            } else if (passwordTextInput.getText().toString().contentEquals("")) {

                errorView.setText("Password cant be empty");

            } else {


                mAuth.signInWithEmailAndPassword(emailTextInput.getText().toString(), passwordTextInput.getText().toString())
                        .addOnCompleteListener(SignIn.this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");

                                FirebaseUser user = mAuth.getCurrentUser();

                                if (user != null) {
                                    if (user.isEmailVerified()) {


                                        System.out.println("Email Verified : " + user.isEmailVerified());
                                        Intent HomeActivity = new Intent(SignIn.this, MainActivity1.class);
                                        setResult(RESULT_OK, null);
                                        startActivity(HomeActivity);
                                        SignIn.this.finish();


                                    } else {

                                        sendVerifyMailAgainButton.setVisibility(View.VISIBLE);
                                        errorView.setText("Please Verify your EmailID and SignIn");

                                    }
                                }

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(SignIn.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                if (task.getException() != null) {
                                    errorView.setText(task.getException().getMessage());
                                }

                            }

                        });


            }


        });


        forgotPasswordButton.setOnClickListener(view -> {

            Intent forgotPasswordActivity = new Intent(SignIn.this, ForgotPassword.class);
            startActivity(forgotPasswordActivity);
            SignIn.this.finish();

        });


    }
}

