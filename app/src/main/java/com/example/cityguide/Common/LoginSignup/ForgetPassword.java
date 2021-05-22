package com.example.cityguide.Common.LoginSignup;

import androidx.annotation.NonNull;
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
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cityguide.R;
import com.example.cityguide.User.AllCategories;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class ForgetPassword extends AppCompatActivity {
    Animation animation;
    Button nextBtn;
    ImageView backImage, mainImage;
    TextView title, description;
    TextInputLayout phoneNumber;
    CountryCodePicker countryCodePicker;
    TextInputLayout username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password);

        // Hooks
        backImage = findViewById(R.id.back_btn);
        mainImage = findViewById(R.id.main_img);
        title = findViewById(R.id.title_forget);
        username = findViewById(R.id.user_forget);
        description = findViewById(R.id.description);
        nextBtn = findViewById(R.id.next_btn);
        phoneNumber = findViewById(R.id.forget_phone_number);
        countryCodePicker = findViewById(R.id.country_code_picker_forget);


        animation = AnimationUtils.loadAnimation(this, R.anim.slide_animation);

        backImage.setAnimation(animation);
        mainImage.setAnimation(animation);
        title.setAnimation(animation);
        username.setAnimation(animation);
        description.setAnimation(animation);
        phoneNumber.setAnimation(animation);
        countryCodePicker.setAnimation(animation);
        nextBtn.setAnimation(animation);
    }

    public void validatePhone(View view) {
        if (!isConnected(this)) {
            showCustomDiaolog();
        }

        if (!validateFields()) {
            return;
        }

        String _username = username.getEditText().getText().toString().trim();
        String _getUserEnteredPhoneNumber = phoneNumber.getEditText().getText().toString().trim(); // GEt phone Number
        String _phoneNo = "+" + countryCodePicker.getFullNumber() + _getUserEnteredPhoneNumber;


        Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("user").equalTo(_username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    username.setError(null);
                    username.setErrorEnabled(false);

                    String systemPassword = snapshot.child(_username).child("phoneNo").getValue(String.class);
                    if (systemPassword.equals(_phoneNo)) {
                        phoneNumber.setError(null);
                        phoneNumber.setErrorEnabled(false);

                        String _fullName_ = snapshot.child(_username).child("name").getValue(String.class);
                        String _email_ = snapshot.child(_username).child("email").getValue(String.class);
                        String _phoneNo_ = snapshot.child(_username).child("phoneNo").getValue(String.class);
                        String _dateOfBirth_ = snapshot.child(_username).child("date").getValue(String.class);

                        Toast.makeText(ForgetPassword.this, _fullName_ + "\n" + _email_ + "\n" + _phoneNo_ + "\n" + _dateOfBirth_, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                        intent.putExtra("phoneNo", _phoneNo);
                        intent.putExtra("user", _username);
                        intent.putExtra("whatToDO", "updateData");
                        startActivity(intent);
                        finish();

                    } else {

                        //progressbar.setVisibility(View.ogne  );
                        Toast.makeText(ForgetPassword.this, "Phone Number does not match", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    //progressbar.setVisibility(View.ogne  );
                    Toast.makeText(ForgetPassword.this, "No such user exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //progressbar.setVisibility(View.ogne  );
                Toast.makeText(ForgetPassword.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }



    private boolean validatePhoneNumber() {
        //Get text from the edit text and cut if to no spaces in firebase
        String val = phoneNumber.getEditText().getText().toString().trim();
        String checkPhone = ("[0-9]{10}");

        if (val.isEmpty()) {
            phoneNumber.setError("Field can not be empty");
            return false;
        } else if (val.length() != 10) {
            phoneNumber.setError("Phone number must be 10 characters!");
            return false;
        } else if (!val.matches(checkPhone)) {
            phoneNumber.setError("Just Numbers Allowed");
            return false;
        } else {
            phoneNumber.setError(null);
            phoneNumber.setErrorEnabled(false);
            return true;
        }

    }
    private void showCustomDiaolog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPassword.this);
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
    private boolean isConnected(ForgetPassword forgetPassword) {
        ConnectivityManager connectivityManager = (ConnectivityManager) forgetPassword.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifiConn != null && wifiConn.isConnected() )|| (mobileConn != null && mobileConn.isConnected())){
            return true;
        }else {
            return false;
        }
    }
    private boolean validateFields() {
        if (validatePhoneNumber() && validateUserName()) {
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


    public void backLogin(View view) {
        ForgetPassword.super.onBackPressed();


    }
}