package com.runekid.appello.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.runekid.appello.R;
import com.runekid.appello.helper.AESCrypt;
import com.runekid.appello.helper.InputValidation;
import com.runekid.appello.model.User;
import com.runekid.appello.sql.DBHelper;

/**
 * Created by Runekid on 08/05/2018.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = RegisterActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;

    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPaswword;

    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;

    private InputValidation inputValidation;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);

        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPaswword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);

        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);

        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewLoginLink);

        mAuth = FirebaseAuth.getInstance();
    }

    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);
    }

    private void initObjects() {
        inputValidation = new InputValidation(activity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonRegister:
                createAccount();
                break;
            case R.id.appCompatTextViewLoginLink:
                finish();
                break;
        }
    }

    private void createAccount() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName,textInputLayoutName,getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail,textInputLayoutEmail,getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail,textInputLayoutEmail,getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword,textInputLayoutPassword,getString(R.string.error_message_password))){
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword,textInputEditTextConfirmPaswword,textInputLayoutConfirmPassword,getString(R.string.error_password_match))){
            return;
        }

        mAuth.createUserWithEmailAndPassword(textInputEditTextEmail.getText().toString().trim(), textInputEditTextPassword.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("CREATE_USER", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Snackbar.make(nestedScrollView,getString(R.string.error_email_exists),Snackbar.LENGTH_LONG).show();
                            Log.e("USER_CREATE_FAIL", "onComplete: Failed=" + task.getException().getMessage());
                        } else {
                            // Sign in success
                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(textInputEditTextName.getText().toString().trim()).build();

                            user.updateProfile(profileUpdates);
                            Snackbar.make(nestedScrollView,getString(R.string.success_message),Snackbar.LENGTH_LONG).show();
                            emptyInputEditText();
                        }

                        // ...
                    }
                });



    }

    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPaswword.setText(null);
    }
}
