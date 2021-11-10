package com.example.weather.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.weather.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {


    private static final String TAG = "ForgotPassword";
    public FirebaseAuth mAuth;
    Button resetPasswordButton;
    EditText emailTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);


        emailTextInput = findViewById(R.id.fpEmailTextInput);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        mAuth = FirebaseAuth.getInstance();


        resetPasswordButton.setOnClickListener(view -> mAuth.sendPasswordResetEmail(emailTextInput.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "Email sent.");


                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            ForgotPassword.this);

                    // set title
                    alertDialogBuilder.setTitle("Reset Password");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("A Reset Password Link Is Sent To Your Registered EmailID")
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, id) -> ForgotPassword.this.finish());

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                }));


    }
}
