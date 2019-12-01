package com.inti.student.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public abstract class ItemListSingleFragmentActivity extends FragmentActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_item_list);

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

    public void goToLogin(android.view.View v) {
        android.content.Intent intent = new android.content.Intent(getBaseContext(), UserLoginActivity.class);
        startActivity(intent);
    }
}