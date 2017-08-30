package com.google.firebase.quickstart.auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class UserOwnPage extends BaseActivity
{
    private TextView mWelcomeTextView;

    boolean IsUserEmailVerified;
    String UserUID;
    String UserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_own_page);

        mWelcomeTextView = (TextView) findViewById(R.id.Welcome);
        IsUserEmailVerified = getIntent().getBooleanExtra("LOGIN_IsUserEmailVerified",false);
        UserUID = getIntent().getStringExtra("LOGIN_UID");
        UserEmail = getIntent().getStringExtra("LOGIN_Email");

        mWelcomeTextView.setText("Welcome !");
        if(!IsUserEmailVerified)
        {
            mWelcomeTextView.setText("An email of login verification has been send to "+ UserEmail+ "Please verify.");
        }





    }
}
