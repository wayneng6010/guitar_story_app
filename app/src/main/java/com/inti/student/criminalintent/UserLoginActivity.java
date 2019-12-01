package com.inti.student.criminalintent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserLoginActivity extends AppCompatActivity {

    private TextView mLoginLblTextView;
    private TextView mRegisterLblTextView;
    private RelativeLayout mLoginContentLayout;
    private RelativeLayout mRegisterContentLayout;
    private EditText mRegNameEditText;
    private EditText mRegEmailEditText;
    private EditText mRegPasswordEditText;
    private EditText mRegCPasswordEditText;
    private EditText mRegAddressEditText;
    private String mRegName;
    private String mRegEmail;
    private String mRegPassword;
    private String mRegCPassword;
    private String mRegAddress;

    private UserDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide default title bar
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        setContentView(R.layout.activity_user_login);

        datasource = new UserDataSource(this);
        datasource.open();

        mLoginLblTextView = (TextView) findViewById(R.id.login_lbl);
        mRegisterLblTextView = (TextView) findViewById(R.id.register_lbl);
        mLoginContentLayout = (RelativeLayout) findViewById(R.id.user_login_content_layout);
        mRegisterContentLayout = (RelativeLayout) findViewById(R.id.user_register_content_layout);

    }

    public void switchToLogin(View v) {
        mLoginLblTextView.setTextColor(Color.parseColor("#FD6776"));
        mRegisterLblTextView.setTextColor(Color.parseColor("#8c8c8c"));
        mLoginContentLayout.setVisibility(View.VISIBLE);
        mRegisterContentLayout.setVisibility(View.GONE);
    }

    public void switchToRegister(View v) {
        mLoginLblTextView.setTextColor(Color.parseColor("#8c8c8c"));
        mRegisterLblTextView.setTextColor(Color.parseColor("#FD6776"));
        mLoginContentLayout.setVisibility(View.GONE);
        mRegisterContentLayout.setVisibility(View.VISIBLE);
    }

    public void register(View v) {
        mRegNameEditText = (EditText) findViewById(R.id.user_register_name);
        mRegName = mRegNameEditText.getText().toString();

        mRegEmailEditText = (EditText) findViewById(R.id.user_register_email);
        mRegEmail = mRegEmailEditText.getText().toString();

        mRegAddressEditText = (EditText) findViewById(R.id.user_register_address);
        mRegAddress = mRegAddressEditText.getText().toString();

        mRegPasswordEditText = (EditText) findViewById(R.id.user_register_password);
        mRegPassword = mRegPasswordEditText.getText().toString();

        mRegCPasswordEditText = (EditText) findViewById(R.id.user_register_cpassword);
        mRegCPassword = mRegCPasswordEditText.getText().toString();

        boolean validate = true;
        String errorMessage = "";

        // input validation
        // check if input is empty or exceed maximum character
        if (mRegName.matches("")) {
            validate = false;
            errorMessage += "Name input is empty\n";
        }

        if (mRegName.length() > 30) {
            validate = false;
            errorMessage += "Name input is too long. Maximum 30 characters.\n";
        }

        if (mRegEmail.matches("")) {
            validate = false;
            errorMessage += "Email input is empty\n";
        } else if (!Patterns.EMAIL_ADDRESS.matcher(mRegEmail).matches()) {
            validate = false;
            errorMessage += "Email input is not valid\n";
        }

        if (mRegEmail.length() > 30) {
            validate = false;
            errorMessage += "Email input is too long. Maximum 30 characters.\n";
        }

        if (mRegAddress.matches("")) {
            validate = false;
            errorMessage += "Address input is empty\n";
        }

        if (mRegAddress.length() > 50) {
            validate = false;
            errorMessage += "Address input is too long. Maximum 50 characters.\n";
        }

        if (mRegPassword.matches("")) {
            validate = false;
            errorMessage += "Password input is empty\n";
        } else if (mRegPassword.length() < 8) {
            validate = false;
            errorMessage += "Password input is too short. Minimum 8 characters.\n";
        }

        if (mRegPassword.length() > 20) {
            validate = false;
            errorMessage += "Password input is too long. Maximum 20 characters.\n";
        }

        if (mRegName.matches(".*\\d.*")) { // if mRegName contains numbers
            validate = false;
            errorMessage += "Name should not contain numbers\n";
        }

        if (mRegCPassword.matches("")) {
            validate = false;
            errorMessage += "Confirm password input is empty\n";
        } else if (!mRegPassword.matches(mRegCPassword)) {
            validate = false;
            errorMessage += "Password does not match\n";
        }

        if (validate) {
            int result = datasource.createUser(mRegName, mRegEmail, mRegPassword, mRegAddress);
            switch (result) {
                case 1:
                    Toast.makeText(getApplicationContext(), "Register successful", Toast.LENGTH_SHORT).show();
                    mRegNameEditText.setText("");
                    mRegEmailEditText.setText("");
                    mRegAddressEditText.setText("");
                    mRegPasswordEditText.setText("");
                    mRegCPasswordEditText.setText("");
                    mLoginLblTextView.performClick();
                    break;
                case 0:
                    Toast.makeText(getApplicationContext(), "Register failed, please try again", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "Email already exist", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            AlertDialog dialog = new AlertDialog.Builder(UserLoginActivity.this)
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

}
