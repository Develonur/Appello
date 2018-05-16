package com.runekid.appello.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.runekid.appello.R;
import com.runekid.appello.helper.InputValidation;
import com.runekid.appello.sql.DBHelper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = LoginActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonLogin;
    private AppCompatTextView textViewLinkRegister;

    private InputValidation inputValidation;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();



        initViews();
        initListeners();
        initObjects();
    }

    private void initViews() {

        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);

        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);

        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewlinkRegister);

        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
    }

    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("USER_SIGNED_IN", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("USER_SIGNED_OUT", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    private void initObjects() {

        inputValidation = new InputValidation(activity);
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
                verifyFromSQLite();
                break;
            case R.id.textViewlinkRegister:
                Intent intentRegister = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }


    private void verifyFromSQLite(){
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail,textInputLayoutEmail,getString(R.string.error_message_email))) {
            return;
        }

        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail,textInputLayoutEmail,getString(R.string.error_message_email))) {
            return;
        }

        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword,textInputLayoutPassword,getString(R.string.error_message_password))) {
            return;
        }





        mAuth.signInWithEmailAndPassword(textInputEditTextEmail.getText().toString().trim(), textInputEditTextPassword.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("SIGN_IN_USER", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Snackbar.make(nestedScrollView,getString(R.string.error_valid_email_password),Snackbar.LENGTH_LONG).show();
                        } else {
                            Intent accountsIntent = new Intent(activity,UsersActivity.class);
                            accountsIntent.putExtra("EMAIL",textInputEditTextEmail.getText().toString().trim());
                            emptyInputEditText();
                            startActivity(accountsIntent);
                        }

                        // ...
                    }
                });
    }

    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }
}
