/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.firebase.quickstart.auth;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.quickstart.auth.models.User;
import com.google.firebase.database.ValueEventListener;


public class EmailPasswordActivity extends BaseActivity implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private TextView mTitle;
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;
    private TextView mInternetStatus;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference refUser;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            setContentView(R.layout.activity_emailpassword);

            // Views
            mStatusTextView = (TextView) findViewById(R.id.status);
            mDetailTextView = (TextView) findViewById(R.id.detail);
            mEmailField = (EditText) findViewById(R.id.field_email);
            mPasswordField = (EditText) findViewById(R.id.field_password);
            mInternetStatus = (TextView) findViewById(R.id.InternetConnection);

            // Buttons
            findViewById(R.id.email_sign_in_button).setOnClickListener(this);
            findViewById(R.id.email_create_account_button).setOnClickListener(this);
            findViewById(R.id.sign_out_button).setOnClickListener(this);
            findViewById(R.id.verify_email_button).setOnClickListener(this);

            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();

        }
        catch(Exception e) {
            Log.e(TAG,e.getMessage());
        }
    }

    // [START on_start_check_user]
    @Override
    public void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            /*
            writeNewUser(currentUser.getUid(),
                         usernameFromEmail(currentUser.getEmail()),
                         currentUser.getEmail());
            */
        }
        updateUI(currentUser);
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        if(!IsNetworkAvailable())
        {
            Toast.makeText(EmailPasswordActivity.this, "No internet connection",Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(EmailPasswordActivity.this, new OnCompleteListener<AuthResult>() 
				{
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                        else
                        {
                            String FailureReason = "Success";
                            try {
                                throw task.getException();
                            }
                            catch(FirebaseAuthWeakPasswordException  e)
                            {
                                FailureReason = "Weak Password";
                            }
                            catch(FirebaseAuthInvalidCredentialsException e)
                            {
                                FailureReason = "Invalid credentials";
                            }
                            catch(FirebaseAuthUserCollisionException e)
                            {
                                FailureReason = "User already exist";
                            }
                            catch(Exception e)
                            {
                                FailureReason = e.getMessage();
                                Log.e(TAG,e.getMessage());
                            }
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:" + FailureReason , task.getException());
                            Toast.makeText(EmailPasswordActivity.this,
                                           "Authentication failed. Error: " + FailureReason,
                                            Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        hideProgressDialog();
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm())
        {
            return;
        }

        if(!IsNetworkAvailable())
        {
            Toast.makeText(EmailPasswordActivity.this, "No internet connection",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            hideProgressDialog();

                            final FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "signInWithEmail:success");

                            //TestCode start
                            refUser = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                            ValueEventListener PostListener = new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot)
                                {
                                    User userdata = dataSnapshot.getValue(User.class);
                                    if(userdata==null)
                                    {
                                        //User info not in database
                                        writeNewUser(user.getUid(),
                                                     usernameFromEmail(user.getEmail()),
                                                     user.getEmail());
                                        finish();
                                    }
                                    else
                                    {
                                        if(userdata.getlogintype() == 1234)
                                        {
                                            Log.d(TAG, "LoginType = " + userdata.getlogintype());
                                            Intent AdminLoginIntent = new Intent(EmailPasswordActivity.this,AdminPage.class);
                                            startActivity(AdminLoginIntent);
                                        }
                                        else
                                        {
                                            finish();
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError)
                                {

                                }
                            };
                            refUser.addValueEventListener(PostListener);

                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        if (!task.isSuccessful()) {
                            mStatusTextView.setText(R.string.auth_failed);
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);

    }

    private void sendEmailVerification() {
        // Disable button
        findViewById(R.id.verify_email_button).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(EmailPasswordActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(EmailPasswordActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();

        if(!IsNetworkAvailable())
            mInternetStatus.setText("Not connecting to internet");

        if (user != null)
        {
            if(user.isEmailVerified())
            {
                mStatusTextView.setText("Welcome back!");
                mDetailTextView.setText(usernameFromEmail(user.getEmail()));
            }
            else
            {
                mStatusTextView.setText("Please verify email that sent to:");
                mDetailTextView.setText(user.getEmail());

            }

            findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
            findViewById(R.id.email_password_fields).setVisibility(View.GONE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
        }
        else
        {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_create_account_button) 
		{
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } 
		else if (i == R.id.email_sign_in_button) 
		{
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
		else if (i == R.id.sign_out_button) 
		{
            signOut();
        } 
		else if (i == R.id.verify_email_button) 
		{
            sendEmailVerification();
        }
    }

    private void writeNewUser(String userId, String name, String email)
    {
        User user = new User(name, email, 1);
        mDatabase.child("users").child(userId).setValue(user);
    }

    private String usernameFromEmail(String email)
    {
        if (email.contains("@"))
        {
            return email.split("@")[0];
        }
        else
        {
            return email;
        }
    }
}
