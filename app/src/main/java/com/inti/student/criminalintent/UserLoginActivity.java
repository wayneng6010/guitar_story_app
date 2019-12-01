package com.inti.student.criminalintent;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserLoginActivity extends AppCompatActivity {

    private TextView mLoginLblTextView;
    private TextView mRegisterLblTextView;
    private RelativeLayout mLoginContentLayout;
    private RelativeLayout mRegisterContentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide default title bar
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        setContentView(R.layout.activity_user_login);

        mLoginLblTextView = (TextView) findViewById(R.id.login_lbl);
        mRegisterLblTextView = (TextView) findViewById(R.id.register_lbl);
        mLoginContentLayout = (RelativeLayout) findViewById(R.id.user_login_content_layout);
        mRegisterContentLayout = (RelativeLayout) findViewById(R.id.user_register_content_layout);

    }

    public void switchToLogin(View v){
        mLoginLblTextView.setTextColor(Color.parseColor("#FD6776"));
        mRegisterLblTextView.setTextColor(Color.parseColor("#8c8c8c"));
        mLoginContentLayout.setVisibility(View.VISIBLE);
        mRegisterContentLayout.setVisibility(View.GONE);
    }

    public void switchToRegister(View v){
        mLoginLblTextView.setTextColor(Color.parseColor("#8c8c8c"));
        mRegisterLblTextView.setTextColor(Color.parseColor("#FD6776"));
        mLoginContentLayout.setVisibility(View.GONE);
        mRegisterContentLayout.setVisibility(View.VISIBLE);
    }

}
