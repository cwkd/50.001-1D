package com.example.cindy.iot;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
    }
    public void clickSignIn(View v)
    {
        startActivity(new Intent(StartingActivity.this,LoginActivity.class));
    }
    public void clickSignUp(View v)
    {
        startActivity(new Intent(StartingActivity.this,SignUpActivity.class));
    }
}
