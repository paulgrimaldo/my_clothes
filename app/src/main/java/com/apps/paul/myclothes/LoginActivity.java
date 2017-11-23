package com.apps.paul.myclothes;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.apps.paul.myclothes.Fragmentos.Auth.LoginFragment;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.container_login_register, LoginFragment.newInstance(null), LoginFragment.TAG)
                .addToBackStack(null)
                .commit();


    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
