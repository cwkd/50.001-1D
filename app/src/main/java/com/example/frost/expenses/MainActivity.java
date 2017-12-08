package com.example.frost.expenses;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements Expenses.OnFragmentInteractionListener,
                    Logger.OnFragmentInteractionListener,
                    Recommender.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        FragmentTransaction transaction = getSupportFragmentManager().
                                beginTransaction();
                        switch (item.getItemId()) {
                            case R.id.navigation_profile:
                                transaction.replace(R.id.content_frame, Expenses.newInstance());
                                break;
                            case R.id.navigation_log:
                                transaction.replace(R.id.content_frame, Logger.newInstance());
                                break;
                            case R.id.navigation_recommend:
                                transaction.replace(R.id.content_frame, Recommender.newInstance());
                                break;
                        }
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().
                beginTransaction();
        transaction.replace(R.id.content_frame, Expenses.newInstance());
        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}


