package com.example.cityguide.Common.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.example.cityguide.R;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

public class SignUp3rdClass extends AppCompatActivity {
    Button nextBtn;
    ScrollView scrollView;
    TextInputLayout phoneNumber;
    CountryCodePicker countryCodePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up3rd_class);
        //Hooks
        nextBtn = findViewById(R.id.signup_next_btn);

        scrollView = findViewById(R.id.signup_3rd_screen_scroll_view);
        phoneNumber = findViewById(R.id.signup_phone_number);
        countryCodePicker = findViewById(R.id.country_code_picker);


    }

    public void callNextSignupScreen(View view) {
        // Validate Fields
        if (!validatePhoneNumber()) {
            return;
        } // Validation succeded and now move to the next screen to verify phone number and save data

        // Get all values passed from previous screens using Intent
        String _fullName = getIntent().getStringExtra("name");
        String _username = getIntent().getStringExtra("user");
        String _email = getIntent().getStringExtra("email");
        String _password = getIntent().getStringExtra("password");
        String _gender = getIntent().getStringExtra("gender");
        String _date = getIntent().getStringExtra("date");


        String _getUserEnteredPhoneNumber = phoneNumber.getEditText().getText().toString().trim(); // GEt phone Number
        String _phoneNo = "+" + countryCodePicker.getFullNumber() + _getUserEnteredPhoneNumber;

        Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
        intent.putExtra("name", _fullName);
        intent.putExtra("user", _username);
        intent.putExtra("email", _email);
        intent.putExtra("password", _password);
        intent.putExtra("gender", _gender);
        intent.putExtra("date", _date);
        intent.putExtra("phoneNo", _phoneNo);
        intent.putExtra("whatToDO", "createNewUser"); // This is to identify that which action should OTP perform after verification.



        //Transition
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(nextBtn, "transition_OTP_screen");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp3rdClass.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }


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
}