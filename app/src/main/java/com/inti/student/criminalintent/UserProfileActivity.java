package com.inti.student.criminalintent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {

    private TextView mPersonalInfoLblTextView;
    private TextView mChangePasswordLblTextView;
    private RelativeLayout mPersonalInfoContentLayout;
    private RelativeLayout mChangePasswordContentLayout;

    // personal info
    private EditText mNameEditText;
    private EditText mEmailEditText;
    private EditText mAddressEditText;
    private String mName;
    private String mEmail;
    private String mAddress;

    // change password
    private EditText mCurrentPswEditText;
    private EditText mNewPswEditText;
    private EditText mConfirmNewPswEditText;
    private String mCurrentPsw;
    private String mNewPsw;
    private String mConfirmNewPsw;

    private UserDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide default title bar
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        setContentView(R.layout.user_profile);

        datasource = new UserDataSource(this);
        datasource.open();

        mPersonalInfoLblTextView = (TextView) findViewById(R.id.personal_info_lbl);
        mChangePasswordLblTextView = (TextView) findViewById(R.id.change_password_lbl);
        mPersonalInfoContentLayout = (RelativeLayout) findViewById(R.id.personal_info_content_layout);
        mChangePasswordContentLayout = (RelativeLayout) findViewById(R.id.change_password_content_layout);

        mNameEditText = (EditText) findViewById(R.id.user_profile_name);
        mEmailEditText = (EditText) findViewById(R.id.user_profile_email);
        mAddressEditText = (EditText) findViewById(R.id.user_profile_address);

        // get all user information from database
        ArrayList<User> values = datasource.getUserInfo();
        for (User member : values) {
            mNameEditText.setText(member.getName());
            mEmailEditText.setText(member.getEmail());
            mAddressEditText.setText(member.getAddress());
        }
    }

    public void switchToPersonalInfo(View v) {
        mPersonalInfoLblTextView.setTextColor(Color.parseColor("#FD6776"));
        mChangePasswordLblTextView.setTextColor(Color.parseColor("#8c8c8c"));
        mPersonalInfoContentLayout.setVisibility(View.VISIBLE);
        mChangePasswordContentLayout.setVisibility(View.GONE);
    }

    public void switchToChangePassword(View v) {
        mPersonalInfoLblTextView.setTextColor(Color.parseColor("#8c8c8c"));
        mChangePasswordLblTextView.setTextColor(Color.parseColor("#FD6776"));
        mPersonalInfoContentLayout.setVisibility(View.GONE);
        mChangePasswordContentLayout.setVisibility(View.VISIBLE);
    }

    public void updatePersonalInfo(View v) {
        mNameEditText = (EditText) findViewById(R.id.user_profile_name);
        mName = mNameEditText.getText().toString();

        mEmailEditText = (EditText) findViewById(R.id.user_profile_email);
        mEmail = mEmailEditText.getText().toString();

        mAddressEditText = (EditText) findViewById(R.id.user_profile_address);
        mAddress = mAddressEditText.getText().toString();

        boolean validate = true;
        String errorMessage = "";

        // input validation
        // check if input is empty or exceed maximum character
        if (mName.matches("")) {
            validate = false;
            errorMessage += "Name input is empty\n";
        }

        if (mName.length() > 30) {
            validate = false;
            errorMessage += "Name input is too long. Maximum 30 characters.\n";
        }

        if (mName.matches(".*\\d.*")) { // if mRegName contains numbers
            validate = false;
            errorMessage += "Name should not contain numbers\n";
        }

        if (mEmail.matches("")) {
            validate = false;
            errorMessage += "Email input is empty\n";
        } else if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            validate = false;
            errorMessage += "Email input is not valid\n";
        }

        if (mEmail.length() > 30) {
            validate = false;
            errorMessage += "Email input is too long. Maximum 30 characters.\n";
        }

        if (mAddress.matches("")) {
            validate = false;
            errorMessage += "Address input is empty\n";
        }

        if (mAddress.length() > 50) {
            validate = false;
            errorMessage += "Address input is too long. Maximum 50 characters.\n";
        }

        if (validate) {
            int result = datasource.updatePersonalInfo(mName, mEmail, mAddress);
            switch (result) {
                case 1:
                    Toast.makeText(getApplicationContext(), "Update successful", Toast.LENGTH_SHORT).show();
                    //mRegNameEditText.setText("");
                    //mRegEmailEditText.setText("");
                    //mRegAddressEditText.setText("");
                    //mRegPasswordEditText.setText("");
                    //mRegCPasswordEditText.setText("");
                    //mLoginLblTextView.performClick();
                    break;
                case 0:
                    Toast.makeText(getApplicationContext(), "Update failed, please try again", Toast.LENGTH_LONG).show();
                    break;
            }
        } else {
            AlertDialog dialog = new AlertDialog.Builder(UserProfileActivity.this)
                    .setTitle("Invalid Input")
                    .setMessage(errorMessage.substring(0, errorMessage.length() - 1))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .create();
            dialog.show();
        }
    }

    public void changePassword(View v) {
        mCurrentPswEditText = (EditText) findViewById(R.id.user_profile_password);
        mCurrentPsw = mCurrentPswEditText.getText().toString();

        mNewPswEditText = (EditText) findViewById(R.id.user_profile_new_password);
        mNewPsw = mNewPswEditText.getText().toString();

        mConfirmNewPswEditText = (EditText) findViewById(R.id.user_profile_confirm_password);
        mConfirmNewPsw = mConfirmNewPswEditText.getText().toString();

        boolean validate = true;
        String errorMessage = "";

        // input validation
        // check if input is empty

        if (mCurrentPsw.matches("")) {
            validate = false;
            errorMessage += "Password input is empty\n";
        }

        if (mNewPsw.matches("")) {
            validate = false;
            errorMessage += "New password input is empty\n";
        } else if (mNewPsw.length() < 8) {
            validate = false;
            errorMessage += "New password input is too short. Minimum 8 characters.\n";
        } else if (mNewPsw.length() > 20) {
            validate = false;
            errorMessage += "New password input is too long. Maximum 20 characters.\n";
        }

        if (mConfirmNewPsw.matches("")) {
            validate = false;
            errorMessage += "Confirm new password input is empty\n";
        } else if (!mNewPsw.matches(mConfirmNewPsw)) {
            validate = false;
            errorMessage += "New password does not match\n";
        }

        if (validate) {
            int result = datasource.changePassword(mCurrentPsw, mNewPsw);
//            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

            switch (result) {
                case 1:
                    // clear session
                    SharedPreferences.Editor editor = getSharedPreferences("LoginSession", MODE_PRIVATE).edit();
                    editor.putBoolean("login", false);
                    editor.putLong("userId", 0);
                    editor.apply();

                    Toast.makeText(getApplicationContext(), "Change password successful", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clean up all activities

                    startActivity(intent);

                    break;
                case 0:
                    Toast.makeText(getApplicationContext(), "Password is incorrect", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "Failed to change password", Toast.LENGTH_LONG).show();
                    break;
            }
        } else {
            AlertDialog dialog = new AlertDialog.Builder(UserProfileActivity.this)
                    .setTitle("Invalid Input")
                    .setMessage(errorMessage.substring(0, errorMessage.length() - 1))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .create();
            dialog.show();
        }
    }

    public void logout(View v){

        AlertDialog dialog = new AlertDialog.Builder(UserProfileActivity.this)
                .setTitle("Logout")
                .setMessage("Confirm to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // clear session
                        SharedPreferences.Editor editor = getSharedPreferences("LoginSession", MODE_PRIVATE).edit();
                        editor.putBoolean("login", false);
                        editor.putLong("userId", 0);
                        editor.apply();

                        Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clean up all activities

                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }

    }
