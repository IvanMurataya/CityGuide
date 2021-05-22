package com.example.cityguide.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.example.cityguide.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetNewPassword extends AppCompatActivity {
    TextInputLayout  password, confirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        password = findViewById(R.id.new_password);
        confirmPassword = findViewById(R.id.password_confirm);


    }

    public void setNewPasswordBtn(View view) {



        if(!isConnected(this)){
            showCustomDiaolog();
            return;
        }
        if (!validatePassword() | !validateNewPassword()){
            return;
        }
        // Get data From fields
        String _newPassword = password.getEditText().getText().toString().trim();
        String _phoneNumber = getIntent().getStringExtra("phoneNo");
        String _userName = getIntent().getStringExtra("userName");
        Toast.makeText(SetNewPassword.this, _newPassword+"\n"+_phoneNumber+"\n"+_userName, Toast.LENGTH_LONG).show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(_userName).child("password").setValue(_newPassword);
        startActivity(new Intent(getApplicationContext(),ForgetPasswordSuccessMessage.class));
        finish();

    }


    private void showCustomDiaolog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SetNewPassword.this);
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
                        startActivity(new Intent(getApplicationContext(),RetailerStarUpScreen.class));
                        finish();
                    }
                });
        AlertDialog alert = builder.create();

        alert.show();

    }

    private boolean isConnected(SetNewPassword setNewPassword) {
        ConnectivityManager connectivityManager = (ConnectivityManager) setNewPassword.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifiConn != null && wifiConn.isConnected() )|| (mobileConn != null && mobileConn.isConnected())){
            return true;
        }else {
            return false;
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
    private boolean validateNewPassword() {
        //Get text from the edit text and cut if to no spaces in firebase
        String val = confirmPassword.getEditText().getText().toString().trim();
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
            confirmPassword.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkPassword)) {
            confirmPassword.setError("Password not valid");
            return false;
        } else {
            confirmPassword.setError(null);
            confirmPassword.setErrorEnabled(false);
            return true;
        }

    }

}