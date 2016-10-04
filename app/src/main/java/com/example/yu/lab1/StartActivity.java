package com.example.yu.lab1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button start_button = (Button)findViewById(R.id.button);

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, ListItemsActivity.class);
                //data back from the activity
                // get the result from this class when called onActivityResult
                startActivityForResult(intent, 5);
            }
        });

        Log.i(ACTIVITY_NAME,"in onCreate");

        Button start_chatButton = (Button)findViewById(R.id.Chat_button);
        start_chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Log.i(ACTIVITY_NAME,"User clicked Start Chat");

                Intent intentChat = new Intent(StartActivity.this,ChatWindow.class);
                startActivity(intentChat);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 5){

            String messagePassed = data.getStringExtra("Response");
            Log.i("abcd", messagePassed);
            //look in the “extra data” container of the Intent to see if there is a string called “Response”.
            if(resultCode ==Activity.RESULT_OK) {

                Log.i(ACTIVITY_NAME,"Returned to StartActivity.onActivityRestult");
                CharSequence text = "ListItemsActivity passed: " + messagePassed;
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(StartActivity.this, text, duration);
                toast.show();
            }

        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");

    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
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
