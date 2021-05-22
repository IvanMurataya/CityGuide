package com.example.cityguide.Common.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.cityguide.Common.OnBoarding;
import com.example.cityguide.Databases.SessionManager;
import com.example.cityguide.LocationOwner.RetailerDashboard;
import com.example.cityguide.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Queue;

public class Login extends AppCompatActivity {

    TextInputLayout username, password;
    RelativeLayout progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_retailer_login);
        // Hooks
        username = findViewById(R.id.user_login);
        password = findViewById(R.id.password_login);


    }

    public void forgetPassword(View view) {
        Intent intent = new Intent(getApplicationContext(), ForgetPassword.class);
        startActivity(intent);
    }

    public void letTheUserLoggedIn(View view) {

        if (!isConnected(this)) {
            showCustomDiaolog();
        }


        if (!validateFields()) {
            return;
        }
//progressbar.setVisibility(View.Visible  );

        // Get Data
        String _username_ = username.getEditText().getText().toString().trim();
        String _password = password.getEditText().getText().toString().trim();

        // Database
        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("user").equalTo(_username_);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    username.setError(null);
                    username.setErrorEnabled(false);

                    String systemPassword = snapshot.child(_username_).child("password").getValue(String.class);
                    if (systemPassword.equals(_password)) {
                        password.setError(null);
                        password.setErrorEnabled(false);

                        //Get users data from firebase database
                        String _fullName = snapshot.child(_username_).child("name").getValue(String.class);
                        String _username = snapshot.child(_username_).child("user").getValue(String.class);
                        String _email = snapshot.child(_username_).child("email").getValue(String.class);
                        String _phoneNo = snapshot.child(_username_).child("phoneNo").getValue(String.class);
                        String _password = snapshot.child(_username_).child("password").getValue(String.class);
                        String _dateOfBirth = snapshot.child(_username_).child("date").getValue(String.class);
                        String _gender = snapshot.child(_username_).child("gender").getValue(String.class);

                        // Create a Session
                        SessionManager sessionManager = new SessionManager(Login.this);
                        sessionManager.createLoginSession(_fullName, _username, _email, _phoneNo, _password, _dateOfBirth, _gender);
                        startActivity(new Intent(getApplicationContext(), RetailerDashboard.class));


                        Toast.makeText(Login.this, _fullName + "\n" + _email + "\n" + _phoneNo + "\n" + _dateOfBirth, Toast.LENGTH_LONG).show();
                        finish();

                    } else {

                        //progressbar.setVisibility(View.ogne  );
                        Toast.makeText(Login.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    //progressbar.setVisibility(View.ogne  );
                    Toast.makeText(Login.this, "No such user exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //progressbar.setVisibility(View.ogne  );
                Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void showCustomDiaolog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setMessage("Please connect to the internet to proceed further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), RetailerStarUpScreen.class));
                        finish();
                    }
                });
        AlertDialog alert = builder.create();

        alert.show();

    }

    private boolean isConnected(Login login) {
        ConnectivityManager connectivityManager = (ConnectivityManager) login.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validateFields() {
        if (validatePassword() && validateUserName()) {
            return true;
        } else {
            return false;
        }

    }


    private boolean validateUserName() {
        //Get text from the edit text and cut if to no spaces in firebase
        String val = username.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{4,20}\\z"; // No spaces / limit to 20 characters / from A to z


        if (val.isEmpty()) {
            username.setError("Field can not be empty");
            return false;
        } else if (val.length() > 20) {
            username.setError("User is too large!");
            return false;
        } else if (!val.matches(checkspaces)) {
            username.setError("No white spaces are allowed");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validatePassword() {
        //Get text from the edit text and cut if to no spaces in firebase
        String val = password.getEditText().getText().toString().trim();
        String checkPassword = ("^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$");

        if (val.isEmpty()) {
            password.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkPassword)) {
            password.setError("Password not valid");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }

    }
}