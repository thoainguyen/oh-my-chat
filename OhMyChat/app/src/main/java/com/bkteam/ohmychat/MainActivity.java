package com.bkteam.ohmychat;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private LinearLayout topToolbar;
    private ActionBar bottomToolbar;
    private TextView test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_chat:
                        break;
                    case R.id.navigation_profile:
                        break;
                    case R.id.navigation_settings:
                        break;
                }
            }
        });

        // For testing
            test = findViewById(R.id.test);
        //

        Bundle extras = getIntent().getExtras();
        if (extras == null) {

        }
        else {
            test.setText("Username: " + extras.get("username") + "\n" + "Password: " + extras.get("password"));
        }

    }


}
