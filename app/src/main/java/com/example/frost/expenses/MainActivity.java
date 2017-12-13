package com.example.frost.expenses;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

import static com.example.frost.expenses.ClassificationRecyclerAdapter.FROM_CLASSIFICATION;
import static com.example.frost.expenses.ClassificationRecyclerAdapter.ITEMS;

public class MainActivity extends AppCompatActivity
        implements Expenses.OnFragmentInteractionListener,
                    SubmitReceipt.OnFragmentInteractionListener,
                    Recommender.OnFragmentInteractionListener{

    public static final String PROFILE = "mProfile";
    public static final String SUBMIT = "mSubmit";
    public static final String RECOMMEND = "mRecommend";


    public Expenses expensesFragment;
    public SubmitReceipt submitReceiptFragment;
    public Recommender recommenderFragemtn;

    public FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExpensesDbHelper helper = new ExpensesDbHelper(this);
        SQLiteDatabase sqliteDatabase = helper.getReadableDatabase();
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(PROFILE) != null) {
            expensesFragment = (Expenses) fragmentManager.getFragment(savedInstanceState, PROFILE);
            Log.d("Profile", "Retrieved old instance");
        } else {
            expensesFragment = Expenses.newInstance();
            fragmentManager.beginTransaction().add(expensesFragment, PROFILE).commit();
        }
        Intent intent = getIntent();
        boolean fromClassification = intent.getBooleanExtra(FROM_CLASSIFICATION, false);
        if (fromClassification) {
            if (expensesFragment != null) {
                double[] items = intent.getDoubleArrayExtra(ITEMS);
                for (int i = 0; i < 4; i++) {
                    if (items[i] == 0) continue;
                    switch (i) {
                        case 0:
                            expensesFragment.analyser.logMainMeal(items[i]);
                        case 1:
                            expensesFragment.analyser.logTransport(items[i]);
                        case 2:
                            expensesFragment.analyser.logEmergency(items[i]);
                        case 3:
                            expensesFragment.analyser.logMisc(items[i]);
                    }
                }
            }
        }

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        FragmentTransaction transaction = getSupportFragmentManager().
                                beginTransaction();
                        switch (item.getItemId()) {
                            case R.id.navigation_profile:
                                transaction.replace(R.id.content_frame, expensesFragment);
                                break;
                            case R.id.navigation_log:
                                transaction.replace(R.id.content_frame, SubmitReceipt.newInstance());
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


