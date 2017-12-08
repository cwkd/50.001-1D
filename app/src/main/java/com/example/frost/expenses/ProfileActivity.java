package com.example.frost.expenses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }
    public void clickGetClassification(View v)
    {
        Intent intent = new Intent(getApplicationContext(), ClassificationForReceiptActivity.class);
        startActivity(intent);
    }
}
