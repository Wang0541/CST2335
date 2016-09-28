package com.example.yu.lab1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "StartActivity";
    protected static final String SETTING = "anything";


    Button login;
    EditText login_Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Bundle are used for passing data between various android activities
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        login = (Button)findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // save data to the context, think about Scanner(System.in)
                SharedPreferences prs = getSharedPreferences(SETTING, Context.MODE_PRIVATE);
                //why do we have to add editor here?
                SharedPreferences.Editor editor = prs.edit();
                login_Name = (EditText)findViewById(R.id.editInp);

                // when you put String here, we should get string back after
                editor.putString("DefaultEmail",login_Name.getText().toString() );
                editor.commit();
                // first parameter: back page; second parameter: what is the next page
                Intent intent = new Intent(LoginActivity.this,StartActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");

    }
    @Override
    protected void onStart(){
        // what is the meaning of onStart?
        // it calls when it Restarted?
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");

        // save data to the context, think about Scanner(System.in)
        SharedPreferences prs = getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        // set defaultEmail symtax: email@domain.com
        String defaultEmail = prs.getString("DefaultEmail", "email@domain.com");
        login_Name = (EditText)findViewById(R.id.editInp);
        login_Name.setText(defaultEmail);
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

}
