package com.inti.student.criminalintent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class ItemListSingleFragmentActivity extends FragmentActivity {

    protected abstract Fragment createFragment();
    private TextView mLoginTextView;
    private ImageView mProfileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_item_list);

        SharedPreferences prefs = this.getSharedPreferences("LoginSession", MODE_PRIVATE);
        Boolean sessionData= prefs.getBoolean("login", false); // retrieve login session saved in preference

        mLoginTextView = (TextView) findViewById(R.id.home_login_text_view);
        mProfileImageView = (ImageView) findViewById(R.id.home_profile_image_view);

        if(sessionData){
            mLoginTextView.setVisibility(View.GONE);
            mProfileImageView.setVisibility(View.VISIBLE);
        } else {
            mLoginTextView.setVisibility(View.VISIBLE);
            mProfileImageView.setVisibility(View.GONE );
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

    }

    public void goToCart(android.view.View v) {
        android.content.Intent intent = new android.content.Intent(getBaseContext(), CartActivity.class);
        startActivity(intent);
    }

    public void goToMyPurchases(android.view.View v) {
        android.content.Intent intent = new android.content.Intent(getBaseContext(), PurchasedItemActivity.class);
        startActivity(intent);
    }

    public void goToLogin(android.view.View v) {
        android.content.Intent intent = new android.content.Intent(getBaseContext(), UserLoginActivity.class);
        startActivity(intent);
    }
}
