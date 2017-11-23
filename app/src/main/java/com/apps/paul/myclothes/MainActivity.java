package com.apps.paul.myclothes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        redirect();

    }

    private void redirect() {
        SharedPreferences preferences = getSharedPreferences("Current_User", MODE_PRIVATE);
        //SharedPreferences.Editor editor = preferences.edit();
        if (!Objects.equals(preferences.getString("email", "Current_User"), "Current_User")) {
            Intent i = new Intent(this, NoticiasActivity.class);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }

    }
}
