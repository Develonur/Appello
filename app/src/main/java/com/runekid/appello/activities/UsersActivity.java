package com.runekid.appello.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.runekid.appello.R;

/**
 * Created by Runekid on 08/05/2018.
 */

public class UsersActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        textView = (TextView) findViewById(R.id.text1);

        String nameFromIntent = getIntent().getStringExtra("EMAIL");

        textView.setText("Welcome " + nameFromIntent);
    }
}
